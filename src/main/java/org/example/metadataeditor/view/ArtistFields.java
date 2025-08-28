package org.example.metadataeditor.view;

import javafx.beans.value.ChangeListener;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import org.example.metadataeditor.controller.Controller;
import org.example.metadataeditor.model.TagEditor;

public class ArtistFields implements FXComponent {
  TagEditor tagEditor;

  Controller controller;

  public ArtistFields(TagEditor tagEditor, Controller controller) {
    this.tagEditor = tagEditor;
    this.controller = controller;
  }

  @Override
  public Parent render() {
    VBox vBox = new VBox();
    vBox.setStyle("-fx-alignment: center-left; -fx-font-size: 14; -fx-padding: 3;");

    Label artistLabel = new Label("Artist: ");

    TextField artistField = new TextField(tagEditor.getArtist());
    artistField.setPrefWidth(500);
    ChangeListener<Boolean> focusListener =
        (_, _, newVal) -> {
          if (artistField.getText() != null) {
            if (!newVal) {
              controller.setArtist(artistField.getText());
            }
          }
        };
    artistField.focusedProperty().addListener(focusListener);

    vBox.getChildren().add(artistLabel);
    vBox.getChildren().add(artistField);

    return vBox;
  }
}
