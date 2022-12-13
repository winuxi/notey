package com.ravenioet.notey.database;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface APIProvider {
/*

    @FormUrlEncoded
    @POST("init_user_api.php")
    Call<Status> login(
            @Field("target") String target,
            @Field("phone") String phone,
            @Field("password") String password);

    @FormUrlEncoded
    @POST("init_user_sys.php")
    Call<Status> updateUser(
            @Field("target") String target,
            @Field("user_name") String user_name,
            @Field("phone") String phone,
            @Field("email") String email,
            @Field("password") String password);


    @GET("init_book_api.php?target=books&userId=test")
    Call<List<Book>> loadBooks();

    @Multipart
    @POST("init_book_sys.php")
    Call<Status> addBookCover(
            @Part("target") RequestBody target,
            @Part("userId") RequestBody userId,
            @Part("bookId") RequestBody bookId,
            @Part MultipartBody.Part cover);

    @FormUrlEncoded
    @POST("init_user_sys.php")
    Call<Status> updateBook(
            @Field("target") String target,
            @Field("userId") String userId,
            @Field("title") String title,
            @Field("description") String description,
            @Field("price") String price);


    @Multipart
    @POST("init_user_api.php")
    Call<Status> registerPro(
            @Part("target") RequestBody target,
            @Part("user_name") RequestBody user_name,
            @Part("phone") RequestBody phone,
            @Part("email") RequestBody email,
            @Part("password") RequestBody password,
            @Part MultipartBody.Part photo);

    @Multipart
    @POST("init_user_api.php")
    Call<Status> uploadPhoto(
            @Part("target") RequestBody target,
            @Part("userId") RequestBody userId,
            @Part MultipartBody.Part photo);

    @Multipart
    @POST("init_book_api.php")
    Call<Status> addBookPro(
            @Part("target") RequestBody target,
            @Part("userId") RequestBody userId,
            @Part("title") RequestBody title,
            @Part("description") RequestBody description,
            @Part("author") RequestBody author,
            @Part("price") RequestBody price,
            @Part("publisher_name") RequestBody publisher_name,
            @Part("published_date") RequestBody published_date,
            @Part("publisher_photo") RequestBody publisher_photo,
            @Part MultipartBody.Part cover);
*/


}
