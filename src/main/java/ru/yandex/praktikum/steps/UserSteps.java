package ru.yandex.praktikum.steps;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import ru.yandex.praktikum.client.RestClient;
import ru.yandex.praktikum.dto.UserCreateRequest;
import ru.yandex.praktikum.dto.UserLoginRequest;
import static ru.yandex.praktikum.config.RestConfig.*;

public class UserSteps extends RestClient {

    @Step("Send POST request to /api/auth/register")
    public Response userCreate(UserCreateRequest userCreateRequest) {
        return getDefaultRequestSpecification()
                .body(userCreateRequest)
                .when()
                .post(USER_CREATE_ENDPOINT);
    }

    @Step("Send POST request to /api/auth/login")
    public Response userLogin(UserLoginRequest userLoginRequest) {
        return getDefaultRequestSpecification()
                .body(userLoginRequest)
                .when()
                .post(USER_LOGIN_ENDPOINT);
    }

    @Step("Send DELETE request to /api/auth/user")
    public Response userDelete(String accessToken) {
        return getDefaultRequestSpecification()
                .header("Authorization", accessToken)
                .when()
                .delete(USER_UPDATE_ENDPOINT);
    }

    @Step("Send PATCH request to /api/auth/user with auth")
    public Response userUpdateWithAuth(UserCreateRequest userCreateRequest, String accessToken) {
        return getDefaultRequestSpecification()
                .header("Authorization", accessToken)
                .body(userCreateRequest)
                .when()
                .patch(USER_UPDATE_ENDPOINT);
    }

    @Step("Send PATCH request to /api/auth/user without auth")
    public Response userUpdateWithoutAuth(UserCreateRequest userCreateRequest) {
        return getDefaultRequestSpecification()
                .body(userCreateRequest)
                .when()
                .patch(USER_UPDATE_ENDPOINT);
    }
}