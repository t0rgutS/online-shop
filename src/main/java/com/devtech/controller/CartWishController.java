package com.devtech.controller;

import com.devtech.entity.CartWish;
import com.devtech.request_response.cartwish.CartWishCURequest;
import com.devtech.request_response.product.ProductSearchRequest;
import com.devtech.service.CartWishService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

@RestController
@CrossOrigin
@RequestMapping("/api/cartwish")
@RequiredArgsConstructor
public class CartWishController {
    private final CartWishService service;

    @RequestMapping(value = "/check/wishlist/{productId}", method = RequestMethod.GET)
    public ResponseEntity checkWishList(@PathVariable Long productId) {
        if (service.check(productId, true))
            return new ResponseEntity(HttpStatus.OK);
        else
            return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    @RequestMapping(value = "/check/cart/{productId}", method = RequestMethod.GET)
    public ResponseEntity checkCart(@PathVariable Long productId) {
        if (service.check(productId, false))
            return new ResponseEntity(HttpStatus.OK);
        else
            return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public CartWish create(@NotNull CartWishCURequest request) {
        return service.create(request);
    }

    @RequestMapping(value = "/update/{id}", method = RequestMethod.PUT)
    public CartWish update(@PathVariable Long id, @NotNull CartWishCURequest request) {
        return service.update(id, request);
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public CartWish delete(@PathVariable Long id) {
        return service.delete(id);
    }

    @RequestMapping(value = "cart", method = RequestMethod.GET)
    public Page<CartWish> getCart(@NotNull ProductSearchRequest request) {
        return service.getAll(request, false);
    }

    @RequestMapping(value = "wishlist", method = RequestMethod.GET)
    public Page<CartWish> getWishList(@NotNull ProductSearchRequest request) {
        return service.getAll(request, false);
    }
}
