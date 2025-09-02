package org.example.metadataeditor.view;

import javafx.beans.value.ChangeListener;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import org.example.metadataeditor.controller.Controller;
import org.example.metadataeditor.model.TagEditor;

public class MiscFields implements FXComponent{
  TagEditor tagEditor;
  Controller controller;

  public MiscFields(TagEditor tagEditor, Controller controller) {
    this.tagEditor = tagEditor;
    this.controller = controller;
  }

  @Override
  public Parent render() {
    HBox hBox = new HBox();
    hBox.setStyle("-fx-alignment: center-left; -fx-padding: 3;");

    Label trackNumberLabel = new Label("Track: ");

    TextField trackNumberField = new TextField(tagEditor.getTrackNumber());
    trackNumberField.setPrefWidth(36);
    ChangeListener<Boolean> focusListener =
        (_, _, newVal) -> {
          if (trackNumberField.getText() != null) {
            if (!newVal) {
              controller.setTrackNumber(trackNumberField.getText());
            }
          }
        };
    trackNumberField.focusedProperty().addListener(focusListener);

    Label yearLabel = new Label("Year: ");
    yearLabel.setStyle("-fx-padding: 0 0 0 10;");

    TextField yearField = new TextField(tagEditor.getYear());
    yearField.setPrefWidth(50);
    ChangeListener<Boolean> focusListener1 =
        (_, _, newVal) -> {
          if (yearField.getText() != null) {
            if (!newVal) {
              controller.setYear(yearField.getText());
            }
          }
        };
    yearField.focusedProperty().addListener(focusListener1);

    Label genreLabel = new Label("Genre: ");
    genreLabel.setStyle("-fx-padding: 0 0 0 10;");

    TextField genreField = new TextField(tagEditor.getGenre());
    genreField.setPrefWidth(120);
    ChangeListener<Boolean> focusListener2 =
        (_, _, newVal) -> {
          if (genreField.getText() != null) {
            if (!newVal) {
              controller.setGenre(genreField.getText());
            }
          }
        };
    genreField.focusedProperty().addListener(focusListener2);

    hBox.getChildren().add(trackNumberLabel);
    hBox.getChildren().add(trackNumberField);
    hBox.getChildren().add(yearLabel);
    hBox.getChildren().add(yearField);
    hBox.getChildren().add(genreLabel);
    hBox.getChildren().add(genreField);

    return hBox;
  }
}
