package com.example.zimma.testketel.Models.ModelsInteract;

public class UserSave {

    private int ID;
    private String Nickname;
    private int Age;
    private boolean Sex;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getNickname() {
        return Nickname;
    }

    public void setNickname(String nickname) {
        Nickname = nickname;
    }

    public int getAge() {
        return Age;
    }

    public void setAge(int age) {
        Age = age;
    }

    public boolean isSex() {
        return Sex;
    }

    public void setSex(boolean sex) {
        Sex = sex;
    }

    public UserSave(int ID, String nickname, int age, boolean sex) {
        this.ID = ID;
        Nickname = nickname;
        Age = age;
        Sex = sex;
    }
}
