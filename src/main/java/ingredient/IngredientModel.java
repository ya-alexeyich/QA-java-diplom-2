package ingredient;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class IngredientModel {
    @SerializedName("_id")
    private String id;
    private String name;
    private String type;
    private Integer proteins;
    private Integer fat;
    private Integer carbohydrates;
    private Integer calories;
    private Integer price;
    private String image;
    @SerializedName("image_mobile")
    private String imageMobile;
    @SerializedName("image_large")
    private String imageLarge;
    @SerializedName("__v")
    private Integer version;
}