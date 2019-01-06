package example.com.taxicityappdriver.controller.activities;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import example.com.taxicityappdriver.R;
import example.com.taxicityappdriver.controller.fragments.SinginFragment;

public class AuthActivity extends AppCompatActivity {
    private FragmentManager fm = getSupportFragmentManager();
    private SinginFragment signInFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        configSignIn();
    }

    private void configSignIn() {

        //Singleton pattern of the fragment.
        if (signInFragment == null)
            signInFragment = new SinginFragment();

        //Adding Welcome Fragment
        fm.beginTransaction().add(R.id.main_frame_layout, signInFragment).commit();
    }
}
