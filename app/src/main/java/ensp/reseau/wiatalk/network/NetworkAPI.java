package ensp.reseau.wiatalk.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkAPI {

    public static final String BASE_URL = "http://192.168.43.22:3000/wiatalk/";
    public static final String SERVER_URL = "http://192.168.43.22:3000/";
    private static Retrofit retrofit = null;
    private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

    public static Retrofit getClient() {
        if (retrofit==null) {
            Gson gson = new GsonBuilder()
                    .setDateFormat(DATE_FORMAT)
                    .create();
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    //.addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
