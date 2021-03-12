package com.devtech.controller;

import com.devtech.dto.cartwish.CartWishCURequest;
import com.devtech.dto.cartwish.CartWishResponse;
import com.devtech.dto.product.ProductSearchRequest;
import com.devtech.service.CartWishService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/api/cartwish")
@RequiredArgsConstructor
public class CartWishController {
    private final CartWishService service;

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public CartWishResponse create(@NotNull CartWishCURequest request) {
        return service.create(request);
    }

    @RequestMapping(value = "/update/{id}", method = RequestMethod.PATCH)
    public CartWishResponse update(@PathVariable Long id, @NotNull CartWishCURequest request) {
        return service.update(id, request);
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public CartWishResponse delete(@PathVariable Long id) {
        return service.delete(id);
    }

    @RequestMapping(value = "cart", method = RequestMethod.GET)
    public Page<CartWishResponse> getCart(@NotNull ProductSearchRequest request) {
        return service.getAll(request, false);
    }

    @RequestMapping(value = "wishlist", method = RequestMethod.GET)
    public Page<CartWishResponse> getWishList(@NotNull ProductSearchRequest request) {
        return service.getAll(request, false);
    }
}
