module org.example.metadataeditor {
  requires javafx.controls;
  requires javafx.fxml;
  requires javafx.graphics;
  requires mp3agic;
  requires javafx.base;
  requires javafx.swing;
  requires java.desktop;


  opens org.example.metadataeditor to javafx.fxml;
  exports org.example.metadataeditor;
}