package config;

import lombok.Data;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@Data
public class Config {
    public static final String GOOGLE_FEED_FORMAT = "GoogleFeed";
    public static final String FACEBOOK_FEED_FORMAT = "FacebookFeed";

    public static final String LOCAL_SOURCE_TYPE = "Local";
    public static final String GOOGLE_DRIVE_SOURCE_TYPE = "GoogleDrive";
    public static final String URL_SOURCE_TYPE = "URL";

    public static final String CSV_FILE_TYPE = "CSV";
    public static final String XML_FILE_TYPE = "XML";

    private final String sourceType;
    private final String destType;
    private final String sourceDir;
    private final String destDir;
    private final String secretFileName;
    private final String outputFormat;
    private final String destFileName;
    private final String fileType;

    private final Map<String, String> mappings = new HashMap<>();
    private final String xPathRoot;
    private Map<String, String> xPathQueries = new HashMap<>();

    public Config(String jsonConfig) {
        JSONObject jsonObject = new JSONObject(jsonConfig);

        sourceType = jsonObject.getString("sourceType");
        destType = jsonObject.getString("destType");
        sourceDir = jsonObject.getString("sourceDir");
        destDir = jsonObject.getString("destDir");
        secretFileName = jsonObject.getString("secretFileName");
        outputFormat = jsonObject.getString("outputFormat");
        destFileName = jsonObject.getString("destFileName");
        fileType = jsonObject.getString("fileType");
        xPathRoot = jsonObject.getString("xPathRoot");

        setMappings(jsonObject);
        setXpathQueries(jsonObject);
    }

    public static Config fromFile(String path) throws IOException {
        String json = new String(Files.readAllBytes(Paths.get(path)));
        return new Config(json);
    }

    private void setXpathQueries(JSONObject jsonObject) {
        JSONObject queryObj = jsonObject.getJSONObject("xPathQueries");
        queryObj.toMap().keySet().stream().forEach(key -> xPathQueries.put(key, queryObj.getString(key)));
    }

    private void setMappings(JSONObject jsonObject) {
        JSONObject mappingObj = jsonObject.getJSONObject("mappings");
        mappingObj.toMap().keySet().stream().forEach(key -> mappings.put(key, mappingObj.getString(key)));
    }
}
