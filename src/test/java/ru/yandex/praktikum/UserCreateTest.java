package ru.yandex.praktikum;

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
    public void checkSuccessfulUserCreation() {
        UserCreateRequest userCreateRequest = new UserCreateRequest(email, password, name);
        Response response = userSteps.userCreate(userCreateRequest);

        verificationSteps.checkSuccessAndAccessTokenInResponseBodyAndStatusCode200(response);
    }

    @Test
    public void checkTheCreationOfTwoIdenticalUsers() {
        UserCreateRequest userCreateRequest = new UserCreateRequest(email, password, name);
        userSteps.userCreate(userCreateRequest);
        Response response = userSteps.userCreate(userCreateRequest);

        verificationSteps.checkSuccessAndMessageInResponseBodyAndStatusCode403(response, messageInResponseBody[0]);
    }

    @Test
    public void checkTheCreationOfTwoUsersWithTheSameLogins() {
        String newPassword = RandomStringUtils.randomAlphanumeric(10);
        String newName = RandomStringUtils.randomAlphanumeric(10);
        UserCreateRequest firstUser = new UserCreateRequest(email, password, name);
        UserCreateRequest secondUser = new UserCreateRequest(email, newPassword, newName);
        userSteps.userCreate(firstUser);
        Response response = userSteps.userCreate(secondUser);

        verificationSteps.checkSuccessAndMessageInResponseBodyAndStatusCode403(response, messageInResponseBody[0]);
    }

    @Test
    public void checkTheCreationOfUserWithoutEmail() {
        UserCreateRequest userCreateRequest = new UserCreateRequest();
        userCreateRequest.setPassword(password);
        userCreateRequest.setName(name);
        Response response = userSteps.userCreate(userCreateRequest);

        verificationSteps.checkSuccessAndMessageInResponseBodyAndStatusCode403(response, messageInResponseBody[1]);
    }

    @Test
    public void checkTheCreationOfUserWithoutPassword() {
        UserCreateRequest userCreateRequest = new UserCreateRequest();
        userCreateRequest.setEmail(email);
        userCreateRequest.setName(name);
        Response response = userSteps.userCreate(userCreateRequest);

        verificationSteps.checkSuccessAndMessageInResponseBodyAndStatusCode403(response, messageInResponseBody[1]);
    }

    @Test
    public void checkTheCreationOfUserWithoutName() {
        UserCreateRequest userCreateRequest = new UserCreateRequest();
        userCreateRequest.setEmail(email);
        userCreateRequest.setPassword(password);
        Response response = userSteps.userCreate(userCreateRequest);

        verificationSteps.checkSuccessAndMessageInResponseBodyAndStatusCode403(response, messageInResponseBody[1]);
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