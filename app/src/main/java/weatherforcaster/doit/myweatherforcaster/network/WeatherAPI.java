package weatherforcaster.doit.myweatherforcaster.network;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import weatherforcaster.doit.myweatherforcaster.models.FiveDayThreeHourModel.Forecasted;

public interface WeatherAPI {
    @GET("forecast")
    Call<Forecasted> getWeatherForCelsius(
            @Query("lat") String lat,
            @Query("lon") String lon,
            @Query("appid") String APP_ID,
            @Query("units") String units);
}
