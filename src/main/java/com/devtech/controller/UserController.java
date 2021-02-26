package com.devtech.controller;

import com.devtech.dto.user.UserCURequest;
import com.devtech.dto.user.UserResponse;
import com.devtech.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService service;

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public UserResponse create(@Valid UserCURequest request) {
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
