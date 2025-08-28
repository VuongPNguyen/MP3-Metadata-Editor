package org.example.metadataeditor.view;

import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import org.example.metadataeditor.controller.Controller;
import org.example.metadataeditor.model.TagEditor;

public class QuickFillButtons implements FXComponent {
  Controller controller;

  public QuickFillButtons(Controller controller) {
    this.controller = controller;
  }

  @Override
  public Parent render() {
    Button coverButton = new Button("Cover");
    coverButton.setOnAction(_ -> controller.updateTags(TagEditor.SongType.COVER));

    Button singleButton = new Button("Single");
    singleButton.setOnAction(_ -> controller.updateTags(TagEditor.SongType.SINGLE));

    Button albumButton = new Button("Album");
    albumButton.setOnAction(_ -> controller.updateTags(TagEditor.SongType.ALBUM));

    HBox hBox = new HBox();
    hBox.getChildren().add(coverButton);
    hBox.getChildren().add(singleButton);
    hBox.getChildren().add(albumButton);

    return hBox;
  }
}
