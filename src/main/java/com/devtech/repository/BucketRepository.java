package com.devtech.repository;

import com.devtech.entity.Bucket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface BucketRepository extends JpaRepository<Bucket, Long>, JpaSpecificationExecutor<Bucket> {
    Page<Bucket> findAllByUser_Login(String login, Pageable pageable);
}
