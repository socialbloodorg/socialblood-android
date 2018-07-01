package co.nexlabs.socialblood.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import co.nexlabs.socialblood.R;
import co.nexlabs.socialblood.model.BloodItem;
import co.nexlabs.socialblood.model.BloodRhItem;

/**
 * Created by myozawoo on 3/15/16.
 */
public class BloodRhTypeAdapterDialog extends BaseAdapter {

    private ArrayList<BloodRhItem> listData;

    private LayoutInflater layoutInflater;

    public BloodRhTypeAdapterDialog(Context context, ArrayList<BloodRhItem> listData) {
        this.listData = listData;
        layoutInflater = LayoutInflater.from(context);
    }


    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.dialog_list_row, null);
            holder = new ViewHolder();
            holder.bloodType = (TextView) convertView.findViewById(R.id.blood_type);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.bloodType.setText(listData.get(position).getBloodRhType());

        return convertView;
    }

    static class ViewHolder {
        TextView bloodType;
    }

}
