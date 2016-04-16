package hackdfw2016.smartalarm;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Kevin on 4/16/2016.
 */
public class DaysAdapter extends RecyclerView.Adapter<DaysAdapter.ViewHolder> {
    private String[] mDataset;
    private ArrayList<Alarm> data;
    Context context;
    ArrayList<String> days;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        TextView day;
        boolean clicked = false;
        public ViewHolder(View v) {
            super(v);
            day = (TextView)v.findViewById(R.id.day);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(),Alarm.class);
                    if(clicked) {
                        v.setBackgroundColor(Color.WHITE);
                        clicked=false;
                    }else{
                        v.setBackgroundResource(R.color.colorPrimary);
                        clicked=true;
                    }
                }
            });
        }
        public void bind(String data){
            day.setText(data);
        }
    }


    public DaysAdapter(Context context) {
        this.context = context;
        days = new ArrayList<String>();
        days.add("S");
        days.add("M");
        days.add("T");
        days.add("W");
        days.add("T");
        days.add("F");
        days.add("S");
    }


    // Create new views (invoked by the layout manager)
    @Override
    public DaysAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                          int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.day_list_item, parent, false);

        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.bind(days.get(position));

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return days.size();
    }
}
