package co.nexlabs.socialblood.adapter;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import co.nexlabs.socialblood.R;
import co.nexlabs.socialblood.model.BloodBank;
import co.nexlabs.socialblood.model.BloodDonor;
import co.nexlabs.socialblood.util.TextUtils;
import co.nexlabs.socialblood.view.CircularTextView;

/**
 * Created by myozawoo on 3/17/16.
 */
public class BloodBankListAdapter extends RecyclerView.Adapter<BloodBankListAdapter.ViewHolder> {

    List<BloodBank> list = new ArrayList<BloodBank>();

    public void setList(List<BloodBank> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.blood_bank_list_row, null);
        ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.bind(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView mHospitalName;
        TextView mAddress;
        TextView mDistance;
        ImageView mPhone;

        BloodBank bloodBank;

        public ViewHolder(View itemView) {
            super(itemView);

            mHospitalName = (TextView) itemView.findViewById(R.id.tv_hospital);
            mAddress = (TextView) itemView.findViewById(R.id.tv_address);
            mDistance = (TextView) itemView.findViewById(R.id.tv_distance);
            mPhone = (ImageView) itemView.findViewById(R.id.iv_call);

        }

        public void bind(final BloodBank bloodBank){
            this.bloodBank = bloodBank;
            mHospitalName.setText(bloodBank.getName());
            mAddress.setText(bloodBank.getAddress());
            String str = bloodBank.getDistance()+"";
            String distance = "";
            if (str.length()>3) {
                for (int i = 0; i < 4; i++) {
                    distance += str.charAt(i);
                }
            }
            mDistance.setText(distance + " km");

            mPhone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (bloodBank.getPhoneNo() != null) {
                        Intent callIntent = new Intent(Intent.ACTION_VIEW);
                        callIntent.setData(Uri.parse("tel:" + bloodBank.getPhoneNo()));
                        itemView.getContext().startActivity(callIntent);
                    } else {
                        Toast.makeText(itemView.getContext(), "Phone Number can't available!", Toast.LENGTH_SHORT).show();
                    }
                }
            });


        }


    }
}
