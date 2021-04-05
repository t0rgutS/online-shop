package com.devtech.service;

import com.devtech.entity.CartWish;
import com.devtech.entity.Product;
import com.devtech.entity.User;
import com.devtech.exception.*;
import com.devtech.repository.CartWishRepository;
import com.devtech.repository.ProductRepository;
import com.devtech.repository.UserRepository;
import com.devtech.request_response.cartwish.CartWishCURequest;
import com.devtech.request_response.product.ProductSearchRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.validation.constraints.NotNull;

import static com.devtech.exception.ExceptionList.*;

@RestController
@RequiredArgsConstructor
public class CartWishService {
    private final CartWishRepository cartWishRepo;
    private final UserRepository userRepo;
    private final ProductRepository productRepo;

    public boolean check(@NotNull Long productId, boolean wishlist) {
        if (!(SecurityContextHolder.
                getContext().getAuthentication().getPrincipal() instanceof User))
            throw new NotAuthroizedException();
        if (wishlist)
            return cartWishRepo.existsByUserAndProduct_Id(((User) SecurityContextHolder.
                            getContext().getAuthentication().getPrincipal()), productId, (CartWish cw) -> cw.getCount() == 0);
        else
            return cartWishRepo.existsByUserAndProduct_Id(((User) SecurityContextHolder.
                    getContext().getAuthentication().getPrincipal()), productId, (CartWish cw) -> cw.getCount() > 0);
    }

    @Transactional
    public CartWish create(@NotNull CartWishCURequest request) {
        if (!(SecurityContextHolder.
                getContext().getAuthentication().getPrincipal() instanceof User))
            throw new NotAuthroizedException();
        User user = userRepo.findByLogin(((User) SecurityContextHolder.
                getContext().getAuthentication().getPrincipal()).getLogin()).orElseThrow(USER_NOT_FOUND);
        Product product = productRepo.findById(request.getProductId()).orElseThrow(PRODUCT_NOT_FOUND);
        if (product.getUser().equals(user))
            throw new AddingYourOwnProductException();
        if (product.getCount() - request.getCount() < 0)
            throw new NoProductsLeftException();
        CartWish cartWish = cartWishRepo.findByUserAndProduct(user, product).orElse(null);
        if (cartWish == null) {
            cartWish = new CartWish();
            cartWish.setUser(user);
            cartWish.setProduct(product);
            cartWish.setCount(request.getCount());
        } else
            cartWish.setCount(cartWish.getCount() + request.getCount());
        product.setCount(product.getCount() - request.getCount());
        productRepo.save(product);
        cartWishRepo.save(cartWish);
        return cartWish;
    }

    @Transactional
    public CartWish update(@NotNull Long id, @NotNull CartWishCURequest request) {
        if (!(SecurityContextHolder.
                getContext().getAuthentication().getPrincipal() instanceof User))
            throw new NotAuthroizedException();
        CartWish cartWish = cartWishRepo.findById(id).orElseThrow(BUCKET_NOT_FOUND);
        if (!((User) SecurityContextHolder.
                getContext().getAuthentication().getPrincipal()).getLogin().equals(cartWish.getUser().getLogin()))
            throw new IncorrectSessionLoginException("Нет доступа!");
        if (request.getCount() != null) {
            if (!((request.getCount() > 0 && cartWish.getCount() == 0)
                    || (request.getCount() == 0 && cartWish.getCount() > 0))) {
                Product product = cartWish.getProduct();
                if (product.getCount() - request.getCount() < 0)
                    throw new NoProductsLeftException();
                cartWish.setCount(request.getCount());
                product.setCount(product.getCount() + cartWish.getCount() - request.getCount());
                productRepo.save(product);
                cartWishRepo.save(cartWish);
            } else if (request.getCount() == 0 && cartWish.getCount() > 0)
                throw new BucketCountException("Вы не можете добавить в корзину 0 единиц товара!");
            else
                throw new BucketCountException("Ошибка: товар в желаемом должен иметь 0 в поле \"Количество\"!");
        }
        return cartWish;
    }

    public CartWish delete(@NotNull Long id) {
        if (!(SecurityContextHolder.
                getContext().getAuthentication().getPrincipal() instanceof User))
            throw new NotAuthroizedException();
        CartWish cartWish = cartWishRepo.findById(id).orElseThrow(BUCKET_NOT_FOUND);
        if (!cartWish.getUser().getLogin().equals(((User) SecurityContextHolder.
                getContext().getAuthentication().getPrincipal()).getLogin()))
            throw new IncorrectSessionLoginException("Нет доступа!");
        Product product = cartWish.getProduct();
        product.setCount(product.getCount() + cartWish.getCount());
        productRepo.save(product);
        cartWishRepo.delete(cartWish);
        return cartWish;
    }

    public Page<CartWish> getAll(ProductSearchRequest request, Boolean wishList) {
        if (!(SecurityContextHolder.
                getContext().getAuthentication().getPrincipal() instanceof User))
            throw new NotAuthroizedException();
        return cartWishRepo.findAll(new Specification<CartWish>() {
            @Nullable
            @Override
            public Predicate toPredicate(Root<CartWish> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
                Predicate predicate = builder.equal(root.get("user").get("login"), ((User) SecurityContextHolder.
                        getContext().getAuthentication().getPrincipal()).getLogin());
                if (wishList)
                    predicate = builder.and(predicate, builder.equal(root.get("count"), 0));
                else
                    predicate = builder.and(predicate, builder.greaterThan(root.get("count"), 0));
                if (request.getSearchString() != null && !request.getSearchString().isEmpty()) {
                    predicate = builder.and(predicate, builder.or(
                            builder.like(root.get("product").get("productName"), "%" + request.getSearchString() + "%"),
                            builder.or(
                                    builder.like(root.get("product").get("producer"), "%" + request.getSearchString() + "%"),
                                    builder.or(
                                            builder.like(root.get("product").get("description"), "%" + request.getSearchString() + "%"),
                                            builder.like(root.get("product").get("category").get("categoryName"),
                                                    "%" + request.getSearchString() + "%")
                                    )
                            )
                    ));
                }
                if (request.getCondition() != null) {
                    predicate = builder.and(predicate,
                            builder.equal(root.get("product").get("condition"), request.getCondition()));
                }
                if (request.getShipType() != null) {
                    predicate = builder.and(predicate,
                            builder.equal(root.get("product").get("shipType"), request.getShipType()));
                }
                if (request.getCount() != null) {
                    Predicate countPredicate;
                    if (request.getCountOp() != null && !request.getCountOp().isEmpty()) {
                        switch (request.getCountOp()) {
                            case ">":
                                countPredicate = builder.greaterThan(root.get("product").get("count"), request.getCount());
                                break;
                            case "<":
                                countPredicate = builder.lessThan(root.get("product").get("count"), request.getCount());
                                break;
                            case ">=":
                                countPredicate = builder.greaterThanOrEqualTo(root.get("product").get("count"), request.getCount());
                                break;
                            case "<=":
                                countPredicate = builder.lessThanOrEqualTo(root.get("product").get("count"), request.getCount());
                                break;
                            default:
                                countPredicate = builder.equal(root.get("product").get("count"), request.getCount());
                        }
                    } else countPredicate = builder.equal(root.get("product").get("count"), request.getCount());
                    predicate = builder.and(predicate, countPredicate);
                }
                if (request.getPrice() != null) {
                    Predicate countPredicate;
                    if (request.getPriceOp() != null && !request.getPriceOp().isEmpty()) {
                        switch (request.getPriceOp()) {
                            case ">":
                                countPredicate = builder.greaterThan(root.get("product").get("price"), request.getPrice());
                                break;
                            case "<":
                                countPredicate = builder.lessThan(root.get("product").get("price"), request.getPrice());
                                break;
                            case ">=":
                                countPredicate = builder.greaterThanOrEqualTo(root.get("product").get("price"), request.getPrice());
                                break;
                            case "<=":
                                countPredicate = builder.lessThanOrEqualTo(root.get("product").get("price"), request.getPrice());
                                break;
                            default:
                                countPredicate = builder.equal(root.get("product").get("price"), request.getPrice());
                        }
                    } else countPredicate = builder.equal(root.get("product").get("price"), request.getPrice());
                    predicate = builder.and(predicate, countPredicate);
                }
                return predicate;
            }
        }, request.pageable());
    }
}
