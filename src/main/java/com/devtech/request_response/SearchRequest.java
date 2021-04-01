package com.devtech.request_response;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@Getter
@Setter
public class SearchRequest {
    @NotNull
    @PositiveOrZero(message = "Номер страницы должен быть больше или равен нулю!")
    protected Integer page = 0;

    @NotNull
    @PositiveOrZero(message = "Число записей на странице должно быть больше или равно нулю!")
    protected Integer size = 10;

    public Pageable pageable() {
        return PageRequest.of(page, size);
    }

}
