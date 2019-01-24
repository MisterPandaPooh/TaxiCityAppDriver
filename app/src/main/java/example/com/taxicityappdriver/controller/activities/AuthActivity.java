package example.com.taxicityappdriver.controller.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import example.com.taxicityappdriver.R;
import example.com.taxicityappdriver.controller.fragments.SingInFragment;
import example.com.taxicityappdriver.model.backend.BackEnd;
import example.com.taxicityappdriver.model.backend.BackEndFactory;

//TODO enlever la bar du haut
public class AuthActivity extends AppCompatActivity {
    private SingInFragment signInFragment = null;
    private static BackEnd db = BackEndFactory.getInstance();
    public static final int REQUEST_CODE_ASK_PERMISSIONS = 531;

    /**
     * OnCreate :
     * 1) Request All permission need by the app to correctly run.
     * 2) Check if the user is authenticated => if yes start MainActivity ELSE Load SignIn Fragment
     *
     * @param savedInstanceState
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        requestAllPermissions();

        db.signOut();
        if (db.isSigned())
            initMainActivity();
        else
            configSignIn();

    }


    /**
     * Init SignIn fragment.
     */
    private void configSignIn() {

        //Singleton pattern of the fragment.
        if (signInFragment == null)
            signInFragment = new SingInFragment();

        //Adding Welcome Fragment
        if (!signInFragment.isVisible()) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.main_frame_layout, signInFragment).commit();
        }
    }

    /**
     * Init Main Activity
     */
    private void initMainActivity() {

        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK); //reset backstack
        startActivity(intent);
        finish();
    }


    /**
     * Request All permission need by the app to correctly run.
     */
    private void requestAllPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_CONTACTS) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat
                    .requestPermissions(this, new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.SEND_SMS,
                            Manifest.permission.CALL_PHONE,
                            Manifest.permission.READ_CONTACTS,
                            Manifest.permission.WRITE_CONTACTS
                    }, REQUEST_CODE_ASK_PERMISSIONS);

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        requestAllPermissions(); //Check After Result
    }
}
