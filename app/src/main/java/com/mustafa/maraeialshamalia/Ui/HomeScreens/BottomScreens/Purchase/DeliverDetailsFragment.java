package com.mustafa.maraeialshamalia.Ui.HomeScreens.BottomScreens.Purchase;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.mustafa.maraeialshamalia.Data.RetrofitClient;
import com.mustafa.maraeialshamalia.Models.Cites.Cites;
import com.mustafa.maraeialshamalia.Models.Cites.Data;
import com.mustafa.maraeialshamalia.Models.Cites.Neighborhood;
import com.mustafa.maraeialshamalia.R;
import com.mustafa.maraeialshamalia.Ui.MapsActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeliverDetailsFragment extends Fragment
{
    private View mView;
    private EditText dateField, timeField, notesField;
    private Spinner citySpinner, neighborhoodSpinner;
    private ImageView openMap;
    private Button next;
    private Calendar myCalendar;
    private List<Data> citesList = new ArrayList<> ();
    private List<String> citesItem = new ArrayList<> ();
    private List<Neighborhood> neighborhoodsList = new ArrayList<> ();
    private List<String> neighborhoodsItem = new ArrayList<> ();
    private int cityId, neighborhoodId;
    private double lat, longt;
    private String language;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        mView = inflater.inflate ( R.layout.fragment_deliver_details, null );
        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated ( savedInstanceState );

        SharedPreferences sharedPreferences = getContext ().getSharedPreferences ( "app_language", Context.MODE_PRIVATE );
        language = sharedPreferences.getString ( "language","en" );

        initViews();
        pickDate ();
        pickTime();
        getCityWithNeighborhood();
    }

    @Override
    public void onStart()
    {
        super.onStart ();

        initSP();
    }

    private void initSP()
    {
        SharedPreferences sharedPreferences = getContext ().getSharedPreferences ( "myLocation", Context.MODE_PRIVATE );

        String latt = sharedPreferences.getString ( "lat", null );
        if (latt != null)
            lat = Double.parseDouble ( latt );

        String longtt = sharedPreferences.getString ( "longt", null );

        if (longtt != null)
            longt = Double.parseDouble ( longtt );

        sharedPreferences.edit ().clear ().apply ();
    }

    private void getCityWithNeighborhood()
    {
        RetrofitClient.getInstance ().getCityWithNeighborhood (language).enqueue ( new Callback<Cites> ()
        {
            @Override
            public void onResponse(Call<Cites> call, Response<Cites> response)
            {
                if (response.isSuccessful ())
                    if (response.code () == 200)
                        if (response.body () != null)
                            if (response.body ().getKey ().equals ( "success" ))
                            {
                                citesList = response.body ().getData ();
                                prepare();
                            }
            }

            @Override
            public void onFailure(Call<Cites> call, Throwable t)
            {

            }
        } );
    }

    private void prepare()
    {
        for (int i = 0 ; i < citesList.size () ; i++)
        {
            citesItem.add ( citesList.get ( i ).getTitle () );
        }

        initSpinner();
    }

    private void initSpinner()
    {
        citySpinner = mView.findViewById ( R.id.city );
        neighborhoodSpinner = mView.findViewById ( R.id.neighborhood );

        ArrayAdapter<String> citesAdapter = new ArrayAdapter<> ( getContext (), R.layout.support_simple_spinner_dropdown_item, citesItem );
        citySpinner.setAdapter ( citesAdapter );

        onCitySpinnerSelected();
    }

    private void onCitySpinnerSelected()
    {
        citySpinner.setOnItemSelectedListener ( new AdapterView.OnItemSelectedListener ()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                cityId = citesList.get ( position ).getId ();
                getNeighborhoods();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
                cityId = citesList.get ( 0 ).getId ();
                getNeighborhoods();
            }
        } );
    }

    private void getNeighborhoods()
    {
        neighborhoodsList = ( citesList.get ( cityId-1 ).getNeighborhoods () );
        neighborhoodsItem.clear ();

        for (int i = 0 ; i < neighborhoodsList.size () ; i++)
        {
            neighborhoodsItem.add ( neighborhoodsList.get ( i ).getTitle () );
        }

        ArrayAdapter<String> neighborhoodAdapter = new ArrayAdapter<> ( getContext (), R.layout.support_simple_spinner_dropdown_item, neighborhoodsItem );
        neighborhoodSpinner.setAdapter ( neighborhoodAdapter );

        onNeighborhoodSpinnerSelected();
    }

    private void onNeighborhoodSpinnerSelected()
    {
        neighborhoodSpinner.setOnItemSelectedListener ( new AdapterView.OnItemSelectedListener ()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                neighborhoodId = neighborhoodsList.get ( position ).getId ();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
                neighborhoodId = neighborhoodsList.get ( 0 ).getId ();
            }
        } );
    }

    private void pickTime()
    {
        timeField.setOnClickListener ( v ->
        {
            Calendar mcurrentTime = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat ("hh:mm");

            int hour = mcurrentTime.get ( Calendar.HOUR_OF_DAY );
            int minute = mcurrentTime.get( Calendar.MINUTE );

            try {
                mcurrentTime.setTime ( sdf.parse ( hour+":"+minute ) );
            } catch (ParseException e) {
                e.printStackTrace ();
            }

            TimePickerDialog mTimePicker;
            mTimePicker = new TimePickerDialog(getContext (), (timePicker, selectedHour, selectedMinute) -> timeField.setText( pad ( selectedHour ) + ":" + pad ( selectedMinute ) ), hour, minute, true);
            mTimePicker.setTitle("Select Time");
            mTimePicker.show();
        } );
    }

    private String pad(int value)
    {

        if(value<10){
            return "0"+value;
        }


        return ""+value;
    }

    private void pickDate()
    {
        myCalendar = Calendar.getInstance();

        DatePickerDialog.OnDateSetListener date = (view, year, monthOfYear, dayOfMonth) ->
        {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        };

        dateField.setOnClickListener ( v ->
                new DatePickerDialog(getContext (), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show()
        );
    }

    private void updateLabel()
    {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        dateField.setText(sdf.format(myCalendar.getTime()));
    }

    private void initViews()
    {
        dateField = mView.findViewById(R.id.calender);
        timeField = mView.findViewById ( R.id.clock );
        notesField = mView.findViewById ( R.id.notes_field );
        openMap = mView.findViewById ( R.id.map );
        next = mView.findViewById ( R.id.next );

        openNotification ();
        onClicks();
    }

    private void openNotification()
    {
        ImageView notification = mView.findViewById ( R.id.notification );
        notification.setOnClickListener ( v ->
        {
            NavController navController = Navigation.findNavController ( getActivity (), R.id.nav_host_fragment );
            navController.navigate ( R.id.action_deliverDetailsFragment_to_notificationFragment );
        } );
    }

    private void onClicks()
    {
        ImageView back = mView.findViewById ( R.id.back );
        back.setOnClickListener ( v -> requireActivity ().onBackPressed () );

        next.setOnClickListener ( v -> {
            String date = dateField.getText ().toString ();
            String time = timeField.getText ().toString ();
            String notes = notesField.getText ().toString ();
            checkValidData(date,time,notes);
        } );

        openMap.setOnClickListener ( v -> startActivity ( new Intent (getContext (), MapsActivity.class ) ) );
    }

    private void checkValidData(String date, String time, String notes)
    {
        if (date.isEmpty ())
        {
            dateField.requestFocus ();
            Toast.makeText ( getContext (), "التاريخ مطلوب", Toast.LENGTH_SHORT ).show ();
            return;
        }

        if (time.isEmpty ())
        {
            timeField.requestFocus ();
            Toast.makeText ( getContext (), "الوقت مطلوب", Toast.LENGTH_SHORT ).show ();
            return;
        }

        if(lat == 0 || longt == 0)
        {
            openMap.requestFocus ();
            Toast.makeText ( getContext (), "الموقع مطلوب", Toast.LENGTH_SHORT ).show ();
            return;
        }

        addNewOrder ( date, time, notes );
    }

    private void addNewOrder(String date, String time, String notes)
    {
        NavController navController = Navigation.findNavController ( getActivity (), R.id.nav_host_fragment );
        Bundle bundle = new Bundle ();
        bundle.putString ( "date", date );
        bundle.putString ( "time", time );
        bundle.putInt ( "cityId", cityId );
        bundle.putInt ( "neighborhoodId", neighborhoodId );
        bundle.putString ( "notes", notes );
        bundle.putDouble ( "lat", lat );
        bundle.putDouble ( "longt", longt );
        navController.navigate ( R.id.action_deliverDetailsFragment_to_confirmationFragment, bundle );
    }
}
