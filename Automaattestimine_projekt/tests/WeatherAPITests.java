/*import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import weatherapi.WeatherAPI;


public class WeatherAPITests {
    private WeatherAPI weatherAPI;

    @Before
    public void setUp() throws Exception {
        this.weatherAPI = new WeatherAPI("Tallinn");
        weatherAPI.setNext3DaysMinTemp();
        weatherAPI.setNext3DaysMaxTemp();
    }

    @Test
    public void testIfCurrentTempIsDouble() {
        try {
            assertTrue(weatherAPI.getCurrentTemperatureByCity() instanceof Double);
        } catch (Exception e) {
            fail();
        }
    }

    @Test ()
    public void testIfDailyHighestTempIsDouble() {
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
            assertTrue(Integer.valueOf(coords.substring(0, index)) instanceof Integer);
        } catch (NumberFormatException e) {
            fail();
        }
    }

    @Test
    public void testYCoordinatesAreInteger() throws Exception {
        String coords = weatherAPI.getGeoCoordinates();
        int index = coords.indexOf(':');
        try {
            assertTrue(Integer.valueOf(coords.substring(index + 1)) instanceof Integer);
        } catch (NumberFormatException e) {
            fail();
        }
    }

    @Test
    public void testIfCurrentWeatherDataNotNull() throws Exception {
        assertNotNull(weatherAPI.getCurrentWeatherData());
    }

    @Test
    public void testIfWeatherForecastDataNotNull() throws Exception {
        assertNotNull(weatherAPI.getWeatherForecastData());
    }

    @Test
    public void testIfCurrentWeatherDataNotEmpty() throws Exception {
        assertFalse(weatherAPI.getCurrentWeatherData().equals(""));
    }

    @Test
    public void testIfWeatherForecastDataNotEmpty() throws Exception {
        assertFalse(weatherAPI.getWeatherForecastData().equals(""));
    }


}
*/
