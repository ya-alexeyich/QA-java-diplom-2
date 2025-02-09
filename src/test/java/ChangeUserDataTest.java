import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import steps.*;
import user.*;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.is;

public class ChangeUserDataTest {
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
    @DisplayName("Тест обновление информации о пользователе почта")
    public void emailCanBeChangedForAuthorizedUserTest() {
        userModel.setEmail("newEmail@gmail.com");
        userSteps.change(accessToken, userModel)
                .assertThat()
                .statusCode(SC_OK)
                .body("success", is(true))
                .body("user.email", is(userModel.getEmail().toLowerCase()))
                .body("user.name", is(userModel.getName()));
    }

    @Test
    @DisplayName("Тест обновление информации о пользователе пароль")
    public void passwordCanBeChangedForAuthorizedUserTest() {
        userModel.setPassword("newPassword");
        userSteps.change(accessToken, userModel)
                .assertThat()
                .statusCode(SC_OK)
                .body("success", is(true))
                .body("user.email", is(userModel.getEmail().toLowerCase()))
                .body("user.name", is(userModel.getName()));
    }

    @Test
    @DisplayName("Тест обновление информации о пользователе имя")
    public void nameCanBeChangedForAuthorizedUserTest() {
        userModel.setName("newName");
        userSteps.change(accessToken, userModel)
                .assertThat()
                .statusCode(SC_OK)
                .body("success", is(true))
                .body("user.email", is(userModel.getEmail().toLowerCase()))
                .body("user.name", is(userModel.getName()));
    }

    @Test
    @DisplayName("Тест Если выполнить запрос на смену почты без авторизации, вернётся код ответа 401 Unauthorized")
    public void emailCanNotBeChangedForUnauthorizedUserTest() {
        userModel.setEmail("newEmail@gmail.com");
        userSteps.change("", userModel)
                .assertThat()
                .statusCode(SC_UNAUTHORIZED)
                .body("success", is(false))
                .body("message", is("You should be authorised"));
    }

    @Test
    @DisplayName("Тест Если выполнить запрос на смену пароля без авторизации, вернётся код ответа 401 Unauthorized")
    public void passwordCanNotBeChangedForUnauthorizedUserTest() {
        userModel.setPassword("newPassword");
        userSteps.change("", userModel)
                .assertThat()
                .statusCode(SC_UNAUTHORIZED)
                .body("success", is(false))
                .body("message", is("You should be authorised"));
    }

    @Test
    @DisplayName("Тест Если выполнить запрос на смену имени без авторизации, вернётся код ответа 401 Unauthorized")
    public void nameCanNotBeChangedForUnauthorizedUserTest() {
        userModel.setName("newName");
        userSteps.change("", userModel)
                .assertThat()
                .statusCode(SC_UNAUTHORIZED)
                .body("success", is(false))
                .body("message", is("You should be authorised"));
    }

    @Test
    @DisplayName("Тест Если выполнить запрос на смену почты и передать почту, которая уже используется, вернётся код ответа 403 Forbidden")
    public void emailCanNotBeChangedAlreadyExistsTest() {

        UserModel secondUser = UserGenerator.getRandom();
        String secondAccessToken = userSteps.register(secondUser).extract().body().jsonPath().get("accessToken");

        userModel.setEmail(secondUser.getEmail());

        userSteps.change(accessToken, userModel)
                .assertThat()
                .statusCode(SC_FORBIDDEN)
                .body("success", is(false))
                .body("message", is("User with such email already exists"));

        userSteps.delete(secondAccessToken);
    }
}
