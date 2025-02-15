package io.hextree.poc.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Utility class for reading and dumping file or content provider data.
 * <p>
 * This class is particularly useful when exploiting hijacked content providers.
 * You can dump file content of a received FileProvider URI, or dump the table of
 * a received ContentProvider.
 * </p>
 */
public class ProviderUtils {

    /**
     * Reads and logs the content of a file at the specified content URI.
     * <p>
     * The method opens an input stream using the provided URI and reads the file's content
     * line by line, logging each line. If an {@link IOException} or {@link SecurityException}
     * occurs, an error message is logged.
     * </p>
     *
     * @param context    the context used to access the content resolver.
     * @param contentUri the content URI of the file to be dumped.
     */
    public static void dumpFile(Context context, Uri contentUri) {
        Log.d("File", "--------------------------------");
        Log.d("File", "Dumping " + contentUri.toString());
        try (InputStream inputStream = context.getContentResolver().openInputStream(contentUri)) {
            // Read and log each line of the file
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                Log.d("File", " [*] " + line);
            }
        } catch (IOException e) {
            Log.d("File", " [!] IOException");
        } catch (SecurityException e) {
            Log.d("File", " [!] SecurityException");
        }
        Log.d("File", "--------------------------------");
    }

    /**
     * Reads and logs the rows of a table from the specified content URI.
     * <p>
     * The method queries the content resolver for data at the given URI, then logs the
     * column count, row count, and the contents of each row in the table.
     * </p>
     *
     * @param context    the context used to access the content resolver.
     * @param contentUri the content URI of the table to be dumped.
     */
    public static void dumpTable(Context context, Uri contentUri) {
        Cursor cursor = context.getContentResolver().query(contentUri, null, null, null, null);

        Log.d("Table", "--------------------------------");
        Log.d("Table", "Dumping " + contentUri);
        Log.i("Table", "cursor: " + cursor);
        if (cursor != null) {
            Log.i("Table", "cursor.getColumnCount: " + cursor.getColumnCount());
            Log.i("Table", "cursor.getCount: " + cursor.getCount());
            if (cursor.moveToFirst()) {
                Log.i("Table", "Dumping columns:");
                do {
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < cursor.getColumnCount(); i++) {
                        if (i > 0) {
                            sb.append(", ");
                        }
                        sb.append(cursor.getColumnName(i)).append(" = ").append(cursor.getString(i));
                    }
                    Log.d("Table", sb.toString());
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        Log.d("Table", "--------------------------------");
    }
}
