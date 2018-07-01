package co.nexlabs.socialblood.util;

import android.util.Patterns;
import android.view.View;

/**
 * Created by myozawoo on 3/14/16.
 */
public class TextUtils {

    public static boolean isValidEmail(String email) {
        return !android.text.TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

//    public static String convertBloodNameToId() {
//
//    }

    public static  String convertBloodIdToBloodName(int bloodTypeId, int bloodRhTypeId) {

        String bloodType = "";

        if (bloodTypeId == 1) {
            if (bloodRhTypeId == 1) {
                bloodType = "O+";
            } else if (bloodRhTypeId == 2) {
                bloodType = "O-";
            } else if (bloodRhTypeId == 3) {
                bloodType = "O";
            }
        } else if (bloodTypeId == 2) {
            if (bloodRhTypeId == 1) {
                bloodType = "A+";
            } else if (bloodRhTypeId == 2) {
                bloodType = "A-";
            } else if (bloodRhTypeId == 3) {
                bloodType = "A";
            }
        } else if (bloodTypeId == 3) {
            if (bloodRhTypeId == 1) {
                bloodType = "B+";
            } else if (bloodRhTypeId == 2) {
                bloodType = "B-";
            } else if (bloodRhTypeId == 3) {
                bloodType = "B";
            }
        } else if (bloodTypeId == 4) {
            if (bloodRhTypeId == 1) {
                bloodType = "AB+";
            } else if (bloodRhTypeId == 2) {
                bloodType = "AB-";
            } else if (bloodRhTypeId == 3) {
                bloodType = "AB";
            }
        }

        return bloodType;
    }

}
