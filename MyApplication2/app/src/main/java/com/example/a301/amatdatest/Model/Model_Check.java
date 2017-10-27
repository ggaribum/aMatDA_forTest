package com.example.a301.amatdatest.Model;

/**
 * Created by 301 on 2017-10-01.
 */

public class Model_Check {

    String studentNum;
    String attendanceDay;
    String attendanceTime;
    String attendanceState;
    String lecture;

    public Model_Check() {
    }

    public Model_Check(String studentNum, String attendanceDay, String attendanceTime, String attendanceState, String lecture) {
        this.studentNum = studentNum;
        this.attendanceDay = attendanceDay;
        this.attendanceTime = attendanceTime;
        this.attendanceState = attendanceState;
        this.lecture = lecture;
    }

    public String getStudentNum() {
        return studentNum;
    }

    public String getAttendanceDay() {
        return attendanceDay;
    }

    public String getAttendanceTime() {
        return attendanceTime;
    }

    public String getAttendanceState() {
        return attendanceState;
    }

    public String getLecture() {
        return lecture;
    }
}
