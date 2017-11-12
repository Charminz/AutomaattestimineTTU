package weatherapi;

import org.json.JSONObject;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class WeatherAPI {
    private String apiKey = "d0fcb2e76efa4e13887c5910130b1ead";
    private HashMap<String, Double> minTemperatures = new HashMap<>();
    private HashMap<String, Double> maxTemperatures = new HashMap<>();
    private String city;

    public static void main(String[] args) throws Exception{
        WeatherAPI api = new WeatherAPI();

        try {
            api.setNext3DaysMinTemp();
            System.out.println(api.getNext3DaysMinimumTemperaturesAsString());
            api.setNext3DaysMaxTemp();
            System.out.println(api.getNext3DaysMaximumTemperaturesAsString());
            System.out.println("Current temperature in " + api.city + ": " + api.getCurrentTemperatureByCity() + "C");
            System.out.println("Coordinates: " + api.getGeoCoordinates());
        } catch (Exception e) {
            e.printStackTrace();
        }

        //System.out.println(api.getCWeatherForecastMinTempFromConsoleInput());
    }

    public WeatherAPI() {
        setCityFromInputFile();
    }

    public WeatherAPI(String city) {
        this.city = city;
    }

    public void setCityFromInputFile() {
        String city = "Tallinn";
        try (BufferedReader reader = Files.newBufferedReader(Paths.get("input.txt"))) {
            while (true) {
                String line = reader.readLine();
                if (line == null) break;
                city = line;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.city = city;
    }

    public void setCity(String city) {
        if (city != null && !city.equals("")) this.city = city;
    }

    public double getCurrentWeatherDataFromConsoleInput() throws Exception{
        System.out.println("Enter city name: ");
        Scanner in = new Scanner(System.in);
        this.city = in.nextLine();
        return getCurrentTemperatureByCity();
    }

    public String getWeatherForecastMaxTempFromConsoleInput() throws Exception{
        System.out.println("Enter city name: ");
        Scanner in = new Scanner(System.in);
        this.city = in.nextLine();

        return getNext3DaysMaximumTemperaturesAsString();
    }

    public String getCWeatherForecastMinTempFromConsoleInput() throws Exception{
        System.out.println("Enter city name: ");
        Scanner in = new Scanner(System.in);
        this.city = in.nextLine();

        return getNext3DaysMinimumTemperaturesAsString();
    }

    public Double getCurrentTemperatureByCity() throws Exception {
        JSONObject json = new JSONObject(WeatherAPIJsonDataReceiver.getCurrentWeatherData(this.apiKey, this.city));
        double temperature = json.getJSONObject("main").getDouble("temp");
        writeDataToFile("Current temperature in " + this.city + ": " + String.valueOf(temperature) + "C");
        return temperature;
    }

    public String getGeoCoordinates() throws Exception {
        JSONObject json = new JSONObject(WeatherAPIJsonDataReceiver.getCurrentWeatherData(this.apiKey, this.city));

        String latitude = String.valueOf(json.getJSONObject("coord").getInt("lat"));
        String longitude = String.valueOf(json.getJSONObject("coord").getInt("lon"));
        writeDataToFile("Coordinates: " + latitude + ":" + longitude);
        return latitude + ":" + longitude;
    }

    public void setNext3DaysMinTemp() throws Exception {
        JSONObject json = new JSONObject(WeatherAPIJsonDataReceiver.getWeatherForecastData(this.apiKey, this.city));
        HashMap<String, Double> minTemps = new HashMap<>();

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

    public void setNext3DaysMaxTemp() throws Exception {
        JSONObject json = new JSONObject(WeatherAPIJsonDataReceiver.getWeatherForecastData(this.apiKey, this.city));
        HashMap<String, Double> maxTemps = new HashMap<>();

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

    public String getNext3DaysMinimumTemperaturesAsString() throws Exception{
        this.setNext3DaysMinTemp();
        String result = "Next 3-day weather forecast (minimum temperature): \n";
        for (String key : this.minTemperatures.keySet()) {
            result += key + ": " + this.minTemperatures.get(key) + "\n";
        }
        this.writeDataToFile(result);
        return result;
    }

    public String getNext3DaysMaximumTemperaturesAsString() throws Exception{
        this.setNext3DaysMaxTemp();
        String result = "Next 3-day weather forecast (maximum temperature): \n";
        for (String key : this.maxTemperatures.keySet()) {
            result += key + ": " + this.maxTemperatures.get(key) + "\n";
        }
        this.writeDataToFile(result);
        return result;
    }

    public Map<String, Double> getNext3DaysMinimumTemperatures() {
        return this.minTemperatures;
    }

    public Map<String, Double> getNext3DaysMaximumTemperatures() {
        return this.maxTemperatures;
    }

    public void writeDataToFile(String data) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("output.txt"));
            writer.write(data);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
