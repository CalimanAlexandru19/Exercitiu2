package com.javatasks.executable;

import com.javatasks.model.Task;
import com.javatasks.model.User;
import com.javatasks.util.AppendableObjectInputStream;
import com.javatasks.util.AppendableObjectOutputStream;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.javatasks.model.enums.MethodParameters.ADD_TASK;
import static com.javatasks.model.enums.MethodParameters.CREATE_USER;
import static com.javatasks.util.CommandOptionsService.*;
import static com.javatasks.util.Constants.*;

public class AppMain {
    public static void main(String[] args) throws IOException, ClassNotFoundException {

        checkArgumentsLength(args);

        String methodName;

        if (checkIsOption(args[0])) {
            methodName = args[0];
        } else {
            throw new IllegalArgumentException("Wrong format of arguments!");
        }

        distributeParameters(methodName, args);

    }

    public static void createUser(String firstName, String lastName, String userName) throws IOException,
            ClassNotFoundException {

        User user = new User(firstName, lastName, userName);
        File file = new File(FILE_PATH);
        boolean isFileCreated = file.exists();

        try (FileOutputStream f = new FileOutputStream(file, true);
             ObjectOutputStream o = new AppendableObjectOutputStream(f);
             FileInputStream fi = new FileInputStream(file);
             ObjectInputStream oi = new AppendableObjectInputStream(fi)) {

            boolean isDuplicateUserName = false;

            if (isFileCreated) {
                while (true) {
                    try {
                        if (((User) oi.readObject()).getUsername().equals(userName)) {
                            isDuplicateUserName = true;
                            System.out.println("Username already exists!");
                            break;
                        }
                    } catch (EOFException ex) {
                        break;
                    }
                }
            }

            if (!isDuplicateUserName) {
                o.writeObject(user);
                System.out.println("User added");
            }
        }
    }

    public static void showAllUsers() throws IOException, ClassNotFoundException {

        try (FileInputStream fi = new FileInputStream(FILE_PATH);
             ObjectInputStream oi = new AppendableObjectInputStream(fi)) {

            while (true){
                User user = (User)(oi.readObject());
                int numberOfTasks = 0;

                if (user.getTaskList() != null) {
                    numberOfTasks = user.getTaskList().size();
                }

                System.out.println("{ 'First Name': " + user.getFirstName() + " 'Last Name': " + user.getLastName() +
                        " 'Number of tasks': " + numberOfTasks + " }");
            }

        } catch (EOFException exc) {
        }
    }

    public static void addTask(String userName, String taskTitle, String taskDescription) throws IOException, ClassNotFoundException {
        File file = new File(FILE_PATH);
        Task task = new Task(taskTitle, taskDescription);
        List<User> userList = new ArrayList<>();

        try (FileOutputStream f = new FileOutputStream(file, true);
             ObjectOutputStream o = new AppendableObjectOutputStream(f);
             FileInputStream fi = new FileInputStream(file);
             ObjectInputStream oi = new AppendableObjectInputStream(fi)) {

            while (true) {
                try {
                    userList.add((User) oi.readObject());
                } catch (EOFException ex) {
                    break;
                }
            }

            for (User user : userList) {
                if ((user.getUsername().equals(userName))) {
                    if (user.getTaskList() == null) {
                        List<Task> tasks = new ArrayList<>();
                        tasks.add(task);
                        user.setTaskList(tasks);
                    } else {
                        user.getTaskList().add(task);
                    }
                    System.out.println("Task added for username " + user.getUsername());
                }
            }

            emptyFile();

            for (User user : userList) {
                o.writeObject(user);
            }
        }
    }

    private static void emptyFile() throws IOException {
        try ( OutputStream os = new FileOutputStream(FILE_PATH)){

        }
    }

    private static void distributeParameters(String methodName, String[] args) throws IOException,
            ClassNotFoundException {

        List<String> actualParameters;
        List<String> expectedParameters;
        Map<String, String> parametersValues;

        switch (methodName) {
            case CREATE_USER_METHOD: {
                actualParameters = getAllParameters(args);
                expectedParameters = CREATE_USER.getParameters();

                if (actualParameters.size() == 3 && checkMethodsParameters(actualParameters, expectedParameters)) {
                    parametersValues = actualParameters.stream()
                            .collect(Collectors.toMap(parameter -> parameter, parameter -> valueOf(args, parameter)));
                    createUser(parametersValues.get(expectedParameters.get(0)), parametersValues.get(
                            expectedParameters.get(1)), parametersValues.get(expectedParameters.get(2))
                    );
                }
                break;
            }
            case SHOW_ALL_USERS_METHOD: {
                showAllUsers();
                break;
            }
            case ADD_TASK_METHOD: {
                actualParameters = getAllParameters(args);
                expectedParameters = ADD_TASK.getParameters();
                if (actualParameters.size() == 3 && checkMethodsParameters(actualParameters, expectedParameters)) {
                    parametersValues = actualParameters.stream()
                            .collect(Collectors.toMap(parameter -> parameter, parameter -> valueOf(args, parameter)));
                    addTask(parametersValues.get(expectedParameters.get(0)), parametersValues.get(
                            expectedParameters.get(1)), parametersValues.get(expectedParameters.get(2))
                    );
                }
                break;
            }
            default: break;
        }
    }
}
