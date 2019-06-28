package util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TimeStampUtil {

    public long createTimeStampFromString(String start) throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = df.parse(start);
        return (date.getTime()) / 1000;
    }

    public String timeStampToString(long timeStamp) {
        timeStamp *= 1000;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return sdf.format(new Date(timeStamp));

    }

    public List<Long> createTimeStampsEveryMin(String date, int maxMinCount) throws ParseException {
        List<Long> timeStamps = new ArrayList<>();
        long now = createTimeStampFromString(date);
        for (int i = 0; i < maxMinCount; i++) {
            timeStamps.add(now + i * 60);
        }

        return timeStamps;
    }

}
