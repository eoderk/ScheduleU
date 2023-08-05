package com.evanknight.scheduleu.entities;

import static com.evanknight.scheduleu.util.EntityTypeID.*;
import static androidx.room.ForeignKey.CASCADE;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;

import com.evanknight.scheduleu.activities.CourseDetails;
import com.evanknight.scheduleu.util.CourseStatus;

import java.util.Date;

@Entity(tableName = "course_table",
        foreignKeys = {@ForeignKey(entity = Term.class,
                                  parentColumns = "itemID",
                                  childColumns = "termID",
                                  onDelete = CASCADE),
                      @ForeignKey(entity = Instructor.class,
                                  parentColumns = "itemID",
                                  childColumns = "instructorID",
                                  onDelete = CASCADE)
                     })
public class Course extends BaseScheduledItem {
    private CourseStatus status;
    private boolean matchTermDates = true;
    protected String entityNote;
    @ColumnInfo(index = true)
    private int termID;
    @ColumnInfo(index = true)
    private int instructorID;

    @Ignore
    public Course(int courseID, String courseName, long courseStartDate, long courseEndDate, CourseStatus courseStatus) {
        super(courseID, courseName, courseStartDate, courseEndDate, COURSE_ENTITY);
        this.status = courseStatus;
    }

    public Course(Course other){
        this.setItemID(other.getItemID());
        this.entityName = other.entityName;
        this.startDate = other.startDate;
        this.endDate = other.endDate;
        this.instructorID = other.instructorID;
        this.termID = other.termID;
        this.status = other.status;
        this.matchTermDates = other.matchTermDates;
        this.entityTypeID = COURSE_ENTITY;
    }

    public Course courseConverter(SUObject o){
        if (this.getClass() == o.getClass()) {
            this.setItemID(o.getItemID());
            this.entityName = o.entityName;
            this.entityTypeID = o.entityTypeID;
            this.startDate = ((Course) o).startDate;
            this.endDate = ((Course) o).endDate;
            this.instructorID = ((Course) o).instructorID;
            this.termID = ((Course) o).termID;
            this.status = ((Course) o).status;
            this.matchTermDates = ((Course) o).matchTermDates;
        }
        return this;
    }

    public Course(){ entityTypeID = COURSE_ENTITY; }

    public CourseStatus getStatus() {
        return status;
    }
    public void setStatus(CourseStatus CourseStatus) {
        this.status= CourseStatus;
    }

    public void setStatusByOrdinal(int o) { this.status = CourseStatus.getStatusByOrdinal(o); }
    public int getStatusAsOrdinal() { return status.ordinal(); }

    public int getTermID() { return termID; }

    public void setTermID(int termID) { this.termID = termID; }

    public int getInstructorID() { return instructorID; }

    public void setInstructorID(int instructorID) { this.instructorID = instructorID; }

    public void setMatchTermDates(boolean matchDates){ this.matchTermDates = matchDates; }
    public boolean getMatchTermDates() { return matchTermDates; }

    public String getEntityNote() { return entityNote;}
    public void setEntityNote(String note) { this.entityNote = note; }

    @Override
    public Class<? extends AppCompatActivity> getDetailsClass() {
        return CourseDetails.class;
    }

    @Override
    public String toString() {
        return "courseEntity{" +
                "courseID=" + getItemID() +
                ", courseName='" + entityName + '\'' +
                ", matchTermDates=" + (matchTermDates ? "TRUE" : "FALSE") +
                ", courseStartDate=" + startDate +
                ", courseEndDate=" + endDate +
                ", courseStatus=" + status.name +
                ", instructorID=" + instructorID +
                ", termID=" + termID +
                '}';
    }
}
