package weatherapi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class WeatherAPIJsonDataReceiver {

    public String getJson(URL url) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
        String data = reader.readLine();
        reader.close();

        return data;
    }

    public String getCurrentWeatherData(String apiKey, String city) throws IOException {
        URL currentWeatherUrl = null;
        try {
            currentWeatherUrl = new URL("http://api.openweathermap.org/data/2.5/weather?&APPID=" + apiKey + "&q=" + city + "&units=metric");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return this.getJson(currentWeatherUrl);
    }

    public String getWeatherForecastData(String apiKey, String city) throws IOException {
        URL currentWeatherUrl = null;
        try {
            currentWeatherUrl = new URL("http://api.openweathermap.org/data/2.5/forecast?&APPID=" + apiKey + "&q=" + city + "&units=metric");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return this.getJson(currentWeatherUrl);
    }
}
