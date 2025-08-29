package org.example.metadataeditor;

import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.metadataeditor.controller.Controller;
import org.example.metadataeditor.model.FileHandler;
import org.example.metadataeditor.model.TagEditor;
import org.example.metadataeditor.view.*;

public class Application extends javafx.application.Application {
  @Override
  public void start(Stage stage) {
    //    FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("hello-view.fxml"));
    //    Scene scene = new Scene(fxmlLoader.load(), 320, 240);
    
    FileHandler fileHandler = new FileHandler();

    TagEditor tagEditor = new TagEditor(null, fileHandler);
    Controller controller = new Controller(tagEditor);
    View view = new View(tagEditor, controller, stage);

    tagEditor.addObserver(view);

    Scene scene = new Scene(view.render());

    stage.setTitle("Metadata Editor");
    stage.setScene(scene);
    stage.show();
  }
}
