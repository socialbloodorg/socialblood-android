package co.nexlabs.socialblood.adapter;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import co.nexlabs.socialblood.R;
import co.nexlabs.socialblood.model.BloodDonor;
import co.nexlabs.socialblood.model.BloodItem;
import co.nexlabs.socialblood.util.TextUtils;
import co.nexlabs.socialblood.view.CircularTextView;

/**
 * Created by myozawoo on 3/17/16.
 */
public class BloodDonorListAdapter extends RecyclerView.Adapter<BloodDonorListAdapter.ViewHolder> {

    List<BloodDonor> list = new ArrayList<BloodDonor>();

    public void setList(List<BloodDonor> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.nearby_list_row, null);
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
        ImageView mProfile;
        CircularTextView mBlood;
        TextView mName;
        TextView mAddress;
        TextView mDistance;
        ImageView mPhone;
        ImageView mMessage;

        BloodDonor donor;

        public ViewHolder(View itemView) {
            super(itemView);

            mProfile = (ImageView) itemView.findViewById(R.id.iv_profile);
            mBlood = (CircularTextView) itemView.findViewById(R.id.tv_blood_type);
            mName = (TextView) itemView.findViewById(R.id.tv_name);
            mAddress = (TextView) itemView.findViewById(R.id.tv_address);
            mDistance = (TextView) itemView.findViewById(R.id.tv_distance);
            mPhone = (ImageView) itemView.findViewById(R.id.iv_call);
            mMessage = (ImageView) itemView.findViewById(R.id.iv_sms);

        }

        public void bind(BloodDonor bloodDonor){
            this.donor = bloodDonor;

            mName.setText(donor.getName());
            mBlood.setText(TextUtils.convertBloodIdToBloodName(donor.getBloodTypeId(),
                    donor.getBloodRhTypeId()));
            mAddress.setText(donor.getCityName());

            String str = donor.getDistance()+"";
            String distance = "";
            if (str.length()>3) {
                for (int i = 0; i < 4; i++) {
                    distance += str.charAt(i);
                }
            }
            mDistance.setText(distance + " km");

            if (!donor.getProfilePhotoUrl().equals("")) {
                Picasso.with(itemView.getContext()).load(donor.getProfilePhotoUrl()).into(mProfile);
            }

            mPhone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (donor.getPhoneNo() != null) {
                        Intent callIntent = new Intent(Intent.ACTION_VIEW);
                        callIntent.setData(Uri.parse("tel:" + donor.getPhoneNo()));
                        itemView.getContext().startActivity(callIntent);
                    } else {
                        Toast.makeText(itemView.getContext(), "Phone Number can't available!", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            mMessage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (donor.getPhoneNo() != null) {
                        Intent smsIntent = new Intent(Intent.ACTION_VIEW);
                        smsIntent.setType("vnd.android-dir/mms-sms");
                        smsIntent.putExtra("address", "12125551212");
                        smsIntent.putExtra("sms_body", "");
                        itemView.getContext().startActivity(smsIntent);
                    } else {
                        Toast.makeText(itemView.getContext(), "Phone Number can't available!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }


    }
}
