package com.devtech.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@Table(name = "cartwish")
@Entity
@Getter
@Setter
@NoArgsConstructor
public class CartWish {
    @Id
    @NotNull(message = "Укажите идентификатор записи!")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cartwish_id")
    private Long id;

    @NotNull(message = "Укажите покупателя!")
    @JoinColumn(name = "user_id")
    @ManyToOne
    private User user;

    @NotNull(message = "Укажите товар!")
    @JoinColumn(name = "product_id")
    @ManyToOne
    private Product product;

    @NotNull(message = "Укажите количество товара!")
    @PositiveOrZero(message = "Количество товара должно быть положительным числом!")
    @Column(name = "count")
    private Integer count;
}
