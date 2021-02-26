package com.devtech.exception;

import lombok.experimental.UtilityClass;

import java.util.function.Supplier;

@UtilityClass
public class ExceptionList {
    public static final Supplier<NotFoundException> CATEGORY_NOT_FOUND = ()
            -> new NotFoundException("Неизвестная категория товаров!");
    public static final Supplier<NotFoundException> USER_NOT_FOUND = ()
            -> new NotFoundException("Указанный пользователь не найден!");
    public static final Supplier<NotFoundException> PRODUCT_NOT_FOUND = ()
            -> new NotFoundException("Указанный товар не найден!");
    public static final Supplier<NotFoundException> CITY_NOT_FOUND = ()
            -> new NotFoundException("Указанный город не найден!");
    public static final Supplier<NotFoundException> BUCKET_NOT_FOUND = ()
            -> new NotFoundException("Вы не заказывали этот товар!");
    public static final Supplier<NotFoundException> RATING_NOT_FOUND = ()
            -> new NotFoundException("Вы не ставили этому товару оценку!");
}
