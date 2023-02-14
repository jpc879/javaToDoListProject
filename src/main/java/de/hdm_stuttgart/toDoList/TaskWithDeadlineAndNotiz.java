package de.hdm_stuttgart.toDoList;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.Date;

public class TaskWithDeadlineAndNotiz extends TaskWithDeadline {

    private String notiz;

    public TaskWithDeadlineAndNotiz(String taskName, String toDoListName, Date deadline, String notiz) {
        super(taskName, toDoListName, deadline);
        this.notiz = notiz;
    }

    @Override
    public HBox Display() {
        HBox root = new HBox(10);
        VBox vbox = new VBox(10);
        root.getChildren().add(vbox);

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

        root.setStyle("-fx-border-color: black");
        root.setPadding(new Insets(5,5,5,5));

        vbox.getChildren().add(hbox);
        HBox hbox2 = new HBox(10);
        Label deadlineLabel = new Label("Deadline :");
        deadlineLabel.setFont(Font.font("Ariel", FontWeight.NORMAL, 12));
        Label deadline = new Label(this.getDeadline().toString());
        deadline.setFont(Font.font("Ariel", FontWeight.NORMAL, 12));
        hbox2.getChildren().add(deadlineLabel);
        hbox2.getChildren().add(deadline);

        vbox.getChildren().add(hbox2);

        HBox hbox3 = new HBox(10);
        Label notizLabel = new Label("Notiz :");
        notizLabel.setFont(Font.font("Ariel", FontWeight.NORMAL, 12));
        Label notiz = new Label(this.notiz);
        notiz.setFont(Font.font("Ariel", FontWeight.NORMAL, 12));
        hbox3.getChildren().add(notizLabel);
        hbox3.getChildren().add(notiz);

        vbox.getChildren().add(hbox3);

        return root;
    }

    public String getNotiz() {
        return notiz;
    }
}
