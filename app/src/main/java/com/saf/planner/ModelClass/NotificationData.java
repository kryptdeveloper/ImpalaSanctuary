package com.saf.planner.ModelClass;

public class NotificationData {
    String Message;
    private boolean expanded;

    public NotificationData(String message) {
        Message = message;
    }

    public String getMessage() {
        return Message;
    }


    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }
    public boolean isExpanded() {
        return expanded;
    }
}
