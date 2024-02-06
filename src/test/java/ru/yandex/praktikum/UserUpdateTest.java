package ru.yandex.praktikum;

import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.praktikum.dto.UserCreateRequest;
import ru.yandex.praktikum.dto.UserLoginRequest;
import ru.yandex.praktikum.steps.UserSteps;
import ru.yandex.praktikum.steps.VerificationSteps;
import java.util.ArrayList;
import java.util.List;
import static ru.yandex.praktikum.config.RestConfig.*;

public class UserUpdateTest {

    private String email;
    private String password;
    private String name;
    private String accessToken;
    private static UserSteps userSteps;
    private VerificationSteps verificationSteps;
    private static List<String> tokens = new ArrayList<>();

    @Before
    public void setUp() {
        email = RandomStringUtils.randomAlphanumeric(10) + "@yandex.ru";
        password = RandomStringUtils.randomAlphanumeric(10);
        name = RandomStringUtils.randomAlphanumeric(10);
        userSteps = new UserSteps();
        verificationSteps = new VerificationSteps();
        UserCreateRequest userCreateRequest = new UserCreateRequest(email, password, name);
        accessToken = userSteps.userCreate(userCreateRequest)
                .then()
                .extract().body().path("accessToken");
        tokens.add(accessToken);
    }

    @Test
    @Description("Проверка изменения email авторизованного пользователя")
    public void checkUpdateEmailFieldWithUserAuth() {
        String newEmail = RandomStringUtils.randomAlphanumeric(10).toLowerCase() + "@yandex.ru";
        UserCreateRequest userUpdateRequest = new UserCreateRequest(newEmail, password, name);
        Response response = userSteps.userUpdateWithAuth(userUpdateRequest, accessToken);

        verificationSteps.checkSuccessAndEmailInResponseBodyAndStatusCode200(response, newEmail);
    }

    @Test
    @Description("Проверка изменения имени авторизованного пользователя")
    public void checkUpdateNameFieldWithUserAuth() {
        String newName = RandomStringUtils.randomAlphanumeric(10);
        UserCreateRequest userUpdateRequest = new UserCreateRequest(email, password, newName);
        Response response = userSteps.userUpdateWithAuth(userUpdateRequest, accessToken);

        verificationSteps.checkSuccessAndNameInResponseBodyAndStatusCode200(response, newName);
    }

    @Test
    @Description("Проверка изменения пароля авторизованного пользователя")
    public void checkUpdatePasswordFieldWithUserAuth() {
        String newPassword = RandomStringUtils.randomAlphanumeric(10);
        UserCreateRequest userUpdateRequest = new UserCreateRequest(email, newPassword, name);
        userSteps.userUpdateWithAuth(userUpdateRequest, accessToken);
        Response response = userSteps.userLogin(new UserLoginRequest(email, newPassword));

        verificationSteps.checkSuccessAndAccessTokenInResponseBodyAndStatusCode200(response);
    }

    @Test
    @Description("Проверка отправки запроса без изменения значений email, пароль и имя для авторизованного пользователя")
    public void checkSetAllFieldsToTheCurrentValueWithUserAuth() {
        UserCreateRequest userUpdateRequest = new UserCreateRequest(email, password, name);
        Response response = userSteps.userUpdateWithAuth(userUpdateRequest, accessToken);

        verificationSteps.checkSuccessAndMessageInResponseBodyAndStatusCode403(response, MESSAGE_IN_RESPONSE_BODY[4]);
    }

    @Test
    @Description("Проверка изменения email без авторизации пользователя")
    public void checkUpdateEmailFieldWithoutUserAuth() {
        String newEmail = RandomStringUtils.randomAlphanumeric(10) + "@yandex.ru";
        UserCreateRequest userUpdateRequest = new UserCreateRequest(newEmail, password, name);
        Response response = userSteps.userUpdateWithoutAuth(userUpdateRequest);

        verificationSteps.checkSuccessAndMessageInResponseBodyAndStatusCode401(response, MESSAGE_IN_RESPONSE_BODY[3]);
    }

    @Test
    @Description("Проверка изменения пароля без авторизации пользователя")
    public void checkUpdatePasswordFieldWithoutUserAuth() {
        String newPassword = RandomStringUtils.randomAlphanumeric(10);
        UserCreateRequest userUpdateRequest = new UserCreateRequest(email, newPassword, name);
        Response response = userSteps.userUpdateWithoutAuth(userUpdateRequest);

        verificationSteps.checkSuccessAndMessageInResponseBodyAndStatusCode401(response, MESSAGE_IN_RESPONSE_BODY[3]);
    }

    @Test
    @Description("Проверка изменения имени без авторизации пользователя")
    public void checkUpdateNameFieldWithoutUserAuth() {
        String newName = RandomStringUtils.randomAlphanumeric(10);
        UserCreateRequest userUpdateRequest = new UserCreateRequest(email, password, newName);
        Response response = userSteps.userUpdateWithoutAuth(userUpdateRequest);

        verificationSteps.checkSuccessAndMessageInResponseBodyAndStatusCode401(response, MESSAGE_IN_RESPONSE_BODY[3]);
    }

    @AfterClass
    public static void deleteTokens() {
        for (int i = 0; i < tokens.size(); i++) {
            userSteps.userDelete(tokens.get(i));
        }
    }
}