package org.example.metadataeditor.view;

import java.io.File;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.example.metadataeditor.controller.Controller;
import org.example.metadataeditor.model.ModelObserver;
import org.example.metadataeditor.model.TagEditor;
import org.example.metadataeditor.model.FileHandler.PathType;
import static org.example.metadataeditor.view.ViewGlobals.*;

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
    stage.setHeight(stage.getHeight());
    Scene scene = new Scene(this.render());
    //    scene.getStylesheets().add("main.css");
    stage.setTitle(
        "Metadata Editor - "
            + tagEditor.getPathString(PathType.SOURCE)
            + " → "
            + tagEditor.getPathString(PathType.TARGET));
    stage.setScene(scene);
  }

  @Override
  public Parent render() {
    ObservableList<Screen> screens = Screen.getScreensForRectangle(
        stage.getX(), stage.getY(), stage.getWidth(), stage.getHeight());
    if (!screens.isEmpty()) {
      for (Screen screen : screens) {
        ScreenWidth += screen.getVisualBounds().getWidth();
      }
    }

    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Open File");
    fileChooser.setInitialDirectory(new File(tagEditor.getPathString(PathType.SOURCE)));

    fileChooser
        .getExtensionFilters()
        .addAll(new FileChooser.ExtensionFilter("MP3 Files", "*.mp3"));

    // ----------------------------------

    StackPane stackPane = new StackPane();

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

    Node appMenuBar = new AppMenuBar(tagEditor, controller, stage).render();

    VBox vBox = new VBox();
    vBox.getChildren()
        .addAll(
            appMenuBar,
            new QuickFillButtons(tagEditor, controller).render(),
            songFields.render(),
            new AlbumImage(tagEditor, controller).render(),
            saveChangesButton
        );

    VBox.setMargin(appMenuBar, new Insets(0, 0, 10, 0));
    VBox.setVgrow(vBox, Priority.ALWAYS);

    stackPane.getChildren().add(vBox);

    ScrollPane scrollPane = new ScrollPane();
    scrollPane.setContent(stackPane);
    scrollPane.setFitToWidth(true);
    scrollPane.setFitToHeight(true);
    scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
    scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

    return scrollPane;
  }
}
