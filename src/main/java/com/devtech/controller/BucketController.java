package com.devtech.controller;

import com.devtech.dto.bucket.BucketCURequest;
import com.devtech.dto.bucket.BucketResponse;
import com.devtech.dto.product.ProductSearchRequest;
import com.devtech.service.BucketService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/buckets")
@RequiredArgsConstructor
public class BucketController {
    private final BucketService service;

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public BucketResponse create(@Valid BucketCURequest request) {
        return service.create(request);
    }

    @RequestMapping(value = "/update/{id}", method = RequestMethod.PATCH)
    public BucketResponse update(@PathVariable Long id, @Valid BucketCURequest request) {
        return service.update(id, request);
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public BucketResponse delete(@PathVariable Long id) {
        return service.delete(id);
    }

    @RequestMapping(method = RequestMethod.GET)
    public Page<BucketResponse> getAll(@Valid ProductSearchRequest request) {
        return service.getAll(request);
    }
}
