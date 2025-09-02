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

public class ArtistMapper {
  private final Path artistFilePath;
  private final String jsonName = "artistMap.json";
  private Map<String, Object> artistMap;

  public ArtistMapper(Path appDirectory) {
    this.artistFilePath = appDirectory.resolve(jsonName);
    ObjectMapper objectMapper = new ObjectMapper();
    File file = artistFilePath.toFile();

    if (Files.exists(artistFilePath)) {
      System.out.println("artistMap.json already exists.");

      try {
        artistMap = objectMapper.readValue(file, new TypeReference<>() {});
      } catch (IOException e) {
        throw new RuntimeException("Exception whilst reading JSON file: " + jsonName);
      }
    } else {
      try {
        Files.createFile(artistFilePath);
        artistMap = new HashMap<>();

        objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, artistMap);
      } catch (IOException e) {
        throw new RuntimeException("Exception whilst creating " + jsonName);
      }
    }
  }

  public String replaceArtist(String originalName) {
    if (artistMap.containsKey(originalName)) {
      return artistMap.get(originalName).toString();
    }
    return originalName;
  }

  public void setNewArtist(String originalName, String newName) {
    File file = artistFilePath.toFile();
    ObjectMapper objectMapper = new ObjectMapper();

    try {
      JsonNode rootNode = objectMapper.readTree(file);

      if (rootNode instanceof ObjectNode) {
        ((ObjectNode) rootNode).put(originalName, newName);
      }

      objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, rootNode);

      resetMap();

    } catch (IOException e) {
      throw new RuntimeException("Exception whilst writing to " + jsonName);
    }
  }

  public void resetMap() {
    File file = artistFilePath.toFile();
    ObjectMapper objectMapper = new ObjectMapper();
    try {
      artistMap = objectMapper.readValue(file, new TypeReference<>() {});
    } catch (IOException e) {
      throw new RuntimeException("Exception whilst reading JSON file " + jsonName);
    }
  }
}
