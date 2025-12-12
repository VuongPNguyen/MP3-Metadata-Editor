package org.example.metadataeditor.view;

import java.util.Objects;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import org.example.metadataeditor.controller.Controller;
import org.example.metadataeditor.model.TagEditor;

public class QuickFillButtons implements FXComponent {
  private final TagEditor tagEditor;
  private final Controller controller;

  public QuickFillButtons(TagEditor tagEditor, Controller controller) {
    this.tagEditor = tagEditor;
    this.controller = controller;
  }

  @Override
  public Parent render() {
    // Radio Buttons
    final ToggleGroup tg = new ToggleGroup();

    RadioButton coverButton = new RadioButton("Cover");
    coverButton.setStyle("-fx-padding: 0 0 0 10;");
    RadioButton singleButton = new RadioButton("Single");
    singleButton.setStyle("-fx-padding: 0 0 0 10;");
    RadioButton albumButton = new RadioButton("Album");
    albumButton.setStyle("-fx-padding: 0 10 0 10;");

    coverButton.setToggleGroup(tg);
    singleButton.setToggleGroup(tg);
    albumButton.setToggleGroup(tg);

    switch (tagEditor.getLastSelectedType()) {
      case COVER -> coverButton.setSelected(true);
      case SINGLE -> singleButton.setSelected(true);
      case ALBUM -> albumButton.setSelected(true);
      case null, default -> {}
    }

    tg.selectedToggleProperty()
        .addListener(
            (_, _, _) -> {
              RadioButton rb = (RadioButton) tg.getSelectedToggle();

              switch (rb.getText()) {
                case "Cover" -> {
                  controller.setLastSelectedType(TagEditor.SongType.COVER);
                  controller.updateTags(TagEditor.SongType.COVER);
                }
                case "Single" -> {
                  controller.setLastSelectedType(TagEditor.SongType.SINGLE);
                  controller.updateTags(TagEditor.SongType.SINGLE);
                }
                case "Album" -> {
                  controller.setLastSelectedType(TagEditor.SongType.ALBUM);
                  controller.updateTags(TagEditor.SongType.ALBUM);
                }
              }
            });

    // Artist Conversion
    Label artistLabel = new Label(tagEditor.getArtist(TagEditor.FileType.OLD));
    artistLabel.setPrefWidth(180);
    artistLabel.setStyle(
        "-fx-border-color: lightgray; "
            + "-fx-border-width: 1; "
            + "-fx-border-radius: 3; "
            + "-fx-background-color: gainsboro ; "
            + "-fx-padding: 3 3 3 5; "
            + "-fx-font-family: 'System'; ");

    Label arrowLabel = new Label(" → ");

    TextField newArtistNameField = new TextField();
    newArtistNameField.setPromptText(
        tagEditor.replaceArtist(tagEditor.getArtist(TagEditor.FileType.OLD)));
    newArtistNameField.setStyle("-fx-prompt-text-fill: #525252");
    newArtistNameField.setPrefWidth(180);

    Button saveConversionButton = new Button("Save Conversion");
    saveConversionButton.setOnAction(
        _ -> {
          checkArtistToMap(artistLabel, newArtistNameField);
          newArtistNameField.setText("");
          newArtistNameField.setPromptText(
              tagEditor.replaceArtist(tagEditor.getArtist(TagEditor.FileType.OLD)));
        });

    // Album Conversion
    Button applyAlbumButton = new Button("Apply Album");
    applyAlbumButton.setOnAction(
        _ -> {
          controller.setLastSelectedType(TagEditor.SongType.ALBUM);
          controller.updateTags(TagEditor.SongType.ALBUM);
        });
    applyAlbumButton.setDisable(!tagEditor.doesAlbumExist(tagEditor.getAlbum(TagEditor.FileType.NEW)));

    HBox hBox = new HBox();
    hBox.setAlignment(Pos.CENTER_LEFT);
    hBox.getChildren()
        .addAll(
            coverButton,
            singleButton,
            albumButton,
            artistLabel,
            arrowLabel,
            newArtistNameField,
            saveConversionButton,
            applyAlbumButton);

    HBox.setMargin(applyAlbumButton, new Insets(0, 0, 0, 10));

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
        controller.setArtist(tagEditor.replaceArtist(textField.getPromptText()));
      }
    }
  }
}
