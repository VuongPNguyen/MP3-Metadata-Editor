package org.example.metadataeditor.view;

import javafx.scene.Parent;
import javafx.scene.layout.VBox;
import org.example.metadataeditor.controller.Controller;
import org.example.metadataeditor.model.TagEditor;

public class SongFields implements FXComponent {
  private final TagEditor tagEditor;
  private final Controller controller;

  public SongFields(TagEditor tagEditor, Controller controller) {
    this.tagEditor = tagEditor;
    this.controller = controller;
  }

  @Override
  public Parent render() {
    VBox vBox = new VBox();

    vBox.getChildren()
        .addAll(
            new TitleFields(tagEditor, controller).render(),
            new ArtistFields(tagEditor, controller).render(),
            new AlbumFields(tagEditor, controller).render(),
            new AlbumArtistFields(tagEditor, controller).render(),
            new MiscFields(tagEditor, controller).render(),
            new AlbumImage(tagEditor, controller, TagEditor.FileType.NEW).render()
          );

    return vBox;
  }
}
