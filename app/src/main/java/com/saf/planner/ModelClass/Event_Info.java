package com.saf.planner.ModelClass;

public class Event_Info {
    String eventID,hostID,eventImage,eventName,eventStartDate,eventEndDate,eventStartTime,eventEndTime,eventPlace,eventType,eventInstruction,noOfGuest,extras,TAG;


    public Event_Info(String eventID,String hostID,String url, String eventName, String eventStartDate,String eventEndDate, String eventStartTime, String eventEndTime, String eventPlace, String eventType, String eventInstruction, String noOfGuest, String extras,String TAG)
    {
        this.eventID = eventID;
        this.hostID = hostID;
        eventImage = url;
        this.eventName = eventName;
        this.eventStartDate = eventStartDate;
        this.eventEndDate = eventEndDate;
        this.eventStartTime = eventStartTime;
        this.eventEndTime = eventEndTime;
        this.eventPlace = eventPlace;
        this.eventType = eventType;
        this.eventInstruction = eventInstruction;
        this.noOfGuest = noOfGuest;
        this.extras = extras;
        this.TAG = TAG;
    }

    public Event_Info(String hostID,String url, String eventName, String eventStartDate,String eventEndDate, String eventStartTime, String eventEndTime, String eventPlace, String eventType, String eventInstruction, String noOfGuest, String extras)
    {
        this.hostID = hostID;
        eventImage = url;
        this.eventName = eventName;
        this.eventStartDate = eventStartDate;
        this.eventEndDate = eventEndDate;
        this.eventStartTime = eventStartTime;
        this.eventEndTime = eventEndTime;
        this.eventPlace = eventPlace;
        this.eventType = eventType;
        this.eventInstruction = eventInstruction;
        this.noOfGuest = noOfGuest;
        this.extras = extras;
    }

    public String getEventID() {
        return eventID;
    }

    public String getHostID() {
        return hostID;
    }

    public String getEventImage() {
        return eventImage;
    }

    public String getEventName() {
        return eventName;
    }

    public String getEventStartDate() {
        return eventStartDate;
    }

    public String getEventEndDate() {
        return eventEndDate;
    }

    public String getEventStartTime() {
        return eventStartTime;
    }

    public String getEventEndTime() {
        return eventEndTime;
    }

    public String getEventPlace() {
        return eventPlace;
    }

    public String getEventType() {
        return eventType;
    }

    public String getEventInstruction() {
        return eventInstruction;
    }

    public String getNoOfGuest() {
        return noOfGuest;
    }

    public String getExtras() {
        return extras;
    }

    public String getTAG() {
        return TAG;
    }
}
