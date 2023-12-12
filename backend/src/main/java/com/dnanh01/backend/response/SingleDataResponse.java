package com.dnanh01.backend.response;

public class SingleDataResponse<T> {
    private String label;
    private T value;

    public SingleDataResponse() {
    }

    public SingleDataResponse(String label, T value) {
        this.label = label;
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

}
