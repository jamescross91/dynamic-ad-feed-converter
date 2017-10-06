package output.feed;

import lombok.Data;

import java.util.Map;

@Data
public class FacebookFeed extends AdFeed {

    public FacebookFeed(Map<String, String> data) {
        fromData(data);
    }


}
