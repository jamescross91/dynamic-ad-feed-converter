package output.map;

import output.feed.FacebookFeed;

import java.util.Map;

public class FacebookMapper extends Mapper {
    public FacebookMapper(Map<String, String> sourceRecord, Map<String, String> mappings) {
        super(sourceRecord, mappings);
    }

    public FacebookFeed getOutputRecord() {
        return new FacebookFeed(getDataRecord());
    }
}
