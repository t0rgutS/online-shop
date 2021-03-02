package com.devtech.service;

import com.devtech.entity.Product;
import com.devtech.entity.Rating;
import com.devtech.entity.User;
import com.devtech.repository.ProductRepository;
import com.devtech.repository.RatingRepository;
import com.devtech.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;

import static com.devtech.exception.ExceptionList.*;

@RestController
@RequiredArgsConstructor
public class RatingService {
    private final RatingRepository ratingRepo;
    private final UserRepository userRepo;
    private final ProductRepository productRepo;

    public void rate(@NotNull Long productId, @NotNull Integer ratingValue) {
        Rating rating = ratingRepo.findByProduct_IdAndUser_Login(productId,
                ((User) SecurityContextHolder.
                        getContext().getAuthentication().getPrincipal()).getLogin()).orElse(null);
        if (rating == null) {
            User user = userRepo.findByLogin(((User) SecurityContextHolder.
                    getContext().getAuthentication().getPrincipal()).getLogin()).orElseThrow(USER_NOT_FOUND);
            Product product = productRepo.findById(productId).orElseThrow(PRODUCT_NOT_FOUND);
            rating = new Rating();
            rating.setUser(user);
            rating.setProduct(product);
        }
        rating.setRating(ratingValue);
        ratingRepo.save(rating);
    }

    public void delete(@NotNull Long productId) {
        Rating rating = ratingRepo.findByProduct_IdAndUser_Login(productId,
                ((User) SecurityContextHolder.
                        getContext().getAuthentication().getPrincipal()).getLogin()).orElseThrow(RATING_NOT_FOUND);
        ratingRepo.delete(rating);
    }
}
