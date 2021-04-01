package com.devtech.controller;

import com.devtech.entity.Category;
import com.devtech.entity.City;
import com.devtech.entity.Producer;
import com.devtech.repository.CategoryRepository;
import com.devtech.repository.CityRepository;
import com.devtech.repository.ProducerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/utils")
@RequiredArgsConstructor
public class UtilEntitiesController {
    private final ProducerRepository producerRepo;
    private final CategoryRepository categoryRepo;
    private final CityRepository cityRepo;

    @RequestMapping(value = "/producers", method = RequestMethod.GET)
    public List<Producer> getProducers() {
        return producerRepo.findAll();
    }

    @RequestMapping(value = "/categories", method = RequestMethod.GET)
    public List<Category> getCategories() {
        return categoryRepo.findAll();
    }

    @RequestMapping(value = "/cities", method = RequestMethod.GET)
    public List<City> getCities() { return cityRepo.findAll(); }
}
