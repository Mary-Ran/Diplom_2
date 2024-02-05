package ru.yandex.praktikum.dto;

import lombok.Data;

@Data
public class UserCreateRequest {
    private String email;
    private String password;
    private String name;

    public UserCreateRequest(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    public UserCreateRequest() {

    }
}
