package example.com.taxicityappdriver.controller.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

import de.hdodenhof.circleimageview.CircleImageView;
import example.com.taxicityappdriver.R;
import example.com.taxicityappdriver.model.backend.BackEnd;
import example.com.taxicityappdriver.model.backend.BackEndFactory;
import example.com.taxicityappdriver.model.interfaces.ActionCallBack;
import example.com.taxicityappdriver.model.interfaces.SimpleCallBack;

import static android.app.Activity.RESULT_OK;


public class SettingsFragment extends Fragment {


    //View Fields

    private EditText newPasswordEditText;
    private EditText confirmNewPasswordEditText;
    private TextView totalTripTextView;
    private TextView totalPricedTextView;
    private Button submitButton;
    private TextView nameTextView;
    private CircleImageView avatarImageView;

    private static final BackEnd db = BackEndFactory.getInstance();
    public static final int PICK_IMAGE = 1; //Image Picked Code ( for change profile picture)

    public SettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Bind View
        totalPricedTextView = view.findViewById(R.id.total_price_txt_view);
        totalTripTextView = view.findViewById(R.id.total_trip_txt_view);
        newPasswordEditText = view.findViewById(R.id.new_password);
        confirmNewPasswordEditText = view.findViewById(R.id.new_password_confirm);
        submitButton = view.findViewById(R.id.submit_btn_form);
        nameTextView = view.findViewById(R.id.name_header);
        avatarImageView = view.findViewById(R.id.profile_image);


        //Init Listenner
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfirmDialog(new SimpleCallBack() {
                    @Override
                    public void execute() {
                        updatePassword();
                    }
                });
            }
        });

        avatarImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //New Intent to select new profile picture.
                Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
                getIntent.setType("image/*");

                Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                pickIntent.setType("image/*");

                Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});

                startActivityForResult(chooserIntent, PICK_IMAGE);
            }
        });

        //UI setText
        totalPricedTextView.setText(String.valueOf((int) db.getCurrentDriver().getTotalSumOfTrips()) + " $");
        totalTripTextView.setText(String.valueOf(db.getCurrentDriver().getTotalTripsCounter()) + " trip(s)");

        nameTextView.setText(db.getCurrentDriver().getFirstName() + " " + db.getCurrentDriver().getLastName() + " - " + db.getCurrentDriver().getIdNumber());

        avatarImageView.post(new Runnable() {
            @Override
            public void run() {
                //if (db.getUserProfilePicture() == null) {
                    //TODO Restore Current user profile picture
                //}
            }
        });

    }

    /**
     * Check Valid Form
     *
     * @return true if valid
     */
    private boolean isValidForm() {
        if (newPasswordEditText == null || confirmNewPasswordEditText == null) {
            Toast.makeText(getContext(), getString(R.string.please_fill_all_required_error_msg), Toast.LENGTH_LONG).show();
            return false;
        }
        if (TextUtils.isEmpty(newPasswordEditText.getText()) || TextUtils.isEmpty(confirmNewPasswordEditText.getText())) {
            Toast.makeText(getContext(), getString(R.string.please_fill_all_required_error_msg), Toast.LENGTH_LONG).show();
            return false;
        }
        if (newPasswordEditText.getText().toString().length() < 6) {
            Toast.makeText(getContext(), getString(R.string.min_6_password_char_error_msg), Toast.LENGTH_LONG).show();
            return false;
        }
        if (!newPasswordEditText.getText().toString().equals(confirmNewPasswordEditText.getText().toString())) {
            Toast.makeText(getContext(), getString(R.string.pasword_not_matching_error_msg), Toast.LENGTH_LONG).show();
            return false;
        }

        return true;

    }

    /**
     * Updating user password
     */
    private void updatePassword() {
        //Check valid form
        if (!isValidForm())
            return;

        db.changeUserPassword(newPasswordEditText.getText().toString(), new ActionCallBack() {
            @Override
            public void onSuccess(Object obj) {
                Toast.makeText(getContext(), getString(R.string.password_changed_success_msg), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Exception exception) {
                Toast.makeText(getContext(), exception.getMessage(), Toast.LENGTH_LONG).show();

            }

            @Override
            public void onProgress(String status, double percent) {

            }
        });

    }


    /**
     * Confirmation Dialog
     * This dialog execute a CallBack function (Void, and no Args).
     *
     * @param function
     */
    private void showConfirmDialog(final SimpleCallBack function) {
        AlertDialog show = new AlertDialog.Builder(getContext())
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(getString(R.string.confirm_action_dialog_title))
                .setMessage(getString(R.string.are_you_sure_msg_dialog_confirm))
                .setPositiveButton(getString(R.string.yes_btn), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (function != null) {
                            function.execute();
                        }
                    }

                })
                .setNegativeButton(getString(R.string.no_btn), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    /**
     * Update Profil picture on activity result to select image.
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            avatarImageView.setImageURI(selectedImage);
            db.updateProfilePicture(selectedImage, new ActionCallBack() {
                @Override
                public void onSuccess(Object obj) {
                    Toast.makeText(getContext(), getString(R.string.photo_changed_success_msg), Toast.LENGTH_LONG).show();
                }

                @Override
                public void onFailure(Exception exception) {

                }

                @Override
                public void onProgress(String status, double percent) {

                }
            });

        }
    }
}
