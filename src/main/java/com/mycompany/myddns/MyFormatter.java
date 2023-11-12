
package com.mycompany.myddns;
import java.util.logging.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MyFormatter extends Formatter {
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ");

    @Override
    public String format(LogRecord rec) {
        StringBuilder sb = new StringBuilder();
        sb.append("[").append(rec.getLevel().getName()).append("] ");
        sb.append(sdf.format(new Date(rec.getMillis()))).append(": ");
        sb.append(rec.getMessage()).append("\n");
        return sb.toString();
    }
}