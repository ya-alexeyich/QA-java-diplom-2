package ingredient;

import java.util.List;

public class IngredientList {
    private Boolean success;
    private List<IngredientModel> data;

    public IngredientList(Boolean success, List<IngredientModel> data) {
        this.success = success;
        this.data = data;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public List<IngredientModel> getData() {
        return data;
    }

    public void setData(List<IngredientModel> data) {
        this.data = data;
    }
}