package com.example.a301.amatdatest.Model;

/**
 * Created by 301 on 2017-09-29.
 */

public class Model_Lecture {
    String lecture;
    String lectureRoom;
    String lectureStartTime;
    String lectureFinishTime;
    String lectureDay;
    String professor;

    public Model_Lecture() {

    }

    public Model_Lecture(String lecture, String lectureRoom, String lectureStartTime, String lectureFinishTime, String lectureDay,String professor) {
        this.lecture = lecture;
        this.lectureRoom = lectureRoom;
        this.lectureStartTime = lectureStartTime;
        this.lectureFinishTime = lectureFinishTime;
        this.lectureDay = lectureDay;
        this.professor=professor;
    }

    public String getLecture() {
        return lecture;
    }

    public String getLectureRoom() {
        return lectureRoom;
    }

    public String getLectureStartTime() {
        return lectureStartTime;
    }

    public String getLectureFinishTime() {
        return lectureFinishTime;
    }

    public String getLectureDay() {
        return lectureDay;
    }

    public String getProfessor() {
        return professor;
    }
}
