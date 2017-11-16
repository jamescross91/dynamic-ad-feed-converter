package io;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

public class UrlHandler {
    public static String download(String url) throws IOException {
        String outputPath = getTempFile();

        URL website = new URL(url);
        ReadableByteChannel rbc = Channels.newChannel(website.openStream());
        FileOutputStream fos = new FileOutputStream(outputPath);
        fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);

        return outputPath;
    }

    private static String getTempFile() throws IOException {
        java.io.File temp = java.io.File.createTempFile("inputFeed", ".xml");
        return temp.getAbsolutePath();
    }
}
