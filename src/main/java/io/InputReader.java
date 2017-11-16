package io;

import config.Config;
import files.CSVReader;
import files.XMLReader;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class InputReader {
    private final String filePath;
    private final Config config;

    public InputReader(String filePath, Config config) {
        this.filePath = filePath;
        this.config = config;
    }

    public List<Map<String, String>> read() throws IOException, XPathExpressionException, SAXException, ParserConfigurationException {
        System.out.println("Parsing input file at path " + filePath);
        if (config.getFileType().equals(Config.CSV_FILE_TYPE)) {
            return new CSVReader().read(filePath);
        }

        if (config.getFileType().equals(Config.XML_FILE_TYPE)) {
            return new XMLReader(config).read(filePath);
        }

        throw new IllegalArgumentException("Invalid or file type " + config.getFileType());
    }

}
