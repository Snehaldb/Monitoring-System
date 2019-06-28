package util;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.text.ParseException;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

public class TimeStampUtilTest {

    private TimeStampUtil timeStampUtil;

    @Before
    public void setUp() {
        timeStampUtil = new TimeStampUtil();
    }

    @Test
    public void createTimeStampFromString_should_return_valid_epoch_when_date_is_valid() throws ParseException {
        // GIVEN
        // WHEN
        final long timeStampFromString = timeStampUtil.createTimeStampFromString("2014-10-31 00:00");

        // THEN
        Assert.assertThat(timeStampFromString, is(1414738800L));
    }


    @Test(expected = ParseException.class)
    public void createTimeStampFromString_should_throw_exception_when_date_is_not_valid() throws ParseException {
        // GIVEN
        // WHEN
        timeStampUtil.createTimeStampFromString("2014-10-31 00");
    }


    @Test
    public void timeStampToString_should_convert_time_stamp_to_valid_String_format() {
        // GIVEN
        // WHEN
        final String dateString = timeStampUtil.timeStampToString(1414738800L);

        // THEN
        Assert.assertThat(dateString, is("2014-10-31 00:00"));

    }

    @Test
    public void createTimeStampsForDateAndMaxCount_should_return_time_stamps_for_five_min() throws ParseException {
        // GIVEN
        List<Long> list = Arrays.asList(1414738800L, 1414738860L, 1414738920L, 1414738980L, 1414739040L);

        // WHEN
        List<Long> timeStampsForFiveMin = timeStampUtil.createTimeStampsEveryMin("2014-10-31 00:00", 5);

        // THEN
        Assert.assertThat(timeStampsForFiveMin, equalTo(list));
    }
}