package it.polito.dp2.rest.rns.utility;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

public class DateConverter {
	
	/**
	 * Function to convert a string with a certain format to the corresponding representation 
	 * in XMLGregorianCalendar
	 * @param inputDate = initial string for the date
	 * @param dateFormat = format of the date to be converted
	 * @return the corresponding representation in XMLGregorianCalendar
	 * @throws ParseException --> can be thrown if the date can't be parsed
	 * @throws DatatypeConfigurationException --> can be thrown when creating the gregorian representation
	 */
	public static XMLGregorianCalendar convertFromString(String inputDate, String dateFormat) throws ParseException, DatatypeConfigurationException {
		DateFormat format = new SimpleDateFormat(dateFormat);
		Date date = format.parse(inputDate);
		
		System.out.println("String: " + inputDate + " --- Converted Date: " + date.toString());
		
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		
		return DatatypeFactory.newInstance().newXMLGregorianCalendar(calendar);
	}
	
	/**
	 * Get time difference in MILLISECONDS between two dates described
	 * as XMLGregorianCalendar
	 * @param start = beginning date
	 * @param end = end date
	 * @return the duration in milliseconds elapsed between the two
	 */
	public static long getDurationFromXMLGregorianCalendar(XMLGregorianCalendar start, XMLGregorianCalendar end) {
		long duration = end.toGregorianCalendar().getTimeInMillis() - start.toGregorianCalendar().getTimeInMillis();
		System.out.println("Start: " + start.toString() + " --- End: " + end.toString());
		return duration;
	}
	
	/**
	 * Function to retrieve the "now" time as XMLGregorian calendar
	 * @return the "now" time embedded in XMLGregorianCalendar structure
	 * @throws DatatypeConfigurationException --> can be thrown when creating the gregorian representation
	 */
	public static XMLGregorianCalendar getXMLGregorianCalendarNow() 
            throws DatatypeConfigurationException
    {
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        DatatypeFactory datatypeFactory = DatatypeFactory.newInstance();
        XMLGregorianCalendar now = 
            datatypeFactory.newXMLGregorianCalendar(gregorianCalendar);
        return now;
    }
}
