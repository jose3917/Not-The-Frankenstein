package com.example.paco.notthefrankenstein;


public class User {
    String user_name;
    String email;

    public User (){
        //default constructor
    }

    public User(String user_name, String email){
        this.user_name = user_name;
        this.email = email;
    }
}
