package com.devtech.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Table(name = "ratings")
@Entity
@Getter
@Setter
@NoArgsConstructor
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rating_id")
    private Long id;

    @NotNull(message = "Укажите пользователя!")
    @JoinColumn(name = "user_id")
    @ManyToOne
    private User user;

    @NotNull(message = "Укажите товар!")
    @JoinColumn(name = "product_id")
    @ManyToOne
    private Product product;

    @Positive(message = "Вы не можете поставить меньше 1 звезды!")
    @Max(value = 5, message = "Нельзя поставить больше 5 звезд!")
    private Integer rating = 0;
}
