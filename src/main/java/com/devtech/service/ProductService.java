package com.devtech.service;

import com.devtech.dto.product.ProductCURequest;
import com.devtech.dto.product.ProductResponse;
import com.devtech.dto.product.ProductSearchRequest;
import com.devtech.entity.Category;
import com.devtech.entity.Product;
import com.devtech.entity.Rating;
import com.devtech.entity.User;
import com.devtech.exception.IncorrectSessionLoginException;
import com.devtech.exception.NoContactsException;
import com.devtech.repository.CategoryRepository;
import com.devtech.repository.ProductRepository;
import com.devtech.repository.RatingRepository;
import com.devtech.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import static com.devtech.exception.ExceptionList.*;

@RestController
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepo;
    private final CategoryRepository categoryRepo;
    private final UserRepository userRepo;
    private final RatingRepository ratingRepo;

    public ProductResponse create(@NotNull ProductCURequest request) {
        Category category = categoryRepo.findByCategoryName(request.getCategoryName()).orElseThrow(CATEGORY_NOT_FOUND);
        User user = userRepo.findByLogin(request.getUserLogin()).orElseThrow(USER_NOT_FOUND);
        if ((user.getEmail() == null && user.getPhone() == null)
                || (user.getEmail() == null && user.getPhone() != null && user.getPhone().isEmpty())
                || (user.getPhone() == null && user.getEmail() != null && user.getEmail().isEmpty())
                || (user.getEmail().isEmpty() && user.getPhone().isEmpty()))
            throw new NoContactsException();
        Product product = new Product();
        product.setProductName(request.getProductName());
        product.setPhotoURL(request.getPhotoURL());
        product.setProducer(request.getProducer());
        product.setPrice(request.getPrice());
        product.setDescription(request.getDescription());
        product.setCondition(request.getCondition());
        product.setShipType(request.getShipType());
        product.setCount(request.getCount());
        product.setCategory(category);
        product.setUser(user);
        productRepo.save(product);
        return new ProductResponse(product);
    }

    public ProductResponse update(@NotNull Long id, @NotNull ProductCURequest request) {
        Product product = productRepo.findById(id).orElseThrow(PRODUCT_NOT_FOUND);
        if (request.getCategoryName() != null && !request.getCategoryName().isEmpty()) {
            Category category = categoryRepo.findByCategoryName(request.getCategoryName()).orElseThrow(CATEGORY_NOT_FOUND);
            product.setCategory(category);
        }
        /*if (request.getUserLogin() != null && !request.getUserLogin().isEmpty()) {
            User user = userRep.findByLogin(request.getUserLogin()).orElseThrow(USER_NOT_FOUND);
            product.setUser(user);
        }*/
        if (request.getProductName() != null && !request.getProductName().isEmpty())
            product.setProductName(request.getProductName());
        if (request.getPhotoURL() != null && !request.getPhotoURL().isEmpty())
            product.setPhotoURL(request.getPhotoURL());
        if (request.getProducer() != null && !request.getProducer().isEmpty())
            product.setProducer(request.getProducer());
        if (request.getPrice() != null)
            product.setPrice(request.getPrice());
        if (request.getDescription() != null && !request.getDescription().isEmpty())
            product.setDescription(request.getDescription());
        if (request.getCondition() != null)
            product.setCondition(request.getCondition());
        if (request.getShipType() != null)
            product.setShipType(request.getShipType());
        if (request.getCount() != null)
            product.setCount(request.getCount());
        productRepo.save(product);
        return new ProductResponse(product);
    }

    public ProductResponse get(@NotNull Long id) {
        Rating rating = ratingRepo.findByProduct_IdAndUser_Login(id, ((User) SecurityContextHolder.
                getContext().getAuthentication().getPrincipal()).getLogin()).orElse(null);
        if (rating != null)
            return new ProductResponse(productRepo.findById(id).orElseThrow(PRODUCT_NOT_FOUND));
        else
            return new ProductResponse(productRepo.findById(id).orElseThrow(PRODUCT_NOT_FOUND), rating.getRating());
    }

    public Page<ProductResponse> getAll(@NotNull ProductSearchRequest request) {
        return productRepo.findAll(new Specification<Product>() {
            @Nullable
            @Override
            public Predicate toPredicate(Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
                Predicate predicate = null;
                if (request.getSearchString() != null && !request.getSearchString().isEmpty()) {
                    predicate = builder.or(
                            builder.like(root.get("productName"), "%" + request.getSearchString() + "%"),
                            builder.or(
                                    builder.like(root.get("producer"), "%" + request.getSearchString() + "%"),
                                    builder.or(
                                            builder.like(root.get("description"), "%" + request.getSearchString() + "%"),
                                            builder.like(root.get("category").get("categoryName"),
                                                    "%" + request.getSearchString() + "%")
                                    )
                            )
                    );
                }
                if (request.getCondition() != null) {
                    if (predicate == null)
                        predicate = builder.equal(root.get("condition"), request.getCondition());
                    else
                        predicate = builder.and(predicate,
                                builder.equal(root.get("condition"), request.getCondition()));
                }
                if (request.getShipType() != null) {
                    if (predicate == null)
                        predicate = builder.equal(root.get("shipType"), request.getShipType());
                    else
                        predicate = builder.and(predicate,
                                builder.equal(root.get("shipType"), request.getShipType()));
                }
                if (request.getCount() != null) {
                    Predicate countPredicate;
                    if (request.getCountOp() != null && !request.getCountOp().isEmpty()) {
                        switch (request.getCountOp()) {
                            case ">":
                                countPredicate = builder.greaterThan(root.get("count"), request.getCount());
                                break;
                            case "<":
                                countPredicate = builder.lessThan(root.get("count"), request.getCount());
                                break;
                            case ">=":
                                countPredicate = builder.greaterThanOrEqualTo(root.get("count"), request.getCount());
                                break;
                            case "<=":
                                countPredicate = builder.lessThanOrEqualTo(root.get("count"), request.getCount());
                                break;
                            default:
                                countPredicate = builder.equal(root.get("count"), request.getCount());
                        }
                    } else countPredicate = builder.equal(root.get("count"), request.getCount());
                    if (predicate == null)
                        predicate = countPredicate;
                    else
                        predicate = builder.and(predicate, countPredicate);
                }
                if (request.getPrice() != null) {
                    Predicate countPredicate;
                    if (request.getPriceOp() != null && !request.getPriceOp().isEmpty()) {
                        switch (request.getPriceOp()) {
                            case ">":
                                countPredicate = builder.greaterThan(root.get("price"), request.getPrice());
                                break;
                            case "<":
                                countPredicate = builder.lessThan(root.get("price"), request.getPrice());
                                break;
                            case ">=":
                                countPredicate = builder.greaterThanOrEqualTo(root.get("price"), request.getPrice());
                                break;
                            case "<=":
                                countPredicate = builder.lessThanOrEqualTo(root.get("price"), request.getPrice());
                                break;
                            default:
                                countPredicate = builder.equal(root.get("price"), request.getPrice());
                        }
                    } else countPredicate = builder.equal(root.get("price"), request.getPrice());
                    if (predicate == null)
                        predicate = countPredicate;
                    else
                        predicate = builder.and(predicate, countPredicate);
                }
                return predicate;
            }
        }, request.pageable()).map(ProductResponse::new);
    }

    public ProductResponse delete(@Valid Long id) {
        Product product = productRepo.findById(id).orElseThrow(PRODUCT_NOT_FOUND);
        if (!product.getUser().getLogin().equals(((User) SecurityContextHolder.
                getContext().getAuthentication().getPrincipal()).getLogin()))
            throw new IncorrectSessionLoginException("Вы не можете удалить чужой товар!");
        productRepo.delete(product);
        return new ProductResponse(product);
    }
}
