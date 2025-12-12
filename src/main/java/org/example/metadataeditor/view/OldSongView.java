package org.example.metadataeditor.view;

import javafx.scene.Parent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.example.metadataeditor.controller.Controller;
import org.example.metadataeditor.model.TagEditor;

public class OldSongView implements FXComponent {
  private final TagEditor te;
  private final Controller controller;

  public OldSongView(TagEditor tagEditor, Controller controller) {
    te = tagEditor;
    this.controller = controller;
  }

  @Override
  public Parent render() {
    VBox vBox = new VBox();

    Label_TextboxStyle title = new Label_TextboxStyle("Title", te.getTitle(TagEditor.FileType.OLD));
    Label_TextboxStyle artist = new Label_TextboxStyle("Artist", te.getArtist(TagEditor.FileType.OLD));
    Label_TextboxStyle album = new Label_TextboxStyle("Album", te.getAlbum(TagEditor.FileType.OLD));
    Label_TextboxStyle albumArtist =
        new Label_TextboxStyle("Album Artist", te.getAlbumArtist(TagEditor.FileType.OLD));
    Label_TextboxStyle track = new Label_TextboxStyle("Track", te.getTrackNumber(TagEditor.FileType.OLD), 36, false);
    Label_TextboxStyle year = new Label_TextboxStyle("Year", te.getYear(TagEditor.FileType.OLD), 50, false);
    Label_TextboxStyle genre = new Label_TextboxStyle("Genre", te.getGenre(TagEditor.FileType.OLD), 120, false);
    
    HBox miscFields = new HBox(track.render(), year.render(), genre.render());

    vBox.getChildren()
        .addAll(
            title.render(),
            artist.render(),
            album.render(),
            albumArtist.render(),
            miscFields,
            new AlbumImage(te, controller, TagEditor.FileType.OLD).render()
            );

    return vBox;
  }
}
