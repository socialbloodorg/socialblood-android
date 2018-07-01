package co.nexlabs.socialblood.util;


import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by h3r0 on 6/2/14.
 */
public class DataUtils {

    public static boolean isExistOnSdcard(String filename) {
        File file =
                new File(Environment.getExternalStorageDirectory().getPath() + "/" + filename.hashCode());
        if (file.exists()) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean DeleteObject(Context mContext, int filename) {
        return DeleteObject(mContext, Integer.toString(filename));
    }

    public static boolean DeleteObject(Context mContext, String filename) {
        File file = mContext.getFileStreamPath(filename);
        if (file != null || file.exists()) {
            file.delete();
            return true;
        }
        return false;
    }

    /**
     * Check if file exist or not
     *
     * @param mContext Application Context
     * @param filename of the file to open (under assets folder)
     */
    public static boolean isExist(Context mContext, int filename) {
        return isExist(mContext, filename + "");
    }

    /**
     * Check if file exist or not
     *
     * @param mContext Application Context
     * @param filename of the file to open (under assets folder)
     */
    public static boolean isExist(Context mContext, String filename) {
        File file = mContext.getFileStreamPath(filename);
        if (file == null || !file.exists()) {
            return false;
        }
        return true;
    }

    /**
     * Open a private object associated with this Context's application package for writing. Creates
     * the file if it doesn't already exist.
     *
     * @param mContext Application Context
     * @param filename of the file to open (under assets folder)
     * @param file     Object
     */
    public static void WriteObject(Context mContext, int filename, Object file) {
        WriteObject(mContext, filename + "", file);
    }

    /**
     * Open a private object associated with this Context's application package for writing. Creates
     * the file if it doesn't already exist.
     *
     * @param mContext Application Context
     * @param filename of the file to open (under assets folder)
     * @param file     Object
     */
    public static void WriteObject(Context mContext, String filename, Object file) {
        try {
            FileOutputStream fos = mContext.openFileOutput(filename, Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(file);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Open a private object associated with this Context's application package for reading.
     *
     * @param mContext Application Context
     * @param filename of the file to open (under assets folder)
     * @return Object
     */
    public static Object ReadObject(Context mContext, int filename) {
        return ReadObject(mContext, filename + "");
    }

    /**
     * Open a private object associated with this Context's application package for reading.
     *
     * @param mContext Application Context
     * @param filename of the file to open (under assets folder)
     * @return Object
     */
    public static Object ReadObject(Context mContext, String filename) {
        try {
            FileInputStream fis = mContext.openFileInput(filename);
            ObjectInputStream ois = new ObjectInputStream(fis);
            Object obj = (Object) ois.readObject();
            fis.close();
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
            return new Object();
        }
    }

    /**
     * Open a private file associated with this Context's application package for writing. Creates
     * the
     * file if it doesn't already exist.
     *
     * @param mContext  Application Context
     * @param filename  name of the file to write
     * @param arraylist to write
     */
    public static <E> void WriteArraylist(Context mContext, int filename, List<E> arraylist) {
        WriteArraylist(mContext, filename + "", arraylist);
    }

    /**
     * Open a private file associated with this Context's application package for writing. Creates
     * the
     * file if it doesn't already exist.
     *
     * @param mContext  Application Context
     * @param filename  name of the file to write
     * @param arraylist to write
     */
    public static <E> void WriteArraylist(Context mContext, String filename, List<E> arraylist) {
        try {
            FileOutputStream fos = mContext.openFileOutput(filename, Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(arraylist);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Open a private file associated with this Context's application package for reading.
     *
     * @param filename of the file to open
     * @return Object
     */
    public static Object ReadArraylist(Context mContext, int filename) {
        return ReadArraylist(mContext, filename + "");
    }

    /**
     * Open a private file associated with this Context's application package for reading.
     *
     * @param filename of the file to open
     * @return Object
     */
    public static Object ReadArraylist(Context mContext, String filename) {
        if (isExist(mContext, filename)) {
            try {
                FileInputStream fis = mContext.openFileInput(filename);
                ObjectInputStream ois = new ObjectInputStream(fis);
                Object obj = (Object) ois.readObject();
                fis.close();
                return obj;
            } catch (Exception e) {
                e.printStackTrace();
                return new ArrayList<Object>();
            }
        } else {
            return new ArrayList<Object>();
        }
    }
}

