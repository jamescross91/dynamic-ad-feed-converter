package output;

import output.feed.GoogleFeed;

import java.util.HashMap;
import java.util.Map;

public class GoogleMapper {
    //Source record field name and value pairs
    private final Map<String, String> sourceRecord;

    //Key is the Google Feed field, value is the source record field
    private final Map<String, String> mappings;

    public GoogleMapper(Map<String, String> sourceRecord, Map<String, String> mappings) {
        this.sourceRecord = sourceRecord;
        this.mappings = mappings;
    }

    public GoogleFeed getOutputRecord() {
        Map<String, String> data = new HashMap<>();

        mappings.keySet().stream().forEach(googleField -> {
            String sourceField = mappings.get(googleField);
            String value = getValue(sourceField);

            data.put(googleField, value);
        });

        return new GoogleFeed(data);
    }

    String getValue(String field) {
        String value = sourceRecord.get(field);

        if(field.contains("%STATIC:")) {
            return  field.replace("%STATIC:", "");
        }

        return value;
    }
}
