package com.evanknight.scheduleu.activities;

import static com.evanknight.scheduleu.util.Constants.*;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.evanknight.scheduleu.R;
import com.evanknight.scheduleu.entities.BaseScheduledItem;
import com.evanknight.scheduleu.entities.SUObject;
import com.evanknight.scheduleu.entities.Term;
import com.evanknight.scheduleu.entities.Course;
import com.evanknight.scheduleu.entities.Assessment;
import com.evanknight.scheduleu.entities.Instructor;
import com.evanknight.scheduleu.util.EasyDate;
import com.evanknight.scheduleu.util.EntityTypeID;

import java.util.List;
import java.util.logging.Logger;

public abstract class SUBaseAdapter<T extends SUObject, V extends SUBaseAdapter.SUBaseViewHolder> extends RecyclerView.Adapter<V> {

    public class SUBaseViewHolder extends RecyclerView.ViewHolder {
        protected int itemID;
        protected final TextView itemNameField;
        protected final TextView itemDateField;
        protected SUBaseViewHolder(View itemView){
            super(itemView);
            instantiateT();
            itemNameField = itemView.findViewById(R.id.doubleListEntryCol1);
            itemDateField = itemView.findViewById(R.id.doubleListEntryCol2);
            // TODO: Get result to refresh view
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String type = "unassigned";
                    try {
                    int position = getBindingAdapterPosition();
                        if (null == mItemList.get(position)){
                            Logger.getLogger("SUBaseAdapter[" + t.getEntityTypeID().toString() + "]").warning("***** List Item Not Set to an Instance of an Object *****");
                        } else {
                            Logger.getLogger("SUBaseAdapter[" + t.getEntityTypeID().name() + "]").warning("***** List Item Number " + position +" *****");
                            final T current = mItemList.get(position);
                            type = current.getEntityTypeID().name() ;
                            Intent intent = new Intent(context, current.getDetailsClass());
                            intent.putExtra(getIdStringKey(), current.getItemID());
                            intent.putExtra(getNameStringKey(), current.getEntityName());
                            if (Instructor.class == current.getClass()) {
                                Instructor inst = (Instructor) current;
                                intent.putExtra(INSTRUCTOR_LAST_NAME_KEY, inst.getLastName());
                            } else {
                                putExtraExtras(intent, current);
                            }
                            context.startActivity(intent);
                        }
                    } catch (Exception ex) {
                        Logger.getLogger("SUBaseAdapter[" + type + "]").warning("**************** SUBaseAdapter OnClick Error **********************");
                        Logger.getLogger("SUBaseAdapter[" + type + "]").warning(ex.getStackTrace().toString());
                        ex.printStackTrace();
                    }
                }
            });
        }
    }

    protected final int NO_ITEM_STRING_1;
    protected final int NO_ITEM_STRING_2;
    protected T t;

    protected List<T> mItemList;
    protected final Context context;
    protected final LayoutInflater mInflater;

    /// Constructor ///
    public SUBaseAdapter(Context context, Enum<?> e){
        NO_ITEM_STRING_1 = getStringResource1();
        NO_ITEM_STRING_2 = getStringResource2();
        setStatus(e);
        mInflater = LayoutInflater.from(context);
        this.context = context;
        instantiateT();
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public V onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.component_two_col_list_view, parent, false);
        return (V)new SUBaseViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull V holder, int position) {
        if (mItemList != null){
            T current = mItemList.get(position);
            holder.itemID = current.getItemID();
            holder.itemNameField.setText(current.getEntityName());
            if (EntityTypeID.INSTRUCTOR_ENTITY == current.getEntityTypeID()) {
                Instructor inst = (Instructor) current;
                holder.itemDateField.setText(inst.getLastName());
            } else {
                BaseScheduledItem scheduledItem = (BaseScheduledItem) current;
                holder.itemDateField.setText(EasyDate.getFormat_ddMMMyyyy(scheduledItem.getStartDate()));
            }
        }
        else {
            holder.itemNameField.setText(NO_ITEM_STRING_1);
            holder.itemDateField.setText(NO_ITEM_STRING_2);
        }
    }
    @Override
    public int getItemCount(){
        return mItemList.size();
    }
    @SuppressLint("NotifyDataSetChanged")
    public void setItemList(List<T> list){
        mItemList = list;
        notifyDataSetChanged();
    }

    private String getIdStringKey(){
        if (Term.class == t.getClass()){
            return TERM_ID_KEY;
        } else if (Course.class == t.getClass()){
            return COURSE_ID_KEY;
        } else if (Assessment.class == t.getClass()){
            return ASSESSMENT_ID_KEY;
        } else if (Instructor.class == t.getClass()){
            return INSTRUCTOR_ID_KEY;
        }
        return ID_KEY;
    }

    private String getNameStringKey(){
        if (Term.class == t.getClass()){
            return TERM_NAME_KEY;
        } else if (Course.class == t.getClass()){
            return COURSE_NAME_KEY;
        } else if (Assessment.class == t.getClass()){
            return ASSESSMENT_NAME_KEY;
        } else if (Instructor.class == t.getClass()){
            return INSTRUCTOR_FIRST_NAME_KEY;
        }
        return NAME_KEY_1;
    }

    protected void putExtraExtras(Intent intent, T current) {}
    protected abstract int getStringResource1();
    protected abstract int getStringResource2();
    protected abstract String getStatus();
    protected abstract void instantiateT();
    protected abstract T instantiateT(T t);
    protected abstract void setStatus(Enum<?> e);
}
