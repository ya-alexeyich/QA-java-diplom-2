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

public class GetOrdersUserTest {
    private UserSteps userSteps;
    private OrderSteps orderSteps;
    private String accessToken;

    @Before
    public void setUp() {
        userSteps = new UserSteps();
        UserModel userModel = UserGenerator.getRandom();
        accessToken = userSteps.register(userModel).extract().body().jsonPath().get("accessToken");
        IngredientSteps ingredientSteps = new IngredientSteps();
        orderSteps = new OrderSteps();
        List<IngredientModel> ingredientsModel = ingredientSteps.getIngredientList();
        orderSteps.create(accessToken, List.of(ingredientsModel.get(0).get_id(), ingredientsModel.get(1).get_id()));
    }

    @After
    public void cleanUp() {
        if (accessToken != null && !accessToken.isEmpty()) {
            userSteps.delete(accessToken);
        }
    }

    @Test
    @DisplayName("Тест Получить заказы конкретного пользователя")
    public void getOrdersForAuthUserTest() {
        orderSteps.getAllOrders(accessToken)
                .assertThat()
                .statusCode(SC_OK)
                .body("success", is(true))
                .body("orders.ingredients", is(notNullValue()));
    }

    @Test
    @DisplayName("Тест Если выполнить запрос получить заказы без авторизации, вернётся код ответа 401 Unauthorized")
    public void canNotGetOrdersForNotAuthUserTest() {
        orderSteps.getAllOrders("")
                .assertThat()
                .statusCode(SC_UNAUTHORIZED)
                .body("success", is(false))
                .body("message", is("You should be authorised"));
    }
}
