package romanmoran.com.mappingyou.api_utility;

import com.google.gson.JsonObject;

import java.util.List;

import io.reactivex.Flowable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import romanmoran.com.mappingyou.data.Event;
import romanmoran.com.mappingyou.data.Item;
import romanmoran.com.mappingyou.data.Response;
import romanmoran.com.mappingyou.data.User;

/**
 * Created by roman on 24.07.2017.
 */

public interface ApiService {

    @Headers(ApiConstants.HEADER)
    @POST(ApiConstants.POST_AUTH)
    Flowable<Response> authorization(@Body User user);

    @Headers(ApiConstants.HEADER)
    @POST(ApiConstants.POST_NEW_USER)
    Flowable<Response> registration(@Body User user);

    @Headers(ApiConstants.HEADER)
    @POST(ApiConstants.POST_SEND_EMAIL_CODE)
    Flowable<Response> sendEmailCode(@Body User user);

    @Headers(ApiConstants.HEADER)
    @POST(ApiConstants.POST_SEND_SMS_CODE)
    Flowable<Response> sendSmsCode(@Body User user);

    @Headers(ApiConstants.HEADER)
    @POST(ApiConstants.POST_SEND_SMS_SOCAUTH)
    Flowable<Response> socauth(@Body User user);

    @Headers(ApiConstants.HEADER)
    @POST(ApiConstants.POST_SEND_SMS_UPD_USER)
    Flowable<Response> updUser(@Body User user);

    @GET(ApiConstants.GET_NEAR_USER)
    Flowable<List<User>> getNearUser(@Query("id")String id,@Query("xmin")String xmin, @Query("ymin")String ymin, @Query("xmax")String xmax, @Query("ymax")String ymax,@Query("sign")String sign);

    @GET(ApiConstants.GET_NEAR_EVENT)
    Flowable<List<Event>> getNearEvent(@Query("id")String id, @Query("xmin")String xmin, @Query("ymin")String ymin, @Query("xmax")String xmax, @Query("ymax")String ymax, @Query("sign")String sign);

    @GET(ApiConstants.GET_CONT_MAP_USER)
    Flowable<List<User>> getContMapUser(@Query("id")String id,@Query("sign")String sign);


    @GET(ApiConstants.GET_COORD)
    Flowable<Response> setCoord(@Query("id")String id,@Query("x")String x,@Query("y")String y,@Query("sign")String sign);

    @GET(ApiConstants.GET_REMOVE)
    Flowable<Response> remove(@Query("id")String id,@Query("sign")String sign);

    @GET(ApiConstants.GET_USER)
    Flowable<User> getUserData(@Query("id")String id,@Query("sign")String sign);

    }
