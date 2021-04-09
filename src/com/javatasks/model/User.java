package com.javatasks.model;

import java.io.Serializable;
import java.util.List;

public class User implements Serializable {

    private String firstName;
    private String lastName;
    private String username;
    private List<Task> taskList;

    public User(String firstName, String lastName, String username) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
    }

    @Override
    public String toString() {
        return "User{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", username='" + username + '\'' +
                ", taskList=" + taskList +
                '}';
    }

    public String getUsername() {
        return username;
    }

    public List<Task> getTaskList() {
        return taskList;
    }

    public void setTaskList(List<Task> taskList) {
        this.taskList = taskList;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
}
