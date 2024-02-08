package ru.yandex.praktikum.steps;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import ru.yandex.praktikum.client.RestClient;
import ru.yandex.praktikum.dto.OrderCreateRequest;
import static ru.yandex.praktikum.config.RestConfig.*;

public class OrderSteps extends RestClient {
    @Step("Send GET request to /api/ingredients")
    public Response getIngredients() {
        return getDefaultRequestSpecification()
                .when()
                .get(INGREDIENTS_ENDPOINT);
    }

    @Step("Send POST request to /api/orders with user auth")
    public Response orderCreateWithUserAuth(OrderCreateRequest orderCreateRequest, String accessToken) {
        return getDefaultRequestSpecification()
                .header("Authorization", accessToken)
                .body(orderCreateRequest)
                .when()
                .post(ORDER_ENDPOINT);
    }

    @Step("Send POST request to /api/orders without user auth")
    public Response orderCreateWithoutUserAuth(OrderCreateRequest orderCreateRequest) {
        return getDefaultRequestSpecification()
                .body(orderCreateRequest)
                .when()
                .post(ORDER_ENDPOINT);
    }

    @Step("Send GET request to /api/orders with user auth")
    public Response getListOfOrdersForASpecificUserWithUserAuth(String accessToken) {
        return getDefaultRequestSpecification()
                .header("Authorization", accessToken)
                .when()
                .get(ORDER_ENDPOINT);
    }

    @Step("Send GET request to /api/orders without user auth")
    public Response getListOfOrdersForASpecificUserWithoutUserAuth() {
        return getDefaultRequestSpecification()
                .when()
                .get(ORDER_ENDPOINT);
    }
}