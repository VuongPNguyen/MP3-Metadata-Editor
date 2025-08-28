package org.example.metadataeditor.view;

import java.io.File;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.example.metadataeditor.controller.Controller;
import org.example.metadataeditor.model.ModelObserver;
import org.example.metadataeditor.model.TagEditor;

public class View implements FXComponent, ModelObserver {
  private final TagEditor tagEditor;
  private final Controller controller;
  private final Stage stage;

  public View(TagEditor tagEditor, Controller controller, Stage stage) {
    this.tagEditor = tagEditor;
    this.controller = controller;
    this.stage = stage;
  }

  @Override
  public void update(TagEditor tagEditor) {
    Scene scene = new Scene(this.render());
    //    scene.getStylesheets().add("main.css");
    stage.setScene(scene);
  }

  @Override
  public Parent render() {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Open Files");
    fileChooser.setInitialDirectory(new File("C:\\Users\\nguye\\Downloads"));

    fileChooser
        .getExtensionFilters()
        .addAll(new FileChooser.ExtensionFilter("Audio Files", "*.mp3"));

    // ----------------------------------

    StackPane stackPane = new StackPane();
    stackPane.setPrefSize(400, 200);

    Button newFileButton = new Button("New Song");
    newFileButton.setOnAction(
        _ -> {
          File tempFile = fileChooser.showOpenDialog(stage);

          if (tempFile != null) {
            String filePath = tempFile.getPath();
            try {
              controller.newFile(filePath);
            } catch (Exception exception) {
              throw new RuntimeException(exception);
            }
          } else {
            controller.newFile(null);
          }
        });

    SongFields songFields = new SongFields(tagEditor, controller);

    Button saveChangesButton = new Button("Save Changes");
    saveChangesButton.setOnAction(
        _ -> {
          songFields.render();
          controller.saveTags();
        });

    VBox vBox = new VBox();
    vBox.getChildren().add(newFileButton);
    vBox.getChildren().add(new QuickFillButtons(controller).render());
    vBox.getChildren().add(songFields.render());
    vBox.getChildren().add(saveChangesButton);
    vBox.getChildren().add(new AlbumImage(tagEditor, controller).render());

    stackPane.getChildren().add(vBox);

    return stackPane;
  }
}
