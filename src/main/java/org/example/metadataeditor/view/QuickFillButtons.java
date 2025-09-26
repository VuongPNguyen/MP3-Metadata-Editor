package org.example.metadataeditor.view;

import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import org.example.metadataeditor.controller.Controller;
import org.example.metadataeditor.model.TagEditor;

import java.util.Objects;

public class QuickFillButtons implements FXComponent {
  private final TagEditor tagEditor;
  private final Controller controller;

  public QuickFillButtons(TagEditor tagEditor, Controller controller) {
    this.tagEditor = tagEditor;
    this.controller = controller;
  }

  @Override
  public Parent render() {
    Button coverButton = new Button("Cover");
    Button singleButton = new Button("Single");
    Button albumButton = new Button("Album");

    Label artistLabel = new Label();
    if (tagEditor.getAlbumArtist().isEmpty()) {
      artistLabel.setText(tagEditor.getArtist());
    } else {
      artistLabel.setText(tagEditor.getAlbumArtist());
    }
    artistLabel.setPrefWidth(120);
    artistLabel.setStyle(
        "-fx-border-color: lightgray; "
            + "-fx-border-width: 1; "
            + "-fx-border-radius: 3; "
            + "-fx-background-color: white; "
            + "-fx-padding: 4; "
            + "-fx-font-family: 'System'; ");

    Label arrowLabel = new Label(" → ");

    TextField newArtistNameField = new TextField();
    if (tagEditor.getAlbumArtist().isEmpty()) {
      newArtistNameField.setPromptText(tagEditor.replaceArtist(tagEditor.getArtist()));
    } else {
      newArtistNameField.setPromptText(tagEditor.replaceArtist(tagEditor.getAlbumArtist()));
    }
    newArtistNameField.setStyle("-fx-prompt-text-fill: #525252");

    // Button Actions
    coverButton.setOnAction(_ -> {
      checkArtistToMap(artistLabel, newArtistNameField);
      controller.updateTags(TagEditor.SongType.COVER);
    });

    singleButton.setOnAction(_ -> {
      checkArtistToMap(artistLabel, newArtistNameField);
      controller.updateTags(TagEditor.SongType.SINGLE);
    });

    albumButton.setOnAction(_ -> {
      checkArtistToMap(artistLabel, newArtistNameField);
      controller.updateTags(TagEditor.SongType.ALBUM);
    });


    HBox hBox = new HBox();
    hBox.setAlignment(Pos.CENTER_LEFT);
    hBox.getChildren()
        .addAll(
            coverButton, singleButton, albumButton, artistLabel, arrowLabel, newArtistNameField);

    return hBox;
  }

  private void checkArtistToMap(Label label, TextField textField) {
    if (!textField.getText().isEmpty()) {
      if (!Objects.equals(label.getText(), textField.getText())) {
        controller.setNewArtistMap(label.getText(), textField.getText());
      }
    } else {
      if (!Objects.equals(label.getText(), textField.getPromptText())) {
        tagEditor.replaceArtist(label.getText());
      }
    }
  }
}
