package com.devtech.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Table(name = "categories")
@Entity
@Getter
@Setter
@NoArgsConstructor
public class Category {
    @Id
    @NotNull(message = "Укажите идентификатор записи!")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long id;

    @NotNull(message = "Укажите название категории!")
    @NotEmpty(message = "Укажите название категории!")
    @Column(name = "category_name", unique = true)
    private String categoryName;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "product")
    private List<Product> products;
}
