import config.Config;

import java.io.IOException;

public class LocalEntrypoint {
    public static void main(String args[]) throws IOException {
        Config config = Config.fromFile("/Users/James/Developer/dynamic-ad-feed-converter/src/main/resources/amc-google-config.json");

        AdFeedProcessor adFeedProcessor = new AdFeedProcessor(config);
        adFeedProcessor.process();
    }
}
