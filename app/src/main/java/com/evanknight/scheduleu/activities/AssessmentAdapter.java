package com.evanknight.scheduleu.activities;

import static com.evanknight.scheduleu.util.AssessmentStatus.*;
import static com.evanknight.scheduleu.util.Constants.*;

import com.evanknight.scheduleu.R;
import com.evanknight.scheduleu.util.AssessmentStatus;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.evanknight.scheduleu.entities.Assessment;
import com.evanknight.scheduleu.util.EasyDate;

import java.util.Calendar;
import java.util.Date;

public class AssessmentAdapter extends SUBaseAdapter<Assessment, AssessmentAdapter.AssessmentViewHolder> {

    class AssessmentViewHolder extends SUBaseViewHolder {
        private AssessmentViewHolder(View viewItem) {
            super(viewItem);
        }
    }

    private AssessmentStatus status;

    public AssessmentAdapter(Context context) {
        super(context, PROJECTED);
    }

    @Override
    protected void putExtraExtras(Intent intent, Assessment current){
        long sDate = EasyDate.todayInMilli();
        long eDate = sDate;
        AssessmentStatus cStatus = PROJECTED;

        if (Assessment.class == current.getClass()){
            sDate = current.getStartDate();
            eDate = current.getEndDate();
            cStatus = current.getStatus();
        }

        intent.putExtra(ASSESSMENT_START_DATE_KEY, sDate);
        intent.putExtra(ASSESSMENT_END_DATE_KEY, eDate);
        intent.putExtra(ASSESSMENT_STATUS_KEY, cStatus);
    }

    @Override
    protected void instantiateT() { t = new Assessment(); }

    @Override
    protected Assessment instantiateT(Assessment assessment) {
        return new Assessment(assessment);
    }

    @Override
    protected String getStatus() {
        return this.status.name;
    }

    @Override
    protected int getStringResource1() {
        return R.string.no_assessment_id;
    }

    @Override
    protected int getStringResource2() {
        return R.string.no_assessment_name;
    }

    @Override
    protected void setStatus(Enum<?> a) {
        if (AssessmentStatus.class == a.getClass()){
            this.status = (AssessmentStatus) a;
        } else {
            this.status = PROJECTED;
        }
    }
}
