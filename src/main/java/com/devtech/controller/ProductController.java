package com.devtech.controller;

import com.devtech.entity.Product;
import com.devtech.request_response.product.ProductCURequest;
import com.devtech.request_response.product.ProductSearchRequest;
import com.devtech.service.ProductService;
import com.devtech.service.RatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@CrossOrigin
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService service;
    private final RatingService ratingService;

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public Product create(@RequestBody @Valid ProductCURequest request) {
        return service.create(request);
    }

    @RequestMapping(value = "/update/{id}", method = RequestMethod.PUT)
    public Product update(@PathVariable Long id, @RequestBody @Valid ProductCURequest request) {
        return service.update(id, request);
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public Product delete(@PathVariable Long id) {
        return service.delete(id);
    }

    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public Page<Product> getAll(@Valid ProductSearchRequest request) {
        return service.getAll(request);
    }

    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    public Product get(@PathVariable Long id) {
        return service.get(id);
    }

    @RequestMapping(value = "/get/{id}/rate/{rating}", method = RequestMethod.POST)
    public void rate(@PathVariable Long id, @PathVariable Integer rating) {
        ratingService.rate(id, rating);
    }

    @RequestMapping(value = "/get/{id}/derate", method = RequestMethod.DELETE)
    public void removeRate(@PathVariable Long id) {
        ratingService.delete(id);
    }
}
