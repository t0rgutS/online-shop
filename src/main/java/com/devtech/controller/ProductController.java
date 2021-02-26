package com.devtech.controller;

import com.devtech.dto.product.ProductCURequest;
import com.devtech.dto.product.ProductResponse;
import com.devtech.dto.product.ProductSearchRequest;
import com.devtech.service.ProductService;
import com.devtech.service.RatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService service;
    private final RatingService ratingService;

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ProductResponse create(@Valid ProductCURequest request) {
        return service.create(request);
    }

    @RequestMapping(value = "/update/{id}", method = RequestMethod.PATCH)
    public ProductResponse update(@PathVariable Long id, @Valid ProductCURequest request) {
        return service.update(id, request);
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public ProductResponse delete(@PathVariable Long id) {
        return service.delete(id);
    }

    @RequestMapping(method = RequestMethod.GET)
    public Page<ProductResponse> getAll(@Valid ProductSearchRequest request) {
        return service.getAll(request);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ProductResponse get(@PathVariable Long id) {
        return service.get(id);
    }

    @RequestMapping(value = "/{id}/rate{rating}", method = RequestMethod.POST)
    public void rate(@PathVariable Long id, @PathVariable Integer rating) {
        ratingService.rate(id, rating);
    }

    @RequestMapping(value = "/{id}/derate", method = RequestMethod.DELETE)
    public void removeRate(@PathVariable Long id) {
        ratingService.delete(id);
    }
}
