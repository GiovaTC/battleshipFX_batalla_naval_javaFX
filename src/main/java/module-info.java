module com.example.battleshipfx {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    opens com.example.battleshipfx to javafx.fxml;
    exports com.example.battleshipfx;
}