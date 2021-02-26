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

    @NotNull
    @NotEmpty
    @Column(name = "city")
    private String city;

    @NotNull
    @JoinColumn(name = "country_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Country country;

}
