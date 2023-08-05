package com.evanknight.scheduleu.activities;

import static com.evanknight.scheduleu.util.TermStatus.*;

import android.content.Context;
import android.view.View;

import com.evanknight.scheduleu.util.TermStatus;
import com.evanknight.scheduleu.R;
import com.evanknight.scheduleu.activities.TermAdapter.TermViewHolder;
import com.evanknight.scheduleu.entities.Term;

public class TermAdapter extends SUBaseAdapter<Term, TermViewHolder> {

        class TermViewHolder extends SUBaseViewHolder {
            private TermViewHolder(View itemView){
                super(itemView);
            }
        }

        private TermStatus status;
        public TermAdapter(Context context){
            super(context, PROJECTED);
        }


        @Override
        protected void instantiateT() { if (null == t){ t = new Term(); } }

        @Override
        protected Term instantiateT(Term term) {
            return new Term(term);
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
        protected void setStatus(Enum<?> s) {
            if (TermStatus.class == s.getClass()) {
                this.status = (TermStatus) s;
            }
        }
}
