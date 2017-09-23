package tests;

import static org.junit.Assert.*;
import org.junit.Test;


public class WeatherAPITests {

    @Test
    public void checkCurrentTempIsInteger() {
        assertTrue(API.getTemperature instanceof Integer);
    }

    @Test
    public void checkDailyHighestTempIsInteger() {
        assertTrue(API.getDailyHightestTemp instanceof Integer);
    }

    @Test
    public void checkDailyLowestTempIsInteger() {
        assertTrue(API.getDailyLowestTemp instanceof Integer);
    }

    @Test
    public void checkIfHighestIsHighest() {
        assertTrue(API.getDailyHighestTemp > API.getDailyLowestTemp);
    }

    @Test
    public void GeoLocationLength() {
        assertTrue(API.getLocation.length == 7);
    }

    @Test
    public void checkXCoordinates() {
        assertTrue(Integer.parseInt(API.getLocation.substring(0, 3)));
    }

    @Test
    public void checkYCoordinates() {
        assertTrue(Integer.parseInt(API.getLocation.substring(4)));
    }
}