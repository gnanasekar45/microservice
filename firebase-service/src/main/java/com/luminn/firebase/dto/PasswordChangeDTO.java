package com.luminn.firebase.dto;

public class PasswordChangeDTO extends PasswordDTO {
    private String changePassword;
    private long userId;

    public String getChangePassword() {
        return changePassword;
    }

    public void setChangePassword(String changePassword) {
        this.changePassword = changePassword;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}
