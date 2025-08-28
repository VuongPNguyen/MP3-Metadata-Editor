module org.example.metadataeditor {
  requires javafx.controls;
  requires javafx.fxml;
  requires javafx.graphics;
  requires mp3agic;
  requires javafx.base;
  requires javafx.swing;
  requires java.desktop;
  requires com.fasterxml.jackson.databind;
  opens org.example.metadataeditor.model to com.fasterxml.jackson.databind;
  
  opens org.example.metadataeditor to javafx.fxml;
  exports org.example.metadataeditor;
}