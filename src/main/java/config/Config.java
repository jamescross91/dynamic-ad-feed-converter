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

    private final String sourceDir;
    private final String destDir;
    private final String destType;
    private final Map<String, String> mappings = new HashMap<>();

    private Config(String jsonConfig) {
        JSONObject jsonObject = new JSONObject(jsonConfig);
        sourceDir = jsonObject.getString("SourceDir");
        destDir = jsonObject.getString("DestDir");
        destType = jsonObject.getString("DestType");

        JSONObject mappingObj = jsonObject.getJSONObject("SourceToDestMappings");
        mappingObj.toMap().keySet().stream().forEach(key -> mappings.put(key, mappingObj.getString(key)));
    }

    public static Config fromFile(String path) throws IOException {
        String json = new String(Files.readAllBytes(Paths.get(path)));
        return new Config(json);
    }
}
