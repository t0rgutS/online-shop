package com.devtech.dto.user;

import com.devtech.dto.SearchRequest;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
@RequiredArgsConstructor
public class UserSearchRequest extends SearchRequest {
    private String surname;
    private String name;
    private String patronymic;
    private Date birthDate;
    private Boolean gender;
    private String cityName;
    private String countryName;
    private String phone;
    private String email;
    private Long soldId;
}
