package com.nrlm.lakhpatikisaan.utils;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.nrlm.lakhpatikisaan.MainApplication;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AppDateFactory {
    private static AppDateFactory dateFactory;
    private Locale locale;
    MainApplication mainApplication;
    AppUtils appUtils;

    public static AppDateFactory getInstance() {
        if (dateFactory == null) {
            dateFactory = new AppDateFactory();
        }
        return dateFactory;
    }

    public AppDateFactory() {
        appUtils = AppUtils.getInstance();
    }

    /****get time stamp in yyyy-MM-dd HH:mm:ss*******/
    public String getTimeStamp() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = df.format(Calendar.getInstance().getTime());
        return date;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String getCurrentDateAndTime() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }

    public String getCurrentDate(){
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        return formatter.format(date);
    }


    /********get date in yyyy-MM-dd**********/
    public String getTodayDate() {
        String dateoftodayis = "";
        try {
            Calendar c = Calendar.getInstance();
            int day = c.get(Calendar.DAY_OF_MONTH);
            int month = c.get(Calendar.MONTH);
            int year = c.get(Calendar.YEAR);
            dateoftodayis = year + "-" + (month + 1) + "-" + day;
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            Date d = df.parse(dateoftodayis);
            dateoftodayis = df.format(d);

        } catch (Exception e) {
            mainApplication.appUtils.showLog("Expection in Today date: " + e, AppDateFactory.class);
        }
        return dateoftodayis;
    }

    /*********change date formate yyyy-MM-dd to dd-MM-yyyy*******/
    public String changeFormate(String inputDate) {
        String outputPattern = "dd-MM-yyyy";
        String inputPattern = "yyyy-MM-dd";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = "";

        try {
            date = inputFormat.parse(inputDate);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;

    }


    /***convert String date to Date object in given formate
     *
     * @param date
     * @return input- dd-MM-YYYY(string)
     *         output- dd-MM-YYY(Date)
     */
    public Date convertStringToDate(String date) {
        Date convertedDate = null;
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        try {
            convertedDate = sdf.parse(date);
            sdf.format(convertedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return convertedDate;
    }


    /***both date is is in DD-mm-YYYY
     *
     * @param memberDOJ
     * @param nrlmFormationDate
     * @return monthname-year(JAN-2019)
     */
    public List<String> monthYear(String memberDOJ, String nrlmFormationDate) {
        List<String> monthYearData = new ArrayList<>();

        if (convertStringToDate(memberDOJ).before(convertStringToDate(nrlmFormationDate))) {
            Date d = null;
            try {
                d = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH).parse(nrlmFormationDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Calendar cal = Calendar.getInstance();
            cal.setTime(d);
            String monthName = new SimpleDateFormat("MMM").format(cal.getTime());
            String year = new SimpleDateFormat("yyyy").format(cal.getTime());
            String yearCode = new SimpleDateFormat("MM").format(cal.getTime());
            appUtils.showLog("***YEAR NAME****" + year, AppDateFactory.class);
            appUtils.showLog("***MONTH NAME****" + monthName, AppDateFactory.class);
            appUtils.showLog("***Year Code****" + yearCode, AppDateFactory.class);

            monthYearData.add(monthName);
            monthYearData.add(year);
            monthYearData.add(yearCode);


        } else if (convertStringToDate(memberDOJ).after(convertStringToDate(nrlmFormationDate))) {

            Date d = null;
            try {
                d = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH).parse(memberDOJ);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Calendar cal = Calendar.getInstance();
            cal.setTime(d);
            cal.add(Calendar.MONTH, -1);
            String monthName = new SimpleDateFormat("MMM").format(cal.getTime());
            String year = new SimpleDateFormat("yyyy").format(cal.getTime());
            String yearCode = new SimpleDateFormat("MM").format(cal.getTime());
            appUtils.showLog("***YEAR NAME****" + year, AppDateFactory.class);
            appUtils.showLog("***MONTH NAME****" + monthName, AppDateFactory.class);
            appUtils.showLog("***Year Code****" + yearCode, AppDateFactory.class);

            monthYearData.add(monthName);
            monthYearData.add(year);
            monthYearData.add(yearCode);

        }
        return monthYearData;

    }

    public int getMemberClanderYear(String memberDOJ, String nrlmFormationDate){

        int finalYear = 2011;

        if (convertStringToDate(memberDOJ).before(convertStringToDate(nrlmFormationDate))) {
            Date d = null;
            try {
                d = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH).parse(nrlmFormationDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Calendar cal = Calendar.getInstance();
            cal.setTime(d);
            String monthName = new SimpleDateFormat("MMM").format(cal.getTime());
            String year = new SimpleDateFormat("yyyy").format(cal.getTime());
            String yearCode = new SimpleDateFormat("MM").format(cal.getTime());
            appUtils.showLog("***YEAR NAME IN AFTER****" + year, AppDateFactory.class);
            appUtils.showLog("***MONTH NAME IN AFTER****" + monthName, AppDateFactory.class);
            appUtils.showLog("***Year Code IN AFTER****" + yearCode, AppDateFactory.class);

            finalYear = Integer.parseInt(year);
        } else if (convertStringToDate(memberDOJ).after(convertStringToDate(nrlmFormationDate))) {

            Date d = null;
            try {
                d = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH).parse(memberDOJ);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Calendar cal = Calendar.getInstance();
            cal.setTime(d);
            cal.add(Calendar.MONTH, -1);
            String monthName = new SimpleDateFormat("MMM").format(cal.getTime());
            String year = new SimpleDateFormat("yyyy").format(cal.getTime());
            String yearCode = new SimpleDateFormat("MM").format(cal.getTime());
            appUtils.showLog("***YEAR NAME IN AFTER****" + year, AppDateFactory.class);
            appUtils.showLog("***MONTH NAME IN AFTER****" + monthName, AppDateFactory.class);
            appUtils.showLog("***Year Code IN AFTER****" + yearCode, AppDateFactory.class);
            finalYear = Integer.parseInt(year);
        }

        return finalYear;


    }

}
