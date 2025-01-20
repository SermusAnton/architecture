package org.example.auth;

public class User {
    private String name;
    private String password;
    private boolean admin;
    private boolean privilegedUser;

    public String getName() {
        return name;
    }

    public User setName(String name) {
        this.name = name;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public User setPassword(String password) {
        this.password = password;
        return this;
    }

    public boolean isAdmin() {
        return admin;
    }

    public User setAdmin(boolean admin) {
        this.admin = admin;
        return this;
    }

    public boolean isPrivilegedUser() {
        return privilegedUser;
    }

    public User setPrivilegedUser(boolean privilegedUser) {
        this.privilegedUser = privilegedUser;
        return this;
    }
}
