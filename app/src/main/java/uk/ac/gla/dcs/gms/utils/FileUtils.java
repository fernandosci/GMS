package uk.ac.gla.dcs.gms.utils;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by ito on 24/07/2015.
 */
public class FileUtils {
    private static final String TAG = "FileUtils";

    public static boolean checkFile(Context context, String filename){
        return getFile(context, filename).exists();
    }

    public static void removeFile(Context context, String filename){
        File f = getFile(context, filename);

        if (f.exists())
            f.delete();
    }


    public static void writeObjToFile (Context context, String filename, Object obj) throws Exception{
        try {
            FileOutputStream fileOutputStream = context.openFileOutput(filename, Context.MODE_PRIVATE);
            ObjectOutputStream outs = new ObjectOutputStream(fileOutputStream);
            outs.writeObject(obj);
            outs.close();
            fileOutputStream.close();
            Log.v(TAG, obj.toString() + "Wrote on private storage file " + filename);
        } catch (java.io.IOException e) {
            e.printStackTrace();
            Log.e(TAG, "Error when saving obj in local private storage");
            throw new Exception("Failed to write on local private storage.");
        }
    }

    public static Object readObjFromFile(Context context, String filename) throws Exception{
        File f = getFile(context, filename);
        if (f.exists()) {
            try {
                FileInputStream fileInputStream = context.openFileInput(filename);
                ObjectInputStream obji = new ObjectInputStream(fileInputStream);
                Object obj = obji.readObject();
                obji.close();
                fileInputStream.close();
                Log.v(TAG, "Object " + obj.toString() + " loaded from local private storage file " + filename);
                return obj;
            } catch (java.io.IOException e) {
                e.printStackTrace();
                Log.e(TAG, "Error when loading obj in local private storage");
                throw new Exception("Failed to read from local private storage.");
            }
        }
        else
            throw new Exception("File " + filename + " not found.");
    }

    private static File getFile(Context context, String filename){
        return new File(context.getFilesDir().getAbsolutePath(),filename);
    }
}
