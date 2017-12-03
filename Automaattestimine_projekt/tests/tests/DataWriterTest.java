package tests;

import org.junit.Before;
import org.junit.Test;
import weatherapi.DataWriter;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class DataWriterTest {
    private DataWriter writer;

    @Before
    public void setUp() throws Exception {
        this.writer = new DataWriter();
    }

    @Test
    public void writeDataToResultFileCorrectOutput() throws Exception {
        writer.writeDataToResultFile("Estonia is my city");
        String result = "";
        try (BufferedReader reader = Files.newBufferedReader(Paths.get("output.txt"))) {
            result = reader.readLine();
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        assertEquals("Estonia is my city", result);
    }

    @Test
    public void writeDataToCityFileCorrectOutput() throws Exception {
        writer.writeDataToCityFile("This better work", "Estonia");
        String result = "";
        try (BufferedReader reader = Files.newBufferedReader(Paths.get("cities/", "Estonia.txt"))) {
            result = reader.readLine();
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        assertEquals("This better work", result);
    }

    @Test
    public void writeDataToResultFileEmptyCity() throws Exception {
        writer.writeDataToCityFile("This better work", "");
        String result = "";
        try (BufferedReader reader = Files.newBufferedReader(Paths.get("cities/", ".txt"))) {
            result = reader.readLine();
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        assertEquals("This better work", result);
    }
}