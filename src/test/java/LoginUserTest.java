import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import steps.*;
import user.*;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.is;

public class LoginUserTest {
    private UserModel userModel;
    private UserSteps userSteps;
    private String accessToken;

    @Before
    public void setUp() {
        userSteps = new UserSteps();
        userModel = UserGenerator.getRandom();
        accessToken = userSteps.register(userModel).extract().body().jsonPath().get("accessToken");
    }

    @After
    public void cleanUp() {
        if (accessToken != null && !accessToken.isEmpty()) {
            userSteps.delete(accessToken);
        }
    }

    @Test
    @DisplayName("Тест Авторизация пользователя")
    public void userCanBeLoginWithValidDataTest() {
        userSteps.login(UserCredentials.from(userModel))
                .assertThat()
                .statusCode(SC_OK)
                .body("success", is(true));
    }

    @Test
    @DisplayName("Тест Тело ответа сервера при успешной авторизации")
    public void checkAllResponseFieldAfterSuccessLoginTest() {
        ValidatableResponse response = userSteps.login(UserCredentials.from(userModel));
        String accessTokenLogin = response.extract().body().jsonPath().get("accessToken");
        String refreshToken = response.extract().body().jsonPath().get("refreshToken");

        response
                .assertThat()
                .statusCode(SC_OK)
                .body("success", is(true))
                .body("user.name", is(userModel.getName()))
                .body("user.email", is(userModel.getEmail().toLowerCase()))
                .body("accessToken", is(accessTokenLogin))
                .body("refreshToken", is(refreshToken));
    }

    @Test
    @DisplayName("Тест При авторизации если логин неверный, вернётся код ответа 401 Unauthorized")
    public void userCanNotBeLoginWithIncorrectEmailTest() {
        userSteps.login(UserCredentials.getCredentialsWithIncorrectEmail(userModel))
                .assertThat()
                .statusCode(SC_UNAUTHORIZED)
                .body("success", is(false))
                .body("message", is("email or password are incorrect"));
    }

    @Test
    @DisplayName("Тест При авторизации если пароль неверный, вернётся код ответа 401 Unauthorized")
    public void userCanNotBeLoginWithIncorrectPasswordTest() {
        userSteps.login(UserCredentials.getCredentialsWithIncorrectPassword(userModel))
                .assertThat()
                .statusCode(SC_UNAUTHORIZED)
                .body("success", is(false))
                .body("message", is("email or password are incorrect"));
    }

    @Test
    @DisplayName("Тест При авторизации если нет одного из полей логина, вернётся код ответа 401 Unauthorized")
    public void userCanBeLoginWithoutEmailTest() {
        userModel.setEmail(null);
        userSteps.login(UserCredentials.from(userModel))
                .assertThat()
                .statusCode(SC_UNAUTHORIZED)
                .body("success", is(false))
                .body("message", is("email or password are incorrect"));
    }

    @Test
    @DisplayName("Тест При авторизации если нет одного из полей пароля, вернётся код ответа 401 Unauthorized")
    public void userCanBeLoginWithoutPasswordTest() {
        userModel.setPassword(null);
        userSteps.login(UserCredentials.from(userModel))
                .assertThat()
                .statusCode(SC_UNAUTHORIZED)
                .body("success", is(false))
                .body("message", is("email or password are incorrect"));
    }
}
