package com.example.coursetable;

import android.view.View;

import java.io.Serializable;
import java.text.SimpleDateFormat;

public class CourseEdition implements Serializable {

    private String courseCode;
    private int verNum;     //Every course has many class version
    private String courseName;
    private String teacher;
    private String classRoom;
    private int day;
    private int classStart;
    private int classEnd;

    public CourseEdition(String courseCode,int verNum, String courseName, String teacher, String classRoom, int day, int classStart, int classEnd) {
        this.courseCode = courseCode;
        this.verNum = verNum;
        this.courseName = courseName;
        this.teacher = teacher;
        this.classRoom = classRoom;
        this.day = day;
        this.classStart = classStart;
        this.classEnd = classEnd;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public int getVerNum() {
        return verNum;
    }

    public void setVerNum(int verNum) {
        this.verNum = verNum;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getClassRoom() {
        return classRoom;
    }

    public void setClassRoom(String classRoom) {
        this.classRoom = classRoom;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getStart() {
        return classStart;
    }

    public void setStart(int classStart) {
        this.classEnd = classStart;
    }

    public int getEnd() {
        return classEnd;
    }

    public void setEnd(int classEnd) {
        this.classEnd = classEnd;
    }

}
