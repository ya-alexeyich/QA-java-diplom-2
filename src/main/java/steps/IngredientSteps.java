package steps;

import base.StellarBurgersHttpClient;
import base.URL;
import ingredient.IngredientList;
import ingredient.IngredientModel;
import io.qameta.allure.Step;

import java.util.List;

import static io.restassured.RestAssured.given;

public class IngredientSteps extends StellarBurgersHttpClient {

    @Step("Получение данных об ингредиентах")
    public List<IngredientModel> getIngredientList() {
        IngredientList ingredients = given()
                .spec(getBaseRequestSpec())
                .get(URL.INGREDIENTS)
                .as(IngredientList.class);
        if (ingredients == null) {
            throw new NullPointerException("ingredients is absent in response");
        }

        return ingredients.getData();
    }
}
