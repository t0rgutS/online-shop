package com.devtech.request_response.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserCURequest {
    private String login;
    private String password;
    //private String surname;
    private String name;
    //private String patronymic;
    //private Long birthDate;
    //private Boolean gender;
    private String cityName;
    private String countryName;
    private String phone;
    private String email;
    //private Boolean organization;
}
