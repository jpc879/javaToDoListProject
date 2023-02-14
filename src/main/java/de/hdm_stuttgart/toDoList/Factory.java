package de.hdm_stuttgart.toDoList;

import de.hdm_stuttgart.toDoList.interfaces.displayable;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Factory {
    /**
     * Creates a Task object that matches the given parameter
     * @param toDoListName
     * @param parameter
     * @return
     */

    private static Logger logger = Logger.getLogger(Factory.class.getName());
    public static displayable CreateTask(String toDoListName,String parameter)
    {
        String[] paraList = parameter.split(";");
        switch (paraList.length) {
            case 1:
                SimpleTask Task = new SimpleTask(paraList[0].substring(1), toDoListName);
                logToExternalFile("CreateTask: " + "Simple task created", Level.INFO);
                return Task;
            case 2:
                if(paraList[1].contains(" Deadline: "))
                {
                    SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
                    TaskWithDeadline task;
                    try {
                        task= new TaskWithDeadline(paraList[0].substring(1), toDoListName,formatter.parse(paraList[1].substring(10)));
                    } catch (ParseException e) {
                        e.printStackTrace();
                        logToExternalFile("CreateTask: " + "Couldn't create TaskWithDeadline", Level.SEVERE);
                        return null;
                    }
                    logToExternalFile("CreateTask: " + "TaskWithDeadline created", Level.INFO);
                    return task;
                }
                else
                {
                    TaskWithNotiz task;
                    task = new TaskWithNotiz(paraList[0].substring(1), toDoListName,paraList[1].substring(7));
                    logToExternalFile("CreateTask: " + "TaskWithNotiz created", Level.INFO);
                    return task;
                }

            case 3:
                SimpleDateFormat formatter2 = new SimpleDateFormat("dd.MM.yyyy");
                TaskWithDeadlineAndNotiz taskDAN;
                try {
                    taskDAN= new TaskWithDeadlineAndNotiz(paraList[0].substring(1), toDoListName,formatter2.parse(paraList[1].substring(10)),paraList[2].substring(7));
                } catch (ParseException e) {
                    e.printStackTrace();
                    logToExternalFile("CreateTask: " + "Couldn't create TaskWithDeadlineAndNotiz", Level.SEVERE);
                    return null;
                }
                logToExternalFile("CreateTask: " + "TaskWithDeadlineAndNotiz created", Level.INFO);
                return taskDAN;
            default:
                SimpleTask dTask = new SimpleTask(paraList[0], toDoListName);
                //Error
                return dTask;
        }
    }

    /**
     * Greates a String out of a Task object
     * @param task
     * @return
     */
    public static ArrayList<String> CreateTaskString(displayable task)
    {
        ArrayList<String> taskAsString = new ArrayList<String>();
        switch (task.getClass().getName()) {
            case "SimpleTask":
                SimpleTask t = (SimpleTask)task;
                taskAsString.add(t.getToDoListName());
                taskAsString.add(t.getTaskName());
                logToExternalFile("CreateTaskString: " + "Successful", Level.INFO);
                return taskAsString;
            case "TaskWithDeadline":
                TaskWithDeadline t2 = (TaskWithDeadline) task;
                taskAsString.add(t2.getToDoListName());
                taskAsString.add(t2.getTaskName());
                taskAsString.add(t2.getDeadline().toString());
                logToExternalFile("CreateTaskString: " + "Successful", Level.INFO);
                return taskAsString;
            case "TaskWithDeadlineAndNotiz":
                TaskWithDeadlineAndNotiz t3 = (TaskWithDeadlineAndNotiz) task;
                taskAsString.add(t3.getToDoListName());
                taskAsString.add(t3.getTaskName());
                taskAsString.add(t3.getDeadline().toString());
                taskAsString.add(t3.getNotiz());
                logToExternalFile("CreateTaskString: " + "Successful", Level.INFO);
                return taskAsString;
            default:
                return null;
        }
    }

    public static void logToExternalFile(String text, Level level) {
        FileHandler fh;
        try {
            // This block configure the logger with handler and formatter
            fh = new FileHandler("logs/" + Factory.class.getName() + ".log", true);
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

