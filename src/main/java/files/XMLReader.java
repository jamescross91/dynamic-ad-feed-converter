package files;

import config.Config;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XMLReader implements Reader {
    private final Config config;

    public XMLReader(Config config) {
        this.config = config;
    }

    @Override
    public List<Map<String, String>> read(String filePath) throws IOException, ParserConfigurationException, SAXException, XPathExpressionException {
        Document xmlDocument = getDocument(filePath);

        List<Map<String, String>> dataList = new ArrayList<>();

        XPath xPath = XPathFactory.newInstance().newXPath();

        NodeList nodes = (NodeList) xPath.evaluate(config.getXPathRoot(),
            xmlDocument.getDocumentElement(), XPathConstants.NODESET);

        for (int i = 0; i < nodes.getLength(); i++) {
            evaluateNodeExpressions(dataList, xPath, nodes, i);
        }

        return dataList;
    }

    private void evaluateNodeExpressions(List<Map<String, String>> dataList, XPath xPath, NodeList nodes, int i) throws XPathExpressionException {
        Node singleNode = nodes.item(i);
        singleNode.getParentNode().removeChild(singleNode);

        Map<String, String> dataMap = new HashMap<>();

        for (Map.Entry<String, String> entry : config.getXPathQueries().entrySet()) {
            dataMap.put(entry.getKey(), xPath.evaluate(entry.getValue(), singleNode));
        }

        dataList.add(dataMap);
    }

    private Document getDocument(String filePath) throws IOException, ParserConfigurationException, SAXException {
        FileInputStream fileIS = new FileInputStream(filePath);
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = builderFactory.newDocumentBuilder();
        return builder.parse(fileIS);
    }
}
