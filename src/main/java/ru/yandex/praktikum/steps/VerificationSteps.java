package ru.yandex.praktikum.steps;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class VerificationSteps {

    @Step("Check 'success' and 'accessToken' in response body and status code 200")
    public void checkSuccessAndAccessTokenInResponseBodyAndStatusCode200(Response response) {
        response.then()
                .statusCode(SC_OK)
                .and()
                .assertThat().body("success", is(true))
                .and()
                .assertThat().body("accessToken", notNullValue());
    }

    @Step("Check 'success' and 'message' in response body and status code 403")
    public void checkSuccessAndMessageInResponseBodyAndStatusCode403(Response response, String message) {
        response.then()
                .statusCode(SC_FORBIDDEN)
                .and()
                .assertThat().body("success", is(false))
                .and()
                .assertThat().body("message", is(message));
    }

    @Step("Check 'success' and 'message' in response body and status code 401")
    public void checkSuccessAndMessageInResponseBodyAndStatusCode401(Response response, String message) {
        response.then()
                .statusCode(SC_UNAUTHORIZED)
                .and()
                .assertThat().body("success", is(false))
                .and()
                .assertThat().body("message", is(message));
    }

    @Step("Check 'success' and 'email' in response body and status code 200")
    public void checkSuccessAndEmailInResponseBodyAndStatusCode200(Response response, String email) {
        response.then()
                .statusCode(SC_OK)
                .and()
                .assertThat().body("success", is(true))
                .and()
                .assertThat().body("user.email", is(email));
    }

    @Step("Check 'success' and 'name' in response body and status code 200")
    public void checkSuccessAndNameInResponseBodyAndStatusCode200(Response response, String name) {
        response.then()
                .statusCode(SC_OK)
                .and()
                .assertThat().body("success", is(true))
                .and()
                .assertThat().body("user.name", is(name));
    }

    @Step("Check 'success' and 'order.number' in response body and status code 200")
    public void checkSuccessAndOrderNumberInResponseBodyAndStatusCode200(Response response) {
        response.then()
                .statusCode(SC_OK)
                .and()
                .assertThat().body("success", is(true))
                .and()
                .assertThat().body("order.number", notNullValue());
    }

    @Step("Check 'success' and 'orders' in response body and status code 200")
    public void checkSuccessAndOrdersInResponseBodyAndStatusCode200(Response response) {
        response.then()
                .statusCode(SC_OK)
                .and()
                .assertThat().body("success", is(true))
                .and()
                .assertThat().body("orders", notNullValue());
    }

    @Step("Check 'success' and 'message' in response body and status code 400")
    public void checkSuccessAndMessageInResponseBodyAndStatusCode400(Response response, String message) {
        response.then()
                .statusCode(SC_BAD_REQUEST)
                .and()
                .assertThat().body("success", is(false))
                .and()
                .assertThat().body("message", is(message));
    }

    @Step("Check status code 500")
    public void checkStatusCode500(Response response) {
        response.then()
                .statusCode(SC_INTERNAL_SERVER_ERROR);
    }
}
