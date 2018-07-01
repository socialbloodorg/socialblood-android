package co.nexlabs.socialblood.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDialog;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.Unbinder;
import co.nexlabs.socialblood.util.Pref;
import com.apkfuns.logutils.LogUtils;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Places;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.nexlabs.socialblood.R;
import co.nexlabs.socialblood.adapter.BloodRhTypeAdapterDialog;
import co.nexlabs.socialblood.adapter.CustomListAdapterDialog;
import co.nexlabs.socialblood.model.BloodItem;
import co.nexlabs.socialblood.model.BloodRhItem;
import co.nexlabs.socialblood.model.Message;
import co.nexlabs.socialblood.model.User;
import co.nexlabs.socialblood.model.UserLocation;
import co.nexlabs.socialblood.rest.RestClient;
import co.nexlabs.socialblood.util.Constants;
import co.nexlabs.socialblood.util.DataUtils;
import co.nexlabs.socialblood.view.CircularTextView;
import pl.tajchert.nammu.Nammu;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by myozawoo on 3/15/16.
 */
public class FinalRegistrationActivity extends AppCompatActivity {

    @BindView(R.id.tv_location)
    TextView mLocation;
    @BindView(R.id.btn_done)
    Button btnDone;
    @BindView(R.id.ed_phone)
    EditText mPhone;
    @BindView(R.id.ed_phone_layout)
    TextInputLayout inputPhone;
    @BindView(R.id.tv_blood_type)
    CircularTextView blood;
    @BindView(R.id.ed_about)
    EditText mAbout;
    @BindView(R.id.ed_about_layout)
    TextInputLayout inputAbout;
    @BindView(R.id.sp_pet)
    Spinner mPetSpinner;
    String mPet = "";
    String has_pet="";
    TelephonyManager mTelephonyManager;
    CustomListAdapterDialog customListAdapterDialog;
    boolean isBloodTypeView = true;
    String bloodType = "";
    UserLocation mUserLocation;
    ProgressDialog dialog;
    private static final int PERMISSION_REQUEST_READ_SMS = 2;

    String bloodTypeId = "4";
    String bloodRhTypeId = "1";
    String email = "";
    String password = "";
    String name = "";
    String status = "";
    User user;
    Unbinder unbinder;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_registration);
        unbinder = ButterKnife.bind(this);
        dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setMessage("Loading...");
        addPetsToSpinner();
        //showToast(blood.getText().toString());
        if(DataUtils.ReadObject(FinalRegistrationActivity.this, Constants.LOCATION) instanceof UserLocation) {
            mUserLocation = (UserLocation) DataUtils.ReadObject(FinalRegistrationActivity.this, Constants.LOCATION);
            mLocation.setText(mUserLocation.getAddress());
        }

        name = getFromExtra("name");
        email = getFromExtra("email");
        password = getFromExtra("password");
        status = getFromExtra("status");

        mPetSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mPet = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        if (!mPet.equals("None")) {
            has_pet = "true";
        } else {
            has_pet = "false";
        }

        showToast(status);

    }

    private void getPhoneNumber() {
        mTelephonyManager = (TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);
        String phoneNumber = mTelephonyManager.getLine1Number();
        if (!phoneNumber.isEmpty()) {
            mPhone.setText(phoneNumber.trim());
        }
    }

    public String getFromExtra(String name) {
        if (getIntent().hasExtra(name))
            return getIntent().getExtras().getString(name);
        else
            return "";
    }

    @OnClick(R.id.btn_done)
    void done() {

        //showToast("Blood ID: " + bloodTypeId + ", Blood RH ID: " + bloodRhTypeId + ", Blood: " + blood.getText());
        //showToast(status);
        String phone = mPhone.getText().toString();
        String location = mLocation.getText().toString();
        String latitude = mUserLocation != null ? mUserLocation.getLat().toString() : "-1";
        String longitude = mUserLocation != null ? mUserLocation.getLon().toString() : "-1";
        String about = mAbout.getText().toString();

        //showToast(name + " " + email + " " + password + " " + latitude + " " + longitude);
        //showToast(bloodRhTypeId + " " + bloodRhTypeId + " " + about + " " + phone);

        if (validatePhone()) {
            if (status.equals("signup")) {
                dialog.show();
                RestClient.getInstance().getService().signUp(email, password, password, name, phone, bloodTypeId,
                        bloodRhTypeId, location, latitude, longitude, about, has_pet, mPet, new Callback<User>() {
                            @Override
                            public void success(User user, Response response) {
                                //showToast("SUCCESS");
                                DataUtils.WriteObject(FinalRegistrationActivity.this, Constants.USER, user);

                                Intent main = new Intent(FinalRegistrationActivity.this, MainActivity.class);
                                main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(main);
                            }

                            @Override
                            public void failure(RetrofitError error) {
                                error.printStackTrace();
                                //showToast("FAIL");
                            }
                        });
            } else if (status.equals("facebook")) {
                dialog.show();
                //showToast("facebook");
                user = (User) DataUtils.ReadObject(FinalRegistrationActivity.this, Constants.USER);
                RestClient.getInstance().getService().updateProfileForFacebookLogin(user.getId(), user.getAuthToken(), user.getName(),
                        phone, bloodTypeId, bloodRhTypeId, location, latitude, longitude, about, has_pet, mPet, new Callback<User>() {
                            @Override
                            public void success(User user, Response response) {
                                dialog.dismiss();
                                //showToast("SUCCESS");
                                LogUtils.i("Response" + user + response.getStatus());
                                DataUtils.DeleteObject(FinalRegistrationActivity.this, Constants.USER);
                                DataUtils.WriteObject(FinalRegistrationActivity.this, Constants.USER, user);
                                Pref.getInstance(getBaseContext()).setAuthToken(user.getAuthToken());
                                Intent main = new Intent(FinalRegistrationActivity.this, MainActivity.class);
                                main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(main);

                            }

                            @Override
                            public void failure(RetrofitError error) {
                                //showToast("Fail");

                                if (error.getKind() == RetrofitError.Kind.NETWORK) {
                                    showErrorDialog("Network Error", "Please check your network!");
                                }

                                if (error.getResponse().getStatus() == 400) {
                                    Message message = (Message) error.getBodyAs(Message.class);
                                    showErrorDialog("Sign in  Fail", message.message);
                                }

                            }
                        });
            }
        }
    }


    private boolean isValid(){

        mPhone.setError(null);

        boolean valid = true;

        if(mPhone.getText().toString().trim().isEmpty()){
            mPhone.setError(getString(R.string.err_msg_phone));
            valid = false;
        }

        return valid;
    }


    @OnClick(R.id.tv_blood_type)
    void bloodChooseDialog() {

        bloodTypeId = "";
        bloodRhTypeId = "";

        final ArrayList<BloodItem> bloodItems = new ArrayList<BloodItem>();
        bloodItems.add(new BloodItem(1, "O"));
        bloodItems.add(new BloodItem(2, "A"));
        bloodItems.add(new BloodItem(3, "B"));
        bloodItems.add(new BloodItem(4, "AB"));


        isBloodTypeView = true;

        final AppCompatDialog dialog = new AppCompatDialog(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_choose_blood_category, null);
        final TextView desc = (TextView) view.findViewById(R.id.tv_description);
        final ListView lv = (ListView) view.findViewById(R.id.blood_list);
        Button btnCancel = (Button) view.findViewById(R.id.btn_cancel);
        customListAdapterDialog = new CustomListAdapterDialog(this, bloodItems);
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
                        blood.setText(bloodType + "+");
                        dialog.dismiss();
                    } else if (bloodRhType.equals(two)) {
                        blood.setText(bloodType + "-");
                        dialog.dismiss();
                    } else if (bloodRhType.equals(three)) {
                        blood.setText(bloodType);
                        dialog.dismiss();
                    }
                }


                BloodRhTypeAdapterDialog bloodRhTypeAdapterDialog = new BloodRhTypeAdapterDialog(FinalRegistrationActivity.this, bloodRhItem);
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

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }


    private boolean validateAbout() {
        if (mAbout.getText().toString().trim().isEmpty()) {
            inputAbout.setError(getString(R.string.err_msg_name));
            requestFocus(mAbout);
            return false;
        } else {
            inputAbout.setErrorEnabled(true);
        }
        return true;
    }

    private boolean validatePhone() {
        if (mPhone.getText().toString().trim().isEmpty()) {
            inputPhone.setError(getString(R.string.err_msg_phone));
            requestFocus(mPhone);
            return false;
        } else {
            inputPhone.setErrorEnabled(true);
        }
        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    void showErrorDialog(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }
    protected void addPetsToSpinner() {
        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("None");
        categories.add("Dog");
        categories.add("Cat");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mPetSpinner.setAdapter(dataAdapter);


    }



}
