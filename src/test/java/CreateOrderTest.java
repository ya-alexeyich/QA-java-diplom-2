import ingredient.IngredientModel;
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
    @DisplayName("Тест Создание заказа с авторизацией")
    public void orderCanBeCreateWithIngredientsAndAuthTest() {
        orderSteps.create(accessToken, List.of(ingredientsModel.get(0).get_id(), ingredientsModel.get(1).get_id()))
                .assertThat()
                .statusCode(SC_OK)
                .body("success", is(true))
                .body("name", is(notNullValue()))
                .body("order.number", is(notNullValue()));
    }

    @Test
    @DisplayName("Тест При создании заказа если не передать ни один ингредиент, вернётся код ответа 400 Bad Request")
    public void orderCanBeCreateWithoutIngredientsAndAuthTest() {
        orderSteps.create(accessToken, null)
                .assertThat()
                .statusCode(SC_BAD_REQUEST)
                .body("success", is(false))
                .body("message", is("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Тест При создании заказа если в запросе передан невалидный хеш ингредиента, вернётся код ответа 500 Internal Server Error")
    public void orderCanBeCreateWithIncorrectHashIngredientAndAuthTest() {
        orderSteps.create(accessToken, List.of("incorrectHash", ingredientsModel.get(2).get_id()))
                .assertThat()
                .statusCode(SC_INTERNAL_SERVER_ERROR);
    }

    @Test
    @DisplayName("Тест Создание заказа без авторизации")
    public void orderCanBeCreateWithIngredientsAndNotAuthTest() {
        orderSteps.create("", List.of(ingredientsModel.get(3).get_id(), ingredientsModel.get(4).get_id()))
                .assertThat()
                .statusCode(SC_OK)
                .body("success", is(true))
                .body("name", is(notNullValue()))
                .body("order.number", is(notNullValue()));
    }

    @Test
    @DisplayName("Тест При создании заказа если не передать ни один ингредиент, вернётся код ответа 400 Bad Request")
    public void orderCanBeCreateWithoutIngredientsAndNotAuthTest() {
        orderSteps.create("", null)
                .assertThat()
                .statusCode(SC_BAD_REQUEST)
                .body("success", is(false))
                .body("message", is("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Тест При создании заказа если в запросе передан невалидный хеш ингредиента, вернётся код ответа 500 Internal Server Error")
    public void orderCanBeCreateWithIncorrectHashIngredientNotAuthTest() {
        orderSteps.create("", List.of(ingredientsModel.get(5).get_id(), "incorrectHash"))
                .assertThat()
                .statusCode(SC_INTERNAL_SERVER_ERROR);
    }
}
