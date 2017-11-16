import config.Config;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;

public class LocalEntrypoint {
    public static void main(String args[]) throws IOException, XPathExpressionException, ParserConfigurationException, SAXException {
        Config config = Config.fromFile("/Users/James/Developer/dynamic-ad-feed-converter/src/main/resources/sample-xml-config.json");

        AdFeedProcessor adFeedProcessor = new AdFeedProcessor(config);
        adFeedProcessor.process();
    }
}
