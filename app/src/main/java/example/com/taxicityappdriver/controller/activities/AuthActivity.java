package example.com.taxicityappdriver.controller.activities;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.ramotion.foldingcell.FoldingCell;

import example.com.taxicityappdriver.R;
import example.com.taxicityappdriver.controller.fragments.SignUpFragment;
import example.com.taxicityappdriver.controller.fragments.SinginFragment;
import example.com.taxicityappdriver.controller.fragments.TripItemFragment;

//TODO enlever la bar du haut
public class AuthActivity extends AppCompatActivity {
    private FragmentManager fm = getSupportFragmentManager();
    private SinginFragment signInFragment = null;
    private SignUpFragment signUpFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        fm.beginTransaction().add(R.id.main_frame_layout, new TripItemFragment()).commit();
    }

    private void configSignIn() {

        //Singleton pattern of the fragment.
        if (signInFragment == null)
            signInFragment = new SinginFragment();

        //Adding Welcome Fragment
        //fm.beginTransaction().add(R.id.main_frame_layout, signInFragment).commit();
    }

    private void configSignUp() {

        //Singleton pattern of the fragment.
        if (signUpFragment == null)
            signUpFragment = new SignUpFragment();

        //Adding Welcome Fragment
        //fm.beginTransaction().add(R.id.main_frame_layout, signUpFragment).commit();
    }


}
