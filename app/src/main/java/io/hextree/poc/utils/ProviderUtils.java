package io.hextree.poc.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ProviderUtils {

    public static void dumpFile(Context context, Uri contentUri) {

        Log.d("File", "--------------------------------");
        Log.d("File", "Dumping "+contentUri.toString());
        try (InputStream inputStream = context.getContentResolver().openInputStream(contentUri)) {
            // Read the file content here
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

    public static void dumpTable(Context context, Uri contentUri) {

        Cursor cursor = context.getContentResolver().query(contentUri,
                null, null,
                null, null);

        Log.d("Table", "--------------------------------");
        Log.d("File", "Dumping "+contentUri.toString());
        Log.i("Table", "cursor: "+cursor);
        if(cursor!=null) {
            Log.i("Table", "cursor.getColumnCount: " + cursor.getColumnCount());
            Log.i("Table", "cursor.getCount: " + cursor.getCount());
            if (cursor.moveToFirst()) {
                Log.i("Table", "dump columns");
                do {
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < cursor.getColumnCount(); i++) {
                        if (sb.length() > 0) {
                            sb.append(", ");
                        }
                        sb.append(cursor.getColumnName(i) + " = " + cursor.getString(i));
                    }
                    Log.d("Table", sb.toString());
                } while (cursor.moveToNext());
            }
        }
        Log.d("Table", "--------------------------------");
    }
}
