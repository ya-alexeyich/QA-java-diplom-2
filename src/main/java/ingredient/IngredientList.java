package ingredient;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

@Data
@AllArgsConstructor
public class IngredientList {
    private Boolean success;
    private List<IngredientModel> data;
}