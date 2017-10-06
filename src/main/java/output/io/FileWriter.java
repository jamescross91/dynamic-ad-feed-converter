package output.io;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

import java.io.*;
import java.util.List;

public class FileWriter {
    public static void writeCSV(String outputPath, List<?> outputFeed) throws IOException {
        System.out.println("Writing data to " + outputPath);
        CsvMapper mapper = new CsvMapper();
        mapper.setVisibility(mapper.getVisibilityChecker().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
        CsvSchema schema = mapper.schemaFor(outputFeed.get(0).getClass()).withHeader();
        schema = schema.withColumnSeparator(',');

        ObjectWriter myObjectWriter = mapper.writer(schema);
        File tempFile = new File(outputPath);
        FileOutputStream tempFileOutputStream = new FileOutputStream(tempFile);
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(tempFileOutputStream, 1024);
        OutputStreamWriter writerOutputStream = new OutputStreamWriter(bufferedOutputStream, "UTF-8");
        myObjectWriter.writeValue(writerOutputStream, outputFeed);
    }
}
