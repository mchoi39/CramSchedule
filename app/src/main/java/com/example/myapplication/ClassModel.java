package com.example.myapplication;

public class ClassModel {

    private String className;


    private ClassModel(){

    }

    private ClassModel(String className){
        this.className = className;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }
}
