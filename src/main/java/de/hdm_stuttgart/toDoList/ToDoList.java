package de.hdm_stuttgart.toDoList;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import de.hdm_stuttgart.toDoList.interfaces.displayable;
import javafx.util.Pair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class ToDoList implements displayable {
    private static Logger logger = Logger.getLogger(ToDoList.class.getName());

    private String listName;
    private ArrayList<displayable> taskList;

    public  ToDoList(String listName)
    {
        this.listName = listName;
        this.taskList = new ArrayList<displayable>();
    }


    @Override
    public HBox Display() {
        HBox root = new HBox(10);
        VBox vbox = new VBox(10);
        HBox hb_toDoList = new HBox(10);
        Label toDoListTitel = new Label(listName);
        toDoListTitel.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        vbox.setAlignment(Pos.TOP_LEFT);
        //
        hb_toDoList.getChildren().add(toDoListTitel);
        Button btn_removeToDoList = new Button("Remove ToDoList");
        hb_toDoList.getChildren().add(btn_removeToDoList);
        vbox.getChildren().add(hb_toDoList);
        //

        HBox hb_taskAdd = new HBox(10);
        Button btn_addTask = new Button("Add Task");
        btn_addTask.setAlignment(Pos.CENTER_RIGHT);
        hb_taskAdd.getChildren().add(btn_addTask);
        vbox.getChildren().add(hb_taskAdd);

        for (int i =0;i<taskList.size();i++)
        {
            vbox.getChildren().add(taskList.get(i).Display());
        }
        root.getChildren().add(vbox);
        btn_addTask.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                /*
                taskList.add(new SimpleTask(taskNameField.getText(),listName));
                Main.main.AddTask(taskNameField.getText(),listName);
                Main.main.SetToDoListDisplay();
                */
                // Create the custom dialog.
                Dialog<ArrayList<String>> dialog = new Dialog<>();
                dialog.setTitle("Task Dialog");
                dialog.setHeaderText("Create a Task");
                // Set the button types.
                ButtonType createButtonType = new ButtonType("Create", ButtonBar.ButtonData.OK_DONE);
                dialog.getDialogPane().getButtonTypes().addAll(createButtonType, ButtonType.CANCEL);

                // Create the username and password labels and fields.
                GridPane grid = new GridPane();
                grid.setHgap(10);
                grid.setVgap(10);
                grid.setPadding(new Insets(20, 150, 10, 10));

                TextField taskname = new TextField();
                taskname.setPromptText("Taskname");
                TextField deadline = new TextField();
                deadline.setPromptText("dd.mm.yyyy");
                TextField notiz = new TextField();
                notiz.setPromptText("Description or something");

                grid.add(new Label("Taskname:"), 0, 0);
                grid.add(taskname, 1, 0);
                grid.add(new Label("Deadline (optional):"), 0, 1);
                grid.add(deadline, 1, 1);
                grid.add(new Label("Notiz (optional):"), 0, 2);
                grid.add(notiz, 1, 2);

                // Enable/Disable login button depending on whether a username was entered.
                Node createButton = dialog.getDialogPane().lookupButton(createButtonType);
                createButton.setDisable(true);

                // Do some validation (using the Java 8 lambda syntax).
                taskname.textProperty().addListener((observable, oldValue, newValue) -> {
                    createButton.setDisable(newValue.trim().isEmpty());
                });

                dialog.getDialogPane().setContent(grid);

                Platform.runLater(() -> taskname.requestFocus());

                dialog.setResultConverter(dialogButton -> {
                    if (dialogButton == createButtonType) {
                        ArrayList<String> result = new ArrayList<>();
                        result.add(listName);
                        result.add(taskname.getText());
                        result.add(deadline.getText());
                        result.add(notiz.getText());
                        return result;
                    }
                    return null;
                });

                Optional<ArrayList<String>> result = dialog.showAndWait();

                result.ifPresent(task -> {
                    Main.main.AddTask(task);
                    Main.main.SetToDoListDisplay();
                });
            }
        });

        btn_removeToDoList.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                Main.main.RemoveToDoList(listName);
                Main.main.SetToDoListDisplay();
                logToExternalFile("Todo list removed", Level.FINE);
            }
        });
        root.setPadding(new Insets(10,30,10,10));
        return root;
    }

    public void setTaskList(ArrayList<displayable> taskList) {
        this.taskList = taskList;
    }
    public ArrayList<displayable> getTaskList() {
        return this.taskList;
    }
    public String getListName() {
        return listName;
    }

    public void setListName(String listName) {
        this.listName = listName;
    }

    public static void logToExternalFile(String text, Level level) {
        FileHandler fh;
        try {
            // This block configure the logger with handler and formatter
            fh = new FileHandler("logs/" + ToDoList.class.getName() + ".log", true);
            logger.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);
            // the following statement is used to log any messages
            logger.log(level, text);
            fh.close();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
