package com.mustafa.maraeialshamalia.Data;

import com.mustafa.maraeialshamalia.Models.Asks.Asks;
import com.mustafa.maraeialshamalia.Models.Auth.Auth;
import com.mustafa.maraeialshamalia.Models.Cart.Cart;
import com.mustafa.maraeialshamalia.Models.Cites.Cites;
import com.mustafa.maraeialshamalia.Models.Favourites.Favourites;
import com.mustafa.maraeialshamalia.Models.Home.Home;
import com.mustafa.maraeialshamalia.Models.Notifications.Notifications;
import com.mustafa.maraeialshamalia.Models.Offers.Offers;
import com.mustafa.maraeialshamalia.Models.OrderDetails.OrderDetails;
import com.mustafa.maraeialshamalia.Models.Orders.Orders;
import com.mustafa.maraeialshamalia.Models.Product.Product;
import com.mustafa.maraeialshamalia.Models.Search.Search;
import com.mustafa.maraeialshamalia.Models.Section.Section;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient
{
    private static RetrofitClient retrofitClient;
    private final static String base_URL="https://training.aait-sa.com/api/";
    private final RetrofitHelper retrofitHelper;

    private RetrofitClient()
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(base_URL)
                .addConverterFactory( GsonConverterFactory.create())
                .build();

        retrofitHelper = retrofit.create ( RetrofitHelper.class );
    }

    static public RetrofitClient getInstance()
    {
        if (retrofitClient==null)
        {
            retrofitClient = new RetrofitClient();
        }
        return retrofitClient;
    }

    public Call<Auth> register(String name, String email, String  phone, String password)
    {
        return retrofitHelper.register( name,email,phone,password);
    }

    public Call<Auth> login(String phoneOrMail, String password, String device_id, String device_type)
    {
        return retrofitHelper.login ( phoneOrMail, password, device_id, device_type );
    }

    public Call<Auth> activeCode(int user_id, int code)
    {
        return retrofitHelper.activeCode ( user_id, code, "5555", "android" );
    }

    public Call<Auth> forgetPassword(String phone)
    {
        return retrofitHelper.forgetPassword ( phone );
    }

    public Call<Auth> updatePassword(int user_id, int code, String password)
    {
        return retrofitHelper.updatePassword ( user_id,code,password);
    }

    public Call<Auth> resendCode(int user_id)
    {
        return retrofitHelper.resendCode ( user_id );
    }

    public Call<Home> getHomeItems(String lang)
    {
        return retrofitHelper.getHomeItems (lang);
    }

    public Call<Auth> logout()
    {
        return retrofitHelper.logout ();
    }

    public Call<Auth> getProfile(String token)
    {
        return retrofitHelper.getProfile ( token );
    }

    public Call<Auth> updateProfile(MultipartBody.Part image_file, String token, String name, String email, String phone)
    {
        return retrofitHelper.updateProfile ( image_file,token, name, email, phone);
    }

    public Call<Auth> resetPassword(String ApiToken, String old_password, String new_Password)
    {
        return retrofitHelper.resetPassword ( ApiToken, old_password, new_Password );
    }

    public Call<Favourites> getFavourites(String ApiToken)
    {
        return retrofitHelper.getFavourites ( ApiToken );
    }

    public Call<Offers> getOffers()
    {
        return retrofitHelper.getOffers ();
    }

    public Call<Product> getProduct(String lang, int id)
    {
        return retrofitHelper.getProduct ( lang,id );
    }

    public Call<Cart> getCartItem(String ApiToken)
    {
        return retrofitHelper.getCartItems ( ApiToken );
    }

    public Call<Cart> addItemToCart(String ApiToken, int product_id, String order_type, String item_type , int quantity, int size_id, int cut_id, int encapsulation_id, int head_id, int minced_quantity, String notes)
    {
        return retrofitHelper.addItemToCart ( ApiToken,product_id, order_type, item_type, quantity, size_id, cut_id, encapsulation_id, head_id, minced_quantity, notes );
    }

    public Call<Cart> addItemToCart(String ApiToken, int product_id, String order_type, String item_type , int quantity, int size_id, int cut_id, int encapsulation_id, int head_id, int charity_id, int minced_quantity, String notes)
    {
        return retrofitHelper.addItemToCart ( ApiToken,product_id, order_type, item_type, quantity, size_id, cut_id, encapsulation_id, head_id, charity_id, minced_quantity, notes );
    }

//    public Call<Cart> editCartQuantity(String ApiToken, List<Map<String,String>> data)
//    {
//        return retrofitHelper.editCartQuantity ( ApiToken, data );
//    }

    public Call<Orders> getOrders(String ApiToken, String status)
    {
        return retrofitHelper.getOrders ( ApiToken, status );
    }

    public Call<OrderDetails> getOrderDetails (String lang, int order_id)
    {
        return retrofitHelper.getOrderDetails ( lang,order_id );
    }

    public Call<Auth> contactUs(String name, String email, String message)
    {
        return retrofitHelper.contactUs ( name, email, message );
    }

    public Call<Product> getTerms(String lang)
    {
        return retrofitHelper.getTerms (lang);
    }

    public Call<Product> getAbout(String lang)
    {
        return retrofitHelper.getAbout (lang);
    }

    public Call<Asks> getAsks(String lang)
    {
        return retrofitHelper.getAsks (lang);
    }

    public Call<Notifications> getNotifications(String lang, String ApiToken)
    {
        return retrofitHelper.getNotifications ( lang,ApiToken );
    }

    public Call<Section> getSection(String lang, int section_id, String price_sort)
    {
        return retrofitHelper.getSection ( lang, section_id, price_sort );
    }

    public Call<Orders> newCartOrder(String ApiToken, String delivery_date, String delivery_time, int city_id, int neighbourhood_id, String notes, double lat, double lng, int coupon_num)
    {
        return retrofitHelper.newCartOrder ( ApiToken, delivery_date, delivery_time, city_id, neighbourhood_id, notes, lat, lng, coupon_num );
    }

    public Call<Orders> newCartOrder(String ApiToken, String delivery_date, String delivery_time, int city_id, int neighbourhood_id, String notes, double lat, double lng)
    {
        return retrofitHelper.newCartOrder ( ApiToken, delivery_date, delivery_time, city_id, neighbourhood_id, notes, lat, lng );
    }

    public Call<Cites> getCityWithNeighborhood(String lang)
    {
        return retrofitHelper.getCityWithNeighborhood (lang);
    }

    public Call<Orders> checkCoupon(int coupon_num, int cart_items)
    {
        return retrofitHelper.checkCoupon ( coupon_num, cart_items );
    }

    public Call<Auth> sendTransfer(MultipartBody.Part image_file, String ApiToken, String bank_name, String account_name, String account_number, int amount)
    {
        return retrofitHelper.sendTransfer ( image_file, ApiToken, bank_name, account_name, account_number, amount );
    }

    public Call<Auth> sendTransfer(MultipartBody.Part image_file, String ApiToken, String bank_name, String account_name, String account_number, int amount, int order_id)
    {
        return retrofitHelper.sendTransfer ( image_file, ApiToken, bank_name, account_name, account_number, amount, order_id );
    }

    public Call<Orders> orderPayType(String ApiToken, int order_id, String pay_type)
    {
        return retrofitHelper.orderPayType ( ApiToken, order_id, pay_type );
    }

    public Call<Cart> removeFromCart(String ApiToken, int cart_item_id)
    {
        return retrofitHelper.removeFromCart ( ApiToken, cart_item_id );
    }

    public Call<Favourites> toggleFavouriteState(String ApiToken, int product_id)
    {
        return retrofitHelper.toggleFavouriteState ( ApiToken, product_id );
    }

    public Call<Section> filterSection(int section_id, String price_sort)
    {
        return retrofitHelper.filterSection ( section_id, price_sort );
    }

    public Call<Search> search(String search)
    {
        return retrofitHelper.search ( search );
    }

    public Call<Auth> changeLanguage(String ApiToken, String lang)
    {
        return retrofitHelper.changeLanguage ( ApiToken, lang );
    }

}