package org.example.metadataeditor.model;

import com.mpatric.mp3agic.Mp3File;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.example.metadataeditor.model.FileHandler.PathType;

public class TagEditor {
  private String filePath;
  private Mp3File oldMp3;
  private Mp3File newMp3;
  
  private final FileHandler fileHandler;
  private final ArtistMapper artistMapper;
  private final AlbumMapper albumMapper;
  
  private String sourceDirectory;
  private String targetFolder;

  private final List<ModelObserver> modelObserverArrayList = new ArrayList<>();

  private SongType lastSelectedType = null;
  
  public enum SongType {
    COVER,
    SINGLE,
    ALBUM
  }
  
  public enum FileType {
    OLD,
    NEW
  }
  
  public boolean genreError = false;

  public TagEditor(String filePath, FileHandler fileHandler) {
    this.fileHandler = fileHandler;
    sourceDirectory = fileHandler.getPathString(PathType.SOURCE);
    targetFolder = fileHandler.getPathString(PathType.TARGET);

    artistMapper = new ArtistMapper(this.fileHandler.getAppDirectory());
    albumMapper = new AlbumMapper(this.fileHandler.getAppDirectory());

    // Tries to set mp3file to designated filepath.
    if (filePath == null) {
      this.filePath = null;
      oldMp3 = null;
      newMp3 = null;
    } else {
      this.filePath = filePath;
      try {
        this.oldMp3 = new Mp3File(filePath);
        this.newMp3 = new Mp3File(filePath);
      } catch (Exception e) {
        throw new RuntimeException();
      }
    }
    notify(this);
  }

  public void newFile(String filePath) {
    setLastSelectedType(null);
    // Does the same as the constructor.
    if (filePath == null) {
      this.filePath = null;
      oldMp3 = null;
      newMp3 = null;
    } else {
      this.filePath = filePath;
      try {
        this.oldMp3 = new Mp3File(filePath);
        this.newMp3 = new Mp3File(filePath);
      } catch (Exception e) {
        throw new RuntimeException();
      }
    }
    notify(this);
  }

  public Mp3File getMp3(FileType fileType) {
    switch (fileType) {
      case NEW -> {return newMp3;}
      case OLD -> {return oldMp3;}
      case null, default -> {return null;}
    }
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
    if (newMp3 != null) {
      // Creates string to save new mp3 file.
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

        newMp3.save(tempFileName.toString());
        
        // Save successful, update album map if necessary.
        if (lastSelectedType == SongType.ALBUM && !albumMapper.doesAlbumExist(getAlbum(FileType.NEW))) {
          Album a = new Album(
              getAlbum(FileType.NEW),
              getAlbumArtist(FileType.NEW),
              getYear(FileType.NEW),
              getGenre(FileType.NEW),
              getAlbumImage(FileType.NEW)
          );
          albumMapper.addAlbumToList(a);
        }
      } catch (Exception e) {
        throw new RuntimeException("Error saving updated tags.");
      }
    }

    notify(this);
  }
  
  public String getTitle(FileType fileType) {
    if (filePath == null) {
      return "";
    }
    switch (fileType) {
      case OLD -> {return Objects.requireNonNullElse(oldMp3.getId3v2Tag().getTitle(), "");}
      case NEW -> {return Objects.requireNonNullElse(newMp3.getId3v2Tag().getTitle(), "");}
    }
    return "";
  }

  public void setTitle(String title) {
    if (newMp3 != null) {
      newMp3.getId3v2Tag().setTitle(title);
      System.out.println("Set title: " + getTitle(FileType.NEW));
    }
    if (lastSelectedType == SongType.SINGLE) {
      setAlbum(title);
    }
    notify(this);
  }

  public String getArtist(FileType fileType) {
    if (filePath == null) {
      return "";
    }
    switch (fileType) {
      case OLD -> {return Objects.requireNonNullElse(oldMp3.getId3v2Tag().getArtist(), "");}
      case NEW -> {return Objects.requireNonNullElse(newMp3.getId3v2Tag().getArtist(), "");}
    }
    return "";
  }

  public void setArtist(String artist) {
    if (newMp3 != null) {
      newMp3.getId3v2Tag().setArtist(artist);
      System.out.println("Set artist: " + getArtist(FileType.NEW));
      notify(this);
    }
  }

  public String getAlbum(FileType fileType) {
    if (filePath == null) {
      return "";
    }
    switch (fileType) {
      case OLD -> {return Objects.requireNonNullElse(oldMp3.getId3v2Tag().getAlbum(), "");}
      case NEW -> {return Objects.requireNonNullElse(newMp3.getId3v2Tag().getAlbum(), "");}
    }
    return "";
  }

  public void setAlbum(String album) {
    if (newMp3 != null) {
      newMp3.getId3v2Tag().setAlbum(album);
      System.out.println("Set album: " + getAlbum(FileType.NEW));
    }
    notify(this);
  }

  public String getAlbumArtist(FileType fileType) {
    if (filePath == null) {
      return "";
    }
    switch (fileType) {
      case OLD -> {return Objects.requireNonNullElse(oldMp3.getId3v2Tag().getAlbumArtist(), "");}
      case NEW -> {return Objects.requireNonNullElse(newMp3.getId3v2Tag().getAlbumArtist(), "");}
    }
    return "";
  }

  public void setAlbumArtist(String albumArtist) {
    if (newMp3 != null) {
      newMp3.getId3v2Tag().setAlbumArtist(albumArtist);
      System.out.println("Set album artist: " + getAlbumArtist(FileType.NEW));
    }
  }

  public String getTrackNumber(FileType fileType) {
    if (filePath == null) {
      return "";
    }
    switch (fileType) {
      case OLD -> {return Objects.requireNonNullElse(oldMp3.getId3v2Tag().getTrack(), "");}
      case NEW -> {return Objects.requireNonNullElse(newMp3.getId3v2Tag().getTrack(), "");}
    }
    return "";
  }

  public void setTrackNumber(String number) {
    if (newMp3 != null) {
      newMp3.getId3v2Tag().setTrack(number);
    }
  }

  public String getYear(FileType fileType) {
    if (filePath == null) {
      return "";
    }
    switch (fileType) {
      case OLD -> {return Objects.requireNonNullElse(oldMp3.getId3v2Tag().getYear(), "");}
      case NEW -> {return Objects.requireNonNullElse(newMp3.getId3v2Tag().getYear(), "");}
    }
    return "";
  }

  public void setYear(String year) {
    if (newMp3 != null) {
      newMp3.getId3v2Tag().setYear(year);
      System.out.println("Set year: " + getYear(FileType.NEW));
    }
  }

  public String getGenre(FileType fileType) {
    if (filePath == null) {
      return "";
    }
    switch (fileType) {
      case OLD -> {return Objects.requireNonNullElse(oldMp3.getId3v2Tag().getGenreDescription(), "");}
      case NEW -> {return Objects.requireNonNullElse(newMp3.getId3v2Tag().getGenreDescription(), "");}
    }
    return "";
  }

  public void setGenre(String genre) {
    if (newMp3 != null) {
      try {
        newMp3.getId3v2Tag().setGenreDescription(genre);
        System.out.println("Set genre: " + getGenre(FileType.NEW));
      } catch (IllegalArgumentException e) {
        System.out.println("Genre is not valid");
        genreError = true;
        notify(this);
      } finally {
        genreError = true;
      }
    }
  }

  public byte[] getAlbumImage(FileType fileType) {
    if (filePath == null) {
      return null;
    }
    switch (fileType) {
      case OLD -> {return Objects.requireNonNullElse(oldMp3.getId3v2Tag().getAlbumImage(), null);}
      case NEW -> {return Objects.requireNonNullElse(newMp3.getId3v2Tag().getAlbumImage(), null);}
    }
    return null;
  }

  public void setAlbumImage(byte[] image) {
    if (newMp3 != null) {
      newMp3.getId3v2Tag().setAlbumImage(image, "image/jpeg");
    }
  }

  public void updateTags(SongType songType) {
    System.out.println("Setting to " + songType.name() + ":");
    setArtist(replaceArtist(getArtist(FileType.OLD)));

    switch (songType) {
      case COVER -> {
        setAlbum(getArtist(FileType.NEW) + " Covers");
        setAlbumArtist(getArtist(FileType.NEW));
      }
      case SINGLE -> {
        setAlbum(getTitle(FileType.NEW));
        setAlbumArtist(getArtist(FileType.NEW));
        setTrackNumber("1");
      }
      case ALBUM -> {
        String albumName = getAlbum(FileType.NEW);
        if (albumMapper.doesAlbumExist(albumName)) {
          Album a = albumMapper.getAlbum(albumName);
          setAlbumArtist(a.getAlbumArtist());
          setYear(a.getYear());
          setGenre(a.getGenre());
          setAlbumImage(a.getAlbumImage());
        }
      }
    }

    notify(this);
  }
  
  public SongType getLastSelectedType() {
    return lastSelectedType;
  }
  
  public void setLastSelectedType(SongType lastSelectedType) {
    this.lastSelectedType = lastSelectedType;
  }
  
  /* Artist Mapping Methods */
  
  public void setNewArtistMap(String originalName, String newName) {
    artistMapper.setNewArtist(originalName, newName);
  }

  public String replaceArtist(String originalName) {
    return artistMapper.replaceArtist(originalName);
  }
  
  /* Album Mapping Methods */
  
  public boolean doesAlbumExist(String albumName) {
    return albumMapper.doesAlbumExist(albumName);
  }
  
  /* MVC Methods */

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
