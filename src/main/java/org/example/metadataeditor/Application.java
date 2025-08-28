package org.example.metadataeditor;

import java.io.IOException;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.metadataeditor.controller.Controller;
import org.example.metadataeditor.model.TagEditor;
import org.example.metadataeditor.view.*;

public class Application extends javafx.application.Application {
  @Override
  public void start(Stage stage) throws IOException {
    //    FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("hello-view.fxml"));
    //    Scene scene = new Scene(fxmlLoader.load(), 320, 240);

    TagEditor tagEditor = new TagEditor(null);
    Controller controller = new Controller(tagEditor);
    View view = new View(tagEditor, controller, stage);

    tagEditor.addObserver(view);

    Scene scene = new Scene(view.render());

    stage.setTitle("Metadata Editor");
    stage.setScene(scene);
    stage.show();
  }
}
