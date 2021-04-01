package com.devtech.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Table(name = "cities")
@Entity
@Getter
@Setter
@NoArgsConstructor
public class City {
    @Id
    @NotNull(message = "Укажите идентификатор записи!")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "city_id")
    private Long id;

    @NotNull(message = "Укажите название города!")
    @NotEmpty(message = "Укажите название города!")
    @Column(name = "city")
    private String city;

    @NotNull(message = "Укажите страну!")
    @JoinColumn(name = "country_id")
    @ManyToOne
    private Country country;

}
