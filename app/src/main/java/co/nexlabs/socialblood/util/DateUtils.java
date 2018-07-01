package co.nexlabs.socialblood.util;

import java.util.Calendar;

/**
 * Created by myozawoo on 3/14/16.
 */
public class DateUtils {

    public static String getAge(String date){

        String insertDate = date;
        String[] items1 = insertDate.split("-");
        String d1=items1[2];
        String m1=items1[1];
        String y1=items1[0];
        int day= Integer.parseInt(d1);
        int month = Integer.parseInt(m1);
        int year = Integer.parseInt(y1);

        Calendar dob = Calendar.getInstance();

        Calendar today = Calendar.getInstance();

        dob.set(year, month, day);

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)){
            age--;
        }

        Integer ageInt = new Integer(age);
        String ageS = ageInt.toString();

        return ageS;
    }
}
