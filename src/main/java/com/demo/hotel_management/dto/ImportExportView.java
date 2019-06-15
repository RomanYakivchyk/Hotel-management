package com.demo.hotel_management.dto;

public class ImportExportView {

    public ImportExportView() {
    }

    private String action = "export";
    private String type;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
