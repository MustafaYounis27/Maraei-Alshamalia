package com.mustafa.maraeialshamalia.Ui.HomeScreens.BottomScreens.Purchase;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.mustafa.maraeialshamalia.Data.RealPathUtil;
import com.mustafa.maraeialshamalia.Data.RetrofitClient;
import com.mustafa.maraeialshamalia.Models.Auth.Auth;
import com.mustafa.maraeialshamalia.Models.Orders.Orders;
import com.mustafa.maraeialshamalia.R;
import com.mustafa.maraeialshamalia.Ui.HomeScreens.HomeActivity;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentFragment extends Fragment
{
    private View mView;
    private EditText couponField, ownerNameField, bankNameField, accountNumField, openCamera;
    private TextView paymentMethodText, changePaymentMethod, activeCoupon, balanceField, bankNumText;
    private Button next;
    private LinearLayout onlineLayout, bankLayout, cashLayout;
    private Bundle bundle;
    private int total, cartItems, cityId, neighborhoodId, couponNum = 0;
    private double lat, longt;
    private String notes, time, date, token, payType = "online", ownerName, bankName, accountNum;
    private Uri transferImage = null;
    private ProgressDialog dialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        mView = inflater.inflate ( R.layout.fragment_payment, null );
        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated ( savedInstanceState );

        SharedPreferences sharedPreferences1 = getContext ().getSharedPreferences ( "myToken", Context.MODE_PRIVATE );
        token = sharedPreferences1.getString ( "token", null );

        if(getArguments () != null)
        {
            bundle = getArguments ();
            getBundle();
        }

        initViews();
        initDialog();
    }

    private void initDialog()
    {
        dialog = new ProgressDialog (getContext ());
        dialog.setMessage("برجاء الانتظار ...");
        dialog.setCancelable(false);
    }

    private void getBundle()
    {
        total = bundle.getInt ( "total" );
        cartItems = bundle.getInt ( "cartItems" );
        cityId = bundle.getInt ( "cityId" );
        neighborhoodId = bundle.getInt ( "neighborhoodId" );

        date = bundle.getString ( "date" );
        time = bundle.getString ( "time" );
        notes = bundle.getString ( "notes" );

        lat = bundle.getDouble ( "lat" );
        longt = bundle.getDouble ( "longt" );
    }

    private void initViews()
    {
        paymentMethodText = mView.findViewById ( R.id.payment_method_field );
        changePaymentMethod = mView.findViewById ( R.id.change_payment_method );
        couponField = mView.findViewById ( R.id.coupon_field );
        activeCoupon = mView.findViewById ( R.id.active_coupon );
        balanceField = mView.findViewById ( R.id.balance );
        ownerNameField = mView.findViewById ( R.id.owner_name );
        bankNameField = mView.findViewById ( R.id.bank_name );
        accountNumField = mView.findViewById ( R.id.account_num );
        openCamera = mView.findViewById ( R.id.transfer_image );
        bankNumText = mView.findViewById ( R.id.bank_num );

        balanceField.setText ( String.valueOf ( total ) );

        next = mView.findViewById ( R.id.next );

        onlineLayout = mView.findViewById ( R.id.online );
        bankLayout = mView.findViewById ( R.id.bank );
        cashLayout = mView.findViewById ( R.id.cash );

        onClicks();
    }

    private void onClicks()
    {
        changePaymentMethod.setOnClickListener ( v -> initBottomDialog() );

        activeCoupon.setOnClickListener ( v -> {
            String coupon = couponField.getText ().toString ();
            checkCoupon(coupon);
        } );

        next.setOnClickListener ( v -> {
            switch (payType)
            {
                case "online":
                    onlinePayment();
                    break;

                case "bank":
                    checkTransfer();
                    break;

                case "cash":
                    addToOrders ();
                    break;
            }
        } );

        openCamera.setOnClickListener ( v -> {
            Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(galleryIntent, 505);
        } );

        ImageView back = mView.findViewById ( R.id.back );
        back.setOnClickListener ( v -> requireActivity ().onBackPressed () );

        openNotification ();
    }

    private void openNotification()
    {
        ImageView notification = mView.findViewById ( R.id.notification );
        notification.setOnClickListener ( v -> {
            NavController navController = Navigation.findNavController ( getActivity (), R.id.nav_host_fragment );
            navController.navigate ( R.id.action_paymentFragment_to_notificationFragment );
        } );
    }

    private void addToOrders()
    {
        dialog.show ();
        if (couponNum != 0)
            RetrofitClient.getInstance ().newCartOrder ( token,date,time,cityId,neighborhoodId,notes,lat,longt,couponNum ).enqueue ( new Callback<Orders> ()
            {
                @Override
                public void onResponse(Call<Orders> call, Response<Orders> response)
                {
                    if (response.isSuccessful ())
                        if (response.code () == 200)
                            if (response.body () != null)
                                if (response.body ().getKey ().equals ( "success" ))
                                {
                                    Toast.makeText ( getContext (), response.body ().getMsg (), Toast.LENGTH_SHORT ).show ();
                                    Toast.makeText ( getContext (), response.body ().getData ().getOrder_id ()+"", Toast.LENGTH_SHORT ).show ();
                                    checkPayType (response.body ().getData ().getOrder_id ());
                                }else{
                                    Toast.makeText ( getContext (), response.body ().getMsg (), Toast.LENGTH_SHORT ).show ();
                                    dialog.dismiss ();
                                }else{
                                Toast.makeText ( getContext (), "يرجى اعادة المحاولة", Toast.LENGTH_SHORT ).show ();
                                dialog.dismiss ();
                            }else{
                                Toast.makeText ( getContext (), "يرجى اعادة المحاولة", Toast.LENGTH_SHORT ).show ();
                                dialog.dismiss ();
                            }else{
                                Toast.makeText ( getContext (), "يرجى اعادة المحاولة", Toast.LENGTH_SHORT ).show ();
                                dialog.dismiss ();
                            }
                }

                @Override
                public void onFailure(Call<Orders> call, Throwable t)
                {
                    Toast.makeText (getContext (), t.getMessage (), Toast.LENGTH_SHORT ).show ();
                    dialog.dismiss ();
                }
            } );
        else
            RetrofitClient.getInstance ().newCartOrder ( token,date,time,cityId,neighborhoodId,notes,lat,longt ).enqueue ( new Callback<Orders> ()
            {
                @Override
                public void onResponse(Call<Orders> call, Response<Orders> response)
                {
                    if (response.isSuccessful ())
                        if (response.code () == 200)
                            if (response.body () != null)
                                if (response.body ().getKey ().equals ( "success" ))
                                {
                                    Toast.makeText ( getContext (), response.body ().getMsg (), Toast.LENGTH_SHORT ).show ();
                                    Toast.makeText ( getContext (), response.body ().getData ().getOrder_id ()+"", Toast.LENGTH_SHORT ).show ();
                                    checkPayType (response.body ().getData ().getOrder_id ());
                                }else{
                                    Toast.makeText ( getContext (), response.body ().getMsg (), Toast.LENGTH_SHORT ).show ();
                                    dialog.dismiss ();
                                }else{
                                Toast.makeText ( getContext (), "يرجى اعادة المحاولة", Toast.LENGTH_SHORT ).show ();
                                dialog.dismiss ();
                            }else{
                            Toast.makeText ( getContext (), "يرجى اعادة المحاولة", Toast.LENGTH_SHORT ).show ();
                            dialog.dismiss ();
                        }else{
                        Toast.makeText ( getContext (), "يرجى اعادة المحاولة", Toast.LENGTH_SHORT ).show ();
                        dialog.dismiss ();
                    }
                }

                @Override
                public void onFailure(Call<Orders> call, Throwable t)
                {
                    Toast.makeText (getContext (), t.getMessage (), Toast.LENGTH_SHORT ).show ();
                    dialog.dismiss ();
                }
            } );
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult ( requestCode, resultCode, data );
        if (requestCode == 505 && resultCode == getActivity().RESULT_OK && data != null) {
            transferImage = data.getData ();
        }
    }

    private void checkPayType(int order_id)
    {
        switch (payType)
        {
            case "online":
                onlinePayment();
                break;

            case "bank":
                sendTransfer ( order_id );
                break;

            case "cash":
                addPayType ( order_id,payType );
                break;
        }
    }

    private void checkTransfer()
    {
        ownerName = ownerNameField.getText ().toString ();
        bankName = bankNameField.getText ().toString ();
        accountNum = accountNumField.getText ().toString ();

        if (ownerName.isEmpty ())
        {
            ownerNameField.requestFocus ();
            Toast.makeText ( getContext (), ownerNameField.getHint (), Toast.LENGTH_SHORT ).show ();
            return;
        }

        if (bankName.isEmpty ())
        {
            bankNameField.requestFocus ();
            Toast.makeText ( getContext (), bankNameField.getHint (), Toast.LENGTH_SHORT ).show ();
            return;
        }

        if (accountNum.isEmpty ())
        {
            accountNumField.requestFocus ();
            Toast.makeText ( getContext (), accountNumField.getHint (), Toast.LENGTH_SHORT ).show ();
            return;
        }

        if (transferImage == null)
        {
            openCamera.requestFocus ();
            Toast.makeText ( getContext (), openCamera.getHint (), Toast.LENGTH_SHORT ).show ();
            return;
        }

        addToOrders ();
    }

    private void sendTransfer(int order_id)
    {
        File file = new File( RealPathUtil.getRealPath(getContext(), transferImage));
        RequestBody requestFile = RequestBody.create( MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("image", file.getName(), requestFile);

        RetrofitClient.getInstance ().sendTransfer ( body,token,bankName,ownerName,accountNum,total,order_id ).enqueue ( new Callback<Auth> ()
        {
            @Override
            public void onResponse(Call<Auth> call, Response<Auth> response)
            {
                if (response.isSuccessful ())
                    if (response.code () == 200)
                        if (response.body () != null)
                        {
                            Toast.makeText ( getContext (), response.body ().getMsg (), Toast.LENGTH_SHORT ).show ();
                            if(response.body ().getKey ().equals ( "success" ))
                                addPayType (order_id,payType);
                            else
                                dialog.dismiss ();
                        }else{
                            Toast.makeText ( getContext (), "null", Toast.LENGTH_SHORT ).show ();
                            dialog.dismiss ();
                        }
            }

            @Override
            public void onFailure(Call<Auth> call, Throwable t)
            {
                Toast.makeText ( getContext (), t.getMessage (), Toast.LENGTH_SHORT ).show ();
                dialog.dismiss ();
            }
        } );
    }

    private void addPayType(int order_id, String payType)
    {
        RetrofitClient.getInstance ().orderPayType ( token,order_id,payType ).enqueue ( new Callback<Orders> ()
        {
            @Override
            public void onResponse(Call<Orders> call, Response<Orders> response)
            {
                if (response.isSuccessful ())
                    if (response.code () == 200)
                        if (response.body () != null)
                        {
                            dialog.dismiss ();
                            Toast.makeText ( getContext (), response.body ().getMsg (), Toast.LENGTH_SHORT ).show ();
                            startActivity ( new Intent (getContext (), HomeActivity.class ) );
                            requireActivity ().finish ();
                        }else{
                            Toast.makeText ( getContext (), "null", Toast.LENGTH_SHORT ).show ();
                            dialog.dismiss ();
                        }
            }

            @Override
            public void onFailure(Call<Orders> call, Throwable t)
            {
                Toast.makeText ( getContext (), t.getMessage (), Toast.LENGTH_SHORT ).show ();
                dialog.dismiss ();
            }
        } );
    }

    private void onlinePayment()
    {

    }

    private void checkCoupon(String coupon)
    {
        int couponNumber = Integer.parseInt ( coupon );

        RetrofitClient.getInstance ().checkCoupon ( couponNumber,cartItems ).enqueue ( new Callback<Orders> ()
        {
            @Override
            public void onResponse(Call<Orders> call, Response<Orders> response)
            {
                if (response.isSuccessful ())
                    if (response.code () == 200)
                        if (response.body () != null)
                        {
                            Toast.makeText ( getContext (), response.body ().getMsg (), Toast.LENGTH_SHORT ).show ();
                            if (response.body ().getKey ().equals ( "success" ))
                            {
                                couponNum = couponNumber;
                            }
                        }else{
                            Toast.makeText ( getContext (), "null", Toast.LENGTH_SHORT ).show ();
                        }
            }

            @Override
            public void onFailure(Call<Orders> call, Throwable t)
            {
                Toast.makeText ( getContext (), t.getMessage (), Toast.LENGTH_SHORT ).show ();
            }
        } );
    }

    private void initBottomDialog()
    {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog ( getContext (), R.style.BottomSheetDialogTheme );

        View view = LayoutInflater.from ( getContext () ).inflate ( R.layout.payment_dialog, mView.findViewById ( R.id.bottom_container ) );

        bottomSheetDialog.setContentView ( view );
        bottomSheetDialog.show ();

        view.findViewById ( R.id.cash_btn ).setOnClickListener ( v -> {
            Button button = (Button) v;
            setUpLayouts("cash",button.getText ().toString ());
            bottomSheetDialog.dismiss ();
        } );

        view.findViewById ( R.id.online_btn ).setOnClickListener ( v -> {
            Button button = (Button) v;
            setUpLayouts("online",button.getText ().toString ());
            bottomSheetDialog.dismiss ();
        } );

        view.findViewById ( R.id.bank_btn ).setOnClickListener ( v -> {
            Button button = (Button) v;
            setUpLayouts("bank",button.getText ().toString ());
            bottomSheetDialog.dismiss ();
        } );
    }

    private void setUpLayouts(String type, String text)
    {
        payType = type;
        switch (type)
        {
            case "online":
                paymentMethodText.setText (text);
                onlineLayout.setVisibility ( View.VISIBLE );
                bankLayout.setVisibility ( View.GONE );
                cashLayout.setVisibility ( View.GONE );
                break;

            case "bank":
                paymentMethodText.setText (text);
                onlineLayout.setVisibility ( View.GONE );
                bankLayout.setVisibility ( View.VISIBLE );
                cashLayout.setVisibility ( View.GONE );
                break;

            case "cash":
                paymentMethodText.setText (text);
                onlineLayout.setVisibility ( View.GONE );
                bankLayout.setVisibility ( View.GONE );
                cashLayout.setVisibility ( View.VISIBLE );
                break;
        }
    }
}
