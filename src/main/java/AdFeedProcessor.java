import config.Config;
import io.DriveDownloader;
import io.InputReader;
import output.feed.AdFeed;
import io.FileWriter;
import output.map.FacebookMapper;
import output.map.GoogleMapper;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AdFeedProcessor {
    private final Config config;

    public AdFeedProcessor(Config config) {
        this.config = config;
    }

    public void process() throws IOException {
        String localPath = readSource();
        InputReader inputReader = new InputReader(localPath);
        List<Map<String, String>> data = inputReader.read();
        List<AdFeed> adFeedList = getAdFeedList(data);
        writeToDestination(adFeedList);
    }

    private void writeToDestination(List<AdFeed> adFeedList) throws IOException {
        if(config.getDestType().equals(Config.LOCAL_SOURCE_TYPE)) {
            System.out.println("Writing data to local directory " + config.getDestDir());
            FileWriter.writeCSV(config.getDestDir(), adFeedList);
        }

        if(config.getDestType().equals(Config.GOOGLE_DRIVE_SOURCE_TYPE)) {
            System.out.println("Writing data to Google Drive directory " + config.getDestDir());
        }

        throw new IllegalArgumentException("Invalid or unknown destination type " + config.getDestType());
    }

    private String readSource() throws IOException {
        if(config.getSourceType().equals(Config.LOCAL_SOURCE_TYPE)) {
            System.out.println("Reading local file from " + config.getSourceDir());
            return config.getSourceDir();
        }

        if(config.getSourceType().equals(Config.GOOGLE_DRIVE_SOURCE_TYPE)) {
            System.out.println("Reading file from Google Drive");
            return new DriveDownloader().downloadLatest(
                this.getClass()
                    .getClassLoader()
                    .getResource(config.getSecretFileName()).getFile(),
                config.getSourceDir());
        }

        throw new IllegalArgumentException("Invalid or unknown source type " + config.getSourceType());
    }

    private List<AdFeed> getAdFeedList(List<Map<String, String>> data) {
        if(config.getOutputFormat().equals(Config.GOOGLE_FEED_FORMAT)) {
            System.out.println("Processing file into Google feed format");
            return data.stream()
                .map(record -> new GoogleMapper(record, config.getMappings()))
                .map(GoogleMapper::getOutputRecord)
                .collect(Collectors.toList());
        }

        if(config.getOutputFormat().equals(Config.FACEBOOK_FEED_FORMAT)) {
            System.out.println("Processing file into Facebook feed format");
            return data.stream()
                .map(record -> new FacebookMapper(record, config.getMappings()))
                .map(FacebookMapper::getOutputRecord)
                .collect(Collectors.toList());
        }

        throw new IllegalArgumentException("Invalid or unknown output format " + config.getOutputFormat());
    }

}
