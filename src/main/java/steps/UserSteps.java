package steps;

import base.StellarBurgersHttpClient;
import base.URL;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import user.*;
import static io.restassured.RestAssured.given;

public class UserSteps extends StellarBurgersHttpClient {

    @Step("Создание пользователя")
    public ValidatableResponse register(UserModel userModel) {
        return given()
                .spec(getBaseRequestSpec())
                .body(userModel)
                .when()
                .post(URL.CREATE_USER)
                .then();
    }

    @Step("Авторизация пользователя")
    public ValidatableResponse login(UserCredentials userCredentials) {
        return given()
                .spec(getBaseRequestSpec())
                .body(userCredentials)
                .when()
                .post(URL.LOGIN_USER)
                .then();
    }

    @Step("Обновление информации о пользователе")
    public ValidatableResponse change(String accessToken, UserModel userModel) {
        return given()
                .spec(getBaseRequestSpec())
                .header("Authorization", accessToken)
                .body(userModel)
                .when()
                .patch(URL.UPDATE_USER_DATA)
                .then();
    }

    @Step("Удаление пользователя")
    public ValidatableResponse delete(String accessToken) {
        return given()
                .spec(getBaseRequestSpec())
                .header("Authorization", accessToken)
                .when()
                .delete(URL.UPDATE_USER_DATA)
                .then();
    }
}
