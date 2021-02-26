package com.devtech.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Table(name = "countries")
@Entity
@Getter
@Setter
@NoArgsConstructor
public class Country {
    @Id
    @NotNull(message = "Укажите идентификатор записи!")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "country_id")
    private Long id;

    @NotNull
    @NotEmpty
    @Column(name = "country")
    private String country;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "city")
    private List<City> cities;
}
