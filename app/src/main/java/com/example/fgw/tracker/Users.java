package com.example.fgw.tracker;

public class Users {
    String email;
    Boolean EmergencyStatus;
    String UserId;
    Boolean WithinDistance = false;

    public Users(String email, Boolean emergencyStatus, String userId, Boolean withinDistance) {
        this.email = email;
        EmergencyStatus = emergencyStatus;
        UserId = userId;
        WithinDistance = withinDistance;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getEmergencyStatus() {
        return EmergencyStatus;
    }

    public void setEmergencyStatus(Boolean emergencyStatus) {
        EmergencyStatus = emergencyStatus;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public Boolean getWithinDistance() {
        return WithinDistance;
    }

    public void setWithinDistance(Boolean withinDistance) {
        WithinDistance = withinDistance;
    }
}
