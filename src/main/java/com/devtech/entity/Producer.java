package com.devtech.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Table(name = "producers")
@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(value = {"products"})
public class Producer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "producer_id")
    private Long id;

    @NotNull(message = "Укажите производителя!")
    @NotEmpty(message = "Укажите производителя!")
    @Column(name = "producer_name", unique = true)
    private String producerName;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "producer")
    private List<Product> products;
}
