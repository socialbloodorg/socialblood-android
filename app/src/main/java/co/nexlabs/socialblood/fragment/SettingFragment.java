package co.nexlabs.socialblood.fragment;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialog;
import android.text.InputType;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import co.nexlabs.socialblood.R;
import co.nexlabs.socialblood.activity.LoginActivity;
import co.nexlabs.socialblood.adapter.BloodRhTypeAdapterDialog;
import co.nexlabs.socialblood.adapter.CustomListAdapterDialog;
import co.nexlabs.socialblood.adapter.PlaceArrayAdapter;
import co.nexlabs.socialblood.model.BloodItem;
import co.nexlabs.socialblood.model.BloodRhItem;
import co.nexlabs.socialblood.model.Message;
import co.nexlabs.socialblood.model.User;
import co.nexlabs.socialblood.rest.RestClient;
import co.nexlabs.socialblood.util.Constants;
import co.nexlabs.socialblood.util.DataUtils;
import co.nexlabs.socialblood.util.Pref;
import co.nexlabs.socialblood.util.TextUtils;
import com.soundcloud.android.crop.Crop;
import com.squareup.picasso.Picasso;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import de.hdodenhof.circleimageview.CircleImageView;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by myozawoo on 3/18/16.
 */
public class SettingFragment extends Fragment implements DatePickerDialog.OnDateSetListener {

    private static final int MAX_YEAR = 1997;
    private static final int MAX_MONTH = 11; // starting count at 0
    private static final int MAX_DAY = 8;

    private int mYear;
    private int mMonth;
    private int mDay;

    @BindView(R.id.tv_blood_name) TextView mBlood;
    @BindView(R.id.iv_profile) CircleImageView mProfileImage;
    @BindView(R.id.tv_phone_number) TextView mPhoneNumber;
    @BindView(R.id.tv_name) TextView mName;
    @BindView(R.id.tv_email) TextView mEmail;
    @BindView(R.id.btn_logout) Button btnLogout;

    @BindView(R.id.tv_blood_type) TextView mBloodTypeChooser;
    @BindView(R.id.tv_phone) TextView mPhoneChooser;
    @BindView(R.id.tv_dob) TextView mBirthDatePicker;
    @BindView(R.id.tv_location) TextView mLocationPicker;
    @BindView(R.id.tv_gender) TextView mGenderChooser;
    private PlaceArrayAdapter mPlaceArrayAdapter;

    boolean isBloodTypeView = true;
    String bloodType = "";
    String bloodTypeId;
    String bloodRhTypeId;
    String profilePhotoBase64 = "";
    CustomListAdapterDialog customListAdapterDialog;

    String[] camera_source = new String[] { "Camera", "Gallery" };
    String imageName = String.valueOf(Calendar.getInstance().getTimeInMillis()) + ".png";

    ProgressDialog mDialog;
    User user;

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    Unbinder unbinder;

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_setting, container, false);
        unbinder = ButterKnife.bind(this, rootView);

        mDialog = new ProgressDialog(getActivity());
        mDialog.setMessage("Profile updating...");
        mDialog.setCancelable(false);
        mDialog.setCanceledOnTouchOutside(false);

        user = (User) DataUtils.ReadArraylist(getActivity(), Constants.USER);
        if (!user.getProfilePhotoUrl().equals("")) {
            Picasso.with(getActivity()).load(user.getProfilePhotoUrl()).into(mProfileImage);
        }
        mName.setText(user.getName());
        mPhoneNumber.setText(user.getPhoneNo());
        mEmail.setText(user.getEmail());
        try {
            mBlood.setText(
                TextUtils.convertBloodIdToBloodName(user.getBloodTypeId(), user.getBloodRhTypeId()));
        } catch (NullPointerException exception) {
            // Do nothing for now.
        }
        return rootView;
    }

    @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == getActivity().RESULT_OK) {

            if (requestCode == 0) {
                beginCrop(Uri.fromFile(new File("sdcard/captured")));
                return;
            }

            if (requestCode == Crop.REQUEST_PICK) {
                beginCrop(data.getData());
                return;
            }
            if (requestCode == Crop.REQUEST_CROP) {
                profilePhotoBase64 = handleCrop(resultCode, data);
                return;
            }
        }
    }

    //logout
    @OnClick(R.id.btn_logout) void logout() {
        final ProgressDialog logoutDialog = new ProgressDialog(getActivity());
        logoutDialog.setMessage("Please Wait!");
        logoutDialog.setCancelable(false);
        logoutDialog.setCanceledOnTouchOutside(false);
        logoutDialog.show();
        RestClient.getInstance().getService().logout(user.getAuthToken(), new Callback<Message>() {
            @Override public void success(Message message, Response response) {
                logoutDialog.dismiss();

                if (DataUtils.ReadObject(getActivity(), Constants.USER) != null) {
                    DataUtils.DeleteObject(getActivity(), Constants.USER);
                }

                if (DataUtils.ReadObject(getActivity(), Constants.BLOOD_DONOR) != null) {
                    DataUtils.DeleteObject(getActivity(), Constants.BLOOD_DONOR);
                }

                if (DataUtils.ReadObject(getActivity(), Constants.LOCATION_LIST) != null) {
                    DataUtils.DeleteObject(getActivity(), Constants.LOCATION_LIST);
                }

                if (DataUtils.ReadObject(getActivity(), Constants.LOCATION) != null) {
                    DataUtils.DeleteObject(getActivity(), Constants.LOCATION);
                }

                Pref.getInstance(getActivity().getBaseContext()).clearAll();

                Intent intent = new Intent(getActivity(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }

            @Override public void failure(RetrofitError error) {
                logoutDialog.dismiss();
                if (error.getResponse().getStatus() == 400) {
                    Message message = (Message) error.getBodyAs(Message.class);
                    //showErrorDialog("Sign in  Fail", message.message);
                }
            }
        });
    }

    //Blood Type Chooser
    @OnClick(R.id.tv_blood_type) void setBlood() {

        final ArrayList<BloodItem> bloodItems = new ArrayList<BloodItem>();
        bloodItems.add(new BloodItem(1, "O"));
        bloodItems.add(new BloodItem(2, "A"));
        bloodItems.add(new BloodItem(3, "B"));
        bloodItems.add(new BloodItem(4, "AB"));

        isBloodTypeView = true;

        final AppCompatDialog dialog = new AppCompatDialog(getActivity());
        View view =
            getActivity().getLayoutInflater().inflate(R.layout.dialog_choose_blood_category, null);
        final TextView desc = (TextView) view.findViewById(R.id.tv_description);
        final ListView lv = (ListView) view.findViewById(R.id.blood_list);
        Button btnCancel = (Button) view.findViewById(R.id.btn_cancel);
        customListAdapterDialog = new CustomListAdapterDialog(getActivity(), bloodItems);
        lv.setAdapter(customListAdapterDialog);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                final ArrayList<BloodRhItem> bloodRhItem = new ArrayList<BloodRhItem>();
                bloodRhItem.add(new BloodRhItem(1, "Positive (+)"));
                bloodRhItem.add(new BloodRhItem(2, "Negative (-)"));
                bloodRhItem.add(new BloodRhItem(3, "None"));

                bloodTypeId = bloodItems.get(position).getBloodId() + "";
                //showToast(bloodItems.get(position).getBloodType());

                BloodRhTypeAdapterDialog bloodRhTypeAdapterDialog =
                    new BloodRhTypeAdapterDialog(getActivity(), bloodRhItem);
                //                customListAdapterDialo.notifyDataSetChanged();
                lv.setAdapter(bloodRhTypeAdapterDialog);
                dialog.setTitle(getString(R.string.choose_blood_rh_type_title));
                desc.setText("Select a blood rh type from the list below");
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        bloodRhTypeId = bloodRhItem.get(position).getBloodRhId() + "";
                        //showToast(bloodRhItem.get(position).getBloodRhType());
                        dialog.dismiss();
                        mDialog.show();
                        RestClient.getInstance()
                            .getService()
                            .updateBlood(user.getId(), user.getAuthToken(), bloodTypeId, bloodRhTypeId,
                                new Callback<User>() {
                                    @Override public void success(User user, Response response) {
                                        mDialog.dismiss();
                                        mBlood.setText(
                                            TextUtils.convertBloodIdToBloodName(user.getBloodTypeId(),
                                                user.getBloodRhTypeId()));

                                        DataUtils.DeleteObject(getActivity().getApplicationContext(),
                                            Constants.USER);
                                        DataUtils.WriteObject(getActivity().getApplicationContext(),
                                            Constants.USER, user);

                                        showToast("Blood type updated!");
                                    }

                                    @Override public void failure(RetrofitError error) {
                                        mDialog.dismiss();
                                        if (error.getResponse().getStatus() == 400) {
                                            Message message = (Message) error.getBodyAs(Message.class);
                                            //showErrorDialog("Sign in  Fail", message.message);
                                        }
                                    }
                                });
                    }
                });
            }
        });
        dialog.setTitle(getString(R.string.choose_blood_type_title));
        dialog.setContentView(view);
        dialog.show();

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    //Profie Image Changer
    @OnClick(R.id.iv_profile) void setProfileImage() {
        new AlertDialog.Builder(getActivity()).setTitle("Select Source")
            .setItems(camera_source, new DialogInterface.OnClickListener() {
                @Override public void onClick(final DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();

                    if (i == 0) {
                        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if (takePictureIntent.resolveActivity(getActivity().getPackageManager())
                            != null) {
                            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                                Uri.fromFile(new File("sdcard/captured")));
                            startActivityForResult(takePictureIntent, 0);
                        }
                    }

                    if (i == 1) {
                        Crop.pickImage(getContext(), SettingFragment.this);
                    }
                }
            })
            .show();
    }

    //Phone Number Changer
    @OnClick(R.id.tv_phone) void setPhone() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setMessage("Enter your phone");
        final EditText inputPhone = new EditText(getActivity());
        inputPhone.setPadding(20, 20, 20, 20);
        inputPhone.setInputType(InputType.TYPE_CLASS_PHONE);
        inputPhone.setHint(mPhoneNumber.getText());
        dialog.setView(inputPhone);
        dialog.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override public void onClick(final DialogInterface dialog, int which) {
                //insert rest service for update phone here
                dialog.dismiss();
                mDialog.show();
                RestClient.getInstance()
                    .getService()
                    .updatePhoneNumber(user.getId(), user.getAuthToken(), inputPhone.getText().toString(),
                        new Callback<User>() {
                            @Override public void success(User user, Response response) {
                                mDialog.dismiss();
                                DataUtils.DeleteObject(getActivity().getApplicationContext(),
                                    Constants.USER);
                                DataUtils.WriteObject(getActivity().getApplicationContext(), Constants.USER,
                                    user);
                                mPhoneNumber.setText(user.getPhoneNo());
                                showToast("Phone number updated!");
                            }

                            @Override public void failure(RetrofitError error) {
                                mDialog.dismiss();
                                if (error.getResponse().getStatus() == 400) {
                                    Message message = (Message) error.getBodyAs(Message.class);
                                    //showErrorDialog("Sign in  Fail", message.message);
                                }
                            }
                        });
            }
        });
        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    //Birth Date Picker
    @OnClick(R.id.tv_dob) void setBirthDate() {
        showCalendar();
    }

    //Location Picker
    @OnClick(R.id.tv_location) void setLocation() {

        final UserAddressUpdateDialogFragment userUpdateDialogFragment =
            UserAddressUpdateDialogFragment.getInstance();
        userUpdateDialogFragment.show(getFragmentManager(), "location");
        userUpdateDialogFragment.setOnSelectListener(
            new UserAddressUpdateDialogFragment.OnSelectListener() {

                @Override public void onSelect(String locationName) {
                    mDialog.show();
                    RestClient.getInstance()
                        .getService()
                        .updateAddress(user.getId(), user.getAuthToken(), locationName,
                            new Callback<User>() {
                                @Override public void success(User user, Response response) {
                                    mDialog.dismiss();
                                    showToast("Address updated!");
                                }

                                @Override public void failure(RetrofitError error) {
                                    mDialog.dismiss();
                                    if (error.getResponse().getStatus() == 400) {
                                        Message message = (Message) error.getBodyAs(Message.class);
                                        //showErrorDialog("Sign in  Fail", message.message);
                                    }
                                }
                            });
                }
            });
    }

    //Gender Chooser
    @OnClick(R.id.tv_gender) void setGender() {
        final ArrayList<String> genders = new ArrayList<String>();
        genders.add("Male");
        genders.add("Female");
        AlertDialog.Builder b = new AlertDialog.Builder(getActivity());
        b.setTitle("Gender");
        b.setSingleChoiceItems(genders.toArray(new String[genders.size()]), 0,
            new DialogInterface.OnClickListener() {
                @Override public void onClick(DialogInterface dialog, int which) {
                    String gender = genders.get(which);
                    dialog.dismiss();
                    mDialog.show();
                    RestClient.getInstance()
                        .getService()
                        .updateGender(user.getId(), user.getAuthToken(), gender, new Callback<User>() {
                            @Override public void success(User user, Response response) {
                                mDialog.dismiss();
                                showToast("Gender updated!");
                            }

                            @Override public void failure(RetrofitError error) {
                                mDialog.dismiss();
                                if (error.getResponse().getStatus() == 400) {
                                    Message message = (Message) error.getBodyAs(Message.class);
                                    //showErrorDialog("Sign in  Fail", message.message);
                                }
                            }
                        });
                }
            });
        b.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        b.show();
    }

    //Gender Chooser
    @OnClick(R.id.tv_pet) void setPet() {
        final ArrayList<String> pets = new ArrayList<String>();
        pets.add("Dog");
        pets.add("Cat");
        pets.add("Both");
        pets.add("None");

        AlertDialog.Builder b = new AlertDialog.Builder(getActivity());
        b.setTitle("Pet");
        b.setSingleChoiceItems(pets.toArray(new String[pets.size()]), 0,
            new DialogInterface.OnClickListener() {
                @Override public void onClick(DialogInterface dialog, int which) {
                    String pet = pets.get(which);
                    String has_pet;
                    if (pet.equals("None")) {
                        has_pet = "false";
                    } else {
                        has_pet = "true";
                    }
                    dialog.dismiss();

                    mDialog.show();

                    RestClient.getInstance()
                        .getService()
                        .updatePet(user.getId(), user.getAuthToken(), has_pet, pet, new Callback<User>() {
                            @Override public void success(User user, Response response) {
                                mDialog.dismiss();
                                showToast("Pet updated!");
                            }

                            @Override public void failure(RetrofitError error) {
                                mDialog.dismiss();
                                if (error.getResponse().getStatus() == 400) {
                                    Message message = (Message) error.getBodyAs(Message.class);
                                    //showErrorDialog("Sign in  Fail", message.message);
                                }
                            }
                        });
                }
            });
        b.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        b.show();
    }

    private String formatDateString() {
        return mYear + "/" + ((mMonth < 10) ? "0" + mMonth : mMonth) + "/" + ((mDay < 10) ? "0" + mDay
            : mDay);
    }

    private void showCalendar() {
        com.wdullaer.materialdatetimepicker.date.DatePickerDialog mDatePicker =
            com.wdullaer.materialdatetimepicker.date.DatePickerDialog.newInstance(this, mYear,
                mMonth - 1, mDay);
        //mDatePicker.setAccentColor(R.color.check_voter_list);
        Calendar maxDate = Calendar.getInstance();
        maxDate.set(MAX_YEAR, MAX_MONTH - 1, MAX_DAY);
        mDatePicker.setMaxDate(maxDate);
        mDatePicker.show(getActivity().getFragmentManager(), "Calendar");
    }

    @Override
    public void onDateSet(com.wdullaer.materialdatetimepicker.date.DatePickerDialog datePickerDialog,
        int year, int month, int date) {
        mYear = year;
        mMonth = month + 1;
        mDay = date;
        //showToast(formatDateString());
        String dob = formatDateString();
        //mDob.setText(TextUtils.convertToMM(formatDateString()));
        mDialog.show();
        RestClient.getInstance()
            .getService()
            .updateDateOfBirth(user.getId(), user.getAuthToken(), dob, new Callback<User>() {
                @Override public void success(User user, Response response) {
                    mDialog.dismiss();
                    showToast("Birth date updated!");
                }

                @Override public void failure(RetrofitError error) {
                    mDialog.dismiss();
                    if (error.getResponse().getStatus() == 400) {
                        Message message = (Message) error.getBodyAs(Message.class);
                        //showErrorDialog("Sign in  Fail", message.message);
                    }
                }
            });
    }

    public void beginCrop(Uri source) {
        Uri outputUri = Uri.fromFile(new File(getActivity().getCacheDir(), imageName));
        Crop.of(source, outputUri)
            .withAspect(1, 1)
            .withMaxSize(256, 256)
            .start(getContext(), SettingFragment.this);
    }

    public String getRealPathFromURI(Uri contentUri) {
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            Cursor cursor = getActivity().managedQuery(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } catch (Exception e) {
            return contentUri.getPath();
        }
    }

    public String handleCrop(int resultCode, Intent result) {
        String selectedImagePath = getRealPathFromURI(Crop.getOutput(result));
        Bitmap img = BitmapFactory.decodeFile(selectedImagePath);
        mProfileImage.setImageBitmap(img);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        img.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();

        String strPhoto = "data:image/jpeg;base64," + Base64.encodeToString(b, Base64.DEFAULT);

        mDialog.show();
        RestClient.getInstance()
            .getService()
            .updateProfilePhoto(user.getId(), user.getAuthToken(), strPhoto, new Callback<User>() {
                @Override public void success(User user, Response response) {
                    mDialog.dismiss();
                    showToast("Profile photo updated!");
                    Picasso.with(getActivity()).load(user.getProfilePhotoUrl()).into(mProfileImage);
                }

                @Override public void failure(RetrofitError error) {
                    mDialog.dismiss();
                    showToast("Upload fail!");
                    if (error.getResponse().getStatus() == 400) {
                        Message message = (Message) error.getBodyAs(Message.class);
                        //showErrorDialog("Sign in  Fail", message.message);
                    }
                }
            });

        return strPhoto;
    }

    void showToast(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }
}



