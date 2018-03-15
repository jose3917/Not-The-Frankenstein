package com.example.paco.notthefrankenstein;


public class User {
    String user_name;
    String email;
    String Uid;

    public User (){
        //default constructor
    }

    public User(String user_name, String email, String Uid){
        this.user_name = user_name;
        this.email = email;
        this.Uid = Uid;
    }
}
