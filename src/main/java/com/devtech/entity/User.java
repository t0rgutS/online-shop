package com.devtech.entity;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@JsonIgnoreProperties(value = {"sold", "password"})
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

    // @Column(name = "surname")
    // private String surname;

    @NotNull(message = "Укажите свое имя или название компании!")
    @NotEmpty(message = "Укажите свое имя или название компании!")
    @Column(name = "name")
    private String name;

    // @Column(name = "patronymic")
    // private String patronymic;

    // @Column(name = "birth_date")
    // private Long birthDate;

    // @Column(name = "gender")
    //private Boolean gender;

    @JoinColumn(name = "city_id")
    @ManyToOne
    private City city;

    @Column(name = "phone")
    private String phone;

    @Column(name = "email")
    private String email;

    // @NotNull(message = "Укажите, является пользователь частным лицом или представителем организации!")
    // @Column(name = "organization")
    // private Boolean organization;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<Product> sold;

    @Transient
    private Integer rating = 0;

    @Transient
    private Boolean editable = false;
}
