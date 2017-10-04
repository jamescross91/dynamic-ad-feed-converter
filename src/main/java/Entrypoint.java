import config.Config;
import input.DriveDownloader;
import input.InputReader;
import output.GoogleMapper;
import output.feed.GoogleFeed;
import output.io.FileWriter;

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

        List<GoogleFeed> feedData = data.stream()
            .map(record -> new GoogleMapper(record, config.getMappings()))
            .map(GoogleMapper::getOutputRecord)
            .collect(Collectors.toList());

        FileWriter.writeCSV(config.getDestDir(), feedData);
    }
}
