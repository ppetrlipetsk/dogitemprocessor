package com.ppsdevelopment.controller.requestclass;

public class FilterDataClass {
    String value;
    boolean checked;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    @Override
    public String toString() {
        return "FilterDataClass{" +
                "value='" + value + '\'' +
                ", checked=" + checked +
                '}';
    }

}
