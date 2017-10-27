package com.example.a301.amatdatest.Controller;

/**
 * Created by 301 on 2017-09-29.
 */

public class Constants {
    public static final String req_URL ="http://13.124.87.34:5000/";

    public static String switchDAY(int day)
    {
        switch (day)
        {
            case 1:
                return "일";
            case 2:
                return "월";
            case 3:
                return "화";
            case 4:
                return "수";
            case 5:
                return "목";
            case 6:
                return "금";
            case 7:
                return "토";
        }
        return null;
    }
}
