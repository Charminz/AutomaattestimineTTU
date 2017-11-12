package weatherapi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class WeatherAPIJsonDataReceiver {

    public static String getJson(URL url) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
        String data = reader.readLine();
        return data;
    }

    public static String getCurrentWeatherData(String apiKey, String city) throws Exception {
        URL currentWeatherUrl = new URL("http://api.openweathermap.org/data/2.5/weather?&APPID=" + apiKey + "&q=" + city + "&units=metric");
        return WeatherAPIJsonDataReceiver.getJson(currentWeatherUrl);
    }

    public static String getWeatherForecastData(String apiKey, String city) throws Exception {
        URL currentWeatherUrl = new URL("http://api.openweathermap.org/data/2.5/forecast?&APPID=" + apiKey + "&q=" + city + "&units=metric");
        return WeatherAPIJsonDataReceiver.getJson(currentWeatherUrl);
    }
}
