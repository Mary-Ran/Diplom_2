package ru.yandex.praktikum;

import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.praktikum.dto.UserCreateRequest;
import ru.yandex.praktikum.dto.UserLoginRequest;
import ru.yandex.praktikum.steps.UserSteps;
import ru.yandex.praktikum.steps.VerificationSteps;
import static ru.yandex.praktikum.config.RestConfig.*;

public class UserLoginTest {
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
        UserCreateRequest userCreateRequest = new UserCreateRequest(email, password, name);
        userSteps.userCreate(userCreateRequest);
    }

    @Test
    public void checkSuccessfulUserAuth() {
        UserLoginRequest userLoginRequest = new UserLoginRequest(email, password);
        Response response = userSteps.userLogin(userLoginRequest);

        verificationSteps.checkSuccessAndAccessTokenInResponseBodyAndStatusCode200(response);
    }

    @Test
    public void checkTheUserAuthWithIncorrectEmail() {
        String newEmail = RandomStringUtils.randomAlphanumeric(10) + "@yandex.ru";
        UserLoginRequest userLoginRequest = new UserLoginRequest(newEmail, password);
        Response response = userSteps.userLogin(userLoginRequest);

        verificationSteps.checkSuccessAndMessageInResponseBodyAndStatusCode401(response, messageInResponseBody[2]);
    }

    @Test
    public void checkTheUserAuthWithIncorrectPassword() {
        String newPassword = RandomStringUtils.randomAlphanumeric(10);
        UserLoginRequest userLoginRequest = new UserLoginRequest(email, newPassword);
        Response response = userSteps.userLogin(userLoginRequest);

        verificationSteps.checkSuccessAndMessageInResponseBodyAndStatusCode401(response, messageInResponseBody[2]);
    }

    @After
    public void deleteUser() {
        accessToken = userSteps.userLogin(new UserLoginRequest(email, password))
                .then()
                .extract().body().path("accessToken");
        userSteps.userDelete(accessToken);
    }
}
