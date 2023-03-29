package notebook.logger;

import notebook.view.UserView;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.logging.*;

public class Logger {
    private final String fileName;
    public Logger (String fileName) {
        this.fileName = fileName;
    }
    public void addToLogFile(String message) {
        // так было реализовано логирование раньше, в ДЗ_5
        /*
        try (FileWriter writer = new FileWriter(fileName, true)) {
            String timeStamp = LocalDateTime.now().toString();
                writer.write(timeStamp + " : " + record);
                writer.append('\n');
            writer.flush();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
         */
        // так реализовано логирование для ДЗ_7
        // специально уровень логирования используется FINE, чтобы логи не летели в консоль
        java.util.logging.Logger newLogger = java.util.logging.Logger.getLogger(UserView.class.getName());
        newLogger.setLevel(Level.FINE);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(fileName, true);
            Handler streamHandler = new StreamHandler(fileOutputStream, new SimpleFormatter());
            streamHandler.setLevel(Level.FINE);
            newLogger.addHandler(streamHandler);
            newLogger.log(Level.FINE, message);
        }
        catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        }
    }
}
