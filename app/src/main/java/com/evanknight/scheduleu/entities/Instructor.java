package com.evanknight.scheduleu.entities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Entity;

import static com.evanknight.scheduleu.util.Constants.*;
import static com.evanknight.scheduleu.util.EntityTypeID.*;

import com.evanknight.scheduleu.activities.CourseDetails;
import com.evanknight.scheduleu.activities.InstructorDetails;
import com.evanknight.scheduleu.util.EntityTypeID;

@Entity(tableName = "instructor_table")
public class Instructor extends SUObject {
    private String lastName;
    private String instructorEmail;
    private String instructorPhoneNumber;

    public Instructor(int instructorID, String instructorNameFirst, String instructorNameLast, @Nullable String instructorEmail, @Nullable String instructorPhoneNumber){
        super(instructorID, instructorNameLast, INSTRUCTOR_ENTITY);
        this.entityName = instructorNameFirst;
        this.lastName = instructorNameLast;
        this.instructorEmail = instructorEmail;
        this.instructorPhoneNumber = instructorPhoneNumber;
    }

    // Copy Constructor
    public Instructor(Instructor other){
        this.setItemID(other.getItemID());
        this.entityName = other.entityName;
        this.lastName = other.lastName;
        this.instructorEmail = other.instructorEmail;
        this.instructorPhoneNumber = other.instructorPhoneNumber;
        this.entityTypeID = INSTRUCTOR_ENTITY;
    }

    public Instructor instructorConverter(SUObject o){
        if (this.getClass() == o.getClass()){
            this.setItemID(o.getItemID());
            this.entityName = o.entityName;
            this.entityTypeID = o.entityTypeID;
            this.lastName = ((Instructor) o).lastName;
            this.instructorEmail = ((Instructor) o).instructorEmail;
            this.instructorPhoneNumber = ((Instructor) o).instructorPhoneNumber;
        }
        return this;
    }

    public Instructor(){ super(); entityTypeID = INSTRUCTOR_ENTITY; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName){ this.lastName = lastName; }

    public String getInstructorEmail(){
        return (instructorEmail != null ? instructorEmail : "");
    }
    public void setInstructorEmail(String email) {
        this.instructorEmail = email;
    }

    public String getInstructorPhoneNumber(){
        return (instructorPhoneNumber != null ? instructorPhoneNumber : "");
    }
    public void setInstructorPhoneNumber(String phoneNumber){
        this.instructorPhoneNumber = phoneNumber;
    }

    @Override
    public Class<? extends AppCompatActivity> getDetailsClass() {
        return InstructorDetails.class;
    }

    @Override
    public String toString(){
        return "instructorEntity{" +
                "instructorID=" + getItemID() +
                ", instructorNameLast='" + entityName + '\'' +
                ", instructorNameFirst=" + lastName + '\'' +
                ", instructorEmail=" + instructorEmail +
                ", instructorPhoneNumber=" + instructorPhoneNumber +
                '}';
    }
}
