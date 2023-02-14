package de.hdm_stuttgart.toDoList;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Border;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import de.hdm_stuttgart.toDoList.interfaces.displayable;

import java.lang.String;

public class SimpleTask implements displayable {
    private String taskName;
    private boolean cheacked;
    private String toDoListName;
    public SimpleTask(String taskName,String toDoListName)
    {
        this.taskName = taskName;
        this.cheacked = false;
        this.toDoListName = toDoListName;
    }

    public  void setTaskname(String taskName){
        this.taskName = taskName;
    }
    public String getTaskName(){
        return this.taskName;
    }
    public String getToDoListName(){
        return this.toDoListName;
    }

    @Override
    public HBox Display() {
        HBox hbox = new HBox(10);
        Label taskText = new Label(getTaskName());
        taskText.setFont(Font.font("Ariel", FontWeight.NORMAL, 12));
        hbox.setAlignment(Pos.CENTER_LEFT);
        hbox.getChildren().add(taskText);
        Button hb_taskDone = new Button("Task done");
        hbox.getChildren().add(hb_taskDone);
        Button btn_taskDelete = new Button("Task delete");
        hbox.getChildren().add(btn_taskDelete);

        btn_taskDelete.setOnAction(e -> {
            Main.main.DeleteTask(this);
            Main.main.SetToDoListDisplay();
        });

        hbox.setStyle("-fx-border-color: black");
        hbox.setPadding(new Insets(5,5,5,5));
        return hbox;
    }
}