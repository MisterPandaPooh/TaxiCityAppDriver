package example.com.taxicityappdriver.controller.fragments;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import example.com.taxicityappdriver.R;
import example.com.taxicityappdriver.controller.activities.MainActivity;
import example.com.taxicityappdriver.model.entities.Driver;
import example.com.taxicityappdriver.model.interfaces.ActionCallBack;
import example.com.taxicityappdriver.model.backend.BackEnd;
import example.com.taxicityappdriver.model.backend.BackEndFactory;
import example.com.taxicityappdriver.model.helpers.Helpers;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignUpFragment extends Fragment {


    //View fields
    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText passwordConfirmEditText;
    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private EditText phoneEditText;
    private EditText idNumberEditText;
    private EditText creditCardNumber;
    private EditText expireOnEditText;
    private EditText cvvEditText;
    private TextView helperTextView;
    private Button btnSubmit;


    public static final String TAG = "SignUpFragment";
    private static BackEnd db = BackEndFactory.getInstance(); //Database Instance


    public SignUpFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_up, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //BindView
        emailEditText = view.findViewById(R.id.email_input_form);
        passwordEditText = view.findViewById(R.id.password_input_form);
        passwordConfirmEditText = view.findViewById(R.id.password_confirm_signup);
        firstNameEditText = view.findViewById(R.id.first_name_input_signup);
        lastNameEditText = view.findViewById(R.id.last_name_input_signup);
        idNumberEditText = view.findViewById(R.id.id_number_input_form);
        phoneEditText = view.findViewById(R.id.phone_input_form);
        creditCardNumber = view.findViewById(R.id.credit_card_number);
        expireOnEditText = view.findViewById(R.id.expire_on_input_form);
        cvvEditText = view.findViewById(R.id.cvv_input_form);
        btnSubmit = view.findViewById(R.id.submit_btn_form_sign_up);

        helperTextView = view.findViewById(R.id.helper_form);
        btnSubmit.setOnClickListener(submitSignUpListener());

    }

    /**
     * Check if the sign up form is valid
     *
     * @return true if is a valid form.
     */
    private boolean isValid() {

        if (emailEditText == null || passwordEditText == null ||
                passwordConfirmEditText == null || firstNameEditText == null ||
                lastNameEditText == null || idNumberEditText == null || phoneEditText == null || creditCardNumber == null || expireOnEditText == null || cvvEditText == null) {
            showHelper(getString(R.string.please_fill_all_required_error_msg), false);
            return false;
        }

        if (TextUtils.isEmpty(emailEditText.getText().toString()) ||
                TextUtils.isEmpty(passwordEditText.getText().toString()) ||
                TextUtils.isEmpty(passwordConfirmEditText.getText().toString()) ||
                TextUtils.isEmpty(firstNameEditText.getText().toString()) ||
                TextUtils.isEmpty(lastNameEditText.getText().toString()) ||
                TextUtils.isEmpty(idNumberEditText.getText().toString()) ||
                TextUtils.isEmpty(phoneEditText.getText().toString()) ||
                TextUtils.isEmpty(creditCardNumber.getText().toString()) ||
                TextUtils.isEmpty(expireOnEditText.getText().toString()) ||
                TextUtils.isEmpty(cvvEditText.getText().toString())
        ) {
            showHelper(getString(R.string.please_fill_all_required_error_msg), false);
            return false;
        }

        if (!Helpers.isValidEmail(emailEditText.getText().toString())) {
            showHelper(getString(R.string.enter_valid_email_error_msg), false);
            return false;
        }

        if (cvvEditText.getText().length() < 3) {
            showHelper(getString(R.string.enter_correct_cvv_error_msg), false);
            return false;
        }

        if (phoneEditText.getText().length() < 10) {
            showHelper(getString(R.string.enter_correct_phone_error_msg), false);
            return false;
        }

        if (expireOnEditText.getText().length() < 6) {
            showHelper(getString(R.string.enter_corect_expiration_date_error_msg), false);
            return false;
        }

        if (creditCardNumber.getText().length() < 16) {
            showHelper(getString(R.string.enter_correct_cb_error_msg), false);
            return false;
        }

        if (passwordEditText.getText().length() < 6) {
            showHelper(getString(R.string.min_6_password_char_error_msg), false);
            return false;

        }
        if (!passwordEditText.getText().toString().equals(passwordConfirmEditText.getText().toString())) {
            showHelper(getString(R.string.pasword_not_matching_error_msg), false);
            return false;
        }

        return true;

    }

    /**
     * Show The helper textview for displaying Success/Error message to user.
     *
     * @param msg       The Message to show.
     * @param isSuccess The type of message (true) Success (false) Error
     */
    private void showHelper(String msg, boolean isSuccess) {
        if (helperTextView == null)
            return;
        if (isSuccess)
            helperTextView.setTextColor(Color.GREEN);
        else
            helperTextView.setTextColor(Color.RED);

        helperTextView.setVisibility(View.VISIBLE);
        helperTextView.setText(msg);
    }


    /**
     * Bind the View with a new driver object.
     *
     * @return Driver Obj
     */
    private Driver formatDriver() {
        Driver driver = new Driver();
        driver.setEmail(emailEditText.getText().toString());
        driver.setCreatedDate(Long.toString(System.currentTimeMillis()));
        driver.setFirstName(firstNameEditText.getText().toString());
        driver.setLastName(lastNameEditText.getText().toString());
        driver.setPhoneNumber(phoneEditText.getText().toString());
        driver.setIdNumber(Long.parseLong(idNumberEditText.getText().toString()));
        driver.setCreditCardNumber(Long.parseLong(creditCardNumber.getText().toString()));
        driver.setcVV(Long.parseLong(cvvEditText.getText().toString()));
        driver.setExpireDateCreditCard(expireOnEditText.getText().toString());
        driver.setBusy(false);
        //TODO
        //driver.setCurrentLocationLat();
        //driver.setCurrentLocationLong();
        return driver;
    }


    /**
     * Submit button click listener.
     * 1) Add the driver to Auth Database [In this case FirebaseAuth]
     * 2) OnSuccess => add the Driver entity to the database.
     *
     * @return
     */
    private View.OnClickListener submitSignUpListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //Check form Validation
                if (!isValid())
                    return;

                try {
                    formatDriver(); //Prevent Exception crash when Parsing Number.
                } catch (Exception e) {
                    showHelper(e.getMessage(), false);
                }

                btnSubmit.setEnabled(false);


                try {
                    //1 SignUp to FireABse Auth
                    db.signUp(emailEditText.getText().toString(), passwordEditText.getText().toString(), new ActionCallBack<Object>() {
                        @Override
                        public void onSuccess(Object obj) {
                            final Driver driver = formatDriver();
                            //2 Add Driver to database
                            db.addDriver(driver, new ActionCallBack() {
                                @Override
                                public void onSuccess(Object obj) {
                                    showHelper(getString(R.string.sign_up_success_msg), true);
                                    initMainActivity();


                                }

                                @Override
                                public void onFailure(Exception exception) {
                                    //Remove user from FirebaseAuth
                                    showHelper(exception.getMessage(), false);
                                    db.deleteCurrentUser();
                                    btnSubmit.setEnabled(true);

                                }

                                @Override
                                public void onProgress(String status, double percent) {

                                }
                            });
                        }

                        @Override
                        public void onFailure(Exception exception) {
                            btnSubmit.setEnabled(true);

                        }

                        @Override
                        public void onProgress(String status, double percent) {

                        }
                    });
                } catch (Exception e) {
                    showHelper(e.getMessage(), false);
                }

            }
        };
    }

    /**
     * Init MainActivity
     */
    private void initMainActivity() {

        Intent intent = new Intent(getContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK); //reset backStack
        startActivity(intent);
        if (getActivity() != null)
            getActivity().finish();
    }

}
