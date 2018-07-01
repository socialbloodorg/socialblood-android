package co.nexlabs.socialblood.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import co.nexlabs.socialblood.R;
import co.nexlabs.socialblood.model.User;
import co.nexlabs.socialblood.util.Constants;
import co.nexlabs.socialblood.util.DataUtils;
import co.nexlabs.socialblood.util.DateUtils;
import co.nexlabs.socialblood.util.TextUtils;
import co.nexlabs.socialblood.view.CircularTextView;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by myozawoo on 3/12/16.
 */
public class ProfileFragment extends Fragment {

    @BindView(R.id.tv_username) TextView username;
    @BindView(R.id.tv_age) TextView age;
    @BindView(R.id.tv_phone) TextView phone;
    @BindView(R.id.iv_profile) CircleImageView profileImage;
    @BindView(R.id.tv_profile_blood) CircularTextView mBlood;
    @BindView(R.id.tv_address) TextView address;

    public ProfileFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        unbinder = ButterKnife.bind(this, rootView);

        if (DataUtils.ReadObject(getActivity(), Constants.USER)!= null) {

            User user = (User) DataUtils.ReadObject(getActivity(), Constants.USER);
            if (!user.getName().equals("")) {
                username.setText(user.getName());
            }
            if (!user.getBirth_date().equals("")) {
                age.setText(DateUtils.getAge(user.getBirth_date()));
            }

            if (!user.getProfilePhotoUrl().equals("")) {
                Picasso.with(getActivity()).load(user.getProfilePhotoUrl()).into(profileImage);
            }
            if (!user.getPhoneNo().equals("")) {
                phone.setText(user.getPhoneNo());
            }

            if (user.getBloodTypeId()!= 0 && user.getBloodRhTypeId() != 0) {
                String bloodName = TextUtils.convertBloodIdToBloodName(user.getBloodTypeId(), user.getBloodRhTypeId());
                mBlood.setText(bloodName);
            }

            if (!user.getAddress().equals("")) {
                address.setText(user.getAddress());
            }


        }

        return rootView;
    }

    Unbinder unbinder;

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


}
