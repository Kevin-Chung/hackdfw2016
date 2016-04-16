package hackdfw2016.smartalarm;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Kevin on 4/16/2016.
 */
public class AlarmListAdapter extends RecyclerView.Adapter<AlarmListAdapter.ViewHolder> {
    private String[] mDataset;
    private ArrayList<Alarm> data;
    Context context;


    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView alarmName;
        public TextView wakeUp;
        public TextView days;
        public ViewHolder(View v) {
            super(v);
            alarmName = (TextView) v.findViewById(R.id.alarmName);
            wakeUp = (TextView) v.findViewById(R.id.wakeUp);
            days = (TextView) v.findViewById(R.id.days);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(),Alarm.class);
                }
            });
        }
        public void bind(Alarm data){
            alarmName.setText(data.getAlarmName());
            days.setText(data.getDays());
            wakeUp.setText(data.getWakeUpTime());

        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public AlarmListAdapter(String[] myDataset) {
        mDataset = myDataset;
    }

    public AlarmListAdapter(ArrayList<Alarm> data, Context context) {
        this.data = data;
        this.context = context;
    }


    // Create new views (invoked by the layout manager)
    @Override
    public AlarmListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);

        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.bind(data.get(position));

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return data.size();
    }
}
