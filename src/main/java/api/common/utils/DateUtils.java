package api.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import api.common.exceptions.InternalServerErrorException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * DateUtils - wrapper class providing utility methods to get or manipulate dates
 */
public class DateUtils {

    /**
     * the format of the session date provided to the JVM
     */
    private static final String SESSION_DATE_MASK = "dd-MM-yyyy";

    /**
     * the name of the session date parameter that is provided to the JVM should session dates be required
     */
    private static final String SESSION_DATE_JVM_ARG = "session_date";

    /**
     * the final day of a tax year is 5th April
     **/
    private static final String TAX_YEAR_END = "05-04-";

    private static Logger LOGGER = LoggerFactory.getLogger(DateUtils.class);

    /**
     * getCurrentDate - returns the current date or a session date if specified
     * <p/>
     * If the JVM contains a session date property then a date representing the specified session date is returned (time portion will be 00:00:00)
     * Where the session date is not specified a new Date() will be returned representing the current time from the operating system
     *
     * @return a date object equivalent to the current date/time or a session date if specified
     */
    public static Date getCurrentDate() {

        //check if the -Dsession_date=dd-mm-yyyy arg has been set or passed to the JVM
        String sessionDate = System.getProperty(SESSION_DATE_JVM_ARG);
        Date currentDate;
        if (sessionDate != null) {
            try {
                currentDate = new SimpleDateFormat(SESSION_DATE_MASK).parse(sessionDate);
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Session Date has been set to [{}]", currentDate.toString());
                }
            } catch (Exception ex) {
                LOGGER.warn("Unable to parse session date [{}] system date used instead", sessionDate);
                currentDate = new Date();
            }
        } else {
            currentDate = new Date();
        }
        return currentDate;
    }

    /**
     * getCurrentTaxYear - for the current System date this method returns the tax year.
     * <p/>
     * Tax year runs 6th April to 5th April
     *
     * @return a 4 character string object representing a tax year
     */
    public static String getCurrentTaxYear() {
        return getTaxYear(getCurrentDate());
    }

    /**
     * getTaxYear - for a specified date this method returns the tax year associated with it.
     * <p/>
     * Tax year runs 6th April to 5th April
     *
     * @param date - a Date object with the date to be checked
     * @return a 4 character string object representing a tax year
     */
    public static String getTaxYear(Date date) {

        if (date == null) {
            throw new InternalServerErrorException();
        }

        //Get the calendar year of the date
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int year = cal.get(Calendar.YEAR);

        //Create a Date object representing the end of the tax year
        String taxYearDate = TAX_YEAR_END + year;

        Date taxYear = getDate(taxYearDate, SESSION_DATE_MASK);

        if (date.after(taxYear)) {
            return String.valueOf(year + 1);
        } else {
            return String.valueOf(year);
        }

    }

    /**
     * dateValidator takes a date and a date format and makes sure that the date is not null and of the correct format
     *
     * @param inputDate  - this is the date that is being checked
     * @param dateFormat - this is the format in which the date should be displayed eg "dd-MM-yyyy"
     * @return returns as boolean true if the date and format match and false if they do not.
     */
    public static boolean dateValidator(String inputDate, String dateFormat) {

        if (inputDate == null || inputDate.equals("") || dateFormat.equals("")) {
            LOGGER.debug("Empty inputDate [{}] or dateFormat [{}]", inputDate, dateFormat);
            return false;
        }

        SimpleDateFormat sdf = new SimpleDateFormat();
        try {
            sdf.applyPattern(dateFormat);
        } catch (Exception e) {
            LOGGER.debug("Invalid dateFormat {}", dateFormat);
            return false;
        }
        sdf.setLenient(false);

        // The method tries to parse the date into a date object of the form dateFormat.
        // If an exception is thrown the method returns false, whereas a successful parse will return true
        try {
            sdf.parse(inputDate);
        } catch (ParseException e) {
            LOGGER.debug("Unable to parse {} using format {}", inputDate, dateFormat);
            return false;
        }

        return true;
    }

    /**
     * getDate takes a String date and a String date format and returns the Date object for the supplied date String
     *
     * @param dateString - this is the date that is being checked
     * @param dateFormat - this is the format in which the date should be displayed eg "dd-MM-yyyy"
     * @return returns a Date object for the supplied date String.
     */
    public static Date getDate(String dateString, String dateFormat) {

        if (dateString == null || dateString.equals("") || dateFormat.equals("")) {
            LOGGER.warn("Empty inputDate [{}] or dateFormat [{}]", dateString, dateFormat);
            throw new InternalServerErrorException();
        }

        SimpleDateFormat sdf = new SimpleDateFormat();
        try {
            sdf.applyPattern(dateFormat);
        } catch (Exception e) {
            LOGGER.warn("Invalid dateFormat {}", dateFormat);
            throw new InternalServerErrorException();
        }

        // The method tries to parse the date into a date object of the form dateFormat.
        // If an exception is thrown the method returns false, whereas a successful parse will return true
        try {
            return sdf.parse(dateString);
        } catch (ParseException e) {
            LOGGER.warn("Unable to parse {} using dateFormat {}", dateString, dateFormat);
            throw new InternalServerErrorException();
        }
    }

    /**
     * dateToString takes a Date Object and a String date format and returns the date as a string in the string format
     *
     * @param inputDate  - this is the date that is being checked
     * @param dateFormat - this is the format in which the date should be displayed eg "dd-MM-yyyy"
     * @return returns the date as a string in the string format.
     */
    public static String dateToString(Date inputDate, String dateFormat) {

        if (inputDate == null || dateFormat.equals("")) {
            LOGGER.warn("Empty inputDate [{}] or dateFormat [{}]", inputDate, dateFormat);
            throw new InternalServerErrorException();
        }

        SimpleDateFormat sdf = new SimpleDateFormat();
        try {
            sdf.applyPattern(dateFormat);
        } catch (Exception e) {
            LOGGER.warn("Invalid dateFormat {}", dateFormat);
            throw new InternalServerErrorException();
        }
        sdf.setLenient(false);
        // The method tries to parse the date into a date object of the form dateFormat.

        return sdf.format(inputDate);
    }

    /**
     * This method takes two string dates and checks that the first date is before the second one.
     *
     * @param startDate  - the date that should occur first
     * @param endDate    - the date that should occur second
     * @param dateFormat - the format of which the date takes eg(dd-MM-yyyy)
     * @return this returns true when the first date occurs before the second, and false when it is the same date or after the other date
     */
    public static boolean dateOrderChecker(String startDate, String endDate, String dateFormat) {
        return getDate(startDate, dateFormat).before(getDate(endDate, dateFormat));
    }

    /**
     * This code finds the number of days between two given string dates. If that number is less than the given number the boolen returns false.
     * Warning: if the dates cross the date of daylight savings then the day figure will be one day incorrect.
     *
     * @param startDate     The first date to compare
     * @param endDate       The second date to compare
     * @param dateFormat    The format the dates are accepted in.
     * @param maxDatePeriod The maximum period apart that the dates can be.
     * @return Returns true if the time between the two dates is less than the maxDatePeriod.
     */
    public static boolean timeBetweenDateChecker(String startDate, String endDate, String dateFormat, int maxDatePeriod) {

        Date start = getDate(startDate, dateFormat);
        Date end = getDate(endDate, dateFormat);

        return isDatesWithinDaysThreshold(start, end, maxDatePeriod);
    }

    /**
     * takes two date objects and checks that they are within a specified date period and not the same date.
     *
     * @param startDate     date object start of the period
     * @param endDate       date object end of the period
     * @param maxDatePeriod the max allowed number of days between the two dates.
     * @return returns true if the difference between the two dates is less than the specified threshold and .
     */
    public static boolean isDatesWithinDaysThreshold(Date startDate, Date endDate, int maxDatePeriod) {

        LOGGER.debug("Comparing start date {} with end date {} using duration in days of {}", startDate, endDate, maxDatePeriod);
        boolean validTime = false;

        long timeDifferenceMilliseconds = endDate.getTime() - startDate.getTime();
        long timeDifferenceDays = TimeUnit.MILLISECONDS.toDays(timeDifferenceMilliseconds);
        if ((timeDifferenceDays < maxDatePeriod) && (timeDifferenceDays > 0)) {
            validTime = true;
        }

        return validTime;
    }

    /**
     * Takes a date in the format yyyyMMdd or yyyyddMM and puts a specified delimiter between Year Month and Day sections
     *
     * @param date  is a date in the format yyyyMMdd or yyyyddMM
     * @param delim is the delimiter in which to separate the date
     * @return the separated date.
     */
    public static String dateFormatter(String date, String delim) {
        if (date != null && !date.isEmpty()) {
            return (date.substring(0, 4) + delim + date.substring(4, 6) + delim + date.substring(6, 8));
        }
        return null;
    }
}