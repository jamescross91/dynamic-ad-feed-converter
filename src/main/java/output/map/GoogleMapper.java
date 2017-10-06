package output.map;

import output.feed.GoogleFeed;

import java.util.HashMap;
import java.util.Map;

public class GoogleMapper extends Mapper {
    public GoogleMapper(Map<String, String> sourceRecord, Map<String, String> mappings) {
        super(sourceRecord, mappings);
    }

    public GoogleFeed getOutputRecord() {
        return new GoogleFeed(getDataRecord());
    }
}
