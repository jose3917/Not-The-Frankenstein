package com.example.paco.notthefrankenstein;


public class User {
    String user_name;
    String email;
    String uid;

    public User (){
        //default constructor
    }

    public User(String user_name, String email, String uid){
        setUser_name(user_name);
        setEmail(email);
        setUid(uid);
    }

    public String getEmail() {
        return email;
    }

    public String getUid() {
        return uid;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }
}
