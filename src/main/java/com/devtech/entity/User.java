package com.devtech.entity;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Table(name = "users")
@Entity
@Getter
@Setter
@NoArgsConstructor
public class User {
    @Id
    @NotNull(message = "Укажите идентификатор записи!")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @NotNull(message = "Укажите логин!")
    @NotEmpty(message = "Укажите логин!")
    @Column(name = "login", unique = true)
    private String login;

    @NotNull(message = "Укажите пароль!")
    @NotEmpty(message = "Укажите пароль!")
    @Column(name = "pass")
    private String password;

    @Column(name = "surname")
    private String surname;

    @NotNull(message = "Укажите имя!")
    @NotEmpty(message = "Укажите имя!")
    @Column(name = "name")
    private String name;

    @Column(name = "patronymic")
    private String patronymic;

    @Column(name = "birth_date")
    private Long birthDate;

    @Column(name = "gender")
    private Boolean gender;

    @JoinColumn(name = "city_id")
    @ManyToOne
    private City city;

    @Column(name = "phone")
    private String phone;

    @Column(name = "email")
    private String email;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<Product> sold;
}
