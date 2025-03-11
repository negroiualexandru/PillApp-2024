package com.example.pillapplication;

import java.util.ArrayList;

public class User {
    String username;
    String password;
    String firstName;
    String lastName;
    int age;
    int weight;
    int height;

    ArrayList<Pill> pills = new ArrayList<>();

    public User (String username, String password, String firstName, String lastName,
                 int age, int weight, int height) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.weight = weight;
        this.height = height;
    }
}
