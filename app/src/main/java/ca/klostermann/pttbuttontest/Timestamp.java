package ca.klostermann.pttbuttontest;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Timestamp {
  public static String now() {
    DateFormat timestampFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    Date now = new Date();

    return timestampFormat.format(now);
  }
}
