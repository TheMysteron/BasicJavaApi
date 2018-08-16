package api.common.utils;

import api.common.exceptions.InternalServerErrorException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static api.common.utils.DateUtils.dateOrderChecker;
import static api.common.utils.DateUtils.dateToString;
import static api.common.utils.DateUtils.dateValidator;
import static api.common.utils.DateUtils.getCurrentDate;
import static api.common.utils.DateUtils.getCurrentTaxYear;
import static api.common.utils.DateUtils.getDate;
import static api.common.utils.DateUtils.getTaxYear;
import static api.common.utils.DateUtils.isDatesWithinDaysThreshold;
import static api.common.utils.DateUtils.timeBetweenDateChecker;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class DateUtilsTest {


    private static final Logger logger = LoggerFactory.getLogger(DateUtilsTest.class);

    private Date lb;
    private Date ub;

    @Before
    public void setUp() throws Exception {
        SimpleDateFormat date = new SimpleDateFormat("yyyyMMdd");
        lb = date.parse("20150405");
        ub = date.parse("20150406");

    }

    @After
    public void tearDown() {
        lb = null;
        ub = null;
    }

    @Test
    public void currentLowerBoundTest() {
        System.setProperty("session_date", "05-04-2015");
        String ty = getCurrentTaxYear();

        logger.info("lower bound tax year: " + ty);
        assertEquals(ty, "2015", "2015");
    }

    @Test
    public void currentUpperBoundTest() {
        System.setProperty("session_date", "06-04-2015");
        String ty = getCurrentTaxYear();

        logger.info("upper bound tax year: " + ty);
        assertEquals(ty, "2016", "2016");
    }

    @Test
    public void specifiedLowerBoundTest() {

        String ty = getTaxYear(lb);

        logger.info("lower bound tax year: " + ty);
        assertEquals(ty, "2015", "2015");
    }

    @Test
    public void specifiedUpperBoundTest() {

        String ty = getTaxYear(ub);

        logger.info("upper bound tax year: " + ty);
        assertEquals(ty, "2016", "2016");
    }

    /**
     * Check the session date is set correctly
     */
    @Test
    public void checkSessionDatePopulated() {

        //mimic the JVM setting the session date to 01/01/2016
        System.setProperty("session_date", "01-01-2016");

        Date sd = getCurrentDate();

        //Create a Calendar to 01-01-2016
        Calendar cal = Calendar.getInstance();
        cal.set(2016, Calendar.JANUARY, 1, 0, 0, 0);
        //zeroise the milliseconds
        cal.set(Calendar.MILLISECOND, 0);

        logger.info(sd.toString() + " : " + cal.getTime().toString());

        assertEquals("Session Date set to 01-01-2016", cal.getTimeInMillis(), sd.getTime());

    }

    /**
     * Check that the nulls come back false.
     */
    @Test
    public void testDateValidatorNull() {
        assertFalse(dateValidator(null, "dd-MM-yyyy"));
    }

    @Test
    public void testDateValidatorEmpty() {
        assertFalse(dateValidator("", "dd-MM-yyyy"));
    }

    /**
     * Check that the correct input returns true.
     */
    @Test
    public void testDateValidatorNotNull() {
        assertTrue(dateValidator("01-01-1999", "dd-MM-yyyy"));
    }

    /**
     * These test that an incorrect date returns false
     */
    @Test
    public void testDateValidatorDifferentFormat() {
        assertFalse(dateValidator("01/01/1999", "dd-MM-yyyy"));
    }

    @Test
    public void testDateValidatorBadDay() {
        assertFalse(dateValidator("40-12-1999", "dd-MM-yyyy"));
    }

    @Test
    public void testDateValidatorBadMonth() {
        assertFalse(dateValidator("04-13-1999", "dd-MM-yyyy"));
    }

    @Test
    public void testDateValidatorShortDate() {
        assertTrue(dateValidator("1-1-199", "dd-MM-yyyy"));
    }

    @Test
    public void testDateValidatorLongYear() {
        assertTrue(dateValidator("01-01-19999", "dd-MM-yyyy"));
    }

    @Test
    public void testDateValidatorDecimal() {
        assertFalse(dateValidator("01-01.2-19999", "dd-MM-yyyy"));
    }

    @Test
    public void testDateValidatorNonsenseDate() {
        assertFalse(dateValidator("ab-cd-efgh", "dd-MM-yyyy"));
    }

    @Test
    public void testDateValidatorInvalidFormat() {
        assertFalse(dateValidator("31-01-2000", "dd-11-yyyy"));
    }

    //Testing dateOrderChecker


    //Make sure that date 1 being before date 2 comes back true
    @Test
    public void testDateOrderChecker() {
        assertTrue(dateOrderChecker("01-01-2000", "02-01-2000", "dd-MM-yyyy"));
    }

    //Make sure that the dates being in the wrong order comes back false
    @Test
    public void testDateOrderCheckerFalse() {
        assertFalse(dateOrderChecker("02-01-2000", "01-01-2000", "dd-MM-yyyy"));
    }

    //test case when dates are the same.
    @Test
    public void testDateOrderCheckerSameDate() {
        assertFalse(dateOrderChecker("01-01-2000", "01-01-2000", "dd-MM-yyyy"));
    }

    //test case when dates are the same.
    @Test(expected = InternalServerErrorException.class)
    public void testDateOrderCheckerInvalidDateSyntax() {
        assertFalse(dateOrderChecker("01/01/2000", "20/01/2000", "dd-MM-yyyy"));
    }

    @Test(expected = InternalServerErrorException.class)
    public void testDateOrderCheckerInvalidFormat() {
        assertFalse(dateOrderChecker("01/01/2000", "20/01/2000", "dd-uu-yyyy"));
    }

    @Test(expected = InternalServerErrorException.class)
    public void testDateOrderCheckerEmptyStrings() {
        assertFalse(dateOrderChecker("", "", ""));
    }

    //test case when dates are the same.
    @Test
    public void testDateOrderCheckerInvalidDateFormat() {
        assertFalse(dateOrderChecker("2000-01-01", "2000-01-01", "dd-MM-yyyy"));
    }


    @Test
    public void timeBetweenDateCheckerTrue() {
        assertTrue(timeBetweenDateChecker("01-01-2016", "01-02-2016", "dd-MM-yyyy", 60));
    }

    @Test
    public void timeBetweenDateCheckerLongPeriod() {
        assertFalse(timeBetweenDateChecker("01-01-2016", "01-06-2016", "dd-MM-yyyy", 60));
    }

    @Test
    public void timeBetweenDateCheckerBackwardsDates() {
        assertFalse(timeBetweenDateChecker("01-02-2016", "01-01-2016", "dd-MM-yyyy", 60));
    }

    @Test
    public void timeBetweenDateCheckerNegativeTime() {
        assertFalse(timeBetweenDateChecker("01-01-2016", "01-02-2016", "dd-MM-yyyy", -60));
    }

    @Test(expected = InternalServerErrorException.class)
    public void timeBetweenDateCheckerEmptyStrings() {
        assertFalse(timeBetweenDateChecker("", "", "", 60));
    }

    @Test(expected = InternalServerErrorException.class)
    public void timeBetweenDateCheckerInvalidSyntax() {
        timeBetweenDateChecker("01/01/2016", "01022016", "dd-MM-yyyy", 60);
    }

    @Test(expected = InternalServerErrorException.class)
    public void timeBetweenDateCheckerInvalidFormat() {
        assertFalse(timeBetweenDateChecker("01/01/2016", "01022016", "dd-jj-yyyy", 60));
    }

    @Test
    public void isDatesWithinDaysThresholdPass() {
        SimpleDateFormat date = new SimpleDateFormat("yyyyMMdd");
        try {
            assertTrue(isDatesWithinDaysThreshold(date.parse("20150405"), date.parse("20150406"), 60));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void isDatesWithinDaysThresholdOutOfThreshold() {
        SimpleDateFormat date = new SimpleDateFormat("yyyyMMdd");
        try {
            assertFalse(isDatesWithinDaysThreshold(date.parse("20150405"), date.parse("20150407"), 1));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void isDatesWithinDaysThresholdOnThreshold() {
        SimpleDateFormat date = new SimpleDateFormat("yyyyMMdd");
        try {
            assertFalse(isDatesWithinDaysThreshold(date.parse("20150405"), date.parse("20150407"), 2));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void dateToStringValid() {
        Long time = new Long("949276800000");
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(time); //set date time to 31/01/2000 00:00:00
        Date date = c.getTime();

        assertEquals("31-01-2000", dateToString(date, "dd-MM-yyyy"));
    }

    @Test(expected = InternalServerErrorException.class)
    public void dateToStringInvalid() {
        Long time = new Long("949276800000");
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(time); //set date time to 31/01/2000 00:00:00
        Date date = c.getTime();

        assertEquals("", dateToString(date, "dd-xx-yyyy"));
    }

    @Test
    public void dateCreatorValid() {
        Long time = new Long("949276800000");
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(time); //set date time to 31/01/2000 00:00:00
        Date valid = c.getTime();

        String date = "31-01-2000";

        assertEquals(valid, getDate(date, "dd-MM-yyyy"));
    }

    @Test(expected = InternalServerErrorException.class)
    public void dateCreatorInvalidString() {
        System.setProperty("session_date", "01-01-2016");

        String date = "31/01/2000";

        assertEquals(getCurrentDate(), getDate(date, "dd-MM-yyyy"));
    }

    @Test(expected = InternalServerErrorException.class)
    public void dateCreatorInvalidFormat() {
        System.setProperty("session_date", "01-01-2016");

        String date = "31/01/2000";

        assertEquals(getCurrentDate(), getDate(date, "dd-xx-yyyy"));
    }

}