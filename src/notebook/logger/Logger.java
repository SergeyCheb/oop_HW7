package notebook.logger;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

public class Logger {
    private final String fileName;
    public Logger (String fileName) {
        this.fileName = fileName;
    }
    public void addToLogFile(String record) {
        try (FileWriter writer = new FileWriter(fileName, true)) {
            String timeStamp = LocalDateTime.now().toString();
                writer.write(timeStamp + " : " + record);
                writer.append('\n');
            writer.flush();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
