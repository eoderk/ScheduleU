package com.evanknight.scheduleu.entities;

import static com.evanknight.scheduleu.util.Constants.*;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.evanknight.scheduleu.util.EntityTypeID;
public class SUObject {

    public static final int MAX_VALUE = 999999;

    @PrimaryKey
    private int itemID = INIT_ENTITY_ID;
    protected String entityName;
    @Ignore
    protected EntityTypeID entityTypeID;

    public SUObject(){ entityTypeID = EntityTypeID.UNDEFINED; }

    public SUObject(int itemID, String entityName, EntityTypeID entityTypeID){
        this.itemID = itemID;
        this.entityName = entityName;
        this.entityTypeID = entityTypeID;
    }

    public int getItemID() { return itemID; }

    public void setItemID(int itemID) { this.itemID = itemID; }


    public String getEntityName() { return entityName; }

    public void setEntityName(String entityName) { this.entityName = entityName; }


    public EntityTypeID getEntityTypeID() { return entityTypeID; }

    public void setEntityTypeID(EntityTypeID entityTypeID) { this.entityTypeID = entityTypeID; }

    public boolean equals(SUObject other){
        boolean match;
        match = this.getItemID() == other.getItemID();
        match = match && this.entityName.equals(other.entityName);
        return match;
    }

    public Class<? extends AppCompatActivity> getDetailsClass() { return null; }
}
