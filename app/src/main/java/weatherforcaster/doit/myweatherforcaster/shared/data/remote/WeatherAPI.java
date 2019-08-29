package weatherforcaster.doit.myweatherforcaster.shared.data.remote;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import weatherforcaster.doit.myweatherforcaster.model.FiveDayThreeHourModel.Forecasted;
import weatherforcaster.doit.myweatherforcaster.model.TodayModel.All;

public interface WeatherAPI {
    @GET("forecast")
    Call<Forecasted> getWeatherForFiveDays(
            @Query("lat") String lat,
            @Query("lon") String lon,
            @Query("appid") String APP_ID,
            @Query("units") String units);

    @GET("weather")
    Call<All> getWeatherForNow(
            @Query("lat") String lat,
            @Query("lon") String lon,
            @Query("appid") String APP_ID,
            @Query("units") String units);
}
