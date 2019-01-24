package example.com.taxicityappdriver.controller.fragments;


import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import example.com.taxicityappdriver.R;
import example.com.taxicityappdriver.controller.activities.MainActivity;
import example.com.taxicityappdriver.model.interfaces.ActionCallBack;
import example.com.taxicityappdriver.model.backend.BackEnd;
import example.com.taxicityappdriver.model.backend.BackEndFactory;
import example.com.taxicityappdriver.model.helpers.Helpers;

/**
 * A simple {@link Fragment} subclass.
 */
public class SingInFragment extends Fragment {

    //View Fields
    private EditText emailEditText;
    private EditText passwordEditText;
    private TextView helperTextView;
    private EditText emailForgotPassword;
    private AlertDialog alertDialog;

    //Instance [Factory of the DataBase
    private static BackEnd db = BackEndFactory.getInstance();
    //SharedPreference to save user Ids
    private static final String PREFS = "PREFS";
    private static final String PREFS_EMAIL = "PREFS_EMAIL";
    private static final String PREFS_PASSWORD = "PREFS_PASSWORD";


    private Button btnSubmit;
    SharedPreferences sharedPreferences; //BUG !
    private SignUpFragment signUpFragment = null;

    public SingInFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_singin, container, false);
    }

    /***
     * OnCreatedView
     * 1) Check if SignIn => if yes => MainActivity
     * 2) Bind View
     * 3) Init Listeners
     * 4) If Id's saved => Restore them.
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Check if SignIn => if yes => MainActivity
        if (db.isSigned())
            initMainActivity();

        //Bind View
        btnSubmit = view.findViewById(R.id.submit_sign_in_btn_form);
        emailEditText = view.findViewById(R.id.email_input_form);
        passwordEditText = view.findViewById(R.id.password_input_form);
        helperTextView = view.findViewById(R.id.helper_form);
        emailForgotPassword = new EditText(getContext());
        alertDialog = new AlertDialog.Builder(getContext()).create();
        LinearLayout joinUsLinearLayout = view.findViewById(R.id.join_us_btn);
        TextView forgotPasswordTextView = view.findViewById(R.id.forgot_password);

        //Init Listeners
        btnSubmit.setOnClickListener(submitListener());
        forgotPasswordTextView.setOnClickListener(forgotPasswordListener());
        joinUsLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                configSignUpFragment(); //Init the Register Fragments
            }
        });


        //If Id's saved => Restore them.
        sharedPreferences = getActivity().getSharedPreferences(PREFS, getContext().MODE_PRIVATE);
        if (sharedPreferences != null) {
            //Restaure old values
            if (sharedPreferences.contains(PREFS_EMAIL) && sharedPreferences.contains(PREFS_PASSWORD)) {
                emailEditText.setText(sharedPreferences.getString(PREFS_EMAIL, null));
                passwordEditText.setText(sharedPreferences.getString(PREFS_PASSWORD, null));
            }
        }


    }


    /**
     * When submit sign in form button is clicked
     *
     * @return
     */
    private View.OnClickListener submitListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Check form Validation
                if (!isValid())
                    return;

                //Prevent double submit
                btnSubmit.setEnabled(false);
                try {

                    //SignIN in Database
                    db.signIn(emailEditText.getText().toString(), passwordEditText.getText().toString(), new ActionCallBack<Object>() {
                        @Override
                        public void onSuccess(Object obj) {
                            //If signed start mainActivity
                            initMainActivity();

                            //Save value of user id's
                            sharedPreferences
                                    .edit()
                                    .putString(PREFS_EMAIL, emailEditText.getText().toString())
                                    .putString(PREFS_PASSWORD, passwordEditText.getText().toString())
                                    .apply();
                        }

                        @Override
                        public void onFailure(Exception exception) {
                            //Show in Helper exception
                            helperTextView.setVisibility(View.VISIBLE);
                            helperTextView.setText(exception.getMessage());
                            btnSubmit.setEnabled(true);
                        }

                        @Override
                        public void onProgress(String status, double percent) {

                        }
                    });
                } catch (Exception e) {
                    helperTextView.setVisibility(View.VISIBLE);
                    helperTextView.setText(e.getMessage());
                    btnSubmit.setEnabled(true);
                }


            }
        };

    }


    /**
     * When forgot password is clicked show forgotPassword Dialog
     *
     * @return
     */
    private View.OnClickListener forgotPasswordListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogBuilder();
            }
        };

    }

    /**
     * Check if the form is valid.
     *
     * @return true if is valid
     */

    private boolean isValid() {
        //HELPER
        if (emailEditText == null || passwordEditText == null) {
            helperTextView.setVisibility(View.VISIBLE);
            helperTextView.setText(getString(R.string.please_fill_all_required_error_msg));
            return false;
        }

        if (TextUtils.isEmpty(emailEditText.getText()) || TextUtils.isEmpty(passwordEditText.getText())) {
            helperTextView.setVisibility(View.VISIBLE);
            helperTextView.setText(getString(R.string.please_fill_all_required_error_msg));
            return false;
        }
        if (!Helpers.isValidEmail(emailEditText.getText().toString())) {
            helperTextView.setVisibility(View.VISIBLE);
            helperTextView.setText(getString(R.string.enter_valid_email_error_msg));
            return false;
        }

        if (passwordEditText.getText().length() < 6) {
            helperTextView.setVisibility(View.VISIBLE);
            helperTextView.setText(getString(R.string.min_6_password_char_error_msg));
            return false;
        }

        return true;
    }

    /**
     * Initialisation of the mainActivity
     */
    private void initMainActivity() {

        Intent intent = new Intent(getContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK); //reset backstack
        startActivity(intent);
        if (getActivity() != null)
            getActivity().finish();
    }

    /**
     * Dialog initialisation of faggoted password.
     */
    private void dialogBuilder() {


        FrameLayout.LayoutParams lp2 = new FrameLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);


        alertDialog.setTitle(getString(R.string.forgot_pasword_dialog_title));
        alertDialog.setMessage(getString(R.string.forgot_pasword_dialog_msg));
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.reset_password_btn_dialog),
                new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, int which) {

                        //Valid form
                        if (TextUtils.isEmpty(emailForgotPassword.getText()) || !Helpers.isValidEmail(emailForgotPassword.getText().toString())) {
                            helperTextView.setVisibility(View.VISIBLE);
                            helperTextView.setText(getString(R.string.enter_valid_email_error_msg));
                            return;
                        }

                        try {
                            //Send Email for reset the password.
                            db.forgotPassword(emailForgotPassword.getText().toString(), new ActionCallBack() {
                                @Override
                                public void onSuccess(Object obj) {
                                    helperTextView.setVisibility(View.VISIBLE);
                                    helperTextView.setTextColor(Color.GREEN);
                                    helperTextView.setText(getString(R.string.reset_password_success_msg));
                                    dialog.dismiss();
                                }

                                @Override
                                public void onFailure(Exception exception) {
                                    helperTextView.setVisibility(View.VISIBLE);
                                    helperTextView.setText(exception.getMessage());

                                }

                                @Override
                                public void onProgress(String status, double percent) {

                                }
                            });
                        } catch (Exception e) {
                            helperTextView.setVisibility(View.VISIBLE);
                            helperTextView.setText(e.getMessage());
                        }


                    }
                });

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.cancel_btn_dialog),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss(); //Close the dialog
                    }
                });
        emailForgotPassword.setLayoutParams(lp2);
        alertDialog.setView(emailForgotPassword);

        alertDialog.show();

    }

    /**
     * Initialisation of the Sign up fragment
     */
    private void configSignUpFragment() {

        //Singleton pattern of the fragment.
        if (signUpFragment == null)
            signUpFragment = new SignUpFragment();

        //Get Fragment manager from the parent
        FragmentManager fm = getActivity().getSupportFragmentManager();

        if (!signUpFragment.isVisible()) {
            fm.beginTransaction().remove(fm.findFragmentById(R.id.main_frame_layout)).commit();
            fm.beginTransaction().replace(R.id.main_frame_layout, signUpFragment).commit();
        }

    }


}
