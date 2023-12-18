package com.dnanh01.backend.request;

public class SelectedTimeRequest {

    private String selectedTime;

    public SelectedTimeRequest() {

    }

    public SelectedTimeRequest(String selectedTime) {
        this.selectedTime = selectedTime;
    }

    public String getSelectedTime() {
        return selectedTime;
    }

    public void setSelectedTime(String selectedTime) {
        this.selectedTime = selectedTime;
    }

}
