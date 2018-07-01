package co.nexlabs.socialblood.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import android.widget.Toast;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import co.nexlabs.socialblood.R;
import co.nexlabs.socialblood.adapter.BloodBankListAdapter;
import co.nexlabs.socialblood.adapter.BloodDonorListAdapter;
import co.nexlabs.socialblood.model.BloodBank;
import co.nexlabs.socialblood.model.BloodDonor;
import co.nexlabs.socialblood.model.User;
import co.nexlabs.socialblood.rest.RestClient;
import co.nexlabs.socialblood.util.Constants;
import co.nexlabs.socialblood.util.DataUtils;
import co.nexlabs.socialblood.view.DividerItemDecoration;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by myozawoo on 3/21/16.
 */
public class BloodBankFragment extends Fragment {

    @BindView(R.id.blood_bank_list)
    RecyclerView recyclerView;

    @BindView(R.id.pg_state)
    ProgressBar pgState;

    BloodBankListAdapter adapter;

    User user;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_blood_bank, container, false);
        unbinder = ButterKnife.bind(this, rootView);

        user = (User) DataUtils.ReadObject(getActivity(), Constants.USER);
        addData();
        adapter = new BloodBankListAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(getActivity(),
                LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);
        return rootView;
    }

    Unbinder unbinder;

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    private void addData() {

        RestClient.getInstance().getService().searchBloodBanks(user.getAuthToken(), new Callback<List<BloodBank>>() {
            @Override
            public void success(List<BloodBank> bloodBanks, Response response) {
                if (bloodBanks != null) {
                    adapter.setList(bloodBanks);
                    pgState.setVisibility(View.INVISIBLE);
                    recyclerView.setVisibility(View.VISIBLE);
                } else {
                    pgState.setVisibility(View.INVISIBLE);
                    recyclerView.setVisibility(View.VISIBLE);
                    Toast.makeText(getActivity().getApplicationContext(), "No blood banks nearby!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }
}
