package com.evanknight.scheduleu.entities;

import static com.evanknight.scheduleu.util.EntityTypeID.*;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Entity;
import androidx.room.Ignore;

import com.evanknight.scheduleu.activities.TermDetails;
import com.evanknight.scheduleu.util.EntityTypeID;
import com.evanknight.scheduleu.util.TermStatus;

import java.util.Date;
import java.util.Map;

@Entity(tableName = "term_table")
public class Term extends BaseScheduledItem {
    TermStatus status;
    private String entityNote;

    @Ignore
    public Term(int itemID, String itemName, long termStartDate, long termEndDate, TermStatus termStatus) {
        super(itemID, itemName, termStartDate, termEndDate, TERM_ENTITY);
        this.status = termStatus;
    }

    public Term(){ entityTypeID = TERM_ENTITY; }

    // Copy Constructor
    public Term(Term other){
        this.setItemID(other.getItemID());
        this.entityName = other.entityName;
        this.startDate = other.startDate;
        this.endDate = other.endDate;
        this.status = other.status;
        this.entityNote = other.entityNote;
        this.entityTypeID = TERM_ENTITY;
    }

    public Term termConverter(SUObject o){
        if (this.getClass() == o.getClass()){
            this.setItemID(o.getItemID());
            this.entityName = o.entityName;
            this.entityTypeID = o.entityTypeID;
            this.entityNote = ((Term) o).entityNote;
            this.startDate = ((Term) o).startDate;
            this.endDate = ((Term) o).endDate;
            this.status = ((Term) o).status;
        }
        return this;
    }

    public TermStatus getStatus() { return status;}
    public int getStatusAsOrdinal() { return status.ordinal(); }

    public void setStatus(TermStatus status) { this.status = status;}
    public void setStatusByOrdinal(int o) { this.status = TermStatus.getStatusByOrdinal(o); }

    public String getEntityNote() { return entityNote;}
    public void setEntityNote(String note) { this.entityNote = note; }

    @Override
    public Class<? extends AppCompatActivity> getDetailsClass(){
        return TermDetails.class;
    }

    @Override
    public String toString() {
        return "termEntity{" +
                "termID=" + getItemID() +
                ", termName='" + entityName + '\'' +
                ", termStartDate=" + startDate +
                ", termEndDate=" + endDate +
                ", termStatus=" + status.name +
                '}';
    }
}
