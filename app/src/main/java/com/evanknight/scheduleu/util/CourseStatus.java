package com.evanknight.scheduleu.util;

public enum CourseStatus {
    ENROLLED("Enrolled"),
    IN_PROGRESS("In Progress"),
    COMPLETED("Completed"),
    FAILED("Failed"),
    INCOMPLETE("Incomplete"),
    DROPPED("Dropped"),
    PROJECTED("Projected"),
    PLANNED("Planned");

    public String name;
    private CourseStatus(String s){
        this.name = s;
    }

    public static CourseStatus getByName(String s){

        for (CourseStatus cs : values()){
            if (cs.name.equals(s)) {
                return cs;
            }
        }
        return null;
    }

    public static CourseStatus getStatusByOrdinal(int o){

        for (CourseStatus cs : values()){
            if (o == cs.ordinal()){
                return cs;
            }
        }
        return null;
    }
}