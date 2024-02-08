package ru.yandex.praktikum;

import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.praktikum.dto.OrderCreateRequest;
import ru.yandex.praktikum.dto.UserCreateRequest;
import ru.yandex.praktikum.steps.OrderSteps;
import ru.yandex.praktikum.steps.UserSteps;
import ru.yandex.praktikum.steps.VerificationSteps;
import java.util.ArrayList;
import java.util.List;

import static ru.yandex.praktikum.config.RestConfig.MESSAGE_IN_RESPONSE_BODY;

public class OrderCreateTest {
    private static UserSteps userSteps;
    private static List<String> tokens = new ArrayList<>();
    private String email;
    private String password;
    private String name;
    private String accessToken;
    private VerificationSteps verificationSteps;
    private OrderSteps orderSteps;

    @Before
    public void setUp() {
        email = RandomStringUtils.randomAlphanumeric(10) + "@yandex.ru";
        password = RandomStringUtils.randomAlphanumeric(10);
        name = RandomStringUtils.randomAlphanumeric(10);
        userSteps = new UserSteps();
        verificationSteps = new VerificationSteps();
        orderSteps = new OrderSteps();
        UserCreateRequest userCreateRequest = new UserCreateRequest(email, password, name);
        accessToken = userSteps.userCreate(userCreateRequest)
                .then()
                .extract().body().path("accessToken");
        tokens.add(accessToken);
    }

    @Test
    @Description("Проверка создания заказа с тремя ингредиентами и авторизованным пользователем")
    public void checkOrderCreateWithUserAuthAndThreeIngredientsInTheList() {
        ArrayList<String> ingredients = orderSteps.getIngredients()
                .then()
                .extract().body().path("data._id");
        OrderCreateRequest orderCreateRequest = new OrderCreateRequest(ingredients);
        Response response = orderSteps.orderCreateWithUserAuth(orderCreateRequest, accessToken);

        verificationSteps.checkSuccessAndOrderNumberInResponseBodyAndStatusCode200(response);
    }

    @Test
    @Description("Проверка создания заказа с пустым списком ингредиентов и авторизованным пользователем")
    public void checkOrderCreateWithUserAuthAndEmptyIngredientsList() {
        ArrayList<String> ingredients = new ArrayList<>();
        OrderCreateRequest orderCreateRequest = new OrderCreateRequest(ingredients);
        Response response = orderSteps.orderCreateWithUserAuth(orderCreateRequest, accessToken);

        verificationSteps.checkSuccessAndMessageInResponseBodyAndStatusCode400(response, MESSAGE_IN_RESPONSE_BODY[5]);
    }

    @Test
    @Description("Проверка создания заказа с неверным хешем ингредиента и авторизованным пользователем")
    public void checkOrderCreateWithUserAuthAndInvalidIngredientHashInTheList() {
        String invalidIngredientHash = RandomStringUtils.randomAlphanumeric(15);
        ArrayList<String> ingredients = orderSteps.getIngredients()
                .then()
                .extract().body().path("data._id");
        ingredients.add(invalidIngredientHash);
        OrderCreateRequest orderCreateRequest = new OrderCreateRequest(ingredients);
        Response response = orderSteps.orderCreateWithUserAuth(orderCreateRequest, accessToken);

        verificationSteps.checkStatusCode500(response);
    }

    @Test
    @Description("Проверка создания заказа с тремя ингредиентами и без авторизации пользователя")
    public void checkOrderCreateWithoutUserAuthAndThreeIngredientsInTheList() {
        ArrayList<String> ingredients = orderSteps.getIngredients()
                .then()
                .extract().body().path("data._id");
        OrderCreateRequest orderCreateRequest = new OrderCreateRequest(ingredients);
        Response response = orderSteps.orderCreateWithoutUserAuth(orderCreateRequest);

        verificationSteps.checkSuccessAndOrderNumberInResponseBodyAndStatusCode200(response);
    }

    @Test
    @Description("Проверка создания заказа с пустым списком ингредиентов и без авторизации пользователя")
    public void checkOrderCreateWithoutUserAuthAndEmptyIngredientsList() {
        ArrayList<String> ingredients = new ArrayList<>();
        OrderCreateRequest orderCreateRequest = new OrderCreateRequest(ingredients);
        Response response = orderSteps.orderCreateWithoutUserAuth(orderCreateRequest);

        verificationSteps.checkSuccessAndMessageInResponseBodyAndStatusCode400(response, MESSAGE_IN_RESPONSE_BODY[5]);
    }

    @Test
    @Description("Проверка создания заказа с неверным хешем ингредиента и без авторизации пользователя")
    public void checkOrderCreateWithoutUserAuthAndInvalidIngredientHashInTheList() {
        String invalidIngredientHash = RandomStringUtils.randomAlphanumeric(15);
        ArrayList<String> ingredients = orderSteps.getIngredients()
                .then()
                .extract().body().path("data._id");
        ingredients.add(invalidIngredientHash);
        OrderCreateRequest orderCreateRequest = new OrderCreateRequest(ingredients);
        Response response = orderSteps.orderCreateWithoutUserAuth(orderCreateRequest);

        verificationSteps.checkStatusCode500(response);
    }

    @AfterClass
    public static void deleteTokens() {
        for (int i = 0; i < tokens.size(); i++) {
            userSteps.userDelete(tokens.get(i));
        }
    }
}