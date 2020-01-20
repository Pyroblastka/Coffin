package com.pyro.entities;

public class Col {
    String column_name;
    String data_type;
    String is_nullable;

    public Col() {
    }

    public Col(String column_name, String data_type, String is_nullable) {
        this.column_name = column_name;
        this.data_type = data_type;
        this.is_nullable = is_nullable;
    }

    public String getColumn_name() {
        return column_name;
    }

    public void setColumn_name(String column_name) {
        this.column_name = column_name;
    }

    public String getData_type() {
        return data_type;
    }

    public void setData_type(String data_type) {
        this.data_type = data_type;
    }

    public String getIs_nullable() {
        return is_nullable;
    }

    public void setIs_nullable(String is_nullable) {
        this.is_nullable = is_nullable;
    }
}
