package com.ppsdevelopment.viewlib;

public class HeaderColumnClass {
    Long id;
    String columnStyle;
    String Caption;

    public HeaderColumnClass() {
    }

    public HeaderColumnClass(Long id, String columnStyle, String caption) {
        this.id = id;
        this.columnStyle = columnStyle;
        Caption = caption;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getColumnStyle() {
        return columnStyle;
    }

    public void setColumnStyle(String columnStyle) {
        this.columnStyle = columnStyle;
    }

    public String getCaption() {
        return Caption;
    }

    public void setCaption(String caption) {
        Caption = caption;
    }

    @Override
    public String toString() {
        return "HeaderColumnClass{" +
                "id=" + id +
                ", columnStyle='" + columnStyle + '\'' +
                ", Caption='" + Caption + '\'' +
                '}';
    }

    public String toArrayString() {
        return "{" +
                "id=" + id +
                ", columnStyle='" + columnStyle + '\'' +
                ", Caption='" + Caption + '\'' +
                '}';
    }
}
