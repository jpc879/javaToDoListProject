package de.hdm_stuttgart.toDoList;

import de.hdm_stuttgart.toDoList.interfaces.displayable;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.omg.PortableInterceptor.SUCCESSFUL;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Main extends Application {

    private Logger logger = Logger.getLogger(Main.class.getName());

    public User user;
    BorderPane toDoListGrid;
    public static Main main;

    @Override
    public void start(Stage primaryStage) throws Exception{
        logToExternalFile("Initializing GUI", Level.INFO);
        //loginscreen
        main = this;
        primaryStage.setTitle("ToDoListVerwaltung");
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        Scene scene = new Scene(grid, 600, 300);

        primaryStage.setScene(scene);
        primaryStage.show();

        Text scenetitle = new Text("To-Do-List");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(scenetitle, 0, 0, 2, 1);

        Label userName = new Label("User Name:");
        grid.add(userName, 0, 1);

        TextField userTextField = new TextField();
        grid.add(userTextField, 1, 1);

        Label pw = new Label("Password:");
        grid.add(pw, 0, 2);

        PasswordField pwBox = new PasswordField();
        grid.add(pwBox, 1, 2);

        Button btn = new Button("Sign in");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(btn);
        grid.add(hbBtn, 1, 4);

        //register button
        Button btn_register = new Button("Register");
        HBox hbBtn_register = new HBox(10);
        hbBtn_register.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn_register.getChildren().add(btn_register);
        grid.add(hbBtn_register, 0, 4);

        final Text actiontarget = new Text();
        grid.add(actiontarget, 1, 6);

        //User select screen
        toDoListGrid = new BorderPane();
        Scene scene2 = new Scene(toDoListGrid, 1000, 600);
        Label scenetitle2 = new Label("To-Do-Lists von Username");
        scenetitle2.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        HBox topHbox = new HBox(10);
        topHbox.setAlignment(Pos.CENTER);
        topHbox.getChildren().add(scenetitle2);

        toDoListGrid.setTop(topHbox);
        HBox hb_bottomRowUsersite = new HBox(10);

        TextField toDoListNameField = new TextField();

        Button btn_addTodo = new Button("Add ToDoList");
        HBox hbBtn_addTodo = new HBox(10);
        hbBtn_addTodo.setAlignment(Pos.BOTTOM_CENTER);
        hbBtn_addTodo.getChildren().add(btn_addTodo);

        Button btn_logout = new Button("Logout");
        HBox hbBtn_logout = new HBox(10);
        hbBtn_logout.setAlignment(Pos.BOTTOM_CENTER);
        hbBtn_logout.getChildren().add(btn_logout);

        hb_bottomRowUsersite.getChildren().add(toDoListNameField);
        hb_bottomRowUsersite.getChildren().add(hbBtn_addTodo);
        hb_bottomRowUsersite.getChildren().add(hbBtn_logout);
        toDoListGrid.setBottom(hb_bottomRowUsersite);

        // check if task list changed
        scene2.getOnMouseReleased();

        // add toDolist
        btn_addTodo.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                AddToDoList(toDoListNameField.getText());
                SetToDoListDisplay();
                logToExternalFile("Todo list Added", Level.INFO);
            }
        });


        // set logoutbutton action
        btn_logout.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                primaryStage.setScene(scene);
                primaryStage.show();

            }
        });

        // set register action
        btn_register.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                if(CheckUser(userTextField.getText()))
                {
                    actiontarget.setFill(Color.FIREBRICK);
                    actiontarget.setText("User already used");
                }
                else
                {
                    AddUser(userTextField.getText(),pwBox.getText());
                    actiontarget.setFill(Color.GREEN);
                    actiontarget.setText("User registerd");
                }
            }
        });

        //set login button action
        btn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                //check if Login Data are correkt
                if(CheckUser(userTextField.getText(),pwBox.getText()))
                {
                    scenetitle2.setText("To-Do-Lists von " + userTextField.getText());
                    user = new User(userTextField.getText(),pwBox.getText());
                    user = GetUserData(user);
                    primaryStage.setScene(scene2);
                    primaryStage.show();
                    SetToDoListDisplay();
                }
                else{
                    actiontarget.setFill(Color.FIREBRICK);
                    actiontarget.setText("Wrong User/passwort combination");
                }
            }
        });
    }

    public void AddToDoList(String toDoListName)
    {
        user.getToDoLists().add(new ToDoList(toDoListName));
        File f = new File("src/userdata/"+ user.getUserName()+".txt");
        try {
            List<String> lines = Files.readAllLines(f.toPath());
            lines.add(toDoListName);
            Files.write(f.toPath(), lines);
            logToExternalFile("AddToDoList " + toDoListName + " Added!", Level.INFO);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            logToExternalFile("AddToDoList " + "There was an exception adding To-Do list\n" + e.toString(), Level.SEVERE);
        }
        //SetToDoListDisplay();
    }

    public void SetToDoListDisplay()
    {
        HBox hb_toDoListView = new HBox(10);
        user = GetUserData(user);
        for(int i = 0;i< user.getToDoLists().size();i++)
        {
            hb_toDoListView.getChildren().add(user.getToDoLists().get(i).Display());
        }
        toDoListGrid.setCenter(hb_toDoListView);
    }

    //gets All the ToDoLists the user already made
    private User GetUserData(User user)
    {
        File f = new File("src/userdata/"+ user.getUserName()+".txt");
        if(f.exists() && !f.isDirectory())
        {
            ArrayList<ToDoList> list = new ArrayList<ToDoList>();
            try {
                String tempToDoListName= "";
                Scanner scan = new Scanner(f);
                String tempTaskName ="";
                String tempDeadline ="";
                String tempNotiz ="";
                while (scan.hasNextLine()) {
                    String line = scan.nextLine();
                    if(line.charAt(0) != '-' && line.charAt(0) != ' ')
                    {
                        list.add(new ToDoList(line));
                        tempToDoListName = line;
                    }
                    else if(line.charAt(0) == '-')
                    {
                        //  list.get(list.size()-1).getTaskList().add(new SimpleTask(line,tempToDoListName));
                        if(tempTaskName.equals(""))
                        {
                            tempTaskName = line;
                        }
                        else
                        {
                            String taskPara = tempTaskName;
                            if(!tempDeadline.equals(""))
                            {
                                taskPara +=";"+tempDeadline;
                            }
                            if(!tempNotiz.equals(""))
                            {
                                taskPara += ";"+tempNotiz;
                            }
                            list.get(list.size()-1).getTaskList().add(Factory.CreateTask(tempToDoListName,taskPara));
                            tempTaskName = line;
                            tempDeadline ="";
                            tempNotiz ="";
                        }
                    }
                    else
                    {
                        if(line.contains(" Deadline: "))
                        {
                            tempDeadline = line;
                        }
                        if(line.contains(" Notiz: "))
                        {
                            tempNotiz = line;
                        }
                    }
                }
                if(!tempTaskName.equals(""))
                {
                    String taskPara = tempTaskName;
                    if(!tempDeadline.equals(""))
                    {
                        taskPara +=";"+tempDeadline;
                    }
                    if(!tempNotiz.equals(""))
                    {
                        taskPara += ";"+tempNotiz;
                    }
                    list.get(list.size()-1).getTaskList().add(Factory.CreateTask(tempToDoListName,taskPara));
                }

                user.setToDoLists(list);
                logToExternalFile("GetUserData: Successfully fetched User Data", Level.INFO);
            }
            catch (IOException e)
            {
                e.printStackTrace();
                logToExternalFile("GetUserData: Failed to get User data:\n" + e.toString(), Level.SEVERE);
            }
        }
        else
        {
            try
            {
                f.getParentFile().mkdir();
                f.createNewFile();
                logToExternalFile("GetUserData: File Created.", Level.INFO);
            }
            catch (IOException e)
            {
                e.printStackTrace();
                logToExternalFile("GetUserData: Failed to create file:\n" + e.toString(), Level.SEVERE);
            }
        }
        return user;
    }

    public void AddUser(String userName, String passwort)
    {
        File f = new File("src/userdata/user.txt");
        if(f.exists() && !f.isDirectory())
        {
            try {
                Files.write(Paths.get(f.getPath()),("U :"+ userName+"\r\nP :"+passwort+"\r\n").getBytes(StandardCharsets.UTF_8), StandardOpenOption.APPEND);
                logToExternalFile("AddUser: " + "SUCCESSFUL", Level.INFO);
            } catch (IOException e) {
                logToExternalFile("AddUser: " + e.toString(), Level.SEVERE);
                e.printStackTrace();
            }
        }
        else
        {
            try
            {
                f.getParentFile().mkdir();
                f.createNewFile();
                Files.write(Paths.get(f.getPath()),("U :"+ userName+"\r\nP :"+passwort+"\r\n").getBytes(StandardCharsets.UTF_8), StandardOpenOption.APPEND);
                logToExternalFile("AddUser: " + "SUCCESSFUL", Level.INFO);
            }
            catch (IOException e)
            {
                e.printStackTrace();
                logToExternalFile("AddUser: " + e.toString(), Level.SEVERE);
            }

        }
    }

    public boolean CheckUser(String userName) {
        File f = new File("src/userdata/user.txt");
        if(f.exists() && !f.isDirectory())
        {
            try {
                Scanner scan = new Scanner(f);
                while(scan.hasNextLine()) {
                    String line = scan.nextLine();
                    if(line.equals( "U :"+userName))
                    {
                        logToExternalFile("CheckUser: " + "SUCCESSFUL", Level.INFO);
                        return true;
                    }
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                logToExternalFile("CheckUser: " + e.toString(), Level.SEVERE);
            }
            logToExternalFile("CheckUser: " + "Invalid Login", Level.INFO);
            return  false;
        }
        else
        {
            return false;
        }
    }
    public boolean CheckUser(String userName,String passwort) {
        File f = new File("src/userdata/user.txt");
        if(f.exists() && !f.isDirectory())
        {
            try {
                Scanner scan = new Scanner(f);
                while(scan.hasNextLine()) {
                    String line = scan.nextLine();
                    if(line.equals( "U :"+userName))
                    {
                        line = scan.nextLine();
                        if(line.equals( "P :" +passwort))
                        {
                            return true;
                        }
                    }
                }
                logToExternalFile("CheckUser: " + "SUCCESSFUL", Level.INFO);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                logToExternalFile("CheckUser: " + e.toString(), Level.SEVERE);
            }
            return  false;
        }
        else
        {
            logToExternalFile("CheckUser: " + "User does not exists", Level.WARNING);
            return false;
        }
    }



    public static void main(String[] args) {
        launch(args);
    }

    public void AddTask(ArrayList<String> taskPara)
    {
        File f = new File("src/userdata/"+ user.getUserName()+".txt");
        try {
            List<String> lines = Files.readAllLines(f.toPath());
            for(int i = 0;i<lines.size();i++)
            {
                if(lines.get(i).equals(taskPara.get(0)))
                {
                    i++;
                    lines.add(i,"-"+ taskPara.get(1));
                    if(!taskPara.get(2).equals(""))
                    {
                        i++;
                        lines.add(i," Deadline: "+ taskPara.get(2));
                    }
                    if(!taskPara.get(3).equals(""))
                    {
                        i++;
                        lines.add(i," Notiz: "+ taskPara.get(3));
                    }
                    break;
                }
            }
            Files.write(f.toPath(), lines);
            logToExternalFile("AddTask: " + "Successful", Level.INFO);
        }
        catch (IOException e)
        {
            logToExternalFile("AddTask: " + e.toString(), Level.SEVERE);
            e.printStackTrace();
        }
    }

    public void DeleteTask(SimpleTask task)
    {
        File f = new File("src/userdata/"+ user.getUserName()+".txt");
        try {
            List<String> lines = Files.readAllLines(f.toPath());
            for(int i = 0;i<lines.size();i++)
            {
                if(lines.get(i).equals(task.getToDoListName()))
                {
                    for(int j = i;j< lines.size();j++)
                    {
                        if(lines.get(j).equals("-" + task.getTaskName()))
                        {
                            lines.remove(j);
                            break;
                        }
                    }
                    break;
                }
            }
            Files.write(f.toPath(), lines);
            logToExternalFile("DeleteTask: " + "Successful", Level.INFO);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            logToExternalFile("DeleteTask: " + e.toString(), Level.SEVERE);
        }
    }
    public void DeleteTask(TaskWithNotiz task)
    {
        File f = new File("src/userdata/"+ user.getUserName()+".txt");
        try {
            List<String> lines = Files.readAllLines(f.toPath());
            for(int i = 0;i<lines.size();i++)
            {
                if(lines.get(i).equals(task.getToDoListName()))
                {
                    for(int j = i;j< lines.size();j++)
                    {
                        if(lines.get(j).equals("-" + task.getTaskName()))
                        {
                            lines.remove(j);
                            lines.remove(j);
                            break;
                        }
                    }
                    break;
                }
            }
            Files.write(f.toPath(), lines);
            logToExternalFile("DeleteTask: " + "Successful", Level.INFO);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            logToExternalFile("DeleteTask: " + e.toString(), Level.SEVERE);
        }
    }
    public void DeleteTask(TaskWithDeadline task)
    {
        File f = new File("src/userdata/"+ user.getUserName()+".txt");
        try {
            List<String> lines = Files.readAllLines(f.toPath());
            for(int i = 0;i<lines.size();i++)
            {
                if(lines.get(i).equals(task.getToDoListName()))
                {
                    for(int j = i;j< lines.size();j++)
                    {
                        if(lines.get(j).equals("-" + task.getTaskName()))
                        {
                            lines.remove(j);
                            lines.remove(j);
                            break;
                        }
                    }
                    break;
                }
            }
            Files.write(f.toPath(), lines);
            logToExternalFile("DeleteTask: " + "Successful", Level.INFO);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            logToExternalFile("DeleteTask: " + e.toString(), Level.SEVERE);
        }
    }
    public void DeleteTask(TaskWithDeadlineAndNotiz task)
    {
        File f = new File("src/userdata/"+ user.getUserName()+".txt");
        try {
            List<String> lines = Files.readAllLines(f.toPath());
            for(int i = 0;i<lines.size();i++)
            {
                if(lines.get(i).equals(task.getToDoListName()))
                {
                    for(int j = i;j< lines.size();j++)
                    {
                        if(lines.get(j).equals("-" + task.getTaskName()))
                        {
                            lines.remove(j);
                            //Remove the deadline line
                            lines.remove(j);
                            //Remove the Notiz line
                            lines.remove(j);
                            break;
                        }
                    }
                    break;
                }
            }
            Files.write(f.toPath(), lines);
            logToExternalFile("DeleteTask: " + "Successful", Level.INFO);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            logToExternalFile("DeleteTask: " + e.toString(), Level.SEVERE);
        }
    }

    public void RemoveToDoList(String listName)
    {
        File f = new File("src/userdata/"+ user.getUserName()+".txt");
        try {
            List<String> lines = Files.readAllLines(f.toPath());
            for(int i = 0;i<lines.size();i++)
            {
                if(lines.get(i).equals(listName))
                {
                    lines.remove(i);
                    for(int j = i;j< lines.size();j++)
                    {
                        while(lines.get(j).charAt(0) == '-') {
                            lines.remove(j);
                            if(lines.size()== j)
                            {
                                break;
                            }
                        }
                    }
                    break;
                }
            }
            Files.write(f.toPath(), lines);
            logToExternalFile("RemoveToDoList: " + "Successfully Removed", Level.INFO);
        }
        catch (IOException e)
        {
            logToExternalFile("RemoveToDoList: " + "Failed to Remove", Level.SEVERE);
            e.printStackTrace();
        }
    }

    public void logToExternalFile(String text, Level level) {
        FileHandler fh;
        try {
            // This block configure the logger with handler and formatter
            fh = new FileHandler("logs/" + Main.class.getName() + ".log", true);
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
