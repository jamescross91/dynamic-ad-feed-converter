package files;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface Reader {
    List<Map<String, String>> read(String filePath) throws IOException, ParserConfigurationException, SAXException, XPathExpressionException;
}
