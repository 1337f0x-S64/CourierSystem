package com.example.courier.models;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Users {
    private IntegerProperty id;
    private StringProperty name;
    private StringProperty lastname;
    private StringProperty username;
    private StringProperty email;
    private IntegerProperty role;
    private StringProperty roleName;

    public Users(int id, String name, String lastname, String username, String email, int role, String roleName){
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.lastname = new SimpleStringProperty(lastname);
        this.username = new SimpleStringProperty(username);
        this.email = new SimpleStringProperty(email);
        this.role = new SimpleIntegerProperty(role);
        this.roleName = new SimpleStringProperty(roleName);
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public void setLastname(String lastname) {
        this.lastname.set(lastname);
    }

    public void setUsername(String username) {
        this.username.set(username);
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public void setRole(int role){
        this.role.set(role);
    }

    public void setRoleName(String roleName) { this.roleName.set(roleName);}



    public int getId() {
        return id.get();
    }

    public String getName() {
        return name.get();
    }

    public String getLastName() {
        return lastname.get();
    }

    public String getUserName() {
        return username.get();
    }

    public String getEmail() {
        return email.get();
    }

    public int getRole () {
        return role.get();
    }

    public String getRoleName() { return roleName.get(); }


    public IntegerProperty idProperty() {
        return id;
    }

    public StringProperty nameProperty(){
        return name;
    }

    public StringProperty lastnameProperty() {
        return lastname;
    }

    public StringProperty usernameProperty() {
        return username;
    }

    public StringProperty emailProperty() {
        return email;
    }

    public IntegerProperty roleProperty() {
        return role;
    }

    public StringProperty roleNameProperty() { return roleName; }
}

