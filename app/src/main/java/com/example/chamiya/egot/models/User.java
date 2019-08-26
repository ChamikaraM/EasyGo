package com.example.chamiya.egot.models;

/**
 * Created by Chamiya on 2/25/2018.
 */

public class User {
    private String user_id,username,email,password,userRole,status;

    public User(){

    }

    public User(String user_id,String username, String email, String password, String userRole, String status){
        this.user_id = user_id;
        this.username = username;
        this.email = email;
        this.password = password;
        //this.status = status;
        this.userRole = userRole;
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail(){
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getUserRole() {
        return userRole;
    }

}
