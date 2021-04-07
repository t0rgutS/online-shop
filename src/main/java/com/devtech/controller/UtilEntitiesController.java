package com.devtech.controller;

import com.devtech.entity.Category;
import com.devtech.entity.City;
import com.devtech.entity.Country;
import com.devtech.entity.Producer;
import com.devtech.repository.CategoryRepository;
import com.devtech.repository.CityRepository;
import com.devtech.repository.CountryRepository;
import com.devtech.repository.ProducerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/utils")
@RequiredArgsConstructor
public class UtilEntitiesController {
    private final ProducerRepository producerRepo;
    private final CategoryRepository categoryRepo;
    private final CityRepository cityRepo;
    private final CountryRepository countryRepo;

    @RequestMapping(value = "/producers", method = RequestMethod.GET)
    public List<Producer> getProducers() {
        return producerRepo.findAll();
    }

    @RequestMapping(value = "/categories", method = RequestMethod.GET)
    public List<Category> getCategories() {
        return categoryRepo.findAll();
    }

    @RequestMapping(value = "/countries", method = RequestMethod.GET)
    public List<Country> getCountries() {
        return countryRepo.findAll();
    }

    @RequestMapping(value = "/cities/{country}", method = RequestMethod.GET)
    public List<City> getCities(@PathVariable String country) {
        return cityRepo.findAllByCountry_Country(country);
    }
}
