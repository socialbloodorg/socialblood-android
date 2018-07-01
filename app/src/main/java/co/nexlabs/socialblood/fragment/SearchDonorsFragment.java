package co.nexlabs.socialblood.fragment;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import co.nexlabs.socialblood.R;
import co.nexlabs.socialblood.activity.BloodDonorListActivity;
import co.nexlabs.socialblood.adapter.BloodRhTypeAdapterDialog;
import co.nexlabs.socialblood.adapter.CustomListAdapterDialog;
import co.nexlabs.socialblood.adapter.LocationListRecyclerViewAdapter;
import co.nexlabs.socialblood.model.BloodDonor;
import co.nexlabs.socialblood.model.BloodItem;
import co.nexlabs.socialblood.model.BloodRhItem;
import co.nexlabs.socialblood.model.Location;
import co.nexlabs.socialblood.model.User;
import co.nexlabs.socialblood.rest.RestClient;
import co.nexlabs.socialblood.util.Constants;
import co.nexlabs.socialblood.util.DataUtils;
import co.nexlabs.socialblood.util.GPSTracker;
import co.nexlabs.socialblood.view.CircularTextView;
import co.nexlabs.socialblood.view.DividerItemDecoration;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by myozawoo on 3/12/16.
 */
public class SearchDonorsFragment extends Fragment {
    @BindView(R.id.tv_blood_type) CircularTextView mBloodType;
    @BindView(R.id.tv_location_list) TextView mLocationList;
    @BindView(R.id.btn_search) Button btnSearch;
    @BindView(R.id.loca_icon)
    ImageView locationIcon;
    String bloodTypeId ="4";
    String bloodRhTypeId = "1";
    boolean isBloodTypeView = true;
    String bloodType = "";
    CustomListAdapterDialog customListAdapterDialog;
    ProgressDialog dialog;
    String lat;
    String lon;

    List<Location> locationList;
    public SearchDonorsFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search_donors, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        dialog = new ProgressDialog(getActivity());
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setMessage("Loading...");

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

    @OnClick(R.id.tv_blood_type) void bloodChoice() {
        bloodChooseDialog();
    }

    @OnClick(R.id.tv_location_list) void showLocationList() {

        final LocationListDialogFragment locationListDialogFragment = LocationListDialogFragment.getInstance();
        locationListDialogFragment.show(getFragmentManager(), "location");
        locationListDialogFragment.setOnSelectListener(new LocationListDialogFragment.OnSelectListener() {
            @Override
            public void onSelect(Location location) {
                //  Toast.makeText(getActivity(), location.getName(), Toast.LENGTH_SHORT).show();
                mLocationList.setText(location.getName());
                lat = location.getLatitude() + "";
                lon = location.getLongitude() + "";
                locationListDialogFragment.dismiss();
            }
        });
    }

    @OnClick(R.id.loca_icon) void searchBLoodDonorTwo() {

            GPSTracker gpsTracker = new GPSTracker(getActivity());
            if (gpsTracker.canGetLocation()) {
                String latitude = gpsTracker.getLatitude() + "";
                String longitude = gpsTracker.getLongitude() + "";
                //showToast(lat+" "+lon);

                dialog.show();

                RestClient.getInstance().getService().searchBloodDonors(bloodTypeId, bloodRhTypeId, latitude, longitude, new Callback<List<BloodDonor>>() {
                    @Override
                    public void success(List<BloodDonor> bloodDonor, Response response) {
                        dialog.dismiss();
                        //Toast.makeText(getActivity(), "SUCCESS", Toast.LENGTH_SHORT).show();
                        //DataUtils.WriteObject(getActivity(), Constants.BLOOD_DONOR, bloodDonor);
                        if (bloodDonor == null) {
                            showToast("There's no result!");
                        } else {
                            Intent intent = new Intent(getActivity(), BloodDonorListActivity.class);

                            ArrayList<BloodDonor> donorArrayList = new ArrayList<BloodDonor>();
                            donorArrayList.addAll(bloodDonor);

                            intent.putExtra("donor", donorArrayList);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        dialog.dismiss();
                        error.printStackTrace();
                        //Toast.makeText(getActivity(), "SUCCESS", Toast.LENGTH_SHORT).show();
                    }
                });

        }

    }

    @OnClick(R.id.btn_search) void searchBloodDonor() {

        if (!mLocationList.getText().equals("Please select your location")) {

            dialog.show();

            RestClient.getInstance().getService().searchBloodDonors(bloodTypeId, bloodRhTypeId, lat, lon, new Callback<List<BloodDonor>>() {
                @Override
                public void success(List<BloodDonor> bloodDonor, Response response) {
                    dialog.dismiss();
                    //Toast.makeText(getActivity(), "SUCCESS", Toast.LENGTH_SHORT).show();
                    //DataUtils.WriteObject(getActivity(), Constants.BLOOD_DONOR, bloodDonor);
                    if (bloodDonor==null) {
                        showToast("There's no result!");
                    } else {
                        Intent intent = new Intent(getActivity(), BloodDonorListActivity.class);

                        ArrayList<BloodDonor> donorArrayList = new ArrayList<BloodDonor>();
                        donorArrayList.addAll(bloodDonor);

                        intent.putExtra("donor", donorArrayList);
                        startActivity(intent);
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    dialog.dismiss();
                    error.printStackTrace();
                    //Toast.makeText(getActivity(), "SUCCESS", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(getActivity(), "Please select one location!", Toast.LENGTH_LONG).show();
        }
    }


    private void bloodChooseDialog() {
        bloodTypeId = "";
        bloodRhTypeId = "";

        final ArrayList<BloodItem> bloodItems = new ArrayList<BloodItem>();
        bloodItems.add(new BloodItem(1, "O"));
        bloodItems.add(new BloodItem(2, "A"));
        bloodItems.add(new BloodItem(3, "B"));
        bloodItems.add(new BloodItem(4, "AB"));


        isBloodTypeView = true;

        final AppCompatDialog dialog = new AppCompatDialog(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_choose_blood_category, null);
        final TextView desc = (TextView) view.findViewById(R.id.tv_description);
        final ListView lv = (ListView) view.findViewById(R.id.blood_list);
        Button btnCancel = (Button) view.findViewById(R.id.btn_cancel);
        customListAdapterDialog = new CustomListAdapterDialog(getActivity(), bloodItems);
        lv.setAdapter(customListAdapterDialog);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                final ArrayList<BloodRhItem> bloodRhItem = new ArrayList<BloodRhItem>();
                bloodRhItem.add(new BloodRhItem(1, "Positive (+)"));
                bloodRhItem.add(new BloodRhItem(2, "Negative (-)"));
                bloodRhItem.add(new BloodRhItem(3, "None"));

                if (isBloodTypeView) {
                    //first array
                    if (!bloodType.equals("")) {
                        bloodType = "";
                        bloodTypeId = "";
                    }
                    isBloodTypeView = false;
                    bloodType += bloodItems.get(position).getBloodType() + "";
                    bloodTypeId += bloodItems.get(position).getBloodId() + "";
                    //showToast(bloodTypeId);
                    //Toast.makeText(getApplicationContext(), "First View"+bloodType, Toast.LENGTH_SHORT).show();

                } else {

                    //secondarray
                    String bloodRhType = bloodRhItem.get(position).getBloodRhId() + "";
                    //Toast.makeText(getApplicationContext(),"RH :"+ bloodRhType +" , B : "+ bloodType, Toast.LENGTH_SHORT).show();
                    bloodRhTypeId = bloodRhItem.get(position).getBloodRhId() + "";
                    //showToast(bloodRhTypeId);
                    String one = 1 + "";
                    String two = 2 + "";
                    String three = 3 + "";
                    if (bloodRhType.equals(one)) {
                        mBloodType.setText(bloodType + "+");
                        dialog.dismiss();
                    } else if (bloodRhType.equals(two)) {
                        mBloodType.setText(bloodType + "-");
                        dialog.dismiss();
                    } else if (bloodRhType.equals(three)) {
                        mBloodType.setText(bloodType);
                        dialog.dismiss();
                    }
                }


                BloodRhTypeAdapterDialog bloodRhTypeAdapterDialog = new BloodRhTypeAdapterDialog(getActivity(), bloodRhItem);
//                customListAdapterDialo.notifyDataSetChanged();
                lv.setAdapter(bloodRhTypeAdapterDialog);
                dialog.setTitle(getString(R.string.choose_blood_rh_type_title));
                desc.setText("Select a blood rh type from the list below");

            }
        });
        dialog.setTitle(getString(R.string.choose_blood_type_title));
        dialog.setContentView(view);
        dialog.show();

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_blood_need, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }


    private void showToast(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }




}
