package com.evanknight.scheduleu.activities;

import static com.evanknight.scheduleu.util.CourseStatus.*;
import static com.evanknight.scheduleu.util.Constants.*;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.evanknight.scheduleu.R;
import com.evanknight.scheduleu.entities.Course;
import com.evanknight.scheduleu.util.CourseStatus;
import com.evanknight.scheduleu.util.EasyDate;

import java.util.Calendar;
import java.util.Date;

public class CourseAdapter extends SUBaseAdapter<Course, CourseAdapter.CourseViewHolder> {
    class CourseViewHolder extends SUBaseViewHolder {
        private CourseViewHolder(View itemView){
            super(itemView);
        }
    }

    private CourseStatus status;
    public CourseAdapter(Context context){
       super(context, PROJECTED);
    }

    @Override
    protected void putExtraExtras(Intent intent, Course current) {
        long sDate = EasyDate.todayInMilli();
        long eDate = EasyDate.addMonths(sDate, TERM_LENGTH);
        CourseStatus cStatus = PROJECTED;

        if (Course.class == current.getClass()){
            sDate = current.getStartDate();
            eDate = current.getEndDate();
            cStatus = current.getStatus();
        }

        intent.putExtra(COURSE_START_DATE_KEY, sDate);
        intent.putExtra(COURSE_END_DATE_KEY, eDate);
        intent.putExtra(COURSE_STATUS_KEY, cStatus);
    }

    @Override
    protected void instantiateT() { t = new Course(); }

    @Override
    protected Course instantiateT(Course course) {
        return new Course(course);
    }

    @Override
    protected String getStatus() {
        return this.status.name;
    }

    @Override
    protected int getStringResource1() {
        return R.string.no_course_id;
    }

    @Override
    protected int getStringResource2() {
        return R.string.no_course_name;
    }

    @Override
    protected void setStatus(Enum<?> c) {
        if(CourseStatus.class == c.getClass()) {
            this.status = (CourseStatus) c;
        } else {
            this.status = PROJECTED;
        }
    }
}
