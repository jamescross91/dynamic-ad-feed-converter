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

    private final String sourceType;
    private final String destType;
    private final String sourceDir;
    private final String destDir;
    private final String secretFileName;
    private final String outputFormat;

    private final Map<String, String> mappings = new HashMap<>();

    private Config(String jsonConfig) {
        JSONObject jsonObject = new JSONObject(jsonConfig);

        sourceType = jsonObject.getString("SourceType");
        destType = jsonObject.getString("DestType");
        sourceDir = jsonObject.getString("SourceDir");
        destDir = jsonObject.getString("OutputDir");
        secretFileName = jsonObject.getString("SecretFileName");
        outputFormat = jsonObject.getString("OutputFormat");

        JSONObject mappingObj = jsonObject.getJSONObject("SourceToDestMappings");
        mappingObj.toMap().keySet().stream().forEach(key -> mappings.put(key, mappingObj.getString(key)));
    }

    public static Config fromFile(String path) throws IOException {
        String json = new String(Files.readAllBytes(Paths.get(path)));
        return new Config(json);
    }
}
