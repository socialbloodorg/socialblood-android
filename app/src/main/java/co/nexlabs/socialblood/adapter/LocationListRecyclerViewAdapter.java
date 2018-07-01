package co.nexlabs.socialblood.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import co.nexlabs.socialblood.R;
import co.nexlabs.socialblood.fragment.LocationListDialogFragment;
import co.nexlabs.socialblood.model.Location;

/**
 * Created by myozawoo on 3/17/16.
 */
public class LocationListRecyclerViewAdapter extends RecyclerView.Adapter<LocationListRecyclerViewAdapter.ViewHolder> {


    List<Location> list = new ArrayList<Location>();



    public void setList(List<Location> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public LocationListRecyclerViewAdapter() {
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Location current = list.get(position);
        holder.location.setText(current.getName());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.location_list_row, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        TextView location;


        public ViewHolder(View itemView) {
            super(itemView);
            location = (TextView) itemView.findViewById(R.id.location);
        }


    }

}
