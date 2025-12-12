package org.example.metadataeditor.model;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class AlbumMapper {
  private final Path albumFilePath;
  private final String jsonName = "albumMap.json";
  private List<Album> albumList;
  
  public AlbumMapper(Path appDirectory) {
    this.albumFilePath = appDirectory.resolve(jsonName);
    ObjectMapper objectMapper = new ObjectMapper();
    File file = albumFilePath.toFile();
    
    if (Files.exists(albumFilePath)) {
      System.out.println("albumMap.json already exists.");
      
      try {
        albumList = objectMapper.readValue(file, new TypeReference<>() {});
      } catch (IOException e) {
        throw new RuntimeException("Exception whilst reading JSON file: " + jsonName);
      }
    } else {
      try {
        Files.createFile(albumFilePath);
        albumList = new ArrayList<>();
        
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, albumList);
      } catch (IOException e) {
        throw new RuntimeException("Exception whilst creating " + jsonName);
      }
    }
  }
  
  public boolean doesAlbumExist(String albumName) {
    for (Album a : albumList) {
      if (a.getAlbumName().equals(albumName)) {
        return true;
      }
    }
    return false;
  }
  
  public void addAlbumToList(Album a) {
    // Ensures album does not already exist.
    if (!doesAlbumExist(a.getAlbumName())) {
      albumList.add(a);
      updateList();
    }
  }
  
  public Album getAlbum(String albumName) {
    assert doesAlbumExist(albumName);
    for (Album a: albumList) {
      if (a.getAlbumName().equals(albumName)) {
        return a;
      }
    }
    return null;
  }
  
  /**
   * Updates the list within the json file and updates the current albumList.
   */
  private void updateList() {
    File file = albumFilePath.toFile();
    ObjectMapper objectMapper = new ObjectMapper();
    try {
      objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, albumList);
      albumList = objectMapper.readValue(file, new TypeReference<>() {});
    } catch (IOException e) {
      throw new RuntimeException("Exception whilst reading or writing to JSON file " + jsonName);
    }
  }
}
