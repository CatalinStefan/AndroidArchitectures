package com.devtides.androidarchitectures.mvvm;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.devtides.androidarchitectures.R;
import com.devtides.androidarchitectures.mvp.MVPActivity;

import java.util.ArrayList;
import java.util.List;

public class MVVMActivity extends AppCompatActivity {

    private List<String> listValues = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    private ListView list;
    private CountriesViewModel viewModel;
    private Button retryButton;
    private ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mvvm);
        setTitle("MVVM Activity");

        viewModel = ViewModelProviders.of(this).get(CountriesViewModel.class);

        list = findViewById(R.id.list);
        retryButton = findViewById(R.id.retryButton);
        progress = findViewById(R.id.progress);
        adapter = new ArrayAdapter<>(this, R.layout.row_layout, R.id.listText, listValues);

        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MVVMActivity.this, "You clicked " + listValues.get(position), Toast.LENGTH_SHORT).show();
            }
        });

        observeViewModel();
    }

    private void observeViewModel() {
        viewModel.getCountries().observe(this, countries -> {
            if(countries != null) {
                listValues.clear();
                listValues.addAll(countries);
                list.setVisibility(View.VISIBLE);
                adapter.notifyDataSetChanged();
            } else {
                list.setVisibility(View.GONE);
            }
        });

        viewModel.getCountryError().observe(this, error -> {
            progress.setVisibility(View.GONE);
            if(error) {
                Toast.makeText(this, getString(R.string.error_message), Toast.LENGTH_SHORT).show();
                retryButton.setVisibility(View.VISIBLE);
            } else {
                retryButton.setVisibility(View.GONE);
            }
        });
    }

    public void onRetry(View view) {
        viewModel.onRefresh();
        list.setVisibility(View.GONE);
        retryButton.setVisibility(View.GONE);
        progress.setVisibility(View.VISIBLE);
    }

    public static Intent getIntent(Context context) {
        return new Intent(context, MVVMActivity.class);
    }
}
