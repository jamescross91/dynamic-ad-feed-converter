package output.feed;

import java.util.Currency;
import java.util.Map;

public abstract class AdFeed{
    String id;
    String availability;
    String title;
    String description;
    String link;
    String image_link;
    String price;

    public void fromData(Map<String, String> data) {
        String id = data.get("id");
        String title = data.get("title");
        String description = data.get("description");
        String link = data.get("link");
        String image_link = data.get("image_link");
        String availability = data.get("availability");
        String price = data.get("price");

        validateAndAssign(id, title, description, link, image_link, availability, price);
    }

    private void validateAndAssign(String id, String title, String description, String link, String imageLink, String availability, String price) {
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

        validatePrice(price);
        this.price = price;
    }

    void validateLength(String value, int maxLength) {
        if (value.length() > maxLength) {
            throw new IllegalArgumentException("String of value " + value + " longer than " + maxLength);
        }
    }

    void validateAvailability(String value) {
        if (!(value.equals("in stock") || value.equals("out of stock") || value.equals("preorder"))) {
            throw new IllegalArgumentException("Value " + value + " not valid for availability field");
        }
    }

    void validatePrice(String value) {
        String currCode = value.substring(value.length() - 3, value.length());
        if (Currency.getInstance(currCode) == null) {
            throw new IllegalArgumentException("Invalid currency code " + currCode + " in price string " + value);
        }
    }
}
