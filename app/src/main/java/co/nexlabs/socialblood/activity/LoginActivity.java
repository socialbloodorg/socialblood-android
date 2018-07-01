package co.nexlabs.socialblood.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.Unbinder;
import co.nexlabs.socialblood.util.Pref;
import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.ndk.CrashlyticsNdk;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceDetectionApi;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;

import com.apkfuns.logutils.LogUtils;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.phillipcalvin.iconbutton.IconButton;

import io.fabric.sdk.android.Fabric;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.nexlabs.socialblood.R;
import co.nexlabs.socialblood.model.Message;
import co.nexlabs.socialblood.model.User;
import co.nexlabs.socialblood.model.UserLocation;
import co.nexlabs.socialblood.rest.RestClient;
import co.nexlabs.socialblood.util.Constants;
import co.nexlabs.socialblood.util.DataUtils;
import co.nexlabs.socialblood.util.DateUtils;
import co.nexlabs.socialblood.util.GPSTracker;
import co.nexlabs.socialblood.util.SystemUtils;
import pl.tajchert.nammu.Nammu;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by myozawoo on 3/11/16.
 */
public class LoginActivity extends AppCompatActivity {

    private GoogleApiClient mGoogleApiClient;
    private  UserLocation userLocation;
    private static final int PERMISSION_REQUEST_FINE_LOCATION = 1;

    @BindView(R.id.info)
    TextView info;

    @BindView(R.id.txt_email_signup)
    TextView signup;

    @BindView(R.id.btn_fb_login)
    IconButton btnFBLogin;

    @BindView(R.id.btn_sign_in)
    Button btnSignIn;

    @BindView(R.id.ed_email) EditText inputEmail;
    @BindView(R.id.ed_password) EditText inputPassword;
    @BindView(R.id.ed_email_layout) TextInputLayout inputLayoutEmail;
    @BindView(R.id.ed_password_layout) TextInputLayout inputLayoutPassword;
    ProgressDialog dialog;

    private CallbackManager callbackManager;
    private User loginUser;
    String name;
    String email;

    Unbinder unbinder;

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics(), new CrashlyticsNdk());
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);
        unbinder = ButterKnife.bind(this);

        if (!Pref.getInstance(getBaseContext()).getAuthToken().equals("")) {
            Intent intent = new Intent(getBaseContext(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }


        mGoogleApiClient = new GoogleApiClient
                .Builder(LoginActivity.this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .build();


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            checkPermission();
        } else {
            getCurrentPlace();
        }

        callbackManager = CallbackManager.Factory.create();

        dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setMessage("Loading...");

        //showToast("LOGIN");

        LoginManager.getInstance()
                .registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(final LoginResult loginResult) {
                        dialog.show();
                        GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject jsonObject, GraphResponse response) {
                                try {
                                    String id = jsonObject.getString("id");
                                    String access_token = loginResult.getAccessToken().getToken();

                                    name = jsonObject.getString("name");
                                    email = jsonObject.getString("email");
                                    String first_name = jsonObject.getString("first_name");
                                    String last_name = jsonObject.getString("last_name");
                                    String gender =
                                            (jsonObject.getString("gender").equalsIgnoreCase("male")) ? "1" : "2";
                                    String picture = jsonObject.getJSONObject("picture")
                                            .getJSONObject("data")
                                            .getString("url");

                                    RestClient.getInstance().getService()
                                            .fbLogin(access_token, new Callback<User>() {
                                                @Override
                                                public void success(User user, Response response) {
                                                    dialog.dismiss();
                                                    DataUtils.WriteObject(LoginActivity.this, Constants.USER, user);
                                                    Pref.getInstance(getBaseContext()).setAuthToken(user.getAuthToken());

                                                    if (user.getBloodTypeId()==0 && user.getBloodRhTypeId()==0){
                                                        Intent intent = new Intent(getBaseContext(), FinalRegistrationActivity.class);
                                                        intent.putExtra("status", "facebook");
                                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                        startActivity(intent);
                                                    } else {
                                                        Intent intent = new Intent(getBaseContext(), MainActivity.class);
                                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                        startActivity(intent);
                                                        finish();
                                                    }


                                                }

                                                @Override
                                                public void failure(RetrofitError error) {

                                                }
                                            });



                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        });
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id,name,email,first_name,last_name,gender,picture");
                        request.setParameters(parameters);
                        request.executeAsync();
                    }

                    @Override
                    public void onCancel() {

                    }

                    @Override
                    public void onError(FacebookException error) {

                    }
                });
        showHashKey();



    }

    @OnClick(R.id.btn_fb_login) void facebook() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            DataUtils.DeleteObject(LoginActivity.this, Constants.LOCATION);
            getCurrentPlace();
        }
        LoginManager.getInstance()
                .logInWithReadPermissions(this, Arrays.asList("public_profile", "email"));
    }

    @OnClick(R.id.btn_sign_in) void signIn() {

        //getCurrentPlace();

        String email = inputEmail.getText().toString().trim();
        String password = inputPassword.getText().toString().trim();
        if (SystemUtils.isNetworkConnected(this) && validateEmail() && validatePassword()) {
            dialog.show();
            RestClient.getInstance().getService().login(email, password, new Callback<User>() {
                @Override
                public void success(User user, Response response) {
                    dialog.dismiss();
                    Pref.getInstance(getBaseContext()).setAuthToken(user.getAuthToken());
                    if (DataUtils.ReadObject(LoginActivity.this, Constants.USER) != null) {
                        DataUtils.DeleteObject(LoginActivity.this, Constants.USER);
                        DataUtils.WriteObject(LoginActivity.this, Constants.USER, user);
                        Intent intent = new Intent(getBaseContext(), MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    } else {
                        DataUtils.WriteObject(LoginActivity.this, Constants.USER, user);
                        Intent intent = new Intent(getBaseContext(), MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }

                }

                @Override
                public void failure(RetrofitError error) {
                    dialog.dismiss();

                    if (error.getKind() == RetrofitError.Kind.NETWORK) {
                      //showErrorDialog("Network Error", "Please check your network!");
                    }

                    if (error.getResponse().getStatus() == 400) {
                        Message message = (Message) error.getBodyAs(Message.class);
                        //showErrorDialog("Sign in  Fail", message.message);
                    }
                }
            });



        } else {
            //Toast.makeText(this, "Please input correct email and password!", Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.txt_email_signup) void signUp() {
        if (SystemUtils.isNetworkConnected(this)) {
            getCurrentPlace();
            startActivity(new Intent(getBaseContext(), SignUpActivity.class));
        } else {
            Toast.makeText(this, "Internet connection problem!", Toast.LENGTH_SHORT).show();
        }
    }

    @TargetApi(Build.VERSION_CODES.M) void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("This app needs location access");
                builder.setMessage("Please grant location access so this app can detect beacons.");
                builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(LoginActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                PERMISSION_REQUEST_FINE_LOCATION);
                        getCurrentPlace();
                    }
                });
                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                builder.show();
            }
        }
    }


    @Override public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                                     @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_FINE_LOCATION: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //L.i("coarse location permission granted");
                    getCurrentPlace();
                } else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Functionality limited");
                    builder.setMessage(
                            "Since location access has not been granted, this app will not be able to discover beacons when in the background.");
                    builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });
                    builder.show();
                }
                return;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Logs 'install' and 'app activate' App Events.
        AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Logs 'app deactivate' App Event.
        AppEventsLogger.deactivateApp(this);
    }

    public  void showHashKey() {
        try {
            PackageInfo info = this.getPackageManager().getPackageInfo("co.nexlabs.socialblood",
                    PackageManager.GET_SIGNATURES);
            for (android.content.pm.Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());

                String sign=Base64.encodeToString(md.digest(), Base64.DEFAULT);
                Log.e("KeyHash:", sign);
                //  Toast.makeText(getApplicationContext(),sign,     Toast.LENGTH_LONG).show();
            }
            Log.d("KeyHash:", "****------------***");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
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

    @Override
    protected void onStart() {
        super.onStart();
        if( mGoogleApiClient != null )
            mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        if( mGoogleApiClient != null && mGoogleApiClient.isConnected() ) {
            mGoogleApiClient.disconnect();
        }
        super.onStop();
    }


    private void getCurrentPlace() {
        userLocation = new UserLocation();
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);

        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            //Execute location service call if user has explicitly granted ACCESS_FINE_LOCATION..
            final PendingResult<PlaceLikelihoodBuffer> result = Places.PlaceDetectionApi
                    .getCurrentPlace(mGoogleApiClient, null);
            result.setResultCallback(new ResultCallback<PlaceLikelihoodBuffer>() {
                @Override
                public void onResult(PlaceLikelihoodBuffer likelyPlaces) {
                    for (PlaceLikelihood placeLikelihood : likelyPlaces) {
                        Log.i("Places", String.format("Place '%s' has likelihood: %g",
                                placeLikelihood.getPlace().getName(),
                                placeLikelihood.getLikelihood()));

                        Double lat = likelyPlaces.get(0).getPlace().getLatLng().latitude;
                        Double lon = likelyPlaces.get(0).getPlace().getLatLng().longitude;
                        String location = likelyPlaces.get(0).getPlace().getAddress().toString();
                        String name = likelyPlaces.get(0).getPlace().getName().toString();
                        Log.i("LatLng : ", likelyPlaces.get(0).getPlace().getLatLng().toString());

                        userLocation.setAddress(location);
                        userLocation.setLon(lon);
                        userLocation.setLat(lat);
                        userLocation.setName(name);

                    }
                    likelyPlaces.release();

                    if (DataUtils.ReadObject(LoginActivity.this, Constants.LOCATION) != null) {
                        DataUtils.DeleteObject(LoginActivity.this, Constants.LOCATION);
                        DataUtils.WriteObject(LoginActivity.this, Constants.LOCATION, userLocation);
                    } else {
                        DataUtils.WriteObject(LoginActivity.this, Constants.LOCATION, userLocation);
                    }

                    if (DataUtils.ReadObject(LoginActivity.this, Constants.LOCATION) != null) {
                        //showToast("Data Exist");
                    } else {
                        //showToast("Data not exist");
                    }
                    //Toast.makeText(LoginActivity.this, userLocation.getName(), Toast.LENGTH_LONG).show();

                }
            });
        }

    }

    private boolean validateEmail() {
        String mEmail = inputEmail.getText().toString().trim();

        if (mEmail.isEmpty() || !isValidEmail(mEmail)) {
            inputLayoutEmail.setError(getString(R.string.err_msg_email));
            requestFocus(inputEmail);
            return false;
        } else {
            inputLayoutEmail.setErrorEnabled(true);
        }

        return true;
    }

    private boolean validatePassword() {
        if (inputPassword.getText().toString().trim().isEmpty()) {
            inputLayoutPassword.setError(getString(R.string.err_msg_password));
            requestFocus(inputPassword);
            return false;
        } else {
            inputLayoutPassword.setErrorEnabled(true);
        }
        return true;
    }

    private static boolean isValidEmail(String email) {
        return !android.text.TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private void showToast(String msg) {
        Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_LONG).show();
    }
}
