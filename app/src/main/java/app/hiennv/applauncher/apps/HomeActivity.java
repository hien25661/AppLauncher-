package app.hiennv.applauncher.apps;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import app.hiennv.applauncher.BaseActivity;
import app.hiennv.applauncher.R;
import app.hiennv.applauncher.apps.MainActivity;

public class HomeActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }

    public void openListApp(View v){
        startActivity(new Intent(this,MainActivity.class));
    }
}
