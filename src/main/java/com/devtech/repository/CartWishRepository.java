package com.devtech.repository;

import com.devtech.entity.CartWish;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CartWishRepository extends JpaRepository<CartWish, Long>, JpaSpecificationExecutor<CartWish> {
    Page<CartWish> findAllByUser_Login(String login, Pageable pageable);
}
