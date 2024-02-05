package ru.yandex.praktikum.client;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;
import static ru.yandex.praktikum.config.RestConfig.BASE_URI;

public abstract class RestClient {
    @Step("Set 'contentType' and 'baseURI'")
    public RequestSpecification getDefaultRequestSpecification() {
        return given()
                .contentType(ContentType.JSON)
                .baseUri(BASE_URI);
    }
}
