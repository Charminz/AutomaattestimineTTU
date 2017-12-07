package weatherapi;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

public class WeatherAPI {
    private DataWriter writer;
    private DataReader reader;
    private WeatherAPIJsonDataReceiver jsonReceiver;
    private String apiKey = "d0fcb2e76efa4e13887c5910130b1ead";
    private Map<String, Double> minTemperatures = new TreeMap<>();
    private Map<String, Double> maxTemperatures = new TreeMap<>();
    private String city;
    private LocalDate currentDate = LocalDate.now();

    public static void main(String[] args) throws Exception {
        WeatherAPI api = new WeatherAPI(new DataWriter(), new DataReader(), new WeatherAPIJsonDataReceiver());

        try {
            api.getInfoAndWriteInCityFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public WeatherAPI(DataWriter writer, DataReader reader, WeatherAPIJsonDataReceiver jsonReceiver) {
        this.writer = writer;
        this.reader = reader;
        this.jsonReceiver = jsonReceiver;
        setCityFromInputFile();
    }

    public WeatherAPI(String city, DataWriter writer, DataReader reader, WeatherAPIJsonDataReceiver jsonReceiver) {
        this.city = city;
        this.writer = writer;
        this.reader = reader;
        this.jsonReceiver = jsonReceiver;
    }


    public void setCityFromInputFile() {
        this.city = reader.getCityFromFile();
    }

    public void setCity(String city) {
        if (city != null && !city.equals("")) this.city = city;
    }

    public void setCityFromConsole() {
        System.out.println("Enter city name: ");
        Scanner in = new Scanner(System.in);
        this.city = in.nextLine();
        in.close();
    }

    public String getCity() {
        return city;
    }

    public String getApiKey() {
        return apiKey;
    }

    public Double getCurrentTemperatureByCity() {
        JSONObject json = null;
        double temperature = -200;
        try {
            json = new JSONObject(this.jsonReceiver.getCurrentWeatherData(this.apiKey, this.city));
            temperature = json.getJSONObject("main").getDouble("temp");
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }

        writer.writeDataToResultFile("Current temperature in " + this.city + ": " + String.valueOf(temperature) + "C");
        return temperature;
    }

    public String getGeoCoordinates() throws IOException, JSONException {
        JSONObject json = new JSONObject(this.jsonReceiver.getCurrentWeatherData(this.apiKey, this.city));
        String latitude = String.valueOf(json.getJSONObject("coord").getInt("lat"));
        String longitude = String.valueOf(json.getJSONObject("coord").getInt("lon"));
        writer.writeDataToResultFile("Coordinates: " + latitude + ":" + longitude);
        return latitude + ":" + longitude;
    }

    public void setNext3DaysMinTemp() {
        try {
            JSONObject json = new JSONObject(this.jsonReceiver.getWeatherForecastData(this.apiKey, this.city));
            Map<String, Double> minTemps = new TreeMap<>();

            for (int count = 0; count < json.getJSONArray("list").length(); count++) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                String date = LocalDate.parse(json.getJSONArray("list").getJSONObject(count).getString("dt_txt"), formatter).toString();
                double minTemp = json.getJSONArray("list").getJSONObject(count).getJSONObject("main").getDouble("temp_min");

                if (this.currentDate.isBefore(LocalDate.parse(date)) && this.currentDate.plusDays(4).isAfter(LocalDate.parse(date))
                        && (!minTemps.containsKey(date) || (minTemps.containsKey(date) && minTemps.get(date) > minTemp))) {
                    minTemps.put(date, minTemp);
                }
            }
            this.minTemperatures = minTemps;
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }

    public void setNext3DaysMaxTemp() {
        try {
            JSONObject json = new JSONObject(this.jsonReceiver.getWeatherForecastData(this.apiKey, this.city));
            Map<String, Double> maxTemps = new TreeMap<>();

            for (int count = 0; count < json.getJSONArray("list").length(); count++) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                String date = LocalDate.parse(json.getJSONArray("list").getJSONObject(count).getString("dt_txt"), formatter).toString();
                double maxTemp = json.getJSONArray("list").getJSONObject(count).getJSONObject("main").getDouble("temp_max");
                if (this.currentDate.isBefore(LocalDate.parse(date)) && this.currentDate.plusDays(4).isAfter(LocalDate.parse(date))
                        && (!maxTemps.containsKey(date) || (maxTemps.containsKey(date) && maxTemps.get(date) < maxTemp))) {
                    maxTemps.put(date, maxTemp);
                }
            }
            this.maxTemperatures = maxTemps;
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }

    public String getNext3DaysMinimumTemperaturesAsString() {
        this.setNext3DaysMinTemp();
        StringBuilder resultBuilder = new StringBuilder("Next 3-day weather forecast (minimum temperature): \n");
        for (String key : this.minTemperatures.keySet()) {
            resultBuilder.append(key).append(": ").append(this.minTemperatures.get(key)).append("\n");
        }
        String result = resultBuilder.toString();
        writer.writeDataToResultFile(result);
        System.out.println(result);
        return result;
    }

    public String getNext3DaysMaximumTemperaturesAsString() {
        this.setNext3DaysMaxTemp();
        StringBuilder resultBuilder = new StringBuilder("Next 3-day weather forecast (maximum temperature): \n");
        for (String key : this.maxTemperatures.keySet()) {
            resultBuilder.append(key).append(": ").append(this.maxTemperatures.get(key)).append("\n");
        }
        String result = resultBuilder.toString();
        writer.writeDataToResultFile(result);
        return result;
    }

    public Map<String, Double> getNext3DaysMinimumTemperatures() {
        return this.minTemperatures;
    }

    public Map<String, Double> getNext3DaysMaximumTemperatures() {
        return this.maxTemperatures;
    }

    public void getInfoAndWriteInCityFile() throws IOException, JSONException {
        String weatherInfo = "City: " + this.city + "\n"
                + "Coordinates: " + this.getGeoCoordinates() + "\n"
                + this.getNext3DaysMaximumTemperaturesAsString() + "\n"
                + this.getNext3DaysMinimumTemperaturesAsString() + "\n"
                + "Current temperature: " + this.getCurrentTemperatureByCity();
        writer.writeDataToCityFile(weatherInfo, this.city);
    }

    public void setCurrentDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        this.currentDate = LocalDate.parse(date, formatter);
    }
}