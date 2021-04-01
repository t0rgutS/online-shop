package com.devtech.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;

@Table(name = "products")
@Entity
@Getter
@Setter
@NoArgsConstructor
public class Product {
    @Id
    @NotNull(message = "Укажите идентификатор записи!")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;

    @NotNull(message = "Укажите название продукта!")
    @NotEmpty(message = "Укажите название продукта!")
    @Size(min = 5, message = "Слишком короткое название продукта!")
    @Size(max = 200, message = "Слишком длинное название продукта!")
    @Column(name = "product_name")
    private String productName;

    @NotNull(message = "Приложите фото товара!")
    @NotEmpty(message = "Приложите фото товара!")
    @Column(name = "photo_url")
    private String photoURL;

    @NotNull(message = "Укажите производителя!")
    @JoinColumn(name = "producer_id")
    @ManyToOne
    private Producer producer;

    @NotNull(message = "Укажите цену товара!")
    @Positive(message = "Цена должна быть положительным числом!")
    @Column(name = "price")
    private BigDecimal price;

    @NotNull(message = "Приложите краткое описание товара!")
    @NotEmpty(message = "Приложите краткое описание товара!")
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @NotNull(message = "Укажите состояние товара!")
    @Column(name = "condition")
    private Boolean condition;

    @NotNull(message = "Укажите тип доставки!")
    @Column(name = "ship_type")
    private Boolean shipType;

    @NotNull(message = "Укажите количество единиц товара!")
    @PositiveOrZero(message = "Количество единиц товара должно быть >= 0!")
    @Column(name = "count")
    private Integer count;

    @NotNull(message = "Укажите категорию товара!")
    @JoinColumn(name = "category_id")
    @ManyToOne
    private Category category;

    @NotNull(message = "Укажите продавца!")
    @JoinColumn(name = "user_id")
    @ManyToOne
    private User user;

    @Transient
    private Boolean editable = false;

    @Transient
    private Integer rating = 0;
}
