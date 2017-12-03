package weatherapi;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class DataReader {

    public String getCityFromFile() {
        String city = "";
        try (BufferedReader reader = Files.newBufferedReader(Paths.get("input.txt"))) {
            List<String> cities = new ArrayList<>();
            while (true) {
                String line = reader.readLine();
                if (line == null) break;
                cities.add(line);
            }
            if (!cities.isEmpty()) {
                city = cities.remove(0);
                writeCitiesToFile(cities);
            } else {
                city = "Tallinn";
            }

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return city;
    }

    public void writeCitiesToFile(List<String> cities) {
        StringBuilder data = new StringBuilder();

        for (int i = 0; i < cities.size(); i++) {
            data.append(cities.get(i)).append("\n");
        }

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("input.txt"));
            writer.write(data.toString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
