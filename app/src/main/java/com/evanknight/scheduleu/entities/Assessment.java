package com.evanknight.scheduleu.entities;

import static com.evanknight.scheduleu.util.EntityTypeID.*;
import static androidx.room.ForeignKey.CASCADE;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;

import com.evanknight.scheduleu.activities.AssessmentDetails;
import com.evanknight.scheduleu.util.AssessmentStatus;

@Entity(tableName = "assessment_table",
        foreignKeys = @ForeignKey(entity = Course.class,
                                  parentColumns = "itemID",
                                  childColumns = "courseID",
                                  onDelete = CASCADE))
public class Assessment extends BaseScheduledItem {
    private boolean isDateRange = false;
    private AssessmentStatus status;
    @ColumnInfo(index = true)
    private int courseID;

    @Ignore
    public Assessment(int assessmentID, String assessmentName, long assessmentStartDate, long assessmentEndDate, AssessmentStatus assessmentStatus) {
        super(assessmentID, assessmentName, assessmentStartDate, assessmentEndDate, ASSESSMENT_ENTITY);
        this.status = assessmentStatus;
        this.isDateRange = true;
    }

    @Ignore
    public Assessment(int assessmentID, String assessmentName, long startDate, AssessmentStatus assessmentStatus){
        this(assessmentID, assessmentName, startDate, startDate, assessmentStatus);
        this.isDateRange = false;
    }

    // Copy constructor
    public Assessment(Assessment other){
        this.setItemID(other.getItemID());
        this.entityName = other.entityName;
        this.startDate = other.startDate;
        this.endDate = other.endDate;
        this.status = other.status;
        this.isDateRange = other.isDateRange;
        this.courseID = other.courseID;
        this.entityTypeID = ASSESSMENT_ENTITY;
    }

    public Assessment assessmentConverter(SUObject o){
        if (this.getClass() == o.getClass()) {
            this.setItemID(o.getItemID());
            this.entityName = o.entityName;
            this.entityTypeID = o.entityTypeID;
            this.startDate = ((Assessment) o).startDate;
            this.endDate = ((Assessment) o).endDate;
            this.status = ((Assessment) o).status;
            this.isDateRange = ((Assessment) o).isDateRange;
            this.courseID = ((Assessment) o).courseID;
        }
        return this;
    }

    public Assessment(){ entityTypeID = ASSESSMENT_ENTITY; }

    public boolean getIsDateRange(){
        return isDateRange;
    }
    public void setIsDateRange(boolean isRange){
        this.isDateRange = isRange;
    }

    public AssessmentStatus getStatus() { return status; }
    public void setStatus(AssessmentStatus assessmentStatus) { this.status= assessmentStatus; }

    public void setStatusByOrdinal(int o) { this.status = AssessmentStatus.getStatusByOrdinal(o); }
    public int getStatusAsOrdinal() { return status.ordinal(); }

    public void setCourseID(int cID) { this.courseID = cID; }
    public int getCourseID() { return courseID; }

    @Override
    public Class<? extends AppCompatActivity> getDetailsClass() {
        return AssessmentDetails.class;
    }

    @Override
    public String toString() {
        return "assessmentEntity{" +
                "assessmentID=" + getItemID() +
                ", assessmentName='" + entityName + '\'' +
                ", assessmentDateIsDateRange=" + (isDateRange ? "TRUE" : "FALSE") +
                ", assessmentStartDate=" + startDate +
                ", assessmentEndDate=" + (isDateRange ? endDate : startDate) +
                ", assessmentStatus=" + status +
                ", courseID=" + courseID +
                '}';
    }
}
