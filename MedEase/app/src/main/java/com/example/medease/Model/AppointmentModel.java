package com.example.medease.Model;

public class AppointmentModel {

    public String doctorId;
    public String patientId;
    public String appointmentDate;
    public String appointmentTime;

    public AppointmentModel() {
    }

    public AppointmentModel(String doctorId, String patientId, String appointmentDate, String appointmentTime) {
        this.doctorId = doctorId;
        this.patientId = patientId;
        this.appointmentDate = appointmentDate;
        this.appointmentTime = appointmentTime;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(String appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public String getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(String appointmentTime) {
        this.appointmentTime = appointmentTime;
    }


}
