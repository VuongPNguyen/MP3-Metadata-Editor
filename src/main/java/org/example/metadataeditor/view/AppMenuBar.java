package org.example.metadataeditor.view;

import javafx.scene.Parent;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.example.metadataeditor.controller.Controller;
import org.example.metadataeditor.model.TagEditor;
import org.example.metadataeditor.model.FileHandler.PathType;

import java.io.File;

public class AppMenuBar implements FXComponent{
  private final TagEditor tagEditor;
  private final Controller controller;
  private final Stage stage;

  private final FileChooser fileChooser;
  private final DirectoryChooser directoryChooserSource;
  private final DirectoryChooser directoryChooserTarget;

  public AppMenuBar(TagEditor tagEditor, Controller controller, Stage stage) {
    this.tagEditor = tagEditor;
    this.controller = controller;
    this.stage = stage;

    fileChooser = new FileChooser();
    fileChooser.setTitle("Open File");
    fileChooser.setInitialDirectory(new File(tagEditor.getPathString(PathType.SOURCE)));

    fileChooser
        .getExtensionFilters()
        .addAll(new FileChooser.ExtensionFilter("MP3 Files", "*.mp3"));

    directoryChooserSource = new DirectoryChooser();
    directoryChooserSource.setTitle("Select Source Directory");
    directoryChooserSource.setInitialDirectory(new File(tagEditor.getPathString(PathType.SOURCE)));

    directoryChooserTarget = new DirectoryChooser();
    directoryChooserTarget.setTitle("Select Target Directory");
    directoryChooserTarget.setInitialDirectory(new File(tagEditor.getPathString(PathType.TARGET)));
  }

  @Override
  public Parent render() {
    MenuBar menuBar = new MenuBar();

    Menu fileMenu = new Menu("File");

    MenuItem newFileItem = new MenuItem("New File");
    newFileItem.setOnAction(
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

    fileMenu.getItems().add(newFileItem);

    Menu directoryMenu = new Menu("Directory");

    MenuItem sourceItem = new MenuItem("Select Source Directory");
    sourceItem.setOnAction(_ -> {
      File chosenDirectory = directoryChooserSource.showDialog(stage);
      if (chosenDirectory != null) {
        tagEditor.setDirectory(PathType.SOURCE, chosenDirectory.getPath());
      }
    });

    MenuItem targetItem = new MenuItem("Select Target Directory");
    targetItem.setOnAction(_ -> {
      File chosenDirectory = directoryChooserTarget.showDialog(stage);
      if (chosenDirectory != null) {
        tagEditor.setDirectory(PathType.TARGET, chosenDirectory.getPath());
      }
    });

    directoryMenu.getItems().addAll(sourceItem, targetItem);

    menuBar.getMenus().addAll(fileMenu, directoryMenu);

    return menuBar;
  }
}
