package com.evanknight.scheduleu.util;

import java.util.logging.Logger;

public enum EntityTypeID {
    UNDEFINED("Schedule U"),
    TERM_ENTITY("Add Term"),
    COURSE_ENTITY("Add Course"),
    ASSESSMENT_ENTITY("Add Assessment"),
    INSTRUCTOR_ENTITY("Add Instructor");
    public final String TITLE;
    private EntityTypeID(String title){
        TITLE = title;
    }

    public static EntityTypeID getEntityType(int i){
        for (EntityTypeID e : values()){
            Logger.getGlobal().info("~~~~~~~~~~~~ Entity Type ID testing e = " + e.ordinal() + " == " + i + " ~~~~~~~~~~~~~~~~~~~~");
            if (e.ordinal() == i){
                return e;
            }
        }
        return UNDEFINED;
    }
}
