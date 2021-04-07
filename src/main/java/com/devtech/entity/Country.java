package com.devtech.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@JsonIgnoreProperties(value = {"cities"})
public class Country {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "country_id")
    private Long id;

    @NotNull(message = "Укажите название страны!")
    @NotEmpty(message = "Укажите название страны!")
    @Column(name = "country")
    private String country;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "country")
    private List<City> cities;
}
