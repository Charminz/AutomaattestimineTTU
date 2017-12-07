package tests;

import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;
import weatherapi.DataReader;
import weatherapi.DataWriter;
import weatherapi.WeatherAPI;
import weatherapi.WeatherAPIJsonDataReceiver;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.TreeMap;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;


public class WeatherAPITests {
    private WeatherAPI weatherAPI;

    @Before
    public void setUp() throws Exception {
        this.weatherAPI = new WeatherAPI("Tallinn", new DataWriter(), new DataReader(), new WeatherAPIJsonDataReceiver());
//        weatherAPI.setNext3DaysMinTemp();
//        weatherAPI.setNext3DaysMaxTemp();
    }

    public String getFakeJsonCurrentWeather() {
        String result = "";
        try (BufferedReader reader = Files.newBufferedReader(Paths.get("FakeJsonFile.txt"))) {
            result = reader.readLine();

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public String getFakeJsonWeatherForecast() {
        String result = "";
        try (BufferedReader reader = Files.newBufferedReader(Paths.get("FakeJsonWeatherForecast.txt"))) {
            result = reader.readLine();

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Test
    public void testIfCurrentTempIsDouble() {
        try {
            assertTrue(weatherAPI.getCurrentTemperatureByCity() instanceof Double);
        } catch (Exception e) {
            fail();
        }
    }

    @Test()
    public void testIfDailyHighestTempIsDouble() {
        weatherAPI.setNext3DaysMaxTemp();
        for (String key : weatherAPI.getNext3DaysMaximumTemperatures().keySet()) {
            try {
                assertTrue(weatherAPI.getNext3DaysMaximumTemperatures().get(key) instanceof Double);
            } catch (Exception e) {
                fail();
            }
        }
    }

    @Test
    public void testIfDailyLowestTempIsInteger() {
        weatherAPI.setNext3DaysMinTemp();
        for (String key : weatherAPI.getNext3DaysMinimumTemperatures().keySet()) {
            try {
                assertTrue(weatherAPI.getNext3DaysMinimumTemperatures().get(key) instanceof Double);
            } catch (Exception e) {
                fail();
            }
        }
    }


    @Test
    public void testIfHighestIsHighest() throws Exception {
        weatherAPI.setNext3DaysMinTemp();
        weatherAPI.setNext3DaysMaxTemp();
        for (String key : weatherAPI.getNext3DaysMinimumTemperatures().keySet()) {
            assertTrue(weatherAPI.getNext3DaysMinimumTemperatures().get(key) < weatherAPI.getNext3DaysMaximumTemperatures().get(key));
        }
    }

    @Test
    public void testGeoLocationLength() throws Exception {
        assertTrue(weatherAPI.getGeoCoordinates().length() == 5);
    }

    @Test
    public void testXCoordinatesAreInteger() throws Exception {
        String coords = weatherAPI.getGeoCoordinates();
        int index = coords.indexOf(':');
        try {
            assertTrue(Integer.valueOf(coords.substring(0, index)) != null);
        } catch (NumberFormatException e) {
            fail();
        }
    }

    @Test
    public void testYCoordinatesAreInteger() throws Exception {
        String coords = weatherAPI.getGeoCoordinates();
        int index = coords.indexOf(':');
        try {
            assertTrue(Integer.valueOf(coords.substring(index + 1)) != null);
        } catch (NumberFormatException e) {
            fail();
        }
    }

    @Test
    public void testSetCityNull() {
        // City is set to "Tallinn" in constructor.

        weatherAPI.setCity(null);
        assertEquals("Tallinn", weatherAPI.getCity());
    }

    @Test
    public void testSetCityEmptyString() {
        // City is set to "Tallinn" in constructor.

        weatherAPI.setCity("");
        assertEquals("Tallinn", weatherAPI.getCity());

    }

    @Test
    public void testGetCurrentTemperatureByCityGotTemp() {
        assertTrue(weatherAPI.getCurrentTemperatureByCity() > -200);
    }

    @Test
    public void testGetCurrentTemperatureByCityGotCorrectTemperature() {
        assertEquals(0, weatherAPI.getCurrentTemperatureByCity(), 25);
    }

    @Test
    public void testCurrentTempByCity() {
        weatherAPI.getCurrentTemperatureByCity();
        String result = "";

        try (BufferedReader reader = Files.newBufferedReader(Paths.get("output.txt"))) {
            while (true) {
                String line = reader.readLine();
                if (line == null) break;
                result = line;
            }

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertTrue(result.contains("Current temperature in Tallinn:"));
    }

    @Test
    public void testGetCurrentTempByCityJsonReceiverCall() throws IOException {
        WeatherAPIJsonDataReceiver json = mock(WeatherAPIJsonDataReceiver.class);
        WeatherAPI api = new WeatherAPI("Tallinn", new DataWriter(), new DataReader(), json);

        when(json.getCurrentWeatherData(api.getApiKey(), "Tallinn")).thenReturn(getFakeJsonCurrentWeather());
        api.getCurrentTemperatureByCity();
        verify(json).getCurrentWeatherData(api.getApiKey(), "Tallinn");
    }

    @Test
    public void testGetCurrentTempByCityWriteDataCall() throws IOException {
        WeatherAPIJsonDataReceiver json = mock(WeatherAPIJsonDataReceiver.class);
        DataWriter writer = mock(DataWriter.class);
        WeatherAPI api = new WeatherAPI("Tallinn", writer, new DataReader(), json);

        when(json.getCurrentWeatherData(api.getApiKey(), "Tallinn")).thenReturn(getFakeJsonCurrentWeather());
        api.getCurrentTemperatureByCity();

        verify(writer).writeDataToResultFile("Current temperature in Tallinn: 1.0C");
    }

    @Test
    public void testGetGeoCoordinatesOfTallinn() throws IOException, JSONException {
        assertEquals("59:24", weatherAPI.getGeoCoordinates());
    }

    @Test
    public void testGetGeoCoordsTallinnJsonReceiverCall() throws Exception {
        WeatherAPIJsonDataReceiver json = mock(WeatherAPIJsonDataReceiver.class);
        WeatherAPI api = new WeatherAPI("Tallinn", new DataWriter(), new DataReader(), json);

        when(json.getCurrentWeatherData(api.getApiKey(), "Tallinn")).thenReturn(getFakeJsonCurrentWeather());
        api.getGeoCoordinates();
        verify(json).getCurrentWeatherData(api.getApiKey(), "Tallinn");
    }

    @Test(expected = IOException.class)
    public void testGetGeoCoordsWrongCity() throws JSONException, IOException {
        weatherAPI.setCity("XXXXXXXXX");
        weatherAPI.getGeoCoordinates();
    }

    @Test
    public void testIf3DayForecastMinTempListIsSet() {
        weatherAPI.setNext3DaysMinTemp();
        assertTrue(!weatherAPI.getNext3DaysMinimumTemperatures().isEmpty());
    }

    @Test
    public void testIf3DayForecastMaxTempListIsSet() {
        weatherAPI.setNext3DaysMaxTemp();
        assertTrue(!weatherAPI.getNext3DaysMaximumTemperatures().isEmpty());
    }

    @Test
    public void testSetNext3DaysMinTempJsonReceiverCall() throws Exception {
        WeatherAPIJsonDataReceiver json = mock(WeatherAPIJsonDataReceiver.class);
        WeatherAPI api = new WeatherAPI("Tallinn", new DataWriter(), new DataReader(), json);

        when(json.getWeatherForecastData(api.getApiKey(), "Tallinn")).thenReturn(getFakeJsonWeatherForecast());
        api.setNext3DaysMinTemp();
        verify(json).getWeatherForecastData(api.getApiKey(), "Tallinn");
    }

    @Test
    public void testSetNext3DaysMaxTempJsonReceiverCall() throws Exception {
        WeatherAPIJsonDataReceiver json = mock(WeatherAPIJsonDataReceiver.class);
        WeatherAPI api = new WeatherAPI("Tallinn", new DataWriter(), new DataReader(), json);

        when(json.getWeatherForecastData(api.getApiKey(), "Tallinn")).thenReturn(getFakeJsonWeatherForecast());
        api.setNext3DaysMaxTemp();
        verify(json).getWeatherForecastData(api.getApiKey(), "Tallinn");
    }

    @Test
    public void testIfNext3DaysMinTempWasSetCorrect() throws IOException {
        WeatherAPIJsonDataReceiver json = mock(WeatherAPIJsonDataReceiver.class);
        WeatherAPI api = new WeatherAPI("Tallinn", new DataWriter(), new DataReader(), json);

        when(json.getWeatherForecastData(api.getApiKey(), "Tallinn")).thenReturn(getFakeJsonWeatherForecast());
        api.setCurrentDate("2017-12-01 15:00:00");
        api.setNext3DaysMinTemp();
        TreeMap<String, Double> map = new TreeMap<>();
        map.put("2017-12-02", 0.61);
        map.put("2017-12-03", 1.75);
        map.put("2017-12-04", 0.49);

        assertEquals(map, api.getNext3DaysMinimumTemperatures());
    }

    @Test
    public void testIfNext3DaysMaxTempWasSetCorrect() throws IOException {
        WeatherAPIJsonDataReceiver json = mock(WeatherAPIJsonDataReceiver.class);
        WeatherAPI api = new WeatherAPI("Tallinn", new DataWriter(), new DataReader(), json);

        when(json.getWeatherForecastData(api.getApiKey(), "Tallinn")).thenReturn(getFakeJsonWeatherForecast());
        api.setCurrentDate("2017-12-01 15:00:00");
        api.setNext3DaysMaxTemp();

        TreeMap<String, Double> map = new TreeMap<>();
        map.put("2017-12-02", 2.53);
        map.put("2017-12-03", 4.44);
        map.put("2017-12-04", 3.43);

        assertEquals(map, api.getNext3DaysMaximumTemperatures());
    }

    @Test
    public void testGetNext3DaysMinTempAsString() throws IOException {
        WeatherAPIJsonDataReceiver json = mock(WeatherAPIJsonDataReceiver.class);
        WeatherAPI api = new WeatherAPI("Tallinn", new DataWriter(), new DataReader(), json);

        when(json.getWeatherForecastData(api.getApiKey(), "Tallinn")).thenReturn(getFakeJsonWeatherForecast());
        api.setCurrentDate("2017-12-01 15:00:00");
        assertEquals("Next 3-day weather forecast (minimum temperature): \n" +
                "2017-12-02: 0.61\n" +
                "2017-12-03: 1.75\n" +
                "2017-12-04: 0.49\n", api.getNext3DaysMinimumTemperaturesAsString());
    }

    @Test
    public void testGetNext3DaysMaxTempAsString() throws IOException {
        WeatherAPIJsonDataReceiver json = mock(WeatherAPIJsonDataReceiver.class);
        WeatherAPI api = new WeatherAPI("Tallinn", new DataWriter(), new DataReader(), json);

        when(json.getWeatherForecastData(api.getApiKey(), "Tallinn")).thenReturn(getFakeJsonWeatherForecast());
        api.setCurrentDate("2017-12-01 15:00:00");

        assertEquals("Next 3-day weather forecast (maximum temperature): \n" +
                "2017-12-02: 2.53\n" +
                "2017-12-03: 4.44\n" +
                "2017-12-04: 3.43\n", api.getNext3DaysMaximumTemperaturesAsString());
    }

    @Test
    public void testGetNext3DaysMinTempStringDataWriterCall() throws IOException {
        WeatherAPIJsonDataReceiver json = mock(WeatherAPIJsonDataReceiver.class);
        DataWriter writer = mock(DataWriter.class);
        WeatherAPI api = new WeatherAPI("Tallinn", writer, new DataReader(), json);

        when(json.getWeatherForecastData(api.getApiKey(), "Tallinn")).thenReturn(getFakeJsonWeatherForecast());
        api.setCurrentDate("2017-12-01 15:00:00");
        api.getNext3DaysMinimumTemperaturesAsString();
        verify(writer).writeDataToResultFile("Next 3-day weather forecast (minimum temperature): \n" +
                "2017-12-02: 0.61\n" +
                "2017-12-03: 1.75\n" +
                "2017-12-04: 0.49\n");
    }

    @Test
    public void testGetNext3DaysMaxTempStringDataWriterCall() throws IOException {
        WeatherAPIJsonDataReceiver json = mock(WeatherAPIJsonDataReceiver.class);
        DataWriter writer = mock(DataWriter.class);
        WeatherAPI api = new WeatherAPI("Tallinn", writer, new DataReader(), json);

        when(json.getWeatherForecastData(api.getApiKey(), "Tallinn")).thenReturn(getFakeJsonWeatherForecast());
        api.setCurrentDate("2017-12-01 15:00:00");
        api.getNext3DaysMaximumTemperaturesAsString();
        verify(writer).writeDataToResultFile("Next 3-day weather forecast (maximum temperature): \n" +
                "2017-12-02: 2.53\n" +
                "2017-12-03: 4.44\n" +
                "2017-12-04: 3.43\n");
    }

    @Test
    public void testGetInfoAndWriteInCityFile() throws IOException, JSONException {
        WeatherAPIJsonDataReceiver json = mock(WeatherAPIJsonDataReceiver.class);
        DataWriter writer = mock(DataWriter.class);
        WeatherAPI api = new WeatherAPI("Tallinn", writer, new DataReader(), json);

        when(json.getCurrentWeatherData(api.getApiKey(), "Tallinn")).thenReturn(getFakeJsonCurrentWeather());
        when(json.getWeatherForecastData(api.getApiKey(), "Tallinn")).thenReturn(getFakeJsonWeatherForecast());
        api.setCurrentDate("2017-12-01 15:00:00");
        api.getInfoAndWriteInCityFile();
        verify(writer).writeDataToCityFile("City: Tallinn\n" +
                "Coordinates: 59:24\n" +
                "Next 3-day weather forecast (maximum temperature): \n" +
                "2017-12-02: 2.53\n" +
                "2017-12-03: 4.44\n" +
                "2017-12-04: 3.43\n" +
                "\n" +
                "Next 3-day weather forecast (minimum temperature): \n" +
                "2017-12-02: 0.61\n" +
                "2017-12-03: 1.75\n" +
                "2017-12-04: 0.49\n" +
                "\n" +
                "Current temperature: 1.0", "Tallinn");
    }
}