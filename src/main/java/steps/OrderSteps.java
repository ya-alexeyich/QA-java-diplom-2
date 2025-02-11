package steps;

import base.StellarBurgersHttpClient;
import base.URL;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import order.OrderModel;
import java.util.List;

import static io.restassured.RestAssured.given;

public class OrderSteps extends StellarBurgersHttpClient {

    @Step("Создание заказа")
    public ValidatableResponse create(String accessToken, List<String> ingredients) {
        return given()
                .spec(getBaseRequestSpec())
                .header("Authorization", accessToken)
                .body(new OrderModel(ingredients))
                .when()
                .post(URL.ORDERS)
                .then();
    }

    @Step("Получить заказы конкретного пользователя")
    public ValidatableResponse getAllOrders(String accessToken) {
        return given()
                .spec(getBaseRequestSpec())
                .header("Authorization", accessToken)
                .when()
                .get(URL.ORDERS)
                .then();
    }
}
