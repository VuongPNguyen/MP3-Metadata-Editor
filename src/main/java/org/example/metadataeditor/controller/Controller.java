package org.example.metadataeditor.controller;

import org.example.metadataeditor.model.TagEditor;

public class Controller {
  final TagEditor tagEditor;

  public Controller(TagEditor tagEditor) {
    this.tagEditor = tagEditor;
  }

  public void setTitle(String title) {
    tagEditor.setTitle(title);
  }

  public void setArtist(String artist) {
    tagEditor.setArtist(artist);
  }

  public void setAlbum(String album) {
    tagEditor.setAlbum(album);
  }

  public void setAlbumArtist(String albumArtist) {
    tagEditor.setAlbumArtist(albumArtist);
  }

  public void setTrackNumber(String number) {
    tagEditor.setTrackNumber(number);
  }

  public void setYear(String year) {
    tagEditor.setYear(year);
  }

  public void setGenre(String genre) {
    tagEditor.setGenre(genre);
  }

  public void setImage(byte[] image) {
    tagEditor.setImage(image);
  }

  public void saveTags() {
    tagEditor.saveTags();
  }

  public void updateTags(TagEditor.SongType songType) {
    tagEditor.updateTags(songType);
  }

  public void setNewArtistMap(String originalName, String newName) {
    tagEditor.setNewArtistMap(originalName, newName);
  }

  public void newFile(String filePath) {
    tagEditor.newFile(filePath);
  }
  
  public void setLastSelectedType(TagEditor.SongType songType) {
    tagEditor.setLastSelectedType(songType);
  }
}
