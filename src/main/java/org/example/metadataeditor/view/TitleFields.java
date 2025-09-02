package org.example.metadataeditor.view;

import javafx.beans.value.ChangeListener;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import org.example.metadataeditor.controller.Controller;
import org.example.metadataeditor.model.TagEditor;

public class TitleFields implements FXComponent {
  TagEditor tagEditor;
  Controller controller;

  public TitleFields(TagEditor tagEditor, Controller controller) {
    this.tagEditor = tagEditor;
    this.controller = controller;
  }

  @Override
  public Parent render() {
    VBox vBox = new VBox();
    vBox.setStyle("-fx-alignment: center-left; -fx-padding: 3;");

    Label titleLabel = new Label("Title: ");

    TextField titleField = new TextField(tagEditor.getTitle());
//    titleField.setPrefWidth(500);
    ChangeListener<Boolean> focusListener =
        (_, _, newVal) -> {
          if (titleField.getText() != null) {
            if (!newVal) {
              controller.setTitle(titleField.getText());
            }
          }
        };
    titleField.focusedProperty().addListener(focusListener);

    vBox.getChildren().add(titleLabel);
    vBox.getChildren().add(titleField);

    return vBox;
  }
}
