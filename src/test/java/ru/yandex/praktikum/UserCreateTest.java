package ru.yandex.praktikum;

import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static ru.yandex.praktikum.config.RestConfig.*;
import ru.yandex.praktikum.dto.UserCreateRequest;
import ru.yandex.praktikum.dto.UserLoginRequest;
import ru.yandex.praktikum.steps.UserSteps;
import ru.yandex.praktikum.steps.VerificationSteps;

public class UserCreateTest {
    private String email;
    private String password;
    private String name;
    private String accessToken;
    private UserSteps userSteps;
    private VerificationSteps verificationSteps;

    @Before
    public void setUp() {
        email = RandomStringUtils.randomAlphanumeric(10) + "@yandex.ru";
        password = RandomStringUtils.randomAlphanumeric(10);
        name = RandomStringUtils.randomAlphanumeric(10);
        userSteps = new UserSteps();
        verificationSteps = new VerificationSteps();
    }

    @Test
    @Description("Проверка успешного создания пользователя")
    public void checkSuccessfulUserCreation() {
        UserCreateRequest userCreateRequest = new UserCreateRequest(email, password, name);
        Response response = userSteps.userCreate(userCreateRequest);

        verificationSteps.checkSuccessAndAccessTokenInResponseBodyAndStatusCode200(response);
    }

    @Test
    @Description("Проверка создания двух одинаковых пользователей")
    public void checkTheCreationOfTwoIdenticalUsers() {
        UserCreateRequest userCreateRequest = new UserCreateRequest(email, password, name);
        userSteps.userCreate(userCreateRequest);
        Response response = userSteps.userCreate(userCreateRequest);

        verificationSteps.checkSuccessAndMessageInResponseBodyAndStatusCode403(response, MESSAGE_IN_RESPONSE_BODY[0]);
    }

    @Test
    @Description("Проверка создания двух пользователей с одинаковым email")
    public void checkTheCreationOfTwoUsersWithTheSameLogins() {
        String newPassword = RandomStringUtils.randomAlphanumeric(10);
        String newName = RandomStringUtils.randomAlphanumeric(10);
        UserCreateRequest firstUser = new UserCreateRequest(email, password, name);
        UserCreateRequest secondUser = new UserCreateRequest(email, newPassword, newName);
        userSteps.userCreate(firstUser);
        Response response = userSteps.userCreate(secondUser);

        verificationSteps.checkSuccessAndMessageInResponseBodyAndStatusCode403(response, MESSAGE_IN_RESPONSE_BODY[0]);
    }

    @Test
    @Description("Проверкка создания пользователя без передачи email")
    public void checkTheCreationOfUserWithoutEmail() {
        UserCreateRequest userCreateRequest = new UserCreateRequest();
        userCreateRequest.setPassword(password);
        userCreateRequest.setName(name);
        Response response = userSteps.userCreate(userCreateRequest);

        verificationSteps.checkSuccessAndMessageInResponseBodyAndStatusCode403(response, MESSAGE_IN_RESPONSE_BODY[1]);
    }

    @Test
    @Description("Проверка создания пользователя без передачи пароля")
    public void checkTheCreationOfUserWithoutPassword() {
        UserCreateRequest userCreateRequest = new UserCreateRequest();
        userCreateRequest.setEmail(email);
        userCreateRequest.setName(name);
        Response response = userSteps.userCreate(userCreateRequest);

        verificationSteps.checkSuccessAndMessageInResponseBodyAndStatusCode403(response, MESSAGE_IN_RESPONSE_BODY[1]);
    }

    @Test
    @Description("Проверка создания пользователя без передачи имени")
    public void checkTheCreationOfUserWithoutName() {
        UserCreateRequest userCreateRequest = new UserCreateRequest();
        userCreateRequest.setEmail(email);
        userCreateRequest.setPassword(password);
        Response response = userSteps.userCreate(userCreateRequest);

        verificationSteps.checkSuccessAndMessageInResponseBodyAndStatusCode403(response, MESSAGE_IN_RESPONSE_BODY[1]);
    }

    @After
    public void deleteUser() {
        accessToken = userSteps.userLogin(new UserLoginRequest(email, password))
                .then()
                .extract().body().path("accessToken");
        if (accessToken != null) {
            userSteps.userDelete(accessToken);
        }
    }
}