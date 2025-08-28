package org.example.metadataeditor.view;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.VBox;
import javax.imageio.ImageIO;
import org.example.metadataeditor.controller.Controller;
import org.example.metadataeditor.model.TagEditor;

public class AlbumImage implements FXComponent {
  private final TagEditor tagEditor;
  private final Controller controller;

  public AlbumImage(TagEditor tagEditor, Controller controller) {
    this.tagEditor = tagEditor;
    this.controller = controller;
  }

  @Override
  public Parent render() {
    VBox vBox = new VBox();

    if (tagEditor.mp3File != null) {

      byte[] imageData = tagEditor.getImage();
      BufferedImage bufferedImage = null;
      try (ByteArrayInputStream bis = new ByteArrayInputStream(imageData)) {
        bufferedImage = ImageIO.read(bis);
      } catch (IOException e) {
        throw new RuntimeException("Error reading image");
      }

      Image image = SwingFXUtils.toFXImage(bufferedImage, null);
      ImageView imageView = new ImageView(image);
      imageView.setFitHeight(100);
      imageView.setFitWidth(100);

      imageView.setOnDragOver(
          event -> {
            if (event.getGestureSource() != imageView && event.getDragboard().hasFiles()) {
              event.acceptTransferModes(TransferMode.COPY);
            }
            event.consume();
          });

      imageView.setOnDragDropped(event -> {
        Dragboard db = event.getDragboard();
        boolean success = false;
        if (db.hasFiles()) {
          for (File file : db.getFiles()) {
            if (file.getName().toLowerCase().endsWith(".png") ||
                file.getName().toLowerCase().endsWith(".jpg") ||
                file.getName().toLowerCase().endsWith(".jpeg")) {
              try {
                Image newImage = new Image(file.toURI().toString());
                imageView.setImage(newImage);

                BufferedImage newBufferedImage = ImageIO.read(file);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(newBufferedImage, "png", baos);
                byte[] imageBytes = baos.toByteArray();
                tagEditor.setImage(imageBytes);

                success = true;
                break;
              } catch (Exception e) {
                throw new RuntimeException("Error copying dragged image");
              }
            }
          }
        }
        event.setDropCompleted(success);
        event.consume();
      });

      vBox.getChildren().add(imageView);
    }
    return vBox;
  }
}
