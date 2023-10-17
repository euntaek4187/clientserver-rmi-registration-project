package logger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;
public class CustomFormatter extends Formatter {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @Override
    public String format(LogRecord record) {
        StringBuilder builder = new StringBuilder();
        builder.append(dateFormat.format(new Date(record.getMillis()))).append(" - ");
        builder.append("[").append(record.getLevel()).append("] ");
        builder.append(formatMessage(record));
        builder.append(System.lineSeparator());
        return builder.toString();
    }
}
