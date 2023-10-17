package logger;
import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
public class LoggerManager {
    private Logger logger;
    public LoggerManager(String logFilePath) {
        logger = Logger.getLogger("ServerLogger");
        initLogger(logFilePath);
    }
    private void initLogger(String logFilePath) {
        try {
            FileHandler fileHandler = new FileHandler(logFilePath, true);
            fileHandler.setFormatter(new CustomFormatter());
            Handler[] handlers = logger.getHandlers();
            for (Handler handler : handlers) {
                logger.removeHandler(handler);
            }
            logger.addHandler(fileHandler);
            logger.setLevel(Level.ALL);
            Logger rootLogger = LogManager.getLogManager().getLogger("");
            Handler[] rootHandlers = rootLogger.getHandlers();
            for (Handler handler : rootHandlers) {
                if (handler instanceof ConsoleHandler) {
                    rootLogger.removeHandler(handler);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void logInfo(String message) {
        logger.info(message);
    }
    public void logError(String message, Exception e) {
        logger.log(Level.SEVERE, message, e);
    }
}