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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class XMLReader implements Reader {
    private final Config config;

    public XMLReader(Config config) {
        this.config = config;
    }

    @Override
    public List<Map<String, String>> read(String filePath) throws IOException, ParserConfigurationException, SAXException, XPathExpressionException {
        Document xmlDocument = getDocument(filePath);


        List<Map<String, String>> rootElements = processExpression(xmlDocument, config.getXPathRoot())
            .stream()
            .map(x -> {
                HashMap<String, String> map = new HashMap<>();
                map.putAll(config.getXPathQueries());
                return map;
            })
            .collect(Collectors.toList());

        for (int i = 1; i <= rootElements.size(); i++) {
            System.out.println("Querying element " + i + " of " + rootElements.size());
            rootElements.set(i - 1, getDataForItem(xmlDocument, rootElements.get(i - 1), i));
        }

        return rootElements;
    }

    private Map<String, String> getDataForItem(Document xmlDocument, Map<String, String> item, int index) throws XPathExpressionException {
        Map<String, String> dataMap = new HashMap<>();
        for (Map.Entry<String, String> entry : item.entrySet()) {
            getData(xmlDocument, index, dataMap, entry);
        }

        return dataMap;
    }

    private void getData(Document xmlDocument, int index, Map<String, String> dataMap, Map.Entry<String, String> entry) throws XPathExpressionException {
        String query = buildQuery(index, config.getXPathRoot(), entry.getValue());
        List<String> data = processExpression(xmlDocument, query);

        if (data.size() > 1) throw new IllegalStateException("Found more than one result for query " + query);

        if (data.size() == 0) {
            dataMap.put(entry.getKey(), "");
        } else {
            dataMap.put(entry.getKey(), data.get(0));
        }
    }

    private String buildQuery(int index, String queryRoot, String queryExpression) {
        return queryRoot + "[" + Integer.toString(index) + "]" + queryExpression;
    }

    private Document getDocument(String filePath) throws IOException, ParserConfigurationException, SAXException {
        FileInputStream fileIS = new FileInputStream(filePath);
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = builderFactory.newDocumentBuilder();
        return builder.parse(fileIS);
    }

    private List<String> processExpression(Document xmlDocument, String expression) throws XPathExpressionException {
        XPath xPath = XPathFactory.newInstance().newXPath();
        NodeList nl = (NodeList) xPath.compile(expression).evaluate(xmlDocument, XPathConstants.NODESET);

        return IntStream.range(0, nl.getLength())
            .mapToObj(nl::item)
            .map(Node::getTextContent)
            .collect(Collectors.toList());
    }
}
