package com.devtech.service;

import com.devtech.dto.bucket.BucketCURequest;
import com.devtech.dto.bucket.BucketResponse;
import com.devtech.dto.product.ProductSearchRequest;
import com.devtech.entity.Bucket;
import com.devtech.entity.Product;
import com.devtech.entity.User;
import com.devtech.exception.AddingYourOwnProductException;
import com.devtech.exception.BucketCountException;
import com.devtech.exception.IncorrectSessionLoginException;
import com.devtech.exception.NoProductsLeftException;
import com.devtech.repository.BucketRepository;
import com.devtech.repository.ProductRepository;
import com.devtech.repository.UserRepository;
import com.devtech.utility.SessionUserData;
import com.sun.org.apache.xpath.internal.operations.Bool;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.validation.constraints.NotNull;

import static com.devtech.exception.ExceptionList.*;

@RestController
@RequiredArgsConstructor
public class BucketService {
    @Resource(name = "sessionUser")
    private SessionUserData sessionUser;

    private final BucketRepository bucketRepo;
    private final UserRepository userRepo;
    private final ProductRepository productRepo;

    public BucketResponse create(@NotNull BucketCURequest request) {
        if (!sessionUser.getLogin().equals(request.getLogin()))
            throw new IncorrectSessionLoginException("Нет доступа!");
        User user = userRepo.findByLogin(request.getLogin()).orElseThrow(USER_NOT_FOUND);
        Product product = productRepo.findById(request.getProductId()).orElseThrow(PRODUCT_NOT_FOUND);
        if (product.getUser().equals(user))
            throw new AddingYourOwnProductException();
        if (product.getCount() - request.getCount() < 0)
            throw new NoProductsLeftException();
        Bucket bucket = new Bucket();
        bucket.setUser(user);
        bucket.setProduct(product);
        bucket.setCount(request.getCount());
        product.setCount(product.getCount() - request.getCount());
        productRepo.save(product);
        bucketRepo.save(bucket);
        return new BucketResponse(bucket);
    }

    public BucketResponse update(@NotNull Long id, @NotNull BucketCURequest request) {
        Bucket bucket = bucketRepo.findById(id).orElseThrow(BUCKET_NOT_FOUND);
        if (!sessionUser.getLogin().equals(bucket.getUser().getLogin()))
            throw new IncorrectSessionLoginException("Нет доступа!");
        if (request.getCount() != null) {
            if (!((request.getCount() > 0 && bucket.getCount() == 0)
                    || (request.getCount() == 0 && bucket.getCount() > 0))) {
                Product product = bucket.getProduct();
                if (product.getCount() + bucket.getCount() - request.getCount() < 0)
                    throw new NoProductsLeftException();
                bucket.setCount(request.getCount());
                product.setCount(product.getCount() + bucket.getCount() - request.getCount());
                productRepo.save(product);
                bucketRepo.save(bucket);
            } else if (request.getCount() == 0 && bucket.getCount() > 0)
                throw new BucketCountException("Вы не можете добавить в корзину 0 единиц товара!");
            else
                throw new BucketCountException("Ошибка: товар в желаемом должен иметь 0 в поле \"Количество\"!");
        }
        return new BucketResponse(bucket);
    }

    public BucketResponse delete(@NotNull Long id) {
        Bucket bucket = bucketRepo.findById(id).orElseThrow(BUCKET_NOT_FOUND);
        if (!bucket.getUser().getLogin().equals(sessionUser.getLogin()))
            throw new IncorrectSessionLoginException("Нет доступа!");
        Product product = bucket.getProduct();
        product.setCount(product.getCount() + bucket.getCount());
        productRepo.save(product);
        bucketRepo.delete(bucket);
        return new BucketResponse(bucket);
    }

    public Page<BucketResponse> getAll(ProductSearchRequest request, Boolean wishList) {
        return bucketRepo.findAll(new Specification<Bucket>() {
            @Nullable
            @Override
            public Predicate toPredicate(Root<Bucket> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
                Predicate predicate = builder.equal(root.get("user").get("login"), sessionUser.getLogin());
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
        }, request.pageable()).map(BucketResponse::new);
    }
}
