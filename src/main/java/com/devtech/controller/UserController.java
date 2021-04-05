package com.devtech.controller;

import com.devtech.entity.User;
import com.devtech.jwt.JWTProvider;
import com.devtech.request_response.user.AuthenticationRequest;
import com.devtech.request_response.user.AuthenticationResponse;
import com.devtech.request_response.user.UserCURequest;
import com.devtech.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@RestController
@CrossOrigin
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final JWTProvider jwtProvider;
    private final UserService service;

    @RequestMapping(value = "/auth", method = RequestMethod.POST)
    public ResponseEntity auth(@RequestBody @Valid AuthenticationRequest request) {
        if (service.findUser(request.getLogin(), request.getPassword()))
            return new ResponseEntity<>(new AuthenticationResponse(request.getLogin(),
                    jwtProvider.generateToken(request.getLogin())), HttpStatus.OK);
        return new ResponseEntity<>("Неверный логин или пароль!", HttpStatus.UNAUTHORIZED);
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public User create(@RequestBody @Valid UserCURequest request) {
        return service.create(request);
    }

    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public User update(@Valid UserCURequest request) {
        return service.update(request);
    }

    @RequestMapping(value = "/changepass", method = RequestMethod.PUT)
    public User changePass(@NotNull @NotEmpty String newPassword) {
        return service.changePassword(newPassword);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public User delete() {
        return service.delete();
    }

    @RequestMapping(value = "/get/{login}", method = RequestMethod.GET)
    public User get(@PathVariable String login) {
        return service.get(login);
    }
}
