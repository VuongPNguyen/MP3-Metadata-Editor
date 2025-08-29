package org.example.metadataeditor.view;

import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import org.example.metadataeditor.model.FileHandler;
import org.example.metadataeditor.model.TagEditor;

import java.io.File;

public class DirectoryButtons implements FXComponent{
  private final DirectoryChooser directoryChooser;
  private final TagEditor tagEditor;
  private final Stage stage;

  public DirectoryButtons(DirectoryChooser directoryChooser, TagEditor tagEditor, Stage stage) {
    this.directoryChooser = directoryChooser;
    this.tagEditor = tagEditor;
    this.stage = stage;
  }

  @Override
  public Parent render() {
    VBox vBox = new VBox();
    vBox.setStyle("-fx-padding: 0 0 10 0;");
    vBox.setAlignment(Pos.TOP_LEFT);

    HBox sourceBox = new HBox();
    sourceBox.setStyle("-fx-alignment: center-left;");
    Button sourceButton = new Button("Select Source");
    sourceButton.setOnAction(_ -> {
      File chosenDirectory = directoryChooser.showDialog(stage);
      if (chosenDirectory != null) {
        tagEditor.setDirectory(FileHandler.PathType.SOURCE, chosenDirectory.getPath());
      }
    });
    Label sourceLabel = new Label(tagEditor.getPathString(FileHandler.PathType.SOURCE));
    sourceLabel.setStyle("-fx-padding: 0 5 0 5");

    sourceBox.getChildren().add(sourceButton);
    sourceBox.getChildren().add(sourceLabel);

    HBox targetBox = new HBox();
    targetBox.setStyle("-fx-alignment: center-left;");

    Button targetButton = new Button("Select Target");
    targetButton.setOnAction(_ -> {
      File chosenDirectory = directoryChooser.showDialog(stage);
      if (chosenDirectory != null) {
        tagEditor.setDirectory(FileHandler.PathType.TARGET, chosenDirectory.getPath());
      }
    });
    Label targetLabel = new Label(tagEditor.getPathString(FileHandler.PathType.TARGET));
    targetLabel.setStyle("-fx-padding: 0 0 0 5");

    sourceBox.getChildren().add(targetButton);
    sourceBox.getChildren().add(targetLabel);

    vBox.getChildren().add(sourceBox);
    vBox.getChildren().add(targetBox);
    return vBox;
  }
}
