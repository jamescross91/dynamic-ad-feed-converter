package io;


import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.FileContent;
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
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DriveHandler {

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

        List<File> files = getFiles(parentDirId, driveService);

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

    public void uploadFile(String credentialPath, String parentDirId, String destFileName, String path) throws IOException {
        System.out.println("Will upload file at path " + path + " to Google Drive parent directory ID " + parentDirId);
        Drive driveService = getDriveService(credentialPath);

        List<File> files = getFiles(parentDirId, driveService);
        List<File> filteredFiles = files.stream().filter(file -> file.getName().equals(destFileName)).collect(Collectors.toList());

        if (filteredFiles.size() == 0) {
            System.out.println("No files found matching name " + destFileName + " will create a new one");
            createNewFile(parentDirId, path, destFileName, driveService);
        } else {
            System.out.println("Found file matching " + destFileName + " will update");
            File toUpdate = filteredFiles.get(0);
            updateFile(path, destFileName, toUpdate.getId(), driveService);
        }
    }

    private void updateFile(String path, String fileName, String fileId, Drive driveService) throws IOException {
        File file = new File();
        file.setName(fileName);
        java.io.File filePath = new java.io.File(path);
        FileContent fileContent = new FileContent("text/csv", filePath);

        System.out.println("Updating file " + fileName + " in Google Drive");
        driveService.files().update(fileId, file, fileContent).execute();
    }

    private void createNewFile(String parentDirId, String path, String fileName, Drive driveService) throws IOException {
        File fileMeta = new File();
        fileMeta.setName(fileName);
        ArrayList<String> parent = new ArrayList<>();
        parent.add(parentDirId);
        fileMeta.setParents(parent);

        java.io.File filePath = new java.io.File(path);
        FileContent fileContent = new FileContent("text/csv", filePath);
        System.out.println("Uploading new file to Google Drive with name " + fileName + " to parent directory " + parentDirId);
        driveService.files().create(fileMeta, fileContent).setFields("id").execute();
    }


    private List<File> getFiles(String parentDirId, Drive driveService) throws IOException {
        FileList result = driveService.files().list()
            .setQ("'" + parentDirId + "' in parents")
            .setPageSize(10)
            .setFields("nextPageToken, files(id, name, modifiedTime)")
            .execute();

        return result.getFiles();
    }
}
