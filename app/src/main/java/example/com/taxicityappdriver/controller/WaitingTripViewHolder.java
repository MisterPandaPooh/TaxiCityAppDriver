package example.com.taxicityappdriver.controller;

import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.Context;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import example.com.taxicityappdriver.R;
import example.com.taxicityappdriver.entities.Driver;
import example.com.taxicityappdriver.entities.Trip;

public class WaitingTripViewHolder extends RecyclerView.ViewHolder  {


    public static Context context;
    public static Driver driver;
    private static final SimpleDateFormat simpleDate = new SimpleDateFormat("dd/MM/yyyy - HH:mm");



    private Trip trip;
    private boolean isInit;
    private boolean wasRequestByYou;
    private boolean contactAdded;
    private final String TAG = "TripItemViewAdapter";

    public WaitingTripViewHolder(@NonNull View itemView, Context c) {
        super(itemView);
        isInit = false;
        wasRequestByYou = false;
        contactAdded = false;
        context = c;
    }


    public TextView startingHour;

    public TextView sourceAddress;

    public TextView destinationAddress;

    public TextView distanceFromYou;

    public TextView tripDistance;

    public TextView startingHourTitleView;

    public TextView destinationAddressTitleView;

    public TextView distanceFromYouTitleView;

    public TextView tripDistanceTitleView;

    public TextView tripIdTitleView;

    public LinearLayout customerButton;
    public Button requestTripButton;
    public Button smsButton;
    public Button callButton;
    public Button emailButton;
    public ImageView imgHeader;

    private View.OnClickListener foldListener;

    private void bind(){
        View cell = itemView;
        this.startingHourTitleView = cell.findViewById(R.id.datetime_trip_title_view);
        this.destinationAddressTitleView = cell.findViewById(R.id.trip_destination_title_view);
        this.distanceFromYouTitleView = cell.findViewById(R.id.distance_from_you_title_view);
        this.tripDistanceTitleView = cell.findViewById(R.id.trip_distance_title_view);
        this.startingHour = cell.findViewById(R.id.datetime_trip);
        this.customerButton = cell.findViewById(R.id.customer_btn);
        this.sourceAddress = cell.findViewById(R.id.source_address);
        this.destinationAddress = cell.findViewById(R.id.trip_destination);
        this.distanceFromYou = cell.findViewById(R.id.distance_from_you);
        this.tripDistance = cell.findViewById(R.id.trip_distance);
        this.requestTripButton = cell.findViewById(R.id.request_trip_btn);
        this.smsButton = cell.findViewById(R.id.sms_btn);
        this.callButton = cell.findViewById(R.id.call_btn);
        this.emailButton = cell.findViewById(R.id.mail_btn);
        this.imgHeader = cell.findViewById(R.id.header_img);
        this.tripIdTitleView = cell.findViewById(R.id.trip_title_id);
        cell.setTag(itemView);

    }



    public void initItems() {
        bind();
        if (trip == null || isInit)
            return;
        if (startingHour == null || customerButton == null || sourceAddress == null || destinationAddress == null || distanceFromYou == null
                || tripDistance == null || startingHourTitleView == null || destinationAddressTitleView == null || distanceFromYouTitleView == null || tripDistanceTitleView == null || tripIdTitleView == null)
            return;
        if (requestTripButton == null || smsButton == null || callButton == null || emailButton == null || imgHeader == null)
            return;

        if (foldListener == null)
            return;

        callButton.setEnabled(false);
        smsButton.setEnabled(false);
        emailButton.setEnabled(false);


        initListener();
        initContent();
        new CalculDistanceAsyncTask().doInBackground(null);
        isInit = true;

    }

    private void initListener() {
        customerButton.setOnClickListener(onCustomerClickListner());
        smsButton.setOnClickListener(onSmsClickListner());
        callButton.setOnClickListener(onCallClickListner());
        emailButton.setOnClickListener(onEmailClickListner());
        imgHeader.setOnClickListener(foldListener);
        itemView.setOnClickListener(foldListener);
        requestTripButton.setOnClickListener(onRequestTripClickListener());

    }

    private void initContent() {

        //  startingHour.setText(simpleDate.format(trip.getStartingHourAsDate()));
        //  startingHourTitleView.setText(simpleDate.format(trip.getStartingHourAsDate()));

        sourceAddress.setText(trip.getSourceAddress());

        destinationAddress.setText(trip.getDestinationAddress());
        destinationAddressTitleView.setText(trip.getDestinationAddress());

        distanceFromYou.setText("TODO");
        distanceFromYouTitleView.setText("TODO");

        tripDistance.setText("TODO");
        tripDistanceTitleView.setText("TODO");

    }




    private View.OnClickListener onCustomerClickListner() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!wasRequestByYou) {
                    Toast.makeText(context, "You need to Request this trip before contact the customer !", Toast.LENGTH_LONG).show();
                    return;
                }
                AddContact();
            }
        };
    }

    private View.OnClickListener onSmsClickListner() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //TODO tester avec un vrai tel
                if (!wasRequestByYou) {
                    Toast.makeText(context, "You need to Request this trip before contact the customer !", Toast.LENGTH_LONG).show();
                    return;
                }

                String smsBody = " Hi " + trip.getCustomerName() + " ! \n  \n We are delighted to tell you that, " + driver.getFirstName() + " " + driver.getLastName() + " will pick you up soon ! \n \n Best regards, \n TaxiCity";

                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(trip.getCustomerPhone(), driver.getPhoneNumber(), smsBody, null, null);

                Toast.makeText(context, "SMS sent !", Toast.LENGTH_SHORT).show();

            }
        };
    }

    private View.OnClickListener onCallClickListner() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!wasRequestByYou) {
                    Toast.makeText(context, "You need to Request this trip before contact the customer !", Toast.LENGTH_LONG).show();
                    return;
                }
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + trip.getCustomerPhone()));
                context.startActivity(intent);
            }
        };
    }

    private View.OnClickListener onRequestTripClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callButton.setEnabled(true);
                smsButton.setEnabled(true);
                emailButton.setEnabled(true);
                wasRequestByYou = true;
            }
        };

    }

    private View.OnClickListener onEmailClickListner() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!wasRequestByYou) {
                    Toast.makeText(context, "You need to Request this trip before contact the customer !", Toast.LENGTH_LONG).show();
                    return;
                }
                String emailBody = " Hi " + trip.getCustomerName() + " ! \n  \n We are delighted to tell you that, " + driver.getFirstName() + " " + driver.getLastName() + " will pick you up soon ! \n \n Best regards, \n TaxiCity";
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


    public static ArrayList<Trip> getTestingList() {
        ArrayList<Trip> items = new ArrayList<>();
        Trip tr = new Trip();
        tr.setCustomerEmail("netanelcohensolal@me.com");
        tr.setCustomerName("Netanel Cohen Solal");
        tr.setCustomerPhone("0587250797");
        tr.setDestinationAddress("1 Rue de la Destination , 95400, DestCity");
        tr.setDestinationLatitude(32);
        tr.setDestinationLongitude(31);
        tr.setDriverID(0);
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
        tr1.setDriverID(0);
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
        tr2.setDriverID(0);
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
        tr3.setDriverID(0);
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
        tr4.setDriverID(0);
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


    public View.OnClickListener getFoldListener() {
        return foldListener;
    }

    public void setFoldListener(View.OnClickListener foldListener) {
        this.foldListener = foldListener;
    }


    private void AddContact() {
        if (contactAdded) //Prevent double contact
            return;
        /*
         * Uri newPerson = addContactName();
         *
         * addMobilePhoneNo(newPerson); addEmail(newPerson);
         * addPostalAddress(newPerson); addOrganization(newPerson);
         */

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

    private float calculTripDistance() {
        float[] result = new float[1];
        Location.distanceBetween(trip.getSourceLatitude(), trip.getSourceLongitude(), trip.getDestinationLatitude(), trip.getDestinationLongitude(), result);
        return result[0];
    }

    private float calculDistanceFromYou() {
        float[] result = new float[1];
        Location.distanceBetween(trip.getSourceLatitude(), trip.getSourceLongitude(), driver.getCurrentLocationLat(), driver.getCurrentLocationLong(), result);
        return result[0];
    }

    private class CalculDistanceAsyncTask extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {
            String distanceFromyoukm = calculDistanceFromYou() / 1000 + " km";
            String tripDistancekm = calculTripDistance() / 1000 + " km";

            distanceFromYouTitleView.setText(distanceFromyoukm);
            distanceFromYou.setText(distanceFromyoukm);

            tripDistanceTitleView.setText(tripDistancekm);
            tripDistance.setText(tripDistancekm);
            return null;
        }
    }


    public Trip getTrip() {
        return trip;
    }

    public void setTrip(Trip trip) {
        this.trip = trip;
    }


}
