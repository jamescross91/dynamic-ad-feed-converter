package files;

import com.google.common.collect.ImmutableMap;
import config.Config;
import org.w3c.dom.Document;
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
import java.util.*;
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

        List<Map<String, String>> data = new ArrayList<>();

        Map<String, String> xPathQueries = config.getXPathQueries();

        for (String field : xPathQueries.keySet()) {
            String query = xPathQueries.get(field);
            List<Map<String, String>> expressionResult = processExpression(xmlDocument, query, field);
            data = mergeExpressionResults(data, expressionResult);
        }

        return data;
    }

    //Horrible code for merging two lists based on index, and merging the contents of the map in each
    private List<Map<String, String>> mergeExpressionResults(List<Map<String, String>> currentData, List<Map<String, String>> results) {
        for(int i = 0; i < results.size(); i++) {
            Map<String, String> current = currentData.con ? currentData.get(i) : new HashMap<>();
            Map<String, String> result = results.get(i);
            current.putAll(result);

            //Pointless assignment to keep the compiler happy
            Object o = currentData.size() == results.size() ? currentData.set(i, current) : currentData.add(current);
        }

        return currentData;
    }

    private Document getDocument(String filePath) throws IOException, ParserConfigurationException, SAXException {
        FileInputStream fileIS = new FileInputStream(filePath);
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = builderFactory.newDocumentBuilder();
        return builder.parse(fileIS);
    }

    private List<Map<String, String>> processExpression(Document xmlDocument, String expression, String mappedField) throws XPathExpressionException {
        XPath xPath = XPathFactory.newInstance().newXPath();
        NodeList nl = (NodeList) xPath.compile(expression).evaluate(xmlDocument, XPathConstants.NODESET);

        return IntStream.range(0, nl.getLength())
            .mapToObj(nl::item)
            .map(x -> ImmutableMap.<String, String>builder().put(mappedField, x.getTextContent()).build())
            .collect(Collectors.toList());
    }
}
