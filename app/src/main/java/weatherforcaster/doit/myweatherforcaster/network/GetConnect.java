package weatherforcaster.doit.myweatherforcaster.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GetConnect {

    private static final String BASE_URL = "http://api.openweathermap.org/data/2.5/";
    private static Retrofit retrofit;

    public static Retrofit getInstance() {
        if (retrofit == null) {
            return retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        } else {
            return retrofit;
        }
    }
}