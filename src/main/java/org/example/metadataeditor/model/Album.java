package org.example.metadataeditor.model;

public class Album {
  private String albumName;
  private String albumArtist;
  private String year;
  private String genre;
  private byte[] albumImage;

  public Album() {}

  public Album(String albumName, String albumArtist, String year, String genre, byte[] albumImage) {
    this.albumName = albumName;
    this.albumArtist = albumArtist;
    this.year = year;
    this.genre = genre;
    this.albumImage = albumImage;
  }

  public String getAlbumName() {
    return albumName;
  }

  public String getAlbumArtist() {
    return albumArtist;
  }

  public String getYear() {
    return year;
  }

  public String getGenre() {
    return genre;
  }

  public byte[] getAlbumImage() {
    return albumImage;
  }
}
