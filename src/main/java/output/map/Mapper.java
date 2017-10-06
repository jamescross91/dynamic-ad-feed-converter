package output.map;

import java.util.HashMap;
import java.util.Map;

public abstract class Mapper {
    //Source record field name and value pairs
    final Map<String, String> sourceRecord;

    //Key is the Google Feed field, value is the source record field
    final Map<String, String> mappings;

    public Mapper(Map<String, String> sourceRecord, Map<String, String> mappings) {
        this.sourceRecord = sourceRecord;
        this.mappings = mappings;
    }

    Map<String, String> getDataRecord() {
        Map<String, String> data = new HashMap<>();

        mappings.keySet().stream().forEach(adFeed -> {
            String sourceField = mappings.get(adFeed);
            String value = getValue(sourceField);

            data.put(adFeed, value);
        });

        return data;
    }

    String getValue(String field) {
        String value = sourceRecord.get(field);

        if(field.contains("%STATIC:")) {
            return  field.replace("%STATIC:", "");
        }

        return value;
    }
}
