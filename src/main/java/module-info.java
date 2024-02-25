module cz.nguyeha.todolist {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;

    opens cz.nguyeha.todolist.controller to javafx.fxml;

    opens cz.nguyeha.todolist to javafx.fxml;
    exports cz.nguyeha.todolist;
}