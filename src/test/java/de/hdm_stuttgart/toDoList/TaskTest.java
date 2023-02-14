package de.hdm_stuttgart.toDoList;

import org.junit.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TaskTest {

    private Main main = new Main();
    private int lines;

    private ToDoList toDoList;
    private SimpleTask task;
    private ArrayList<String> tasklist;

    @Before
    public void setUp() {
        main.user = new User ("", "");
        toDoList = new ToDoList("Liste");
        task = new SimpleTask("Aufgabe", "Liste");

        main.AddToDoList("Liste");

        tasklist = new ArrayList<>();
        tasklist.add(toDoList.getListName()); // Todoliste
        tasklist.add(task.getTaskName()); // Task
        tasklist.add(""); // Deadline
        tasklist.add(""); // Notiz
        main.AddTask(tasklist);
    }

    // Helper
    private int getNumberOfLines() throws Exception {
        Scanner file = new Scanner(new File("src/userdata/" + main.user.getUserName() + ".txt"));
        int n = 0;
        while (file.hasNextLine()) {
            n++;
            file.nextLine();
        }
        return n;
    }


    // Test 1
    @Test
    public void testSimpleTask() {
        SimpleTask task = new SimpleTask("Aufgabe", "Liste");
        assertEquals("Aufgabe", task.getTaskName());
    }

    // Test 2
    @Test
    public void testToDoList() {
        toDoList = new ToDoList("Liste");
        assertEquals("Liste", toDoList.getListName());
    }

    // Test 3: Neue Todoliste wird erstellt
    @Test
    public void testAddToDoList() throws Exception {
        lines = getNumberOfLines();

        main.AddToDoList("Liste");
        assertEquals(lines+1, getNumberOfLines());
    }

    // Test 4: Neue Task wird hinzugefügt
    @Test
    public void testAddTask() throws Exception {
        lines = getNumberOfLines();

        main.AddTask(tasklist);
        assertEquals(lines+1, getNumberOfLines());
    }

    // Test 5: Task wird gelöscht
    @Test
    public void testDeleteTask() throws Exception {
        lines = getNumberOfLines();

        main.DeleteTask(task);
        assertEquals(lines-1, getNumberOfLines());
    }


    // Test 6: To-Do-Liste wird entfernt
    @Test
    public void testRemoveToDoList() throws Exception {
        lines = getNumberOfLines();

        main.RemoveToDoList("Liste");
        assertTrue(getNumberOfLines() < lines);

    }

    @After
    public void tearDown() {
        main.RemoveToDoList("Liste");
        main.RemoveToDoList("Liste");
        main.user = null;
        lines = 0;
        toDoList = null;
        task = null;
        tasklist = null;
    }
}
