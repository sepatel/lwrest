package org.inigma.lwrest.logger;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class DetailedLogger extends Formatter {
    @Override
    public String format(LogRecord record) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd'T'HH.mm.ss.SSS");
        return String.format("%s %5s [Thread %s] (%s %s) %s\n", sdf.format(new Date(record.getMillis())), record
                .getLevel().getResourceBundleName(), record.getThreadID(), record.getLoggerName(), record
                .getSourceMethodName(), record.getMessage());
    }
}
