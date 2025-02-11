import ingredient.IngredientModel;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import steps.*;
import user.*;
import java.util.List;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

public class CreateOrderTest {
    private UserSteps userSteps;
    private OrderSteps orderSteps;
    private String accessToken;
    private List<IngredientModel> ingredientsModel;

    @Before
    public void setUp() {
        userSteps = new UserSteps();
        UserModel userModel = UserGenerator.getRandom();
        accessToken = userSteps.register(userModel).extract().body().jsonPath().get("accessToken");
        IngredientSteps ingredientSteps = new IngredientSteps();
        orderSteps = new OrderSteps();
        ingredientsModel = ingredientSteps.getIngredientList();
    }

    @After
    public void cleanUp() {
        if (accessToken != null && !accessToken.isEmpty()) {
            userSteps.delete(accessToken);
        }
    }

    @Test
    @DisplayName("Тест Создание заказа с ингредиентами")
    @Description("Проверка с авторизацией кода и тела ответа при успешном создание заказа с ингредиентами")
    public void orderCanBeCreateWithIngredientsAndAuthTest() {
        orderSteps.create(accessToken, List.of(ingredientsModel.get(0).getId(), ingredientsModel.get(1).getId()))
                .assertThat()
                .statusCode(SC_OK)
                .body("success", is(true))
                .body("name", is(notNullValue()))
                .body("order.number", is(notNullValue()));
    }

    @Test
    @DisplayName("Тест Создание заказа без ингредиентов")
    @Description("Проверка При создании заказа с авторизацией если не передать ни один ингредиент, вернётся код ответа 400 Bad Request")
    public void orderCanBeCreateWithoutIngredientsAndAuthTest() {
        orderSteps.create(accessToken, null)
                .assertThat()
                .statusCode(SC_BAD_REQUEST)
                .body("success", is(false))
                .body("message", is("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Тест Создание заказа с неверным хешем ингредиентов")
    @Description("Проверка При создании заказа с авторизацией если в запросе передан невалидный хеш ингредиента, вернётся код ответа 500 Internal Server Error")
    public void orderCanBeCreateWithIncorrectHashIngredientAndAuthTest() {
        orderSteps.create(accessToken, List.of("incorrectHash", ingredientsModel.get(2).getId()))
                .assertThat()
                .statusCode(SC_INTERNAL_SERVER_ERROR);
    }

    @Test
    @DisplayName("Тест Создание заказа без авторизации с ингредиентами")
    @Description("Проверка без авторизации кода и тела ответа при успешном создание заказа с ингредиентами")
    public void orderCanBeCreateWithIngredientsAndNotAuthTest() {
        orderSteps.create("", List.of(ingredientsModel.get(3).getId(), ingredientsModel.get(4).getId()))
                .assertThat()
                .statusCode(SC_OK)
                .body("success", is(true))
                .body("name", is(notNullValue()))
                .body("order.number", is(notNullValue()));
    }

    @Test
    @DisplayName("Тест Создание заказа без авторизации без ингредиентов")
    @Description("Проверка При создании заказа без авторизации если не передать ни один ингредиент, вернётся код ответа 400 Bad Request")
    public void orderCanBeCreateWithoutIngredientsAndNotAuthTest() {
        orderSteps.create("", null)
                .assertThat()
                .statusCode(SC_BAD_REQUEST)
                .body("success", is(false))
                .body("message", is("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Тест Создание заказа без авторизации с неверным хешем ингредиентов")
    @Description("Проверка При создании заказа без авторизации если в запросе передан невалидный хеш ингредиента, вернётся код ответа 500 Internal Server Error")
    public void orderCanBeCreateWithIncorrectHashIngredientNotAuthTest() {
        orderSteps.create("", List.of(ingredientsModel.get(5).getId(), "incorrectHash"))
                .assertThat()
                .statusCode(SC_INTERNAL_SERVER_ERROR);
    }
}
