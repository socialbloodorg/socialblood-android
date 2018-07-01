package co.nexlabs.socialblood.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.facebook.appevents.AppEventsLogger;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import co.nexlabs.socialblood.R;
import co.nexlabs.socialblood.adapter.ViewPagerAdapter;
import co.nexlabs.socialblood.fragment.FragmentOne;
import co.nexlabs.socialblood.fragment.BloodBankFragment;
import co.nexlabs.socialblood.fragment.FragmentTwo;
import co.nexlabs.socialblood.fragment.SearchDonorsFragment;
import co.nexlabs.socialblood.fragment.SettingFragment;
import co.nexlabs.socialblood.model.User;
import co.nexlabs.socialblood.rest.RestClient;
import co.nexlabs.socialblood.util.Constants;
import co.nexlabs.socialblood.util.DataUtils;
import co.nexlabs.socialblood.util.GPSTracker;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by myozawoo on 3/12/16.
 */
public class MainActivity extends AppCompatActivity {

    private static String TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.tabs)
    TabLayout tabLayout;
    @BindView(R.id.viewpager)
    ViewPager viewPager;
    private User user;
    private ProgressDialog dialog;
    private int[] tabIcons = {R.drawable.ic_home_24dp, R.drawable.ic_place_24dp, R.drawable.ic_user_group,
            R.drawable.ic_hospital, R.drawable.ic_settings_24dp};

    Unbinder unbinder;

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_tab);

        unbinder = ButterKnife.bind(this);
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcon();
        tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.white));

        dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setMessage("Loading...");

        if (DataUtils.ReadObject(this, Constants.USER) != null) {
            user = (User) DataUtils.ReadObject(this, Constants.USER);
            //Log.e("Profile Link: ", user.getProfilePhotoUrl().toString());
        }

        updateLocation();

    }

    @Override
    protected void onResume() {
        super.onResume();
        AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        AppEventsLogger.deactivateApp(this);
    }

    private void setupTabIcon(){
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
        tabLayout.getTabAt(3).setIcon(tabIcons[3]);
        tabLayout.getTabAt(4).setIcon(tabIcons[4]);
    }

    private void setupViewPager(ViewPager vp) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new SearchDonorsFragment());
        adapter.addFragment(new FragmentOne());
        adapter.addFragment(new FragmentTwo());
        adapter.addFragment(new BloodBankFragment());
        adapter.addFragment(new SettingFragment());
        vp.setAdapter(adapter);
    }

    private void updateLocation() {
        GPSTracker gpsTracker = new GPSTracker(MainActivity.this);
        if (gpsTracker.canGetLocation()) {
            String lat = gpsTracker.getLatitude() + "";
            String lon = gpsTracker.getLongitude() + "";
            //showToast(lat+" "+lon);
            RestClient.getInstance().getService().updateLocation(user.getId(), user.getAuthToken(), lat, lon, new Callback<User>() {
                @Override
                public void success(User users, Response response) {
                    DataUtils.DeleteObject(MainActivity.this, Constants.USER);
                    DataUtils.WriteObject(MainActivity.this, Constants.USER, users);
                    user = (User) DataUtils.ReadObject(MainActivity.this, Constants.USER);
                    //showToast("Lat : " + user.getLatitude() + " Lon :" + user.getLongitude());
                }

                @Override
                public void failure(RetrofitError error) {

                }
            });
        }
    }


//    private void showToast(String msg) {
//        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_LONG).show();
//    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}

