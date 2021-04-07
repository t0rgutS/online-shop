package com.devtech.repository;

import com.devtech.entity.CartWish;
import com.devtech.entity.Product;
import com.devtech.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.function.Predicate;

public interface CartWishRepository extends JpaRepository<CartWish, Long>, JpaSpecificationExecutor<CartWish> {
    Optional<CartWish> findByUserAndProduct(User user, Product product);
}
