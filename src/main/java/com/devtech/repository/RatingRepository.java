package com.devtech.repository;

import com.devtech.entity.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface RatingRepository extends JpaRepository<Rating, Long>, JpaSpecificationExecutor<Rating> {
    List<Rating> findAllByProduct_User_Login(String userLogin);

    Optional<Rating> findByProduct_IdAndUser_Login(Long productId, String userLogin);
}
