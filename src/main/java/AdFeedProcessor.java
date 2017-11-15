import config.Config;
import io.DriveHandler;
import io.FileWriter;
import io.InputReader;
import io.UrlHandler;
import output.feed.AdFeed;
import output.map.FacebookMapper;
import output.map.GoogleMapper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
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
        InputReader inputReader = new InputReader(localPath, config);
        List<Map<String, String>> data = inputReader.read();
        List<AdFeed> adFeedList = getAdFeedList(data);
        writeToDestination(adFeedList);
    }

    private void writeToDestination(List<AdFeed> adFeedList) throws IOException {
        if(config.getDestType().equals(Config.LOCAL_SOURCE_TYPE)) {
            System.out.println("Writing data to local directory " + config.getDestDir());
            FileWriter.writeCSV(Paths.get(config.getDestDir(), config.getDestFileName()).toString(), adFeedList);

            return;
        }

        if(config.getDestType().equals(Config.GOOGLE_DRIVE_SOURCE_TYPE)) {
            String path = writeTempFile(adFeedList);
            System.out.println("Writing data to Google Drive directory " + config.getDestDir());
            new DriveHandler().uploadFile(
                this.getClass()
                    .getClassLoader()
                    .getResource(config.getSecretFileName()).getFile(),
                config.getDestDir(),
                config.getDestFileName(),
                path
            );

            return;
        }

        throw new IllegalArgumentException("Invalid or unknown destination type " + config.getDestType());
    }

    private String writeTempFile(List<AdFeed> data) throws IOException {
        File temp = File.createTempFile("temp-file-name", ".csv");
        FileWriter.writeCSV(temp.getPath(), data);

        return temp.getPath();
    }

    private String readSource() throws IOException {
        if(config.getSourceType().equals(Config.LOCAL_SOURCE_TYPE)) {
            System.out.println("Reading local file from " + config.getSourceDir());
            return config.getSourceDir();
        }

        if(config.getSourceType().equals(Config.GOOGLE_DRIVE_SOURCE_TYPE)) {
            System.out.println("Reading file from Google Drive");
            return new DriveHandler().downloadLatest(
                this.getClass()
                    .getClassLoader()
                    .getResource(config.getSecretFileName()).getFile(),
                config.getSourceDir());
        }

        if (config.getSourceType().equals(Config.URL_SOURCE_TYPE)) {
            System.out.println("Downloading file from the web");
            return UrlHandler.download(config.getSourceDir());
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
