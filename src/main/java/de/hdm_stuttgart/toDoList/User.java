package de.hdm_stuttgart.toDoList;

import java.util.ArrayList;
import java.util.List;

public class User {
    private  String userName;
    private  String passwort;
    private ArrayList<ToDoList> toDoLists;
    public User(String userName,String passwort)
    {
        this.userName = userName;
        this.passwort = passwort;
        this.toDoLists = new ArrayList<ToDoList>();
    }

    public List<ToDoList> getToDoLists() {
        return toDoLists;
    }

    public void setToDoLists(ArrayList<ToDoList> toDoLists) {
        this.toDoLists = toDoLists;
    }

    public String getUserName() {
        return userName;
    }

    public void setPasswort(String passwort) {
        this.passwort = passwort;
    }
}
