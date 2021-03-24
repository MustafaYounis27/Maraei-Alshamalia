package com.mustafa.maraeialshamalia.Data;

import com.mustafa.maraeialshamalia.Models.Asks.Asks;
import com.mustafa.maraeialshamalia.Models.Auth.Auth;
import com.mustafa.maraeialshamalia.Models.Auth.Data;
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

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RetrofitHelper
{
    @POST("sign-up")
    Call<Auth> register(@Query("name") String name,
                        @Query("email") String email,
                        @Query("phone") String phone,
                        @Query("password") String password);

    @POST("sign-in")
    Call<Auth> login(@Query("phoneOrMail") String phoneOrMail,
                     @Query("password") String password,
                     @Query("device_id") String device_id,
                     @Query("device_type") String device_type);

    @POST("active-code")
    Call<Auth> activeCode(@Query("user_id") int user_id,
                          @Query("code") int code,
                          @Query("device_id") String device_id,
                          @Query("device_type") String device_type);

    @POST("forget-password")
    Call<Auth> forgetPassword(@Query("phone") String phone);

    @POST("forget-password/update")
    Call<Auth> updatePassword(@Query("user_id") int user_id,
                              @Query("code") int code,
                              @Query("new_password") String new_password);

    @POST("resend-code")
    Call<Auth> resendCode(@Query("user_id") int user_id);

    @GET("user-home")
    Call<Home> getHomeItems(@Header ( "lang" ) String lang);

    @GET("logout")
    Call<Auth> logout();

    @GET("profile")
    Call<Auth> getProfile(@Header("ApiToken") String ApiToken);

    @Multipart
    @Headers({"Accept: application/json; charset=utf-8"})
    @POST("profile/update")
    Call<Auth> updateProfile(@Part MultipartBody.Part image_file,
                             @Header("ApiToken") String ApiToken,
                             @Query("name") String name,
                             @Query("email") String email,
                             @Query("phone") String phone);

    @POST("reset-password")
    Call<Auth> resetPassword(@Header("ApiToken") String ApiToken,
                             @Query("old_password") String old_password,
                             @Query("new_password") String new_password);

    @GET("show-favs")
    Call<Favourites> getFavourites(@Header("ApiToken") String ApiToken);

    @GET("offers")
    Call<Offers> getOffers();

    @GET("show-product/{product_id}")
    Call<Product> getProduct(@Header ( "lang" ) String lang,
                             @Path("product_id") int product_id);

    @GET("get-cart-items")
    Call<Cart> getCartItems(@Header("ApiToken") String ApiToken);

    @POST("add-to-cart")
    Call<Cart> addItemToCart(@Header("ApiToken") String ApiToken,
                             @Query("product_id") int product_id,
                             @Query("order_type") String order_type,
                             @Query("item_type") String item_type,
                             @Query("quantity") int quantity,
                             @Query("size_id") int size_id,
                             @Query("cut_id") int cut_id,
                             @Query("encapsulation_id") int encapsulation_id,
                             @Query("head_id") int head_id,
                             @Query("minced_quantity") int minced_quantity,
                             @Query("notes") String notes);

    @POST("add-to-cart")
    Call<Cart> addItemToCart(@Header("ApiToken") String ApiToken,
                             @Query("product_id") int product_id,
                             @Query("order_type") String order_type,
                             @Query("item_type") String item_type,
                             @Query("quantity") int quantity,
                             @Query("size_id") int size_id,
                             @Query("cut_id") int cut_id,
                             @Query("encapsulation_id") int encapsulation_id,
                             @Query("head_id") int head_id,
                             @Query("charity_id") int charity_id,
                             @Query("minced_quantity") int minced_quantity,
                             @Query("notes") String notes);

    @POST("edit-cart-quantities")
    Call<Cart> editCartQuantity(@Header("ApiToken") String ApiToken,
                                @Query("data") List<HashMap> data);

    @GET("orders/{status}")
    Call<Orders> getOrders(@Header("ApiToken") String ApiToken,
                           @Path("status") String status);

    @GET("order-details/{order_id}")
    Call<OrderDetails> getOrderDetails(@Header ( "lang" ) String lang,
                                       @Path("order_id") int order_id);

    @POST("contact-us")
    Call<Auth> contactUs(@Query("name") String name,
                         @Query("email") String email,
                         @Query("message") String message);

    @GET("terms")
    Call<Product> getTerms(@Header ( "lang" ) String lang);

    @GET("about")
    Call<Product> getAbout(@Header ( "lang" ) String lang);

    @GET("asks")
    Call<Asks> getAsks(@Header ( "lang" ) String lang);

    @GET("notifications")
    Call<Notifications> getNotifications(@Header ( "lang" ) String lang,
                                         @Header("ApiToken") String ApiToken);

    @GET("section-products/{section_id}")
    Call<Section> getSection(@Header ( "lang" ) String lang,
                             @Path("section_id") int section_id,
                             @Query("price_sort") String price_sort);

    @POST("new-cart-order")
    Call<Orders> newCartOrder(@Header("ApiToken") String ApiToken,
                              @Query("delivery_date") String delivery_date,
                              @Query("delivery_time") String delivery_time,
                              @Query("city_id") int city_id,
                              @Query("neighbourhood_id") int neighbourhood_id,
                              @Query("notes") String notes,
                              @Query("lat") double lat,
                              @Query("lng") double lng,
                              @Query("coupon_num") int coupon_num);

    @POST("new-cart-order")
    Call<Orders> newCartOrder(@Header("ApiToken") String ApiToken,
                              @Query("delivery_date") String delivery_date,
                              @Query("delivery_time") String delivery_time,
                              @Query("city_id") int city_id,
                              @Query("neighbourhood_id") int neighbourhood_id,
                              @Query("notes") String notes,
                              @Query("lat") double lat,
                              @Query("lng") double lng);

    @GET("cities-with-neighborhoods")
    Call<Cites> getCityWithNeighborhood(@Header ( "lang" ) String lang);

    @GET("check-coupon/{coupon_num}/{cart_items}")
    Call<Orders> checkCoupon(@Path("coupon_num") int coupon_num,
                             @Path("cart_items") int cart_items);

    @Multipart
    @Headers({"Accept: application/json; charset=utf-8"})
    @POST("send-transfer")
    Call<Auth> sendTransfer(@Part MultipartBody.Part image_file,
                            @Header("ApiToken") String ApiToken,
                            @Query("bank_name") String bank_name,
                            @Query("account_name") String account_name,
                            @Query("account_number") String account_number,
                            @Query("amount") int amount);

    @Multipart
    @Headers({"Accept: application/json; charset=utf-8"})
    @POST("send-transfer")
    Call<Auth> sendTransfer(@Part MultipartBody.Part image_file,
                            @Header("ApiToken") String ApiToken,
                            @Query("bank_name") String bank_name,
                            @Query("account_name") String account_name,
                            @Query("account_number") String account_number,
                            @Query("amount") int amount,
                            @Query("order_id") int order_id);

    @POST("order-pay-type")
    Call<Orders> orderPayType(@Header("ApiToken") String ApiToken,
                             @Query("order_id") int order_id,
                             @Query("pay_type") String pay_type);

    @DELETE("delete-from-cart/{cart_item_id}")
    Call<Cart> removeFromCart(@Header ( "ApiToken" ) String ApiToken,
                              @Path ( "cart_item_id" ) int cart_item_id);

    @GET("toggle-fav/{product_id}")
    Call<Favourites> toggleFavouriteState(@Header ( "ApiToken" ) String ApiToken,
                                          @Path ( "product_id" ) int product_id);

    @GET ( "section-products/{section_id}" )
    Call<Section> filterSection(@Path ( "section_id" ) int section_id,
                                @Query ( "price_sort" ) String price_sort);

    @GET("search")
    Call<Search> search(@Query ( "search" ) String search);

    @POST("change_lang")
    Call<Auth> changeLanguage(@Header ( "ApiToken" ) String ApiToken,
                              @Query ( "lang" ) String lang);
}