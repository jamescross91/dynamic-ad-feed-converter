import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import config.Config;
import org.apache.commons.io.IOUtils;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Lambda implements RequestStreamHandler, RequestHandler<Object, Object>{
    @Override
    public Object handleRequest(Object o, Context context) {
        return null;
    }

    @Override
    public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) throws IOException {
        String JSONConfig = IOUtils.toString(inputStream);
        System.out.println("JSON input is: " + JSONConfig);
        Config config = new Config(JSONConfig);

        AdFeedProcessor adFeedProcessor = new AdFeedProcessor(config);
        try {
            adFeedProcessor.process();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
    }
}
