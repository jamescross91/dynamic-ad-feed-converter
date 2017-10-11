package io;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class InputReader {
    private final String filePath;

    public InputReader(String filePath) {
        this.filePath = filePath;
    }

    public List<Map<String, String>> read() throws IOException {
        System.out.println("Parsing input file at path " + filePath);
        Reader in = new FileReader(filePath);
        Iterable<CSVRecord> records = CSVFormat.EXCEL.withHeader().withIgnoreSurroundingSpaces().parse(in);

        return StreamSupport.stream(records.spliterator(), false)
            .map(CSVRecord::toMap)
            .collect(Collectors.toList());
    }

    public Set<String> getHeader() throws IOException {
        Reader in = new FileReader(filePath);
        Iterable<CSVRecord> records = CSVFormat.EXCEL.withHeader().withIgnoreSurroundingSpaces().parse(in);

        return StreamSupport.stream(records.spliterator(), false)
            .findFirst()
            .get()
            .toMap()
            .keySet();
    }
}
