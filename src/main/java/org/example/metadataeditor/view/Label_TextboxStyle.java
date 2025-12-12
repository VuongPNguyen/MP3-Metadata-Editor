package org.example.metadataeditor.view;

import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class Label_TextboxStyle implements FXComponent {
  private final String title;
  private final String value;
  private double width = 0;
  private boolean isVertical = true;

  /** Title will have ":" added afterwards. */
  public Label_TextboxStyle(String labelTitle, String labelText) {
    title = labelTitle;
    value = labelText;
  }

  public Label_TextboxStyle(String labelTitle, String labelText, double width, boolean isVertical) {
    title = labelTitle;
    value = labelText;
    this.width = width;
    this.isVertical = isVertical;
  }

  @Override
  public Parent render() {
    Parent box;
    if (isVertical) {
      box = new VBox();
    } else {
      box = new HBox();
    }

    box.setStyle("-fx-alignment: center-left; -fx-padding: 3;");

    Label titleLabel = new Label(title + ": ");

    Label valueLabel = new Label(value);
    valueLabel.setStyle(
        "-fx-border-color: lightgray; "
            + "-fx-border-width: 1; "
            + "-fx-border-radius: 3; "
            + "-fx-background-color: gainsboro; "
            + "-fx-padding: 3 3 3 5; "
            + "-fx-font-family: 'System'; ");

    if (width > 0) {
      valueLabel.setPrefWidth(width);
    } else {
      // Bind width to half of the scene width
      box.sceneProperty()
          .addListener(
              (_, _, newScene) -> {
                if (newScene != null) {
                  valueLabel.prefWidthProperty().bind(newScene.widthProperty().divide(2));
                }
              });
    }

    if (isVertical) {
      assert box instanceof VBox;
      ((VBox) box).getChildren().add(titleLabel);
      ((VBox) box).getChildren().add(valueLabel);
    } else {
      assert box instanceof HBox;
      if (title == null) {
        ((HBox) box).getChildren().addAll(titleLabel);
      }
      ((HBox) box).getChildren().addAll(valueLabel);
    }

    return box;
  }
}
