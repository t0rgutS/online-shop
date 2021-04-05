package com.devtech.service;

import com.devtech.entity.*;
import com.devtech.exception.IncorrectSessionLoginException;
import com.devtech.exception.NoContactsException;
import com.devtech.exception.NotAuthroizedException;
import com.devtech.repository.*;
import com.devtech.request_response.product.ProductCURequest;
import com.devtech.request_response.product.ProductSearchRequest;
import com.devtech.utility.MultipartFileUploader;
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
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import java.util.List;

import static com.devtech.exception.ExceptionList.*;

@RestController
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepo;
    private final CategoryRepository categoryRepo;
    private final UserRepository userRepo;
    private final RatingRepository ratingRepo;
    private final ProducerRepository producerRepo;
    private final MultipartFileUploader uploader;

    @Transactional
    public Product create(@NotNull ProductCURequest request) {
        if (!(SecurityContextHolder.
                getContext().getAuthentication().getPrincipal() instanceof User))
            throw new NotAuthroizedException();
        Category category = categoryRepo.findByCategoryName(request.getCategoryName()).orElseThrow(CATEGORY_NOT_FOUND);
        User user = userRepo.findByLogin(((User) SecurityContextHolder.
                getContext().getAuthentication().getPrincipal()).getLogin()).orElseThrow(USER_NOT_FOUND);
        Producer producer = producerRepo.findByProducerName(request.getProducer()).orElse(null);
        if (producer == null) {
            producer = new Producer();
            producer.setProducerName(request.getProducer());
            producerRepo.save(producer);
        }
        if ((user.getEmail() == null && user.getPhone() == null)
                || (user.getEmail() == null && user.getPhone() != null && user.getPhone().isEmpty())
                || (user.getPhone() == null && user.getEmail() != null && user.getEmail().isEmpty())
                || (user.getEmail().isEmpty() && user.getPhone().isEmpty()))
            throw new NoContactsException();
        Product product = new Product();
        product.setProductName(request.getProductName());
        product.setPhotoURL(uploader.uploadFile(request.getPhoto()));
        product.setProducer(producer);
        product.setPrice(request.getPrice());
        product.setDescription(request.getDescription());
        product.setCondition(request.getCondition());
        product.setShipType(request.getShipType());
        product.setCount(request.getCount());
        product.setCategory(category);
        product.setUser(user);
        productRepo.save(product);
        return product;
    }

    @Transactional
    public Product update(@NotNull Long id, @NotNull ProductCURequest request) {
        if (!(SecurityContextHolder.
                getContext().getAuthentication().getPrincipal() instanceof User))
            throw new NotAuthroizedException();
        Product product = productRepo.findById(id).orElseThrow(PRODUCT_NOT_FOUND);
        if (!product.getUser().getLogin().equals(((User) SecurityContextHolder.
                getContext().getAuthentication().getPrincipal()).getLogin()))
            throw new IncorrectSessionLoginException("Вы не можете редактировать чужой товар!");
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
        if (request.getPhoto() != null)
            product.setPhotoURL(uploader.uploadFile(request.getPhoto()));
        if (request.getProducer() != null && !request.getProducer().isEmpty()) {
            Producer producer = producerRepo.findByProducerName(request.getProducer()).orElse(null);
            if (producer == null) {
                producer = new Producer();
                producer.setProducerName(request.getProducer());
                producerRepo.save(producer);
            }
            product.setProducer(producer);
        }
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
        return product;
    }

    public Product get(@NotNull Long id) {
        Rating rating;
        if (SecurityContextHolder.
                getContext().getAuthentication().getPrincipal() instanceof User)
            rating = ratingRepo.findByProduct_IdAndUser_Login(id, ((User) SecurityContextHolder.
                    getContext().getAuthentication().getPrincipal()).getLogin()).orElse(null);
        else
            rating = null;
        Product product;
        if (rating != null) {
            product = rating.getProduct();
            product.setRating(rating.getRating());
        } else {
            product = productRepo.findById(id).orElseThrow(PRODUCT_NOT_FOUND);
            List<Rating> ratings = ratingRepo.findAllByProduct_Id(product.getId());
            if (ratings.size() > 0)
                product.setRating((int) Math.round(ratings.stream().mapToDouble(Rating::getRating).sum()
                        / ratings.size()));
        }
        if (SecurityContextHolder.
                getContext().getAuthentication().getPrincipal() instanceof User)
            if (product.getUser().getLogin().equals(((User) SecurityContextHolder.
                    getContext().getAuthentication().getPrincipal()).getLogin()))
                product.setEditable(true);
        return product;
    }

    public Page<Product> getAll(@NotNull ProductSearchRequest request) {
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
                if (request.isCurrentUser()) {
                    if (SecurityContextHolder.
                            getContext().getAuthentication().getPrincipal() instanceof User)
                        if (predicate == null)
                            predicate = builder.equal(root.get("user").get("login"), ((User) SecurityContextHolder.
                                    getContext().getAuthentication().getPrincipal()).getLogin());
                        else
                            predicate = builder.and(predicate, builder.equal(root.get("user").get("login"), ((User) SecurityContextHolder.
                                    getContext().getAuthentication().getPrincipal()).getLogin()));
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
        }, request.pageable());
    }

    public Product delete(@Valid Long id) {
        if (!(SecurityContextHolder.
                getContext().getAuthentication().getPrincipal() instanceof User))
            throw new NotAuthroizedException();
        Product product = productRepo.findById(id).orElseThrow(PRODUCT_NOT_FOUND);
        if (!product.getUser().getLogin().equals(((User) SecurityContextHolder.
                getContext().getAuthentication().getPrincipal()).getLogin()))
            throw new IncorrectSessionLoginException("Вы не можете удалить чужой товар!");
        productRepo.delete(product);
        return product;
    }
}
