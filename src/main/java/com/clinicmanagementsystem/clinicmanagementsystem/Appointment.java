package com.clinicmanagementsystem.clinicmanagementsystem;

import javafx.beans.property.*;

public class Appointment {
    private final IntegerProperty appointmentId;
    private final IntegerProperty queueNumber;
    private final StringProperty status;
    private final StringProperty patientName;
    private final StringProperty doctorName;
    private final StringProperty scheduleDay ;
    private final StringProperty createdAt ;

    public Appointment(int appointmentId, int queueNumber, String status, String patientName, String doctorName , String scheduleDay , String createdAt) {
        this.appointmentId = new SimpleIntegerProperty(appointmentId);
        this.queueNumber = new SimpleIntegerProperty(queueNumber);
        this.status = new SimpleStringProperty(status);
        this.patientName = new SimpleStringProperty(patientName);
        this.doctorName = new SimpleStringProperty(doctorName);
        this.scheduleDay = new SimpleStringProperty(scheduleDay);
        this.createdAt = new SimpleStringProperty(createdAt);

    }

    public int getAppointmentId() {
        return appointmentId.get();
    }

    public IntegerProperty appointmentIdProperty() {
        return appointmentId;
    }

    public int getQueueNumber() {
        return queueNumber.get();
    }

    public IntegerProperty queueNumberProperty() {
        return queueNumber;
    }

    public String getStatus() {
        return status.get();
    }

    public StringProperty statusProperty() {
        return status;
    }

    public String getScheduleDay() {
        return scheduleDay.get();
    }

    public StringProperty scheduleDayProperty() {
        return scheduleDay;
    }


    public String getPatientName() {
        return patientName.get();
    }

    public StringProperty patientNameProperty() {
        return patientName;
    }

    public String getDoctorName() {
        return doctorName.get();
    }

    public StringProperty createdAtProperty() {
        return createdAt;
    }
    public String getCreatedAt() {
        return createdAt.get();
    }



}
