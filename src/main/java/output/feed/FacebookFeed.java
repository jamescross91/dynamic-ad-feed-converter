package output.feed;

import lombok.Data;

import java.util.Map;

@Data
public class FacebookFeed extends AdFeed {
    private String condition;
    private String brand;

    public FacebookFeed(Map<String, String> data) {
        fromData(data);

        validateCondition(data.get("condition"));
        this.condition = data.get("condition");

        this.brand = data.get("brand");
    }

    private void validateCondition(String value) {
        if (!(value.equals("new") || value.equals("refurbished") || value.equals("used"))) {
            throw new IllegalArgumentException("Value " + value + " not valid for condition field");
        }
    }
}
