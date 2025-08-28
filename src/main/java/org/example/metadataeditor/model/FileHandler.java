package org.example.metadataeditor.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileHandler {
  private File userDirectoryFile;
  
  public FileHandler() {
    // Gets user home directory and concatenates a path for app.
    String userHomeDir = System.getProperty("user.home");
    Path appDir = Paths.get(userHomeDir, "MP3-Metadata-Editor");

    // Tries to create folder for app.
    try {
      Files.createDirectories(appDir);
    } catch (IOException ioe) {
      throw new RuntimeException("Error creating app directory.");
    }

    createPathDataFiles(appDir);
  }

  public void createPathDataFiles(Path appDir) {
    // Create file to store source and target files.
    Path directoryDataFilePath = appDir.resolve("userDirectoryDesignation.json");
    boolean fileExists = false;
    if (Files.exists(directoryDataFilePath)) {
      System.out.println("File already exists.");
      fileExists = true;
    } else {
      try {
        Files.createFile(directoryDataFilePath);
      } catch (IOException e) {
        throw new RuntimeException("Exception whilst creating directoryDataFile.json");
      }
    }

    // Populate file if file is newly created.
    if (!fileExists) {
      String userHomeDirString = System.getProperty("user.home");
      Path userHomeDir = Paths.get(userHomeDirString);
      Path defaultSourceDir = userHomeDir.resolve("Downloads");
      Path defaultTargetDir = defaultSourceDir.resolve("newSong");

      // Create new object to insert into JSON.
      UserDirectories userDirectories =
          new UserDirectories(defaultSourceDir.toString(), defaultTargetDir.toString());
      try {
        ObjectMapper objectMapper = new ObjectMapper();
        userDirectoryFile = directoryDataFilePath.toFile();
        objectMapper.writeValue(userDirectoryFile, userDirectories);
      } catch (IOException e) {
        throw new RuntimeException(
            "Exception whilst writing default values to directoryDataFile.json");
      }
    }
  }

  static class UserDirectories {
    private String source;
    private String target;

    public UserDirectories() {}

    public UserDirectories(String source, String target) {
      this.source = source;
      this.target = target;
    }

    public String getSource() {
      return source;
    }

    public void setSource(String source) {
      this.source = source;
    }

    public String getTarget() {
      return target;
    }

    public void setTarget(String target) {
      this.target = target;
    }
  }

  public enum PathType {
    SOURCE,
    TARGET
  }

  public String getPathString(PathType pathType) {
    ObjectMapper objectMapper = new ObjectMapper();
    String userHomeDir = System.getProperty("user.home");
    Path appDir = Paths.get(userHomeDir, "MP3-Metadata-Editor");
    Path directoryDataFilePath = appDir.resolve("userDirectoryDesignation.json");
    
    File file = directoryDataFilePath.toFile();
    
    UserDirectories userDirectories;
    // Read JSON into a Map.
    try {
      userDirectories = objectMapper.readValue(file, objectMapper.constructType(UserDirectories.class));
    } catch (IOException e) {
      throw new RuntimeException("Error reading JSON file: userDirectoryDesignation");
    }
    
    switch (pathType) {
      case SOURCE -> {
        return userDirectories.getSource();
      }
      case TARGET -> {
        return userDirectories.getTarget();
      }
    }
    return null;
  }
  
  public File getUserDirectoryFile() {
    return userDirectoryFile;
  }
}
