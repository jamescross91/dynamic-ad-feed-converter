package output.feed;

import lombok.Data;

import java.util.Map;

@Data
public class GoogleFeed extends AdFeed {

    private String google_product_category;

    public GoogleFeed(Map<String, String> data) {
        fromData(data);
        this.google_product_category = data.containsKey("google_product_category") ? data.get("google_product_category") : "";
    }


}
