package com.mycompany.foreverhomedbmsproject.Server;

import java.time.LocalDate;
import java.time.LocalTime;

public class Event {

    private int eventId;
    private String eventName;
    private LocalDate dateOfEvent;
    private LocalTime timeOfEvent;
    private String location;
    private double fundingGoal;

    public Event() {
    }

    public Event(int eventId, String eventName, LocalDate dateOfEvent, LocalTime timeOfEvent, String location, double fundingGoal) {
        this.eventId = eventId;
        this.eventName = eventName;
        this.dateOfEvent = dateOfEvent;
        this.timeOfEvent = timeOfEvent;
        this.location = location;
        this.fundingGoal = fundingGoal;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public LocalDate getDateOfEvent() {
        return dateOfEvent;
    }

    public void setDateOfEvent(LocalDate dateOfEvent) {
        this.dateOfEvent = dateOfEvent;
    }

    public LocalTime getTimeOfEvent() {
        return timeOfEvent;
    }

    public void setTimeOfEvent(LocalTime timeOfEvent) {
        this.timeOfEvent = timeOfEvent;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public double getFundingGoal() {
        return fundingGoal;
    }

    public void setFundingGoal(double fundingGoal) {
        this.fundingGoal = fundingGoal;
    }

    @Override
    public String toString() {
        return "Event{" +
                "eventId=" + eventId +
                ", eventName='" + eventName + '\'' +
                ", dateOfEvent=" + dateOfEvent +
                ", timeOfEvent=" + timeOfEvent +
                ", location='" + location + '\'' +
                ", fundingGoal=" + fundingGoal +
                '}';
    }
}
