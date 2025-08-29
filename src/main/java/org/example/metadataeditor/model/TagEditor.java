package org.example.metadataeditor.model;

import com.mpatric.mp3agic.ID3v24Tag;
import com.mpatric.mp3agic.Mp3File;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class TagEditor {
  private String filePath;
  public Mp3File mp3File;
  
  public FileHandler fileHandler;
  
  private final String sourceDirectory;
  private final String targetFolder;

  private final List<ModelObserver> modelObserverArrayList = new ArrayList<>();

  public enum SongType {
    COVER,
    SINGLE,
    ALBUM
  }

  public TagEditor(String filePath, FileHandler fileHandler) {
    this.fileHandler = fileHandler;
    sourceDirectory = fileHandler.getPathString(FileHandler.PathType.SOURCE);
    targetFolder = fileHandler.getPathString(FileHandler.PathType.TARGET);
    
    if (filePath == null) {
      this.filePath = null;
      mp3File = null;
    } else {
      this.filePath = filePath;
      try {
        this.mp3File = new Mp3File(filePath);
        ID3v24Tag id3v24Tag = (ID3v24Tag) this.mp3File.getId3v2Tag();
        id3v24Tag.setGenreDescription("");
      } catch (Exception e) {
        throw new RuntimeException();
      }
    }
    notify(this);
  }

  public void newFile(String filePath) {
    if (filePath == null) {
      this.filePath = null;
      mp3File = null;
    } else {
      this.filePath = filePath;
      try {
        this.mp3File = new Mp3File(filePath);
      } catch (Exception e) {
        throw new RuntimeException();
      }
    }
    notify(this);
  }

  public void setDirectory(FileHandler.PathType pathType, String path) {
    fileHandler.setDirectory(pathType, path);
    notify(this);
  }

  public String getPathString(FileHandler.PathType pathType) {
    return fileHandler.getPathString(pathType);
  }
  
  @SuppressWarnings("ResultOfMethodCallIgnored")
  public void saveTags() {
    StringBuilder tempFileName = new StringBuilder(filePath);
    try {
      File tempFile = new File(targetFolder);
      if (!tempFile.exists()) {
        tempFile.mkdir();
      }
      tempFileName.insert(sourceDirectory.length(), "\\NewSong");
      //      System.out.println(tempFileName.toString());
      mp3File.save(tempFileName.toString());
    } catch (Exception e) {
      throw new RuntimeException("Error saving updated tags.");
    }
  }

  public String getTitle() {
    if (filePath == null) {
      return "";
    }
    if (mp3File.getId3v2Tag().getTitle() == null) {
      return "";
    } else {
      return mp3File.getId3v2Tag().getTitle();
    }
  }

  public void setTitle(String title) {
    if (mp3File != null) {
      mp3File.getId3v2Tag().setTitle(title);
      System.out.println(getTitle());
    }
  }

  public String getArtist() {
    if (filePath == null) {
      return "";
    }
    if (mp3File.getId3v2Tag().getArtist() == null) {
      return "";
    } else {
      return mp3File.getId3v2Tag().getArtist();
    }
  }

  public void setArtist(String artist) {
    if (mp3File != null) {
      mp3File.getId3v2Tag().setArtist(artist);
      System.out.println(getArtist());
    }
  }

  public String getAlbum() {
    if (filePath == null) {
      return "";
    }
    if (mp3File.getId3v2Tag().getAlbum() != null) {
      return mp3File.getId3v2Tag().getAlbum();
    } else {
      return "";
    }
  }

  public void setAlbum(String album) {
    if (mp3File != null) {
      mp3File.getId3v2Tag().setAlbum(album);
      System.out.println(getAlbum());
    }
  }

  public String getAlbumArtist() {
    if (filePath == null) {
      return "";
    }
    if (mp3File.getId3v2Tag().getAlbumArtist() == null) {
      return "";
    } else {
      return mp3File.getId3v2Tag().getAlbumArtist();
    }
  }

  public void setAlbumArtist(String albumArtist) {
    if (mp3File != null) {
      mp3File.getId3v2Tag().setAlbumArtist(albumArtist);
      System.out.println(getAlbumArtist());
    }
  }

  public String getTrackNumber() {
    if (filePath == null) {
      return "";
    }
    if (mp3File.getId3v2Tag().getTrack() == null) {
      return "";
    } else {
      return mp3File.getId3v2Tag().getTrack();
    }
  }

  public void setTrackNumber(String number) {
    if (mp3File != null) {
      mp3File.getId3v2Tag().setTrack(number);
    }
  }

  public String getYear() {
    if (filePath == null) {
      return "";
    }
    if (mp3File.getId3v2Tag().getYear() == null) {
      return "";
    } else {
      return mp3File.getId3v2Tag().getYear();
    }
  }

  public void setYear(String year) {
    if (mp3File != null) {
      mp3File.getId3v2Tag().setYear(year);
      System.out.println(getYear());
    }
  }

  public String getGenre() {
    if (filePath == null) {
      return "";
    }
    if (mp3File.getId3v2Tag().getGenreDescription() == null) {
      return "";
    } else {
      return mp3File.getId3v2Tag().getGenreDescription();
    }
  }

  public void setGenre(String genre) {
    if (mp3File != null) {
      mp3File.getId3v2Tag().setGenreDescription(genre);
      System.out.println(getGenre());
    }
  }

  public byte[] getImage() {
    if (filePath == null) {
      return null;
    }
    if (mp3File.getId3v2Tag().getGenreDescription() == null) {
      return null;
    } else {
      return mp3File.getId3v2Tag().getAlbumImage();
    }
  }

  public void setImage(byte[] image) {
    if (mp3File != null) {
      mp3File.getId3v2Tag().setAlbumImage(image, "album_img");
    }
  }

  public void updateTags(SongType songType) {
    switch (songType) {
      case COVER -> {
        setAlbum(getArtist() + " Covers");
        setAlbumArtist(getArtist());
      }
      case SINGLE -> {
        setAlbum(getTitle());
        setAlbumArtist(getArtist());
        setTrackNumber("1");
      }
      case ALBUM -> setAlbumArtist(getArtist());
    }

    notify(this);
  }

  public void notify(TagEditor tagEditor) {
    for (ModelObserver m : modelObserverArrayList) {
      m.update(tagEditor);
    }
  }

  public void addObserver(ModelObserver observer) {
    modelObserverArrayList.add(observer);
  }

  @SuppressWarnings("unused")
  public void removeObserver(ModelObserver observer) {
    modelObserverArrayList.remove(observer);
  }
}
