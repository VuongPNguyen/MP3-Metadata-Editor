package org.example.metadataeditor.view;

import javafx.beans.value.ChangeListener;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import org.example.metadataeditor.controller.Controller;
import org.example.metadataeditor.model.TagEditor;

public class AlbumArtistFields implements FXComponent {
  final TagEditor tagEditor;
  final Controller controller;

  public AlbumArtistFields(TagEditor tagEditor, Controller controller) {
    this.tagEditor = tagEditor;
    this.controller = controller;
  }

  @Override
  public Parent render() {
    VBox vBox = new VBox();
    vBox.setStyle("-fx-alignment: center-left; -fx-padding: 3;");

    Label albumArtistLabel = new Label("Album Artist: ");

    TextField albumArtistField = new TextField(tagEditor.getAlbumArtist(TagEditor.FileType.NEW));
    ChangeListener<Boolean> focusListener =
        (_, _, newVal) -> {
          if (albumArtistField.getText() != null) {
            if (!newVal) {
              controller.setAlbumArtist(albumArtistField.getText());
            }
          }
        };
    albumArtistField.focusedProperty().addListener(focusListener);

    vBox.getChildren().add(albumArtistLabel);
    vBox.getChildren().add(albumArtistField);

    return vBox;
  }
}
