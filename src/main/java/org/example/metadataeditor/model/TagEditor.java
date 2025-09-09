package org.example.metadataeditor.model;

import com.mpatric.mp3agic.Mp3File;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.example.metadataeditor.model.FileHandler.PathType;

public class TagEditor {
  private String filePath;
  private Mp3File mp3File;
  
  private final FileHandler fileHandler;
  private final ArtistMapper artistMapper;
  
  private String sourceDirectory;
  private String targetFolder;

  private final List<ModelObserver> modelObserverArrayList = new ArrayList<>();

  public enum SongType {
    COVER,
    SINGLE,
    ALBUM
  }

  public TagEditor(String filePath, FileHandler fileHandler) {
    this.fileHandler = fileHandler;
    sourceDirectory = fileHandler.getPathString(PathType.SOURCE);
    targetFolder = fileHandler.getPathString(PathType.TARGET);

    artistMapper = new ArtistMapper(this.fileHandler.getAppDirectory());

    // Tries to set mp3file to designated filepath.
    if (filePath == null) {
      this.filePath = null;
      mp3File = null;
    } else {
      this.filePath = filePath;
      try {
        // Assigns mp3file and removes genre. (Genre is usually Music when downloaded)
        this.mp3File = new Mp3File(filePath);
        this.mp3File.getId3v2Tag().setGenreDescription("");
      } catch (Exception e) {
        throw new RuntimeException();
      }
    }
    notify(this);
  }

  public void newFile(String filePath) {
    // Does the same as the constructor.
    if (filePath == null) {
      this.filePath = null;
      mp3File = null;
    } else {
      this.filePath = filePath;
      try {
        this.mp3File = new Mp3File(filePath);
        this.mp3File.getId3v2Tag().setGenreDescription("");
      } catch (Exception e) {
        throw new RuntimeException();
      }
    }
    notify(this);
  }

  public Mp3File getMp3File() {
    return mp3File;
  }

  public void setDirectory(PathType pathType, String path) {
    fileHandler.setDirectory(pathType, path);
    sourceDirectory = fileHandler.getPathString(PathType.SOURCE);
    targetFolder = fileHandler.getPathString(PathType.TARGET);
    notify(this);
  }

  public String getPathString(PathType pathType) {
    return fileHandler.getPathString(pathType);
  }
  
  @SuppressWarnings("ResultOfMethodCallIgnored")
  public void saveTags() {
    if (mp3File != null) {
      // Creates string to save new mp3 file.
      StringBuilder tempFileName = new StringBuilder(filePath);
      try {
        File tempFile = new File(targetFolder);
        if (!tempFile.exists()) {
          tempFile.mkdir();
        }
        // Replaces source directory of file to target directory.
        if (Objects.equals(sourceDirectory, targetFolder)) {
          tempFileName.insert(tempFileName.length() - 4, " (1)");
        } else {
          tempFileName.replace(0, sourceDirectory.length(), targetFolder);
        }

        mp3File.save(tempFileName.toString());
      } catch (Exception e) {
        throw new RuntimeException("Error saving updated tags.");
      }
    }
  }

  public String getTitle() {
    if (filePath == null) {
      return "";
    }
    return Objects.requireNonNullElse(mp3File.getId3v2Tag().getTitle(), "");
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
    return Objects.requireNonNullElse(mp3File.getId3v2Tag().getArtist(), "");
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
    return Objects.requireNonNullElse(mp3File.getId3v2Tag().getAlbum(), "");
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
    return Objects.requireNonNullElse(mp3File.getId3v2Tag().getAlbumArtist(), "");
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
    return Objects.requireNonNullElse(mp3File.getId3v2Tag().getTrack(), "");
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
    return Objects.requireNonNullElse(mp3File.getId3v2Tag().getYear(), "");
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
    return Objects.requireNonNullElse(mp3File.getId3v2Tag().getGenreDescription(), "");
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
    return Objects.requireNonNullElse(mp3File.getId3v2Tag().getAlbumImage(), null);
  }

  public void setImage(byte[] image) {
    if (mp3File != null) {
      mp3File.getId3v2Tag().setAlbumImage(image, "image/jpeg");
    }
  }

  public void updateTags(SongType songType) {
    setArtist(replaceArtist(getArtist()));

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

  public void setNewArtistMap(String originalName, String newName) {
    artistMapper.setNewArtist(originalName, newName);
  }

  public String replaceArtist(String originalName) {
    return artistMapper.replaceArtist(originalName);
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
