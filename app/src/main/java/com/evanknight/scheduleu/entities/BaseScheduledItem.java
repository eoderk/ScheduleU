package com.evanknight.scheduleu.entities;

import static com.evanknight.scheduleu.util.Constants.*;

import com.evanknight.scheduleu.util.EntityTypeID;

// @Entity
public class BaseScheduledItem extends SUObject  {
    protected long startDate = INIT_DATE;
    protected long endDate = INIT_DATE;
    public BaseScheduledItem(int itemID, String itemName, long startDate, long endDate, EntityTypeID entityTypeID){
        super(itemID, itemName, entityTypeID);
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public BaseScheduledItem(){}

    public long getStartDate() {
        return startDate;
    }

    public void setStartDate(long termStart) {
        this.startDate = termStart;
    }

    public long getEndDate() {
        return endDate;
    }

    public void setEndDate(long termEnd) {
        this.endDate = termEnd;
    }
}
