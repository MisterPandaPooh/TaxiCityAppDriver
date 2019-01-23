package example.com.taxicityappdriver.controller;

import android.app.AlertDialog;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ramotion.foldingcell.FoldingCell;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import example.com.taxicityappdriver.R;
import example.com.taxicityappdriver.model.helpers.LocationHelper;
import example.com.taxicityappdriver.model.entities.Driver;
import example.com.taxicityappdriver.model.entities.Trip;
import example.com.taxicityappdriver.model.interfaces.ActionCallBack;
import example.com.taxicityappdriver.model.backend.BackEnd;
import example.com.taxicityappdriver.model.backend.BackEndFactory;
import example.com.taxicityappdriver.model.interfaces.SimpleCallBack;


/**
 * WaitingTripViewHolder used for RecycleView
 */
public class WaitingTripViewHolder extends RecyclerView.ViewHolder {


    //Context to display Toast
    public static final Driver driver = BackEndFactory.getInstance().getCurrentDriver(); //CurrentDriver

    private static final BackEnd db = BackEndFactory.getInstance();
    private static String busyKey = null;
    private static final SimpleDateFormat simpleDate = new SimpleDateFormat("dd/MM/yy - HH:mm"); //Format Date for UI
    private static final String TAG = "TripItemViewAdapter";//Tag for debug
    private static Context context;

    private boolean isInit; //Singeton check if inited before
    private boolean contactAdded; //Singeton check if contact added  before
    private Trip trip; //Current Trip


    //UI ViewHolder Content Cell
    public TextView startingHour;
    public TextView sourceAddress;
    public TextView destinationAddress;
    public TextView distanceFromYou;
    public TextView tripDistance;

    //UI ViewHolder Title View
    public TextView startingHourTitleView;
    public TextView destinationAddressTitleView;
    public TextView distanceFromYouTitleView;
    public TextView tripDistanceTitleView;
    public TextView tripIdTitleView;


    //Action UI Button Content Cell
    public Button smsButton; //Send SMS confirmation
    public Button callButton; //CALL customer
    public Button emailButton;  //Send EMAIL confirmation
    public ImageView imgHeader; //Image header to fold the cell
    public LinearLayout customerButton; //Add customer to your contact list.
    public Button requestTripButton; //Request Trip
    public Button endTripButton; //End Trip
    public Button cancelButton; // Cancel Trip
    public View dividerRequested;

    //Listener
    private View.OnClickListener foldListener; // Fold the Content cell (seted By the Adapter)

    /* **** STATIC FUNCTIONS **** */

    public static String getBusyKey() {

        return busyKey;
    }

    public static void setContext(Context context) {
        WaitingTripViewHolder.context = context;
    }


    //CONSTRUCTOR
    public WaitingTripViewHolder(@NonNull View itemView) {
        super(itemView);
        isInit = false;
        contactAdded = false;
    }


    /**
     * Main function of initialisation of the Cell
     */
    public void init() {

        //Init References
        bindView();

        //Prevent NullPointerException
        if (trip == null || isInit) //IsInit Singleton
            return;
        if (startingHour == null || sourceAddress == null || destinationAddress == null || distanceFromYou == null || tripDistance == null)
            return;
        if (startingHourTitleView == null || destinationAddressTitleView == null || distanceFromYouTitleView == null || tripDistanceTitleView == null || tripIdTitleView == null)
            return;

        if (requestTripButton == null || customerButton == null || smsButton == null || callButton == null || emailButton == null || imgHeader == null || endTripButton == null || cancelButton == null)
            return;
        if (foldListener == null || dividerRequested == null)
            return;

        initClickListener(); //Init Listeners

        initUIContent(); //Init UI Content

        isInit = true;
    }


    /**
     * Bind View References.
     */
    private void bindView() {
        FoldingCell cell = (FoldingCell) itemView;

        //TitleView
        this.startingHourTitleView = cell.findViewById(R.id.datetime_trip_title_view);
        this.destinationAddressTitleView = cell.findViewById(R.id.trip_destination_title_view);
        this.distanceFromYouTitleView = cell.findViewById(R.id.distance_from_you_title_view);
        this.tripDistanceTitleView = cell.findViewById(R.id.trip_distance_title_view);
        this.tripIdTitleView = cell.findViewById(R.id.trip_title_id);


        //Content Cell
        this.startingHour = cell.findViewById(R.id.datetime_trip);
        this.sourceAddress = cell.findViewById(R.id.source_address);
        this.destinationAddress = cell.findViewById(R.id.trip_destination);
        this.distanceFromYou = cell.findViewById(R.id.distance_from_you);
        this.tripDistance = cell.findViewById(R.id.trip_distance);

        //UI Btn
        this.requestTripButton = cell.findViewById(R.id.request_trip_btn);
        this.smsButton = cell.findViewById(R.id.sms_btn);
        this.callButton = cell.findViewById(R.id.call_btn);
        this.emailButton = cell.findViewById(R.id.mail_btn);
        this.imgHeader = cell.findViewById(R.id.header_img);

        this.customerButton = cell.findViewById(R.id.customer_btn);
        this.endTripButton = cell.findViewById(R.id.end_trip_btn);
        this.cancelButton = cell.findViewById(R.id.cancel_trip_btn);

        this.dividerRequested = cell.findViewById(R.id.divider_requested);


    }


    /**
     * Initialisation of User Interface ClickListeners
     */

    private void initClickListener() {

        customerButton.setOnClickListener(onCustomerClickListener());
        smsButton.setOnClickListener(onSmsClickListener());
        callButton.setOnClickListener(onCallClickListner());
        emailButton.setOnClickListener(onEmailClickListener());
        requestTripButton.setOnClickListener(onRequestTripClickListener());

        if (!wasRequestByYou()) {
            //Init listener for folding the cell
            imgHeader.setOnClickListener(foldListener);
            itemView.setOnClickListener(foldListener);
        }

        cancelButton.setOnClickListener(onCancelClickListener());
        endTripButton.setOnClickListener(onEndTripClickListener());


    }

    /**
     * Initialisation of User Interface Content
     */
    private void initUIContent() {

        //TitleView
        startingHourTitleView.setText(simpleDate.format(trip.getStartingHourAsDate()));
        destinationAddressTitleView.setText(trip.getDestinationAddress());
        distanceFromYouTitleView.setText(context.getString(R.string.in_progress_cell));
        tripDistanceTitleView.setText(context.getString(R.string.in_progress_cell));
        tripIdTitleView.setText("#000");

        //ContentCell
        startingHour.setText(simpleDate.format(trip.getStartingHourAsDate()));
        sourceAddress.setText(trip.getSourceAddress());
        destinationAddress.setText(trip.getDestinationAddress());
        distanceFromYou.setText(context.getString(R.string.in_progress_cell));
        tripDistance.setText(context.getString(R.string.in_progress_cell));


        //Disable Action button preventing contact the customer before requesting the trip.
        if (!wasRequestByYou()) {
            callButton.setEnabled(false);
            smsButton.setEnabled(false);
            emailButton.setEnabled(false);

        } else {
            //Unfold the current trip cell
            final FoldingCell cell = (FoldingCell) itemView;
            cell.post(new Runnable() {
                @Override
                public void run() {
                    cell.toggle(true);

                }
            });
            requestTripButton.setVisibility(View.GONE);
            endTripButton.setVisibility(View.VISIBLE);
            cancelButton.setVisibility(View.VISIBLE);
            dividerRequested.setVisibility(View.VISIBLE);

        }

        new CalculateDistanceAsyncTask().doInBackground(null); //Calculate Distance AsyncTask

    }


    /**
     * ClickListener to add the customer to your contact list.
     *
     * @return The Customer Add Contact Click Listener
     */
    private View.OnClickListener onCustomerClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Prevent Contact the customer before requesting the trip
                if (!wasRequestByYou()) {
                    Toast.makeText(context, "You need to Request this trip before contact the customer !", Toast.LENGTH_LONG).show();
                    return;
                }

                AddContact(); //Content Provider function to add contact
            }
        };
    }

    /**
     * ClickListener to send SMS confirmation to the customer
     * NOTE : NOT TESTED DUE TO SIMULATOR.
     *
     * @return The SMS confirmation Click Listener
     */
    private View.OnClickListener onSmsClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //TODO tester avec un vrai tel
                if (!wasRequestByYou()) {
                    Toast.makeText(context, "You need to Request this trip before contact the customer !", Toast.LENGTH_LONG).show();
                    return;
                }

                String smsBody = " Hi " + trip.getCustomerName() + " ! \n  \n We are delighted to tell you that, " + driver.getFirstName() + " " + driver.getLastName() + " will pick you up soon ! \n \n Best regards, \n TaxiCity";

                //Send the SMS
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(trip.getCustomerPhone(), driver.getPhoneNumber(), smsBody, null, null);

                Toast.makeText(context, "SMS sent !", Toast.LENGTH_SHORT).show();

            }
        };
    }

    /**
     * ClickListener to CALL the customer
     *
     * @return The CALL Click Listener
     */
    private View.OnClickListener onCallClickListner() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!wasRequestByYou()) {
                    Toast.makeText(context, "You need to Request this trip before contact the customer !", Toast.LENGTH_LONG).show();
                    return;
                }
                //Intent for a new call
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + trip.getCustomerPhone()));
                context.startActivity(intent);
            }
        };
    }

    /**
     * ClickListener to Request the trip.
     * <p>
     * 1) If the driver isn't busy
     * 1.1) Change The trip info and update it.
     * 1.2) On Update Success set his key to the busy get.
     * 1.3) Enable UI Button and Prevent Folding
     * <p>
     * 2) Else - Error Toast
     *
     * @return The Request Trip Click Listener
     */
    private View.OnClickListener onRequestTripClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //If not Busy Add change trip status
                if (!WaitingTripAdapter.isBusyDriver()) {

                    //Change trip info
                    trip.setStatusAsEnum(Trip.TripStatus.IN_PROGRESS);
                    trip.setDriverEmail(driver.getEmail());
                    //Update the trip
                    db.updateTrip(trip, new ActionCallBack() {
                        @Override
                        public void onSuccess(Object obj) {

                            busyKey = trip.getKey();

                            if (wasRequestByYou()) {
                                //Enable Button Action
                                callButton.setEnabled(true);
                                smsButton.setEnabled(true);
                                emailButton.setEnabled(true);

                                //Prevent Fold the current Cell
                                itemView.setOnClickListener(null);
                                imgHeader.setOnClickListener(null);

                                //Change Button interface
                                requestTripButton.setVisibility(View.GONE);
                                endTripButton.setVisibility(View.VISIBLE);
                                cancelButton.setVisibility(View.VISIBLE);
                                dividerRequested.setVisibility(View.VISIBLE);
                            }


                        }

                        @Override
                        public void onFailure(Exception exception) {

                            Toast.makeText(context, exception.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onProgress(String status, double percent) {

                        }
                    });

                } else {
                    Toast.makeText(context, "Your are Busy !", Toast.LENGTH_SHORT).show();
                }


            }
        };

    }

    /**
     * ClickListener to send EMAIL confirmation to the customer
     *
     * @return The EMAIL confirmation Click Listener
     */
    private View.OnClickListener onEmailClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!wasRequestByYou()) {
                    Toast.makeText(context, "You need to Request this trip before contact the customer !", Toast.LENGTH_LONG).show();
                    return;
                }


                //Email Body
                String emailBody = " Hi " + trip.getCustomerName() + " ! \n  \n We are delighted to tell you that, " + driver.getFirstName() + " " + driver.getLastName() + " will pick you up soon ! \n \n Best regards, \n TaxiCity";

                //Initialisation of the email Intent
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("message/rfc822");
                i.putExtra(Intent.EXTRA_EMAIL, new String[]{trip.getCustomerEmail()});
                i.putExtra(Intent.EXTRA_SUBJECT, "TaxiCity : Your trip has started !");
                i.putExtra(Intent.EXTRA_TEXT, emailBody);
                try {
                    context.startActivity(Intent.createChooser(i, "Send mail..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(context, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                }

            }
        };
    }


    private View.OnClickListener onCancelClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDialog(new SimpleCallBack() {
                    @Override
                    public void execute() {
                        cancelTrip();
                    }
                });

            }
        };
    }

    private View.OnClickListener onEndTripClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(new SimpleCallBack() {
                    @Override
                    public void execute() {
                        endTrip();
                    }
                });

            }
        };
    }


    private void endTrip() {
        if (!wasRequestByYou())
            return;

        trip.setEndingHour(String.valueOf(System.currentTimeMillis()));
        trip.setStatusAsEnum(Trip.TripStatus.FINISHED);

        db.updateTrip(trip, new ActionCallBack() {
            @Override
            public void onSuccess(Object obj) {
                AlertDialog show = new AlertDialog.Builder(context)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Trip Finished !")
                        .setMessage("TOTAL COST :  23$")
                        .setPositiveButton("Fine !", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }

                        })
                        .show();

                busyKey = null;
                ((ViewGroup) itemView.getParent()).removeView(itemView);
                getAdapterPosition();

            }

            @Override
            public void onFailure(Exception exception) {

            }

            @Override
            public void onProgress(String status, double percent) {

            }
        });


    }


    private void cancelTrip() {
        if (!wasRequestByYou())
            return;

        trip.setStatusAsEnum(Trip.TripStatus.AVAILABLE);
        trip.setDriverEmail(null);

        db.updateTrip(trip, new ActionCallBack() {
            @Override
            public void onSuccess(Object obj) {
                Log.i(TAG, "Trip Cancelled");
                //Enable Button Action
                callButton.setEnabled(false);
                smsButton.setEnabled(false);
                emailButton.setEnabled(false);

                //Prevent Fold the current Cell
                itemView.setOnClickListener(foldListener);
                imgHeader.setOnClickListener(foldListener);

                //Change Button interface
                requestTripButton.setVisibility(View.VISIBLE);
                endTripButton.setVisibility(View.GONE);
                cancelButton.setVisibility(View.GONE);
                dividerRequested.setVisibility(View.GONE);


            }

            @Override
            public void onFailure(Exception exception) {
                Log.e(TAG, "onFailure: ", exception);

            }

            @Override
            public void onProgress(String status, double percent) {

            }
        });

        busyKey = null;
    }


    private void showDialog(final SimpleCallBack function) {
        AlertDialog show = new AlertDialog.Builder(context)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Confirm Action")
                .setMessage("Are you sure you want to do this ?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (function != null) {
                            function.execute();
                        }
                    }

                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }


    /**
     * Initialisation and build of Content Provider to add the customer to driver's contact list.
     */
    private void AddContact() {
        if (contactAdded) //Preventdouble contact (singleton)
            return;

        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
        int rawContactInsertIndex = ops.size();

        ops.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null).build());

        //Phone Number
        ops.add(ContentProviderOperation
                .newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID,
                        rawContactInsertIndex)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, trip.getCustomerPhone())
                .withValue(ContactsContract.Contacts.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, "1").build());

        //Display name/Contact name
        ops.add(ContentProviderOperation
                .newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Contacts.Data.RAW_CONTACT_ID,
                        rawContactInsertIndex)
                .withValue(ContactsContract.Contacts.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, trip.getCustomerName())
                .build());
        //Email details
        ops.add(ContentProviderOperation
                .newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID,
                        rawContactInsertIndex)
                .withValue(ContactsContract.Contacts.Data.MIMETYPE, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Email.DATA, trip.getCustomerEmail())
                .withValue(ContactsContract.Contacts.Data.MIMETYPE, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Email.TYPE, "1").build());

        try {
            ContentProviderResult[] res = context.getContentResolver().applyBatch(
                    ContactsContract.AUTHORITY, ops);
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (OperationApplicationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        Toast.makeText(context, "Contact added to you phone !", Toast.LENGTH_LONG).show();

        contactAdded = true;

    }


    /**
     * Calculation of Trip distance, and distance between the driver and the customer location.
     * Update the UI.
     */
    private class CalculateDistanceAsyncTask extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {
            //Calculate distance in meter and convert it to km
            String distanceFromYouKm = LocationHelper.calculDistanceFromYou(trip, driver) / 1000 + " km";
            String tripDistanceKm = LocationHelper.calculTripDistance(trip) / 1000 + " km";

            //Update UI
            tripDistance.setText(tripDistanceKm);
            distanceFromYou.setText(distanceFromYouKm);

            tripDistanceTitleView.setText(tripDistanceKm);
            distanceFromYouTitleView.setText(distanceFromYouKm);

            return null;
        }

    }

    /**
     * Check if this cell is the current trip requested by the driver.
     *
     * @return 'true' if the key matching else 'false'.
     */
    private boolean wasRequestByYou() {
        if (busyKey == null)
            return false;
        return busyKey.equals(trip.getKey());
    }

    /* ********** GETTERS AND SETTER ************** */

    /**
     * @return Fold cell Listener
     */
    public View.OnClickListener getFoldListener() {
        return foldListener;
    }

    /**
     * Set the Fold cell Listener
     *
     * @param foldListener Fold cell Listener to set.
     */
    public void setFoldListener(View.OnClickListener foldListener) {
        this.foldListener = foldListener;
    }


    /**
     * @return The current Trip of the cell.
     */
    public Trip getTrip() {
        return trip;
    }

    /**
     * Set the Current Trip of the cell.
     *
     * @param trip The trip to set.
     */
    public void setTrip(Trip trip) {
        this.trip = trip;
    }


    /**
     * @return List of Trip for TESTS
     */
    public static ArrayList<Trip> getTestingList() {
        ArrayList<Trip> items = new ArrayList<>();
        Trip tr = new Trip();
        tr.setCustomerEmail("netanelcohensolal@me.com");
        tr.setCustomerName("Netanel Cohen Solal");
        tr.setCustomerPhone("0587250797");
        tr.setDestinationAddress("1 Rue de la Destination , 95400, DestCity");
        tr.setDestinationLatitude(32);
        tr.setDestinationLongitude(31);
        tr.setDriverEmail("michelle@me.com");
        tr.setKey("key1");
        tr.setSourceAddress("50 Rue du 8 mai, 92400, Sarcelles");
        tr.setSourceLatitude(33);
        tr.setSourceLongitude(32);
        //    tr.setStartingHourAsDate(new Date());
        tr.setStatusAsEnum(Trip.TripStatus.AVAILABLE);


        Trip tr1 = new Trip();
        tr1.setCustomerEmail("jeanyves@me.com");
        tr1.setCustomerName("Jean Louis");
        tr1.setCustomerPhone("5642");
        tr1.setDestinationAddress("1 Rue du jean louis, 65400, BelleViulle");
        tr1.setDestinationLatitude(32);
        tr1.setDestinationLongitude(31);
        tr1.setDriverEmail("michelle@me.com");
        tr1.setKey("key2");
        tr1.setSourceAddress("50 Rue Victor hugo, 92400, Dubois");
        tr1.setSourceLatitude(33);
        tr1.setSourceLongitude(32);
//        tr1.setStartingHourAsDate(new Date());
        tr1.setStatusAsEnum(Trip.TripStatus.AVAILABLE);

        Trip tr2 = new Trip();
        tr2.setCustomerEmail("jerusalem@me.com");
        tr2.setCustomerName("JeruSalem");
        tr2.setCustomerPhone("8485");
        tr2.setDestinationAddress("1 Rue du tel aviv, 65400, Tel Aviv");
        tr2.setDestinationLatitude(32);
        tr2.setDestinationLongitude(31);
        tr2.setDriverEmail("michelle@me.com");
        tr2.setKey("key3");
        tr2.setSourceAddress("50 Rue Satre, 92400, Jerusalem");
        tr2.setSourceLatitude(33);
        tr2.setSourceLongitude(32);
        //    tr2.setStartingHourAsDate(new Date());
        tr2.setStatusAsEnum(Trip.TripStatus.AVAILABLE);

        Trip tr3 = new Trip();
        tr3.setCustomerEmail("jerusalem@me.com");
        tr3.setCustomerName("JeruSalem");
        tr3.setCustomerPhone("8485");
        tr3.setDestinationAddress("1 Rue du tel aviv, 65400, Tel Aviv");
        tr3.setDestinationLatitude(33);
        tr3.setDestinationLongitude(31);
        tr3.setDriverEmail("michelle@me.com");
        tr3.setKey("key4");
        tr3.setSourceAddress("50 Rue Satre, 93400, Jerusalem");
        tr3.setSourceLatitude(33);
        tr3.setSourceLongitude(33);
        //    tr3.setStartingHourAsDate(new Date());
        tr3.setStatusAsEnum(Trip.TripStatus.AVAILABLE);

        Trip tr4 = new Trip();
        tr4.setCustomerEmail("jerusalem@me.com");
        tr4.setCustomerName("JeruSalem");
        tr4.setCustomerPhone("8485");
        tr4.setDestinationAddress("1 Rue du tel aviv, 65400, Tel Aviv");
        tr4.setDestinationLatitude(44);
        tr4.setDestinationLongitude(41);
        tr4.setDriverEmail("michelle@me.com");
        tr4.setKey("key5");
        tr4.setSourceAddress("50 Rue Satre, 94400, Jerusalem");
        tr4.setSourceLatitude(44);
        tr4.setSourceLongitude(44);
        //    tr4.setStartingHourAsDate(new Date());
        tr4.setStatusAsEnum(Trip.TripStatus.AVAILABLE);

        items.add(tr);
        items.add(tr1);
        items.add(tr2);
        items.add(tr3);
        items.add(tr4);
        items.add(tr);
        items.add(tr1);
        items.add(tr2);
        items.add(tr3);
        items.add(tr4);

        return items;

    }


}
