package com.evanknight.scheduleu.util;

public enum AssessmentStatus {
    PROJECTED("Projected"),
    SCHEDULED("Scheduled"),
    TAKEN("Taken"),
    COMPLETED("Completed");

    public String name;
    private AssessmentStatus(String s){
        this.name = s;
    }

    public static AssessmentStatus getByName(String s){

        for (AssessmentStatus as : values()){
            if (as.name.equals(s)) {
                return as;
            }
        }
        return null;
    }

    public static AssessmentStatus getStatusByOrdinal(int o){

        for (AssessmentStatus as : values()){
            if (o == as.ordinal()){
                return as;
            }
        }
        return null;
    }
}