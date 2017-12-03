package tests;

import org.junit.Before;
import org.junit.Test;
import weatherapi.WeatherAPIJsonDataReceiver;

import java.io.IOException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

public class WeatherAPIJsonDataReceiverTests {

    private WeatherAPIJsonDataReceiver receiver;

    @Before
    public void setUp() throws Exception {
        this.receiver = new WeatherAPIJsonDataReceiver();
    }

    @Test
    public void testIfCurrentWeatherDataNotNull() throws IOException {
        assertNotNull(receiver.getCurrentWeatherData("d0fcb2e76efa4e13887c5910130b1ead", "Tallinn"));
    }

    @Test
    public void testIfWeatherForecastDataNotNull() throws IOException {
        assertNotNull(receiver.getWeatherForecastData("d0fcb2e76efa4e13887c5910130b1ead", "Tallinn"));
    }

    @Test
    public void testIfCurrentWeatherDataNotEmpty() throws IOException {
        assertFalse(receiver.getCurrentWeatherData("d0fcb2e76efa4e13887c5910130b1ead", "Tallinn").equals(""));
    }

    @Test
    public void testIfWeatherForecastDataNotEmpty() throws IOException {
        assertFalse(receiver.getWeatherForecastData("d0fcb2e76efa4e13887c5910130b1ead", "Tallinn").equals(""));
    }
}