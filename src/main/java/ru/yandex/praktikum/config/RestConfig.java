package ru.yandex.praktikum.config;

public class RestConfig {
    public static final String BASE_URI = "https://stellarburgers.nomoreparties.site/";
    public static final String USER_CREATE_ENDPOINT = "/api/auth/register";
    public static final String USER_LOGIN_ENDPOINT = "/api/auth/login";
    public static final String USER_UPDATE_ENDPOINT = "/api/auth/user";
    public static final String ORDER_ENDPOINT = "/api/orders";
    public static final String INGREDIENTS_ENDPOINT = "/api/ingredients";
    public static final String[] messageInResponseBody = {
            "User already exists",
            "Email, password and name are required fields",
            "email or password are incorrect",
            "You should be authorised",
            "User with such email already exists",
            "Ingredient ids must be provided"
    };
}
