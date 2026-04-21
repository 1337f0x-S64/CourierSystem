package com.example.courier.models;

public class UserGlobal {

    private static UserGlobal instance;

    private int id;
    private String name;
    private String lastName;
    private String username;
    private String email;
    private String password;
    private String rol;

    private UserGlobal(){}

    public static UserGlobal getInstance() {
        if (instance== null) {
            instance = new UserGlobal();
        }
        return  instance;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLastName() {
        return lastName;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getRol() {
        return rol;
    }


    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

}
