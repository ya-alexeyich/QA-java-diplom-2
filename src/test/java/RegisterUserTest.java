import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import steps.*;
import user.*;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.is;

public class RegisterUserTest {
    private UserModel userModel;
    private UserSteps userSteps;
    private String accessToken;

    @Before
    public void setUp() {
        userSteps = new UserSteps();
        userModel = UserGenerator.getRandom();
    }

    @After
    public void cleanUp() {
        if (accessToken != null && !accessToken.isEmpty()) {
            userSteps.delete(accessToken);
        }
    }

    @Test
    @DisplayName("Тест Создание пользователя")
    @Description("Проверка кода и тела ответа при успешной регистрации")
    public void checkAllResponseFieldAfterRegisterTest() {
        ValidatableResponse response = userSteps.register(userModel);
        accessToken = response.extract().body().jsonPath().get("accessToken");
        String refreshToken = response.extract().body().jsonPath().get("refreshToken");

        response
                .assertThat()
                .statusCode(SC_OK)
                .body("success", is(true))
                .body("user.name", is(userModel.getName()))
                .body("user.email", is(userModel.getEmail().toLowerCase()))
                .body("accessToken", is(accessToken))
                .body("refreshToken", is(refreshToken));

    }

    @Test
    @DisplayName("Тест Создать пользователя и не заполнить одно из обязательных полей")
    @Description("Проверка При регистрации если нет почты, вернётся код ответа 403 Forbidden")
    public void userCanNotBeRegisterWithoutEmailTest() {
        userModel.setEmail(null);
        ValidatableResponse response = userSteps.register(userModel);

        response
                .assertThat()
                .statusCode(SC_FORBIDDEN)
                .body("success", is(false))
                .body("message", is("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Тест Создать пользователя и не заполнить одно из обязательных полей")
    @Description("Проверка При регистрации если нет пароля, вернётся код ответа 403 Forbidden")
    public void userCanNotBeRegisterWithoutPasswordTest() {
        userModel.setPassword(null);
        ValidatableResponse response = userSteps.register(userModel);

        response
                .assertThat()
                .statusCode(SC_FORBIDDEN)
                .body("success", is(false))
                .body("message", is("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Тест Создать пользователя и не заполнить одно из обязательных полей")
    @Description("Проверка При регистрации если нет имени, вернётся код ответа 403 Forbidden")
    public void userCanNotBeRegisterWithoutNameTest() {
        userModel.setName(null);
        ValidatableResponse response = userSteps.register(userModel);

        response
                .assertThat()
                .statusCode(SC_FORBIDDEN)
                .body("success", is(false))
                .body("message", is("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Тест Создать пользователя, который уже зарегистрирован")
    @Description("Проверка При регистрации если пользователь существует, вернётся код ответа 403 Forbidden")
    public void userCanNotBeRegisterSecondTime() {
        accessToken = userSteps.register(userModel)
                .extract().body().jsonPath().get("accessToken");

        ValidatableResponse secondRegister = userSteps.register(userModel);

        secondRegister
                .assertThat()
                .statusCode(SC_FORBIDDEN)
                .body("success", is(false))
                .body("message", is("User already exists"));
    }
}
