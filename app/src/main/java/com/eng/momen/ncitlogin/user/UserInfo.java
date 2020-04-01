package com.eng.momen.ncitlogin.user;

public class UserInfo {
    public static String userName;
    public static int id = -1;
    public static String feedbackMessage;


    public static String getUserName() {
        return userName;
    }

    public static void setUserName(String userName) {
        UserInfo.userName = userName;
    }

    public static int getId() {
        return id;
    }

    public static void setId(int id) {
        UserInfo.id = id;
    }

    public static String getFeedbackMessage() {
        return feedbackMessage;
    }

    public static void setFeedbackMessage(String feedbackMessage) {
        UserInfo.feedbackMessage = feedbackMessage;
    }
}
