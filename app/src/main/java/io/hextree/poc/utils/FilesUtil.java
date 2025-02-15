package io.hextree.poc.utils;

import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

/**
 * Utility class for file operations in an Android application.
 * <p>
 * This class provides helper methods to write, copy, read, and delete files
 * from the application's internal storage as well as reading files from the assets folder.
 * </p>
 */
public class FilesUtil {

    /**
     * Writes the specified content to a file in the application's internal storage.
     * <p>
     * This method ensures that the parent directory for the file exists, creating it if necessary.
     * </p>
     *
     * @param context       the context used to access the internal storage directory
     * @param internal_fname the file name or relative path within the internal storage where the content will be written
     * @param content       the content to write into the file
     * @throws RuntimeException if an I/O error occurs during writing
     */
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

    /**
     * Copies a file from the apk's assets folder to the internal storage.
     * <p>
     * The file is read line by line from the assets and written to the specified internal storage location.
     * If the file already exists in internal storage, it will be overwritten.
     * </p>
     *
     * @param context       the context used to access the assets and internal storage
     * @param asset_fname   the name of the file in the assets folder to be copied
     * @param internal_fname the desired file name or relative path in the internal storage
     * @return the {@code File} object representing the newly created file in internal storage
     * @throws RuntimeException if an I/O error occurs during the copying process
     */
    public static File copyFileFromAssetToInternal(Context context, String asset_fname, String internal_fname) {
        try {
            InputStream inputStream = context.getAssets().open(asset_fname);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line).append("\n");
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

    /**
     * Deletes a file from the application's internal storage.
     *
     * @param context       the context used to access the internal storage directory
     * @param internal_fname the file name or relative path of the file to be deleted
     * @return {@code true} if the file existed and was successfully deleted; {@code false} if the file does not exist or deletion failed
     */
    public static boolean deleteFileFromInternal(Context context, String internal_fname) {
        File file = new File(context.getFilesDir(), internal_fname);
        if (file.exists()) {
            return file.delete();
        }
        return false; // File does not exist
    }

    /**
     * Reads the contents of a file from the apk's assets folder.
     *
     * @param context     the context used to access the assets
     * @param asset_fname the name of the asset file to read
     * @return a {@code String} containing the contents of the asset file
     * @throws RuntimeException if an I/O error occurs during reading
     */
    public static String readAssetFile(Context context, String asset_fname) {
        try {
            InputStream inputStream = context.getAssets().open(asset_fname);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line).append("\n");
            }
            reader.close();
            return builder.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Reads the contents of a file from the application's internal storage.
     *
     * @param context       the context used to access the internal storage directory
     * @param internal_fname the file name or relative path of the file to read
     * @return a {@code String} containing the contents of the file
     * @throws RuntimeException if an I/O error occurs during reading
     */
    public static String readInternalFile(Context context, String internal_fname) {
        File inputFile = new File(context.getFilesDir(), internal_fname);
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(inputFile)));
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line).append("\n");
            }
            reader.close();
            return builder.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

