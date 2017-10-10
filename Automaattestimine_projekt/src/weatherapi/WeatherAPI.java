package weatherapi;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class WeatherAPI {
    private String apiKey = "d0fcb2e76efa4e13887c5910130b1ead";
    private Map<String, Double> minTemperatures = new HashMap<>();
    private Map<String, Double> maxTemperatures = new HashMap<>();

    public static void main(String[] args) {
        String city = "Tallinn";
        WeatherAPI api = new WeatherAPI();

        try {
            api.setNext3DaysMinTemp("Tallinn");
            System.out.println(api.getNext3DaysMinimumTemperaturesAsString());
            api.setNext3DaysMaxTemp("Tallinn");
            System.out.println(api.getNext3DaysMaximumTemperaturesAsString());
            System.out.println("Current temperature in Tallinn: " + api.getCurrentTemperatureByCity(city) + "C");
            System.out.println("Coordinates: " + api.getGeoCoordinates(city));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getCurrentWeatherData(String city) throws Exception {
        URL currentWeatherUrl = new URL("http://api.openweathermap.org/data/2.5/weather?&APPID=" + apiKey + "&q=" + city + "&units=metric");
        return getJson(currentWeatherUrl);
    }

    public String getWeatherForecastData(String city) throws Exception {
        URL currentWeatherUrl = new URL("http://api.openweathermap.org/data/2.5/forecast?&APPID=" + apiKey + "&q=" + city + "&units=metric");
        return getJson(currentWeatherUrl);
    }

    public String getJson(URL url) throws IOException{
        BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
        String data = reader.readLine();

        return data;
    }

    public Double getCurrentTemperatureByCity(String city) throws Exception {
        JSONObject json = new JSONObject(getCurrentWeatherData(city));
        double temperature = json.getJSONObject("main").getDouble("temp");
        return temperature;
    }

    public String getGeoCoordinates(String city) throws Exception{
        JSONObject json = new JSONObject(getCurrentWeatherData(city));

        String latitude = String.valueOf(json.getJSONObject("coord").getInt("lat"));
        String longitude = String.valueOf(json.getJSONObject("coord").getInt("lon"));
        return latitude + ":" + longitude;
    }

    public void setNext3DaysMinTemp(String city) throws Exception {
        JSONObject json = new JSONObject(getWeatherForecastData(city));
        Map<String, Double> minTemps = new HashMap<>();

        for (int count = 0; count < json.getJSONArray("list").length(); count++) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String date = LocalDate.parse(json.getJSONArray("list").getJSONObject(count).getString("dt_txt"), formatter).toString();
            double minTemp = json.getJSONArray("list").getJSONObject(count).getJSONObject("main").getDouble("temp_min");

            if (LocalDate.now().isBefore(LocalDate.parse(date)) && LocalDate.now().plusDays(4).isAfter(LocalDate.parse(date))
                    && (!minTemps.containsKey(date) || (minTemps.containsKey(date) && minTemps.get(date) > minTemp))) {
                minTemps.put(date, minTemp);
            }
        }
         this.minTemperatures = minTemps;
    }

    public void setNext3DaysMaxTemp(String city) throws Exception {
        JSONObject json = new JSONObject(getWeatherForecastData(city));
        Map<String, Double> maxTemps = new HashMap<>();

        for (int count = 0; count < json.getJSONArray("list").length(); count++) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String date = LocalDate.parse(json.getJSONArray("list").getJSONObject(count).getString("dt_txt"), formatter).toString();
            double maxTemp = json.getJSONArray("list").getJSONObject(count).getJSONObject("main").getDouble("temp_max");

            if (LocalDate.now().isBefore(LocalDate.parse(date)) && LocalDate.now().plusDays(4).isAfter(LocalDate.parse(date))
                    && (!maxTemps.containsKey(date) || (maxTemps.containsKey(date) && maxTemps.get(date) < maxTemp))) {
                maxTemps.put(date, maxTemp);
            }
        }

        this.maxTemperatures = maxTemps;
    }

    public String getNext3DaysMinimumTemperaturesAsString() {
        String result = "Next 3-day weather forecast (minimum temperature): \n";
        for (String key : this.minTemperatures.keySet()) {
            result += key + ": " + this.minTemperatures.get(key) + "\n";
        }

        return result;
    }

    public String getNext3DaysMaximumTemperaturesAsString() {
        String result = "Next 3-day weather forecast (minimum temperature): \n";
        for (String key : this.maxTemperatures.keySet()) {
            result += key + ": " + this.maxTemperatures.get(key) + "\n";
        }

        return result;
    }

    public Map<String, Double> getNext3DaysMinimumTemperatures(){
        return this.minTemperatures;
    }

    public Map<String, Double> getNext3DaysMaximumTemperatures(){
        return this.maxTemperatures;
    }
}
