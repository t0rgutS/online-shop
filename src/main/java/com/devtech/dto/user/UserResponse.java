package com.devtech.dto.user;

import com.devtech.entity.User;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
public class UserResponse {
    private Long id;
    private String surname;
    private String name;
    private String patronymic;
    private Long birthDate;
    private Boolean gender;
    private String cityName;
    private String countryName;
    private String phone;
    private String email;
    private Double rating = 0d;

    public UserResponse(User user) {
        this.id = user.getId();
        this.surname = user.getSurname();
        this.name = user.getName();
        this.patronymic = user.getPatronymic();
        this.birthDate = user.getBirthDate();
        this.gender = user.getGender();
        this.cityName = user.getCity().getCity();
        this.countryName = user.getCity().getCountry().getCountry();
        this.phone = user.getPhone();
        this.email = user.getEmail();
    }

    public UserResponse(User user, Double rating) {
        this(user);
        this.rating = rating;
    }
}
