package app.hiennv.applauncher.apps;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

import app.hiennv.applauncher.launcher.AppItem;
import app.hiennv.applauncher.launcher.AppItemAdapter;
import app.hiennv.applauncher.launcher.AsyncLoaderApplication;
import app.hiennv.applauncher.R;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<AppItem>>, AppItemAdapter.OpenAppItemListener {
    private AppItemAdapter mAppItemAdapter;
    private RecyclerView rcvAppList;
    private static final int NUMBER_APP_IN_LINE = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        getSupportLoaderManager().initLoader(0, null, this);
    }

    private void initView() {
        rcvAppList = (RecyclerView) findViewById(R.id.rcvAppList);
        rcvAppList.setLayoutManager(new GridLayoutManager(this, NUMBER_APP_IN_LINE));
    }


    @NonNull
    @Override
    public Loader<ArrayList<AppItem>> onCreateLoader(int id, @Nullable Bundle args) {
        return new AsyncLoaderApplication(this);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<ArrayList<AppItem>> loader, ArrayList<AppItem> data) {
        //set adapter here
        mAppItemAdapter = new AppItemAdapter(data,this);
        rcvAppList.setAdapter(mAppItemAdapter);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<ArrayList<AppItem>> loader) {

    }

    @Override
    public void openAppItem(AppItem appItem) {
        if (appItem != null) {
            Intent intent = getPackageManager().getLaunchIntentForPackage(appItem.getApplicationPackageName());
            if (intent != null) {
                startActivity(intent);
            }
        }
    }
}
