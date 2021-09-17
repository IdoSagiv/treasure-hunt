package huji.postpc2021.treasure_hunt;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;

public class RequestPermissionsActivity extends AppCompatActivity {
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 1;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_permissions);

        // disable dark mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        // Make to run your application only in portrait mode
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        String[] permissions = {
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.INTERNET
        };

        if (!needToAskPermissions(permissions)) {
            updateUI();
        }

        findViewById(R.id.buttonGrantPermissions).setOnClickListener(v ->
                requestPermissionsIfNecessary(permissions));
    }

    private boolean needToAskPermissions(String[] permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                return true;
            }
        }
        return false;
    }


    private void requestPermissionsIfNecessary(String[] permissions) {
        ArrayList<String> permissionsToRequest = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(permission);
            }
        }
        if (permissionsToRequest.size() > 0) {
            ActivityCompat.requestPermissions(
                    this,
                    permissionsToRequest.toArray(new String[0]),
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        } else {
            updateUI();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        for (int grantResult : grantResults) {
            if (grantResult == PackageManager.PERMISSION_DENIED) {
                Toast.makeText(this, "cant work without permissions", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        updateUI();
    }

    private void updateUI() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
