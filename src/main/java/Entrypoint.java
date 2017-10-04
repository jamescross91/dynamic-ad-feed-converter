import input.InputReader;

import java.io.IOException;
import java.util.Set;

public class Entrypoint {
    public static void main(String args[]) throws IOException {
        InputReader inputReader = new InputReader("/Users/James/Developer/dynamic-ad-feed-converter/sample-data.csv");
        Set<String> header = inputReader.getHeader();
        System.out.println();
    }
}
