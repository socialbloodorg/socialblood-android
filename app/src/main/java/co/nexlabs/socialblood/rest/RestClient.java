package co.nexlabs.socialblood.rest;

import android.os.AsyncTask;
import com.squareup.okhttp.OkHttpClient;
import java.util.concurrent.TimeUnit;

import co.nexlabs.socialblood.util.Constants;
import retrofit.RestAdapter;
import retrofit.android.MainThreadExecutor;
import retrofit.client.OkClient;

/**
 * Created by myozawoo on 3/14/16.
 */
public class RestClient {

    private static RestClient instance;
    private RestService mService;

    private OkHttpClient getClient() {
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(5, TimeUnit.MINUTES);
        client.setReadTimeout(5, TimeUnit.MINUTES);
        return client;
    }

    public RestClient() {
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(Constants.BASE_URL)
                .setExecutors(AsyncTask.THREAD_POOL_EXECUTOR, new MainThreadExecutor())
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setClient(new OkClient(getClient()))
                .build();
        mService = restAdapter.create(RestService.class);
    }

    public static RestClient getInstance() {
        if (instance == null) {
            instance = new RestClient();
        }
        return instance;
    }

    public RestService getService() {
        return mService;
    }
}
