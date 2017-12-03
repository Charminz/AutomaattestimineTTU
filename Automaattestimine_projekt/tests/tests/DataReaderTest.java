package tests;

import org.junit.Before;
import org.junit.Test;
import weatherapi.DataReader;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class DataReaderTest {
    private DataReader reader;

    @Before
    public void setUp() throws Exception {
        this.reader = new DataReader();
    }

    @Test
    public void getCityFromFile() throws Exception {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("input.txt"));
            writer.write("Keila");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String city = reader.getCityFromFile();

        assertEquals("Keila", city);
    }

    @Test
    public void writeCitiesToFile() throws Exception {
        reader.writeCitiesToFile(Arrays.asList("Keila", "Tallinn", "Moskva"));

        List<String> cities = new ArrayList<>();

        try (BufferedReader reader = Files.newBufferedReader(Paths.get("input.txt"))) {
            while (true) {
                String line = reader.readLine();
                if (line == null) break;
                cities.add(line);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        assertEquals(Arrays.asList("Keila", "Tallinn", "Moskva"), cities);

    }

}