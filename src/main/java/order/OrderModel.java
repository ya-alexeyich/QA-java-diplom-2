package order;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import java.util.List;

@Data
public class OrderModel {
    private List<String> ingredients;
    @SerializedName("_id")
    private String id;
    private String status;
    private String name;
    private String createdAt;
    private String updatedAt;
    private int number;

    public OrderModel(List<String> ingredients) {
        this.ingredients = ingredients;
    }
}