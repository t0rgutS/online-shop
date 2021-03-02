package com.devtech.controller;

import com.devtech.dto.user.AuthenticationRequest;
import com.devtech.dto.user.UserCURequest;
import com.devtech.dto.user.UserResponse;
import com.devtech.jwt.JWTProvider;
import com.devtech.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final JWTProvider jwtProvider;
    private final UserService service;

    @RequestMapping(value = "/auth", method = RequestMethod.POST)
    public ResponseEntity<String> auth(@RequestBody @Valid AuthenticationRequest request) {
        if (service.findUser(request.getLogin(), request.getPassword()))
            return new ResponseEntity<>(jwtProvider.generateToken(request.getLogin()), HttpStatus.OK);
        return new ResponseEntity<>("Неверный логин или паролЬ!", HttpStatus.UNAUTHORIZED);
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public UserResponse create(@RequestBody @Valid UserCURequest request) {
        return service.create(request);
    }

    @RequestMapping(value = "/update/{id}", method = RequestMethod.PATCH)
    public UserResponse update(@PathVariable Long id, @Valid UserCURequest request) {
        return service.update(id, request);
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public UserResponse delete(@PathVariable Long id) {
        return service.delete(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public UserResponse get(@PathVariable Long id) {
        return service.get(id);
    }
}
