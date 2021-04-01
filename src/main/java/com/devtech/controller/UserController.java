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

    @RequestMapping(value = "/update/{id}", method = RequestMethod.PATCH)
    public User update(@PathVariable Long id, @Valid UserCURequest request) {
        return service.update(id, request);
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public User delete(@PathVariable Long id) {
        return service.delete(id);
    }

    @RequestMapping(value = "/get/{login}", method = RequestMethod.GET)
    public User get(@PathVariable String login) {
        return service.get(login);
    }
}
