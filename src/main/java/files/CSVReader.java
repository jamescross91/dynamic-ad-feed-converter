package files;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class CSVReader implements files.Reader{
    @Override
    public List<Map<String, String>> read(String filePath) throws IOException {
        Reader in = new FileReader(filePath);
        Iterable<CSVRecord> records = CSVFormat.EXCEL.withHeader().withIgnoreSurroundingSpaces().parse(in);

        return StreamSupport.stream(records.spliterator(), false)
            .map(CSVRecord::toMap)
            .collect(Collectors.toList());
    }
}
