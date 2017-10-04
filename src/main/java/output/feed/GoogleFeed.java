package output.feed;

import lombok.Data;

import java.util.Map;

@Data
public class GoogleFeed extends AdFeed {
    private String title;
    private String description;
    private String link;
    private String image_link;
    private String availability;
    private String google_product_category;
    private String price;

    public GoogleFeed(
        String id,
        String title,
        String description,
        String link,
        String image_link,
        String availability,
        String price,
        String google_product_category) {

        validateAndAssign(id, title, description, link, image_link, availability, price, google_product_category);
    }

    public GoogleFeed(Map<String, String> data) {
        String id = data.get("id");
        String title = data.get("title");
        String description = data.get("description");
        String link = data.get("link");
        String image_link = data.get("image_link");
        String availability = data.get("availability");
        String price = data.get("price");
        String google_product_category = data.get("google_product_category");

        validateAndAssign(id, title, description, link, image_link, availability, price, google_product_category);
    }

    private void validateAndAssign(String id, String title, String description, String link, String imageLink, String availability, String price, String google_product_category) {
        validateLength(id, 50);
        this.id = id;

        validateLength(title, 150);
        this.title = title;

        validateLength(description, 5000);
        this.description = description;

        validateLength(link, 2000);
        this.link = link;

        validateLength(imageLink, 2000);
        this.image_link = imageLink;

        validateAvailability(availability);
        this.availability = availability;

        this.google_product_category = google_product_category;

        validatePrice(price);
        this.price = price;
    }

    private void validateLength(String value, int maxLength) {
//        if(value.length() > maxLength) {
//            throw new IllegalArgumentException("String of value " + value + " longer than " + maxLength);
//        }
    }

    private void validateAvailability(String value) {
//        if (!value.equals("in stock") || !value.equals("out of stock") || !value.equals("preorder")) {
//            throw new IllegalArgumentException("Value " + value + " not valid for availability field");
//        }
    }

    private void validatePrice(String value) {
//        String currCode = value.substring(value.length() - 3, value.length() - 1);
//        if(Currency.getInstance(currCode) == null) {
//            throw new IllegalArgumentException("Invalid currency code " + currCode + " in price string " + value);
//        }
    }
}
