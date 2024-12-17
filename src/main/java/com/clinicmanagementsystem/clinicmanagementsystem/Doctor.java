package com.clinicmanagementsystem.clinicmanagementsystem;

public class Doctor {
    private int id;
    private String name;
    private String speciality;
    private int age;
    String gender;
    String phone;
    String email;
    public Doctor(int id, String name, int age , String speciality , String gender , String phone , String email) {
        this.id = id;
        this.name = name;
        this.speciality = speciality;
        this.age = age;
        this.gender = gender;
        this.phone = phone;
        this.email = email;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getSpeciality() {
        return speciality;
    }
    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }
    public int getAge() {
        return age;
    }
    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
}
