import config.Config;
import input.DriveDownloader;
import input.InputReader;
import output.feed.AdFeed;
import output.io.FileWriter;
import output.map.FacebookMapper;
import output.map.GoogleMapper;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Entrypoint {
    public static void main(String args[]) throws IOException {
        Config config = Config.fromFile("/Users/James/Developer/dynamic-ad-feed-converter/src/main/resources/sample-config.json");

        String path = new DriveDownloader().downloadLatest(
            "/Users/James/Developer/dynamic-ad-feed-converter/src/main/resources/client_secret.json", config.getSourceDir());

        InputReader inputReader = new InputReader(path);
        List<Map<String, String>> data = inputReader.read();

        FileWriter.writeCSV(config.getDestDir(), getAdFeedList(config, data));
    }

    private static List<AdFeed> getAdFeedList(Config config, List<Map<String, String>> data) {
        if(config.getDestType().equals(Config.GOOGLE_FEED_TYPE)) {
            return data.stream()
                .map(record -> new GoogleMapper(record, config.getMappings()))
                .map(GoogleMapper::getOutputRecord)
                .collect(Collectors.toList());
        }

        if(config.getDestType().equals(Config.FACEBOOK_FEED_TYPE)) {
            return data.stream()
                .map(record -> new FacebookMapper(record, config.getMappings()))
                .map(FacebookMapper::getOutputRecord)
                .collect(Collectors.toList());
        }

        return null;
    }
}
