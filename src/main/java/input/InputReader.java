package input;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Set;
import java.util.stream.StreamSupport;

public class InputReader {
    private final String filePath;

    public InputReader(String filePath) {
        this.filePath = filePath;
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
