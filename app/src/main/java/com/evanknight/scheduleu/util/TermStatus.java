package com.evanknight.scheduleu.util;

public enum TermStatus {
    ENROLLED("Enrolled"),
    IN_PROGRESS("In Progress"),
    COMPLETED("Completed"),
    WITHDRAWN("Withdrawn"),
    PROJECTED("Projected");

    public String name;
    TermStatus(String s){
        this.name = s;
    }

    public static TermStatus getByName(String s){

        for (TermStatus ts : values()){
            if (ts.name.equals(s)) {
                return ts;
            }
        }
        return null;
    }

    public static TermStatus getStatusByOrdinal(int o){

        for (TermStatus ts : values()){
            if (o == ts.ordinal()){
                return ts;
            }
        }
        return null;
    }
}
