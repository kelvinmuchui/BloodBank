package com.example.kelvin.blooddonation.Model;

public class UserAccountSetting {

    private  String username;
    private long phone_number;
    private String email;
    private String blood_group;
    private String gender;
    private String weight;

    public UserAccountSetting(String username, long phone_number, String email, String blood_group, String gender, String weight) {
        this.username = username;
        this.phone_number = phone_number;
        this.email = email;
        this.blood_group = blood_group;
        this.gender = gender;
        this.weight = weight;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public UserAccountSetting() {

    }



    public long getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(long phone_number) {
        this.phone_number = phone_number;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBlood_group() {
        return blood_group;
    }

    public void setBlood_group(String blood_group) {
        this.blood_group = blood_group;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }
}
