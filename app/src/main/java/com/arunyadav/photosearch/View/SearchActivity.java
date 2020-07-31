package com.arunyadav.photosearch.View;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.paging.PagedList;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.widget.SearchView;
import android.widget.Toast;

import com.arunyadav.photosearch.Utils.NetworkState;
import com.arunyadav.photosearch.R;
import com.arunyadav.photosearch.ViewModel.MainActivityViewModel;
import com.arunyadav.photosearch.model.Photo;


import butterknife.BindView;
import butterknife.ButterKnife;

import static com.arunyadav.photosearch.Utils.Constants.API_LOADING;
import static com.arunyadav.photosearch.Utils.Constants.TOAST_GREEN;
import static com.arunyadav.photosearch.Utils.Constants.TOAST_RED;
import static com.arunyadav.photosearch.Utils.Constants.TOAST_SEARCH_EMPTY;

public class SearchActivity extends AppCompatActivity {

    MainActivityViewModel mainActivityViewModel;
    @BindView(R.id.recylerview)
    RecyclerView photoRecylerview;

    @BindView(R.id.searchView)
    SearchView searchView;

    private PhotosAdapter photosAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        mainActivityViewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);
        photosAdapter = new PhotosAdapter();
        photoRecylerview.setLayoutManager(new LinearLayoutManager(this));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query.length() != 0 && query != null)
                    getdata(query);
                else
                    showToast(getApplicationContext(), TOAST_SEARCH_EMPTY, TOAST_RED);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length() != 0 && newText != null)
                    getdata(newText);
                return false;
            }
        });
        photoRecylerview.setAdapter(photosAdapter);

    }

    public void showToast(Context context, String message, String color) {
        Toast toast = Toast.makeText(context, Html.fromHtml("<font color='#" + color + "' ><b>" + message + "</b></font>"), Toast.LENGTH_LONG);
        toast.show();
    }

    public void getdata(String query) {
        mainActivityViewModel.getPagedListLiveData(query).observe(this, new Observer<PagedList<Photo>>() {
            @Override
            public void onChanged(@Nullable PagedList<Photo> photos) {
                photosAdapter.submitList(photos);
            }
        });
        mainActivityViewModel.getNetworkStateLiveData().observe(this, new Observer<NetworkState>() {
            @Override
            public void onChanged(@Nullable NetworkState networkState) {
                photosAdapter.setNetworkState(networkState);
                if (networkState.getStatus().equals(NetworkState.Status.FAILED))
                    showToast(getApplicationContext(), networkState.getMsg().toString(), TOAST_RED);
                else if (networkState.getStatus().equals(NetworkState.Status.RUNNING))
                    showToast(getApplicationContext(), API_LOADING, TOAST_GREEN);

            }
        });
    }
}