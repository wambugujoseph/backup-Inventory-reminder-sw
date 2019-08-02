package com.backup.reports.util;

public enum InventoryOccurrenceOption {

    MONTH, MONDAY, FRIDAY;

    InventoryOccurrenceOption(){}

    public String value(){
        return name();
    }

    public static InventoryOccurrenceOption fromvalue(String v){
        return valueOf(v);
    }
}
