package io;

import config.Config;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class InputReader {
    private final String filePath;
    private final Config config;

    public InputReader(String filePath, Config config) {
        this.filePath = filePath;
        this.config = config;
    }

    public List<Map<String, String>> read() throws IOException {
        System.out.println("Parsing input file at path " + filePath);
        Reader in = new FileReader(filePath);

        if (config.getFileType().equals(Config.CSV_FILE_TYPE)) {
            return readCsv(in);
        }

        if (config.getFileType().equals(Config.XML_FILE_TYPE)) {
            return null;
        }

        throw new IllegalArgumentException("Invalid or file type " + config.getFileType());
    }

    private List<Map<String, String>> readCsv(Reader in) throws IOException {
        Iterable<CSVRecord> records = CSVFormat.EXCEL.withHeader().withIgnoreSurroundingSpaces().parse(in);

        return StreamSupport.stream(records.spliterator(), false)
            .map(CSVRecord::toMap)
            .collect(Collectors.toList());
    }
}
