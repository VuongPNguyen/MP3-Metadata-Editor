package org.example.metadataeditor.view;

import javafx.scene.Parent;
import javafx.scene.layout.VBox;
import org.example.metadataeditor.controller.Controller;
import org.example.metadataeditor.model.TagEditor;

public class SongFields implements FXComponent {
  private TagEditor tagEditor;
  private Controller controller;

  TitleFields titleFields;

  public SongFields(TagEditor tagEditor, Controller controller) {
    this.tagEditor = tagEditor;
    this.controller = controller;
  }

  @Override
  public Parent render() {
    VBox vBox = new VBox();

    vBox.getChildren().add(new TitleFields(tagEditor, controller).render());
    vBox.getChildren().add(new ArtistFields(tagEditor, controller).render());
    vBox.getChildren().add(new AlbumFields(tagEditor, controller).render());
    vBox.getChildren().add(new AlbumArtistFields(tagEditor, controller).render());
    vBox.getChildren().add(new MiscFields(tagEditor, controller).render());
    return vBox;
  }
}
