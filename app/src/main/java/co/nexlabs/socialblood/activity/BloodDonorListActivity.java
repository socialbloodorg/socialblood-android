package co.nexlabs.socialblood.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import co.nexlabs.socialblood.R;
import co.nexlabs.socialblood.adapter.BloodDonorListAdapter;
import co.nexlabs.socialblood.model.BloodDonor;
import co.nexlabs.socialblood.util.Constants;
import co.nexlabs.socialblood.util.DataUtils;
import co.nexlabs.socialblood.view.DividerItemDecoration;

/**
 * Created by myozawoo on 3/17/16.
 */
public class BloodDonorListActivity extends AppCompatActivity {

    @BindView(R.id.blood_donor_list)
    RecyclerView recyclerView;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    BloodDonorListAdapter adapter;

    List<BloodDonor> list;

    Unbinder unbinder;

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_blood_donor_list);
        unbinder = ButterKnife.bind(this);
        toolbar.setTitle("Blood Donors");
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorPrimary));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        list = (List<BloodDonor>) getIntent().getExtras().get("donor");
        adapter = new BloodDonorListAdapter();
        //addDonorList();
        adapter.setList(list);
        recyclerView.setLayoutManager(new LinearLayoutManager(BloodDonorListActivity.this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(BloodDonorListActivity.this,
                LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);

    }




}
