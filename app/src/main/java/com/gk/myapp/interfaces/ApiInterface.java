package com.gk.myapp.interfaces;

import com.gk.myapp.model.CartListResponse;
import com.gk.myapp.model.CommonSuccessModel;
import com.gk.myapp.model.DealerTargetList;
import com.gk.myapp.model.LeaveDetailsDataModel;
import com.gk.myapp.model.LeaveListModel;
import com.gk.myapp.model.MessageListModel;
import com.gk.myapp.model.NotificationModel;
import com.gk.myapp.model.NotificationResponseModel;
import com.gk.myapp.model.OrderHistoryResponse;
import com.gk.myapp.model.PartyListModel;
import com.gk.myapp.model.PaymentHistoryResponse;
import com.gk.myapp.model.ProductListResponse;
import com.gk.myapp.model.SelectAsmResponse;
import com.gk.myapp.model.StateResponseModel;
import com.gk.myapp.model.UserDetails;
import com.gk.myapp.model.YearTargetResponse;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by Gk on 19-02-2017.
 */

public interface ApiInterface {

    @FormUrlEncoded
    @POST("login")
    Call<UserDetails> userLogin(@Field("empcode") String empCode, @Field("password") String password, @Field("device_token") String deviceToken, @Field("device_type") String deviceType, @Field("isCustomer") String isCutomer,@Field("imei_number") String imei);


    @FormUrlEncoded
    @POST("getUserProfile")
    Call<UserDetails> getCompleteProfile(@Field("user_id") String userId);

    @FormUrlEncoded
    @POST("jobStartStop")
    Call<CommonSuccessModel> startJob(@Field("empcode") String empCode, @Field("lat") String lat, @Field("long") String lng, @Field("location") String location, @Field("job_status") String jobStatus);

//    @FormUrlEncoded
//    @POST("updateLocation")
//    Call<CommonSuccessModel> updateLocation(@Field("user_id") String userId, @Field("lat") String lat, @Field("long") String lng, @Field("location") String location,@Field("mobile_information") String imeiNumber);

    @FormUrlEncoded
    @POST("updateLocationNew")
    Call<CommonSuccessModel> updateLocationMultipleLat(@Field("user_id") String userId, @Field("data") String data);

    @FormUrlEncoded
    @POST("lockLocation")
    Call<CommonSuccessModel> locationLock(@Field("user_id") String userId, @Field("lat") String lat, @Field("long") String lng, @Field("location") String location, @Field("mobile_information") String imei);


    @FormUrlEncoded
    @POST("getMessageListGM")
    Call<MessageListModel> getGMMessages(@Field("user_id") String userId);

    @FormUrlEncoded
    @POST("getMessageListASM")
    Call<MessageListModel> getASMMessages(@Field("user_id") String userId);

    @FormUrlEncoded
    @POST("getMessageListSO")
    Call<MessageListModel> getSOMessages(@Field("user_id") String userId);

//    @FormUrlEncoded
//    @POST("sendBroadcastMessage")
//    Call<CommonSuccessModel> sendBroadcastMessage(@Field("user_id") String userId);

    @Multipart
    @POST("sendBroadcastMessage")
    Call<CommonSuccessModel> sendBroadCastMessage(@Part MultipartBody.Part image, @Part MultipartBody.Part image2, @Part MultipartBody.Part image3, @Part("user_id") RequestBody userId, @Part("content") RequestBody content, @Part("created_for") RequestBody createdFor);

    @Multipart
    @POST("sendMessage")
    Call<CommonSuccessModel> sendMessage(@Part MultipartBody.Part image, @Part MultipartBody.Part image2, @Part MultipartBody.Part image3, @Part("user_id") RequestBody userId, @Part("content") RequestBody content, @Part("created_for") RequestBody createdFor);

    @FormUrlEncoded
    @POST("getMyLeave")
    Call<LeaveListModel> getMyLeaves(@Field("user_id") String userId);

    @FormUrlEncoded
    @POST("getJuniorLeave")
    Call<LeaveListModel> getStaffLeave(@Field("user_id") String userId);

    @FormUrlEncoded
    @POST("applyLeave")
    Call<CommonSuccessModel> applyLeave(@Field("user_id") String userId, @Field("reason") String reason, @Field("role") String role, @Field("date_from") String dateFrom, @Field("date_to") String date_to, @Field("total_day") String totalDay, @Field("leave_type") String leaveType);

    @FormUrlEncoded
    @POST("getLeaveDetail")
    Call<LeaveDetailsDataModel> getLeaveDetails(@Field("leave_id") String leaveId);

    @FormUrlEncoded
    @POST("changeStatusLeave")
    Call<CommonSuccessModel> changeLeaveStatus(@Field("user_id") String userId, @Field("leave_id") String leaveId, @Field("status") String status);

    @FormUrlEncoded
    @POST("getNotification")
    Call<NotificationResponseModel> getNotification(@Field("user_id") String userId, @Field("notify_type") String notifType);

    @FormUrlEncoded
    @POST("receivedPayment")
    Call<CommonSuccessModel> addPayment(@Field("user_id") String userId, @Field("amount") String amount, @Field("paymentMode") String paymentMode, @Field("transactionID") String transactionId, @Field("date") String date, @Field("party_id") String partyId);

    @FormUrlEncoded
    @POST("partyList")
    Call<PartyListModel> partyList(@Field("user_id") String userId);

    @FormUrlEncoded
    @POST("getPaymentHistory")
    Call<PaymentHistoryResponse> paymentHistory(@Field("user_id") String userId);


    @FormUrlEncoded
    @POST("registerCustomer")
    Call<CommonSuccessModel> signUp(@Field("name") String name,@Field("father_name") String fatherName, @Field("email") String email, @Field("password") String password, @Field("dob") String age, @Field("company_name") String companyName, @Field("gender") String gender, @Field("phone_number") String phone, @Field("address") String address, @Field("city") String city, @Field("state_id") String stateId,@Field("aadhar_number") String aadharNo,@Field("party_name") String partyName);

    @POST("stateList")
    Call<StateResponseModel> stateList();

    @FormUrlEncoded
    @POST("submitEnquiry")
    Call<CommonSuccessModel> submitEnquiry(@Field("owner_name") String name, @Field("party_name") String partyName, @Field("contact_number") String phone, @Field("party_address") String address, @Field("city") String city, @Field("state") String stateId, @Field("message") String message);

    @FormUrlEncoded
    @POST("getProductList")
    Call<ProductListResponse> productList(@Field("user_id") String userId);

    @FormUrlEncoded
    @POST("addToCart")
    Call<CommonSuccessModel> addToCart(@Field("user_id") String userId, @Field("product_attr_id") String productId, @Field("quantity") String quantity);

    @FormUrlEncoded
    @POST("viewCartProduct")
    Call<CartListResponse> getCart(@Field("user_id") String userId);

    @FormUrlEncoded
    @POST("deleteCart")
    Call<CommonSuccessModel> deleteCart(@Field("user_id") String userId, @Field("cart_id") String cartId);

    @FormUrlEncoded
    @POST("orderCreate")
    Call<CommonSuccessModel> placeOrder(@Field("user_id") String userId, @Field("cart_id") String cartId,@Field("created_for") String partyId);

    @FormUrlEncoded
    @POST("customerPayment")
    Call<CommonSuccessModel> customerPayment(@Field("user_id") String userId, @Field("amount") String amount,@Field("order_number") String orderNumber);


    @FormUrlEncoded
    @POST("SaveUserAllowance")
    Call<CommonSuccessModel> saveAllowances(@Field("user_id") String userId, @Field("daily_allowance") String dailyAllowance,@Field("other_allowance") String otherAllowance);



    //Old Target APIs
//    @FormUrlEncoded
//    @POST("targetListYear")
//    Call<YearTargetResponse> getYearlyTarget(@Field("user_id") String userId, @Field("user_type") String userType,@Field("year") String year);

//    @FormUrlEncoded
//    @POST("targetListEachHalfForGM")
//    Call<SelectAsmResponse> getHalfYearlyTarget(@Field("user_id") String userId, @Field("half") String half,@Field("year") String year);

//    @FormUrlEncoded
//    @POST("targetSOListForGM")
//    Call<SelectAsmResponse> getTargetSoForGm(@Field("user_id") String userId, @Field("half") String half,@Field("year") String year, @Field("asm_id") String asmId);

//    @FormUrlEncoded
//    @POST("targetListEachHalfForASM")
//    Call<SelectAsmResponse> getTargetForAsm(@Field("user_id") String userId, @Field("half") String half,@Field("year") String year);
//
//
//    @FormUrlEncoded
//    @POST("targetEachSODetail")
//    Call<SelectAsmResponse> getTargetSo(@Field("user_id") String userId, @Field("half") String half,@Field("year") String year,@Field("so_id") String soId);


    //old
    @FormUrlEncoded
    @POST("targetListYearDealer")
    Call<YearTargetResponse> targetYearListDealer(@Field("user_id") String userId,@Field("year") String year);


    //new
    @FormUrlEncoded
    @POST("dealerTargetList")
    Call<DealerTargetList> getDealerMonthList(@Field("user_id") String userId,@Field("year") String year);


    @FormUrlEncoded
    @POST("dealerTargetListEach")
    Call<SelectAsmResponse> getMonthlyData(@Field("user_id") String userId, @Field("month") String month,@Field("year") String year);


    @FormUrlEncoded
    @POST("orderHistory")
    Call<OrderHistoryResponse> getOrderHistory(@Field("user_id") String userId, @Field("page") String page);

    @FormUrlEncoded
    @POST("cancelLeave")
    Call<CommonSuccessModel> cancelLeave(@Field("user_id") String userId, @Field("leave_id") String leaveId);

    @FormUrlEncoded
    @POST("changePassword")
    Call<CommonSuccessModel> changePassword(@Field("user_id") String userId, @Field("old_password") String oldPassword,@Field("new_password") String newPassword);

    @FormUrlEncoded
    @POST("targetListYearMonth")
    Call<DealerTargetList> getYearlyTarget(@Field("user_id") String userId, @Field("user_type") String userType,@Field("year") String year);

    @FormUrlEncoded
    @POST("targetListEachHalfForGMMonth")
    Call<SelectAsmResponse> getHalfYearlyTarget(@Field("user_id") String userId, @Field("month") String half,@Field("year") String year);

    @FormUrlEncoded
    @POST("targetSOListForGMMonth")
    Call<SelectAsmResponse> getTargetSoForGm(@Field("user_id") String userId, @Field("month") String half,@Field("year") String year, @Field("asm_id") String asmId);

    @FormUrlEncoded
    @POST("targetListEachHalfForASMMonth")
    Call<SelectAsmResponse> getTargetForAsm(@Field("user_id") String userId, @Field("month") String half,@Field("year") String year);


    @FormUrlEncoded
    @POST("targetEachSODetailMonth")
    Call<SelectAsmResponse> getTargetSo(@Field("user_id") String userId, @Field("month") String half,@Field("year") String year,@Field("so_id") String soId);


    @FormUrlEncoded
    @POST("cancelOrder")
    Call<CommonSuccessModel> cancelOrder(@Field("user_id") String userId, @Field("order_id") String orderId);


//    @GET("start_job")
//    Call<CommonSuccessModel> startJob(@Query("emp_code") String empCode,@Query("start_date") String startDate,@Query("start_time") String startTime,@Query(" job_name") String jobName);

//    @GET("stop_job")
//    Call<CommonSuccessModel> stopJob(@Query("emp_code") String empCode,@Query("stop_date") String stopDate,@Query("stop_time") String stopTime);
//
//    @GET("storeLatLong")
//    Call<CommonSuccessModel> storeLatLong(@Query("user_id") String userId,@Query("date") String date,@Query("lat") String lat,@Query("long") String lng,@Query("distance") String distance);
//    @GET("movie/{id}")
//    Call<MoviesResponse> getMovieDetails(@Path("id") int id, @Query("api_key") String apiKey);
}
