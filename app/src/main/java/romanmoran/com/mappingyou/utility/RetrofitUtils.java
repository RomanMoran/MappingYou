package romanmoran.com.mappingyou.utility;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import romanmoran.com.mappingyou.api_utility.ApiService;

/**
 * Created by roman on 02.08.2017.
 */

public class RetrofitUtils {

    private static final String TAG = RetrofitUtils.class.getName();
    private static final String BASE_URL = "http://yanakarte.ru/RestServiceImpl.svc/";

    private static ApiService apiService;
    private static RetrofitUtils instance;

    public static RetrofitUtils getInstance(){
        if (instance == null) instance = new RetrofitUtils();
        return instance;
    }

    private ApiService initApiService(){
        return apiService = provideRetrofit(BASE_URL)
                .create(ApiService.class);
    }

    private HttpLoggingInterceptor provideHttpLoggingInterceptor() {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return httpLoggingInterceptor;
    }

    private Retrofit provideRetrofit(String baseUrl){
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(new OkHttpClient.Builder().addInterceptor(provideHttpLoggingInterceptor()).build())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public ApiService getApiService(){
        return apiService !=null? apiService :initApiService();
    }

}
