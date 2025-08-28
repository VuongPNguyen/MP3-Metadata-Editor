package org.example.metadataeditor.view;

import javafx.beans.value.ChangeListener;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import org.example.metadataeditor.controller.Controller;
import org.example.metadataeditor.model.TagEditor;

public class AlbumFields implements FXComponent {
  TagEditor tagEditor;

  Controller controller;

  public AlbumFields(TagEditor tagEditor, Controller controller) {
    this.tagEditor = tagEditor;
    this.controller = controller;
  }

  @Override
  public Parent render() {
    VBox vBox = new VBox();
    vBox.setStyle("-fx-alignment: center-left; -fx-font-size: 14; -fx-padding: 3;");

    Label albumLabel = new Label("Album: ");

    TextField albumField = new TextField(tagEditor.getAlbum());
    albumField.setPrefWidth(500);
    ChangeListener<Boolean> focusListener =
        (_, _, newVal) -> {
          if (albumField.getText() != null) {
            if (!newVal) {
              controller.setAlbum(albumField.getText());
            }
          }
        };
    albumField.focusedProperty().addListener(focusListener);

    vBox.getChildren().add(albumLabel);
    vBox.getChildren().add(albumField);

    return vBox;
  }
}
