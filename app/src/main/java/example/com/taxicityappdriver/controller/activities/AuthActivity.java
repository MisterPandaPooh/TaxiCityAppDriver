package example.com.taxicityappdriver.controller.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;

import com.ramotion.foldingcell.FoldingCell;

import example.com.taxicityappdriver.R;
import example.com.taxicityappdriver.controller.fragments.SignUpFragment;
import example.com.taxicityappdriver.controller.fragments.SinginFragment;
import example.com.taxicityappdriver.controller.fragments.TripItemFragment;
import example.com.taxicityappdriver.model.backend.BackEnd;
import example.com.taxicityappdriver.model.backend.BackEndFactory;

//TODO enlever la bar du haut
public class AuthActivity extends AppCompatActivity {
    private FragmentManager fm = getSupportFragmentManager();
    private SinginFragment signInFragment = null;
    private static BackEnd db = BackEndFactory.getInstance();
    public static final int REQUEST_CODE_ASK_PERMISSIONS = 531;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        requestAllPermissions();

        if (db.isSigned())
            initMainActivity();
        else
            configSignIn();

    }


    private void configSignIn() {

        //Singleton pattern of the fragment.
        if (signInFragment == null)
            signInFragment = new SinginFragment();

        //Adding Welcome Fragment
        if (!signInFragment.isVisible()) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.activity_main_frame_layout, signInFragment).commit();
        }
    }

    private void initMainActivity() {

        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK); //reset backstack
        startActivity(intent);
        finish();
    }


    private void requestAllPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat
                    .requestPermissions(this, new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.SEND_SMS
                    }, REQUEST_CODE_ASK_PERMISSIONS);

        }
    }


}
