package weatherapi;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class DataWriter {

    public void writeDataToResultFile(String data) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("output.txt"));
            writer.write(data);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeDataToCityFile(String data, String city) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("cities/" + city +".txt"));
            writer.write(data);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
