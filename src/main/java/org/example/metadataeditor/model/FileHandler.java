package org.example.metadataeditor.model;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class FileHandler {
  private final Path appDirectory;
  private final String jsonName = "userDirectoryDesignation.json";
  
  public FileHandler() {
    // Gets user home directory and concatenates a path for app.
    String userHomeDir = System.getProperty("user.home");
    appDirectory = Path.of(userHomeDir, "MP3-Metadata-Editor");

    // Tries to create folder for app.
    try {
      Files.createDirectories(appDirectory);
    } catch (IOException ioe) {
      throw new RuntimeException("Error creating app directory.");
    }

    createPathDataFiles(appDirectory);
  }

  public Path getAppDirectory() {
    return appDirectory;
  }

  public void createPathDataFiles(Path appDir) {
    // Create file to store source and target files.
    Path directoryDataFilePath = appDir.resolve(jsonName);
    boolean fileExists = false;
    if (Files.exists(directoryDataFilePath)) {
      System.out.println("userDirectoryDesignation.json already exists.");
      fileExists = true;
    } else {
      try {
        Files.createFile(directoryDataFilePath);
      } catch (IOException e) {
        throw new RuntimeException("Exception whilst creating " + jsonName);
      }
    }

    // Populate file if file is newly created.
    if (!fileExists) {
      String userHomeDirString = System.getProperty("user.home");
      Path userHomeDir = Path.of(userHomeDirString);
      Path defaultSourceDir = userHomeDir.resolve("Downloads");
      Path defaultTargetDir = defaultSourceDir.resolve("newSong");

      // Create new object to insert into JSON.
      Map<String, Object> map = new HashMap<>();
      map.put("source", defaultSourceDir.toString());
      map.put("target", defaultTargetDir.toString());

      try {
        ObjectMapper objectMapper = new ObjectMapper();
        File userDirectoryFile = directoryDataFilePath.toFile();

        objectMapper.writerWithDefaultPrettyPrinter().writeValue(userDirectoryFile, map);
      } catch (IOException e) {
        throw new RuntimeException(
            "Exception whilst writing default values to " + jsonName);
      }
    }
  }

  public enum PathType {
    SOURCE,
    TARGET
  }

  public String getPathString(PathType pathType) {
    // Setup path to JSON.
    ObjectMapper objectMapper = new ObjectMapper();
    Path directoryDataFilePath = appDirectory.resolve(jsonName);
    
    File file = directoryDataFilePath.toFile();

    Map<String, Object> map;
    
    try {
      map = objectMapper.readValue(file, new TypeReference<>() {});
    } catch (IOException e) {
      throw new RuntimeException("Error reading JSON file: " + jsonName);
    }
    
    switch (pathType) {
      case SOURCE -> {
        return map.get("source").toString();
      }
      case TARGET -> {
        return map.get("target").toString();
      }
    }
    return null;
  }
  
  public void setDirectory (PathType pathType, String directory) {
    ObjectMapper objectMapper = new ObjectMapper();
    File jsonFile = appDirectory.resolve(jsonName).toFile();

    try {
      JsonNode rootNode = objectMapper.readTree(jsonFile);

      if (rootNode instanceof ObjectNode) {
        String attribute = "";
        switch (pathType) {
          case SOURCE -> attribute = "source";
          case TARGET -> attribute = "target";
        }
        ((ObjectNode) rootNode).put(attribute, directory);
      }

      objectMapper.writerWithDefaultPrettyPrinter().writeValue(jsonFile, rootNode);
    } catch (IOException e) {
      throw new RuntimeException("Exception whilst writing to " + jsonName);
    }
  }
}
