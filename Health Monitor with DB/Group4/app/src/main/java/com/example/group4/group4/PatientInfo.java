package com.example.group4.group4;

public class PatientInfo {
    private String patientName;
    private String patientSex;
    private String patientID;
    private String patientAge;

    public static final String AUTO_ID = "ID";
    public static final String TIME_STAMP = "tSTAMP";
    public static final String X_AXIS = "xValue";
    public static final String Y_AXIS = "yValue";
    public static final String Z_AXIS = "zValue";


    public PatientInfo(String name, String sex, String id, String age){
        patientName = name;
        patientSex = sex;
        patientID = id;
        patientAge = age;
    }


}
