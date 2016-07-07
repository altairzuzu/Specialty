package ru.solomatin.specialty.Model;

import java.util.ArrayList;

/**
 * Работник
 */
public class Person {
    private String f_name, l_name, avatr_url;
    private String birthday;
    private int age = 0;
    private ArrayList<Specialty> specialty;

    public Person() {
    }

    public Person(String f_name, String l_name, String birthday, String avatr_url,
                  ArrayList<Specialty> specialty) {
        this.f_name = f_name;
        this.l_name = l_name;
        this.birthday = birthday;
        this.avatr_url = avatr_url;
        this.specialty = specialty;
    }

    public String getF_name() {
        return f_name;
    }

    public void setF_name(String f_name) {
        this.f_name = f_name;
    }

    public String getL_name() {
        return l_name;
    }

    public void setL_name(String l_name) {
        this.l_name = l_name;
    }

    public String getAvatr_url() {
        return avatr_url;
    }

    public void setAvatr_url(String avatr_url) {
        this.avatr_url = avatr_url;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public ArrayList<Specialty> getSpecialty() {
        return specialty;
    }

    public void setSpecialty(ArrayList<Specialty> specialty) {
        this.specialty = specialty;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}

