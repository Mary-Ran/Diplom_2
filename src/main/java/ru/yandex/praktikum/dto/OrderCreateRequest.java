package ru.yandex.praktikum.dto;

import lombok.Data;
import java.util.List;

@Data
public class OrderCreateRequest {
    private List<String> ingredients;

    public OrderCreateRequest(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public OrderCreateRequest() {

    }
}