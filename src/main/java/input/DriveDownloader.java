package input;


import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class DriveDownloader {

    /** Application name. */
    private static final String APPLICATION_NAME =
        "Drive API Java Quickstart";

    /** Global instance of the JSON factory. */
    private static final JsonFactory JSON_FACTORY =
        JacksonFactory.getDefaultInstance();

    /** Global instance of the HTTP transport. */
    private static HttpTransport HTTP_TRANSPORT;

    static {
        try {
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        } catch (Throwable t) {
            t.printStackTrace();
            System.exit(1);
        }
    }

    private Credential authorize(String credentialsPath) throws IOException {
        return GoogleCredential
            .fromStream(new FileInputStream(credentialsPath))
            .createScoped(DriveScopes.all());
    }

    private Drive getDriveService(String credentialsPath) throws IOException {
        Credential credential = authorize(credentialsPath);
        return new Drive.Builder(
            HTTP_TRANSPORT, JSON_FACTORY, credential)
            .setApplicationName(APPLICATION_NAME)
            .build();
    }

    private String getTempFile() throws IOException {
        java.io.File temp = java.io.File.createTempFile("inputFeed", ".csv");
        return temp.getAbsolutePath();
    }

    public String downloadLatest(String credentialPath, String parentDirId) throws IOException {
        Drive driveService = getDriveService(credentialPath);

        FileList result = driveService.files().list()
            .setQ("'" + parentDirId + "' in parents")
            .setPageSize(10)
            .setFields("nextPageToken, files(id, name, modifiedTime)")
            .execute();

        List<File> files = result.getFiles();

        System.out.println("Found " + files.size() + " files, will download the latest");

        File file = files
            .stream()
            .sorted((file1, file2) -> Long.compare(file1.getModifiedTime().getValue(), file2.getModifiedTime().getValue()))
            .findFirst()
            .get();

        String outputPath = getTempFile();

        System.out.println("Downloading file " + file.getId());
        OutputStream outputStream = new FileOutputStream(outputPath);
        driveService.files().get(file.getId())
            .executeMediaAndDownloadTo(outputStream);

        System.out.println("Downloaded successfully to " + outputPath);
        return outputPath;
    }


}
