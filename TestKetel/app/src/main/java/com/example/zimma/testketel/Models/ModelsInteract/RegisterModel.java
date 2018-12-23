package com.example.zimma.testketel.Models.ModelsInteract;

public class RegisterModel {

    public RegisterModel(String login, String password, String passwordConfirmed, boolean sex, int birthday) {
        Login = login;
        Password = password;
        PasswordConfirmed = passwordConfirmed;
        Sex = sex;
        Birthday = birthday;
    }

    public boolean isSex() {
        return Sex;
    }

    public void setSex(boolean sex) {
        Sex = sex;
    }

    public int getBirthday() {
        return Birthday;
    }

    public void setBirthday(int birthday) {
        Birthday = birthday;
    }

    public String getLogin() {
        return Login;
    }

    public void setLogin(String login) {
        Login = login;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getPasswordConfirmed() {
        return PasswordConfirmed;
    }

    public void setPasswordConfirmed(String passwordConfirmed) {
        PasswordConfirmed = passwordConfirmed;
    }

    private String Login;
    private String Password;
    private String PasswordConfirmed;
    private boolean Sex;
    private int Birthday;
}
