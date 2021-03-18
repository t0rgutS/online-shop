package com.devtech.repository;

import com.devtech.entity.Producer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProducerRepository extends JpaRepository<Producer, Long> {
    Optional<Producer> findByProducerName(String producerName);
}
