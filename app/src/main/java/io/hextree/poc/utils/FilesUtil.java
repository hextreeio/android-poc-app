package io.hextree.poc.utils;

import android.content.Context;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

public class FilesUtil {

    public static void writeFile(Context context, String internal_fname, String content) {
        File outputDir = new File(context.getFilesDir(), internal_fname).getParentFile();
        if (!outputDir.exists()) {
            outputDir.mkdirs();
        }
        File outputFile = new File(context.getFilesDir(), internal_fname);
        try (OutputStream outputStream = new FileOutputStream(outputFile)) {
            outputStream.write(content.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static File copyFileFromAssetToInternal(Context context, String asset_fname, String internal_fname) {
        // copy `fname` from `./assets/` folder to the app internal `./files/` folder
        try {
            InputStream inputStream = context.getAssets().open(asset_fname);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line+"\n");
            }
            reader.close();

            File internalFile = new File(context.getFilesDir(), internal_fname);
            FileOutputStream fos = new FileOutputStream(internalFile);
            fos.write(builder.toString().getBytes());
            fos.flush();
            fos.close();
            return internalFile;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean deleteFileFromInternal(Context context, String internal_fname) {
        // Delete `fname` from the app internal `./files/` folder
        File file = new File(context.getFilesDir(), internal_fname);
        if (file.exists()) {
            return file.delete();
        }
        return false; // File does not exist
    }

    public static String readAssetFile(Context context, String asset_fname) {
        try {
            InputStream inputStream = null;
            inputStream = context.getAssets().open(asset_fname);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line + "\n");
            }
            reader.close();
            return builder.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String readInternalFile(Context context, String internal_fname) {
        File inputFile = new File(context.getFilesDir(), internal_fname);
        if (!inputFile.exists()) {
            return null; // or throw an exception, or return an empty string
        }
        try{
             BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(inputFile)));
             StringBuilder builder = new StringBuilder();
             String line;
             while ((line = reader.readLine()) != null) {
                builder.append(line + "\n");
             }
             reader.close();
             return builder.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
