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
import static ru.yandex.praktikum.config.RestConfig.*;

public class OrderGetTest {
    private String email;
    private String password;
    private String name;
    private String accessToken;
    private static UserSteps userSteps;
    private VerificationSteps verificationSteps;
    private OrderSteps orderSteps;
    private static List<String> tokens = new ArrayList<>();

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
    @Description("Проверка получения заказов конкретного пользователя без авторизации пользователя")
    public void checkGetListOfOrdersForASpecificUserWithoutUserAuth() {
        ArrayList<String> ingredients = orderSteps.getIngredients()
                .then()
                .extract().body().path("data._id");
        OrderCreateRequest orderCreateRequest = new OrderCreateRequest(ingredients);
        orderSteps.orderCreateWithUserAuth(orderCreateRequest, accessToken);
        Response response = orderSteps.getListOfOrdersForASpecificUserWithoutUserAuth();

        verificationSteps.checkSuccessAndMessageInResponseBodyAndStatusCode401(response, MESSAGE_IN_RESPONSE_BODY[3]);
    }

    @Test
    @Description("Проверка получения заказов конкретного авторизованного пользователя")
    public void checkGetListOfOrdersForASpecificUserWithUserAuth() {
        ArrayList<String> ingredients = orderSteps.getIngredients()
                .then()
                .extract().body().path("data._id");
        OrderCreateRequest orderCreateRequest = new OrderCreateRequest(ingredients);
        orderSteps.orderCreateWithUserAuth(orderCreateRequest, accessToken);
        Response response = orderSteps.getListOfOrdersForASpecificUserWithUserAuth(accessToken);

        verificationSteps.checkSuccessAndOrdersInResponseBodyAndStatusCode200(response);
    }

    @AfterClass
    public static void deleteTokens() {
        for (int i = 0; i < tokens.size(); i++) {
            userSteps.userDelete(tokens.get(i));
        }
    }
}
