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
  private final AlbumMapper albumMapper;
  
  private String sourceDirectory;
  private String targetFolder;

  private SongType lastPressed = null;

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
    albumMapper = new AlbumMapper(this.fileHandler.getAppDirectory());

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
    lastPressed = null;
    // Does the same as the constructor.
    if (filePath == null) {
      this.filePath = null;
      mp3File = null;
    } else {
      this.filePath = filePath;
      try {
        this.mp3File = new Mp3File(filePath);
        this.mp3File.getId3v2Tag().setGenreDescription("");

        // If artist has an existing map (current artist != replaced artist) then change album artist and artist.
        if (!getArtist().equals(replaceArtist(getArtist()))) {
          setArtist(replaceArtist(getArtist()));
          setAlbumArtist(replaceArtist(getArtist()));
        }
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
      // Creates stringBuilder for mp3 filePath + name.
      StringBuilder tempFileName = new StringBuilder(filePath);
      try {
        File tempFile = new File(targetFolder);
        // Ensures that target directory exists, if not then makes one.
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

        // Save successful, update album map if necessary.
        if (lastPressed == SongType.ALBUM && !albumMapper.doesAlbumExist(getAlbumName())) {
          Album a = new Album(getAlbumName(), getAlbumArtist(), getYear(), getGenre(), getAlbumImage());
          albumMapper.addAlbumToList(a);
        }

      } catch (Exception e) {
        throw new RuntimeException("Error saving updated tags.");
      }
    }

    notify(this);
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

  public String getAlbumName() {
    if (filePath == null) {
      return "";
    }
    return Objects.requireNonNullElse(mp3File.getId3v2Tag().getAlbum(), "");
  }

  public void setAlbumName(String album) {
    if (mp3File != null) {
      mp3File.getId3v2Tag().setAlbum(album);
      System.out.println(getAlbumName());
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

  public byte[] getAlbumImage() {
    if (filePath == null) {
      return null;
    }
    return mp3File.getId3v2Tag().getAlbumImage();
  }

  public void setAlbumImage(byte[] image) {
    if (mp3File != null) {
      mp3File.getId3v2Tag().setAlbumImage(image, "image/jpeg");
    }
  }

  public void updateTags(SongType songType) {
    switch (songType) {
      case COVER -> {
        lastPressed = SongType.COVER;
        setAlbumName(getAlbumArtist() + " Covers");
      }
      case SINGLE -> {
        lastPressed = SongType.SINGLE;
        setAlbumName(getTitle());
        setTrackNumber("1");
      }
      case ALBUM -> {
        lastPressed = SongType.ALBUM;
        if (albumMapper.doesAlbumExist(getAlbumName())) {
          Album a = albumMapper.getAlbum(getAlbumName());
          setYear(a.getYear());
          setGenre(a.getGenre());
          setAlbumImage(a.getAlbumImage());
        }
      }
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
