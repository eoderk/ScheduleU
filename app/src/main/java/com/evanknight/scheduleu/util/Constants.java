package com.evanknight.scheduleu.util;

public class Constants {
    // Root Level Constants
    public static final int TERM_LENGTH = 4; // Length of term in months
    public static final int INIT_ENTITY_ID = -1;    // All objects are initialized to -1 to prevent inadvertent insertion to db
    public static final long INIT_DATE = -1L;
    public static final long BAD_DATE = -1L;
    public static final String TERM = "term";
    public static final String COURSE = "course";
    public static final String ASSESSMENT = "assessment";
    public static final String INSTRUCTOR = "instructor";
    public static final String DEFAULT_NAME = "UN_NAMED";

    // Attribute Constants
    public static final boolean DEFAULT_IS_MATCH_TERM_DATES = true;
    public static final boolean DEFAULT_IS_DATE_RANGE = false;
    public static final String NO_EMAIL = "no_email_provided";
    public static final String NO_PHONE_NUMBER = "no_number_provided";


    // String Keys
    public static final String CALLING_CLASS = "calling_class";
    public static final String ID_KEY = "_id";
    public static final String NAME_KEY_1 = "_name_1";
    public static final String NAME_KEY_2 = "_name_2";
    public static final String START_DATE_KEY = "_start_date";
    public static final String END_DATE_KEY = "_end_date";
    public static final String STATUS_KEY = "_status";
    public static final String NOTE_KEY = "_note";
    public static final String TYPE_ID_KEY = "type_id";
    public static final String PARENT_ID = "parent_id_number";
    public static final String PARENT_TYPE_ID_KEY = "parent_type_id";
    public static final String CHILD_LIST_KEY = "child_list";

    public static final String TERM_ID_KEY =            TERM + ID_KEY;
    public static final String TERM_NAME_KEY =          TERM + NAME_KEY_1;
    public static final String TERM_START_DATE_KEY =    TERM + START_DATE_KEY;
    public static final String TERM_END_DATE_KEY =      TERM + END_DATE_KEY;
    public static final String TERM_STATUS_KEY =        TERM + STATUS_KEY;
    public static final String TERM_NOTE_KEY =          TERM + NOTE_KEY;

    public static final String COURSE_ID_KEY =          COURSE + ID_KEY;
    public static final String COURSE_NAME_KEY =        COURSE + NAME_KEY_1;
    public static final String COURSE_START_DATE_KEY =  COURSE + START_DATE_KEY;
    public static final String COURSE_END_DATE_KEY =    COURSE + END_DATE_KEY;
    public static final String COURSE_STATUS_KEY =      COURSE + STATUS_KEY;
    public static final String COURSE_NOTE_KEY =        COURSE + NOTE_KEY;
    public static final String COURSE_MATCH_DATE_KEY =  COURSE + "_match_term_dates";

    public static final String ASSESSMENT_ID_KEY =          ASSESSMENT + ID_KEY;
    public static final String ASSESSMENT_NAME_KEY =        ASSESSMENT + NAME_KEY_1;
    public static final String ASSESSMENT_START_DATE_KEY =  ASSESSMENT + START_DATE_KEY;
    public static final String ASSESSMENT_END_DATE_KEY =    ASSESSMENT + END_DATE_KEY;
    public static final String ASSESSMENT_STATUS_KEY =      ASSESSMENT + STATUS_KEY;
    public static final String ASSESSMENT_NOTE_KEY =        ASSESSMENT + NOTE_KEY;
    public static final String ASSESSMENT_HAS_DATE_RANGE =  ASSESSMENT + "_has_date_range";

    public static final String INSTRUCTOR_ID_KEY =          INSTRUCTOR + ID_KEY;
    public static final String INSTRUCTOR_FIRST_NAME_KEY =             NAME_KEY_1;
    public static final String INSTRUCTOR_LAST_NAME_KEY =              NAME_KEY_2;
    public static final String EMAIL_KEY =                  "instructor_email";
    public static final String PHONE_NUMBER_KEY =           "instructor_phone_number";

    // Utility Constants
    public static final int NOTIFICATION_ID = 7;
    public static final String NOTIFICATION_CHANNEL_ID = "schedule_u";
    public static final String NOTIFICATION_TITLE = "NotificationTest";
}
