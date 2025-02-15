package io.hextree.poc.utils;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Set;

/**
 * Utility class for working with {@link Intent} and {@link Bundle} objects.
 * <p>
 * This class provides methods to "dump" the contents of an Intent or Bundle into a formatted string
 * for debugging purposes, as well as a helper method to display a dialog with the details of an Intent.
 * </p>
 */
public class IntentUtils {

    /**
     * Returns a formatted string representing the details of the specified {@link Intent}.
     * <p>
     * This is a convenience method that initiates the dump process with no indentation.
     * </p>
     *
     * @param context the context used for any necessary operations (not used directly here)
     * @param intent  the {@link Intent} to be dumped
     * @return a string containing the formatted details of the Intent, or "Intent is null" if the intent is null
     */
    public static String dumpIntent(Context context, Intent intent) {
        return dumpIntent(context, intent, 0);
    }

    /**
     * Recursively dumps the contents of the given {@link Intent} into a formatted string.
     * <p>
     * The method prints the action, categories, data URI, component, flags, and extras of the Intent.
     * If any extras are themselves Intents or Bundles, they are dumped recursively with increased indentation.
     * </p>
     *
     * @param context     the context used for any necessary operations (currently not used in the dump)
     * @param intent      the {@link Intent} to be dumped
     * @param indentLevel the current indentation level (number of indents) used for formatting nested content
     * @return a formatted string representing the contents of the Intent
     */
    private static String dumpIntent(Context context, Intent intent, int indentLevel) {
        if (intent == null) {
            return "Intent is null";
        }

        StringBuilder sb = new StringBuilder();
        // Build an indentation string with 4 spaces per indent level
        String indent = new String(new char[indentLevel]).replace("\0", "    ");

        // Append basic intent information: action, categories, data, component, and flags.
        sb.append(indent).append("[Action]    ").append(intent.getAction()).append("\n");

        // Append intent categories if available
        Set<String> categories = intent.getCategories();
        if (categories != null) {
            for (String category : categories) {
                sb.append(indent).append("[Category]  ").append(category).append("\n");
            }
        }

        // Append additional intent properties
        sb.append(indent).append("[Data]      ").append(intent.getDataString()).append("\n");
        sb.append(indent).append("[Component] ").append(intent.getComponent()).append("\n");
        sb.append(indent).append("[Flags]     ").append(getFlagsString(intent.getFlags())).append("\n");

        // Append extras, dumping nested Intents or Bundles recursively with increased indentation.
        Bundle extras = intent.getExtras();
        if (extras != null) {
            for (String key : extras.keySet()) {
                Object value = extras.get(key);
                if (value instanceof Intent) {
                    sb.append(indent).append("[Extra:'").append(key).append("'] -> Intent\n");
                    sb.append(dumpIntent(context, (Intent) value, indentLevel + 1));
                } else if (value instanceof Bundle) {
                    sb.append(indent).append("[Extra:'").append(key).append("'] -> Bundle\n");
                    sb.append(dumpBundle((Bundle) value, indentLevel + 1));
                } else if (value instanceof PendingIntent) {
                    sb.append(indent).append("[Extra:'").append(key).append("'] -> ").append(value.toString()).append("\n");
                    sb.append(indent).append("[getCreatorPackage:'").append(((PendingIntent) value).getCreatorPackage()).append("']\n");
                } else {
                    sb.append(indent).append("[Extra:'").append(key).append("']: ").append(value).append("\n");
                }
            }
        }

        // If desired, you can uncomment the following block to query the content URI when the appropriate flag is set.
        /*
        if ((intent.getFlags() & Intent.FLAG_GRANT_READ_URI_PERMISSION) != 0) {
            Uri data = intent.getData();
            if (data != null) {
                sb.append(queryContentUri(context, data, indentLevel + 1));
            }
        }
        */

        return sb.toString();
    }

    /**
     * Converts the given integer flags of an {@link Intent} into a human-readable string.
     *
     * @param flags the flags to decode
     * @return a string listing the set flags separated by " | ", or an empty string if none of the recognized flags are set
     */
    private static String getFlagsString(int flags) {
        StringBuilder flagBuilder = new StringBuilder();
        if ((flags & Intent.FLAG_GRANT_READ_URI_PERMISSION) != 0) flagBuilder.append("GRANT_READ_URI_PERMISSION | ");
        if ((flags & Intent.FLAG_GRANT_WRITE_URI_PERMISSION) != 0) flagBuilder.append("GRANT_WRITE_URI_PERMISSION | ");
        if ((flags & Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION) != 0) flagBuilder.append("GRANT_PERSISTABLE_URI_PERMISSION | ");
        if ((flags & Intent.FLAG_GRANT_PREFIX_URI_PERMISSION) != 0) flagBuilder.append("GRANT_PREFIX_URI_PERMISSION | ");
        if ((flags & Intent.FLAG_ACTIVITY_NEW_TASK) != 0) flagBuilder.append("ACTIVITY_NEW_TASK | ");
        if ((flags & Intent.FLAG_ACTIVITY_SINGLE_TOP) != 0) flagBuilder.append("ACTIVITY_SINGLE_TOP | ");
        if ((flags & Intent.FLAG_ACTIVITY_NO_HISTORY) != 0) flagBuilder.append("ACTIVITY_NO_HISTORY | ");
        if ((flags & Intent.FLAG_ACTIVITY_CLEAR_TOP) != 0) flagBuilder.append("ACTIVITY_CLEAR_TOP | ");
        if ((flags & Intent.FLAG_ACTIVITY_FORWARD_RESULT) != 0) flagBuilder.append("ACTIVITY_FORWARD_RESULT | ");
        if ((flags & Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP) != 0) flagBuilder.append("ACTIVITY_PREVIOUS_IS_TOP | ");
        if ((flags & Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS) != 0) flagBuilder.append("ACTIVITY_EXCLUDE_FROM_RECENTS | ");
        if ((flags & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) flagBuilder.append("ACTIVITY_BROUGHT_TO_FRONT | ");
        if ((flags & Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED) != 0) flagBuilder.append("ACTIVITY_RESET_TASK_IF_NEEDED | ");
        if ((flags & Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY) != 0) flagBuilder.append("ACTIVITY_LAUNCHED_FROM_HISTORY | ");
        if ((flags & Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET) != 0) flagBuilder.append("ACTIVITY_CLEAR_WHEN_TASK_RESET | ");
        if ((flags & Intent.FLAG_ACTIVITY_NEW_DOCUMENT) != 0) flagBuilder.append("ACTIVITY_NEW_DOCUMENT | ");
        if ((flags & Intent.FLAG_ACTIVITY_NO_USER_ACTION) != 0) flagBuilder.append("ACTIVITY_NO_USER_ACTION | ");
        if ((flags & Intent.FLAG_ACTIVITY_REORDER_TO_FRONT) != 0) flagBuilder.append("ACTIVITY_REORDER_TO_FRONT | ");
        if ((flags & Intent.FLAG_ACTIVITY_NO_ANIMATION) != 0) flagBuilder.append("ACTIVITY_NO_ANIMATION | ");
        if ((flags & Intent.FLAG_ACTIVITY_CLEAR_TASK) != 0) flagBuilder.append("ACTIVITY_CLEAR_TASK | ");
        if ((flags & Intent.FLAG_ACTIVITY_TASK_ON_HOME) != 0) flagBuilder.append("ACTIVITY_TASK_ON_HOME | ");
        if ((flags & Intent.FLAG_ACTIVITY_RETAIN_IN_RECENTS) != 0) flagBuilder.append("ACTIVITY_RETAIN_IN_RECENTS | ");
        if ((flags & Intent.FLAG_ACTIVITY_LAUNCH_ADJACENT) != 0) flagBuilder.append("ACTIVITY_LAUNCH_ADJACENT | ");
        if ((flags & Intent.FLAG_ACTIVITY_REQUIRE_DEFAULT) != 0) flagBuilder.append("ACTIVITY_REQUIRE_DEFAULT | ");
        if ((flags & Intent.FLAG_ACTIVITY_REQUIRE_NON_BROWSER) != 0) flagBuilder.append("ACTIVITY_REQUIRE_NON_BROWSER | ");
        if ((flags & Intent.FLAG_ACTIVITY_MATCH_EXTERNAL) != 0) flagBuilder.append("ACTIVITY_MATCH_EXTERNAL | ");
        if ((flags & Intent.FLAG_ACTIVITY_MULTIPLE_TASK) != 0) flagBuilder.append("ACTIVITY_MULTIPLE_TASK | ");
        if ((flags & Intent.FLAG_RECEIVER_REGISTERED_ONLY) != 0) flagBuilder.append("RECEIVER_REGISTERED_ONLY | ");
        if ((flags & Intent.FLAG_RECEIVER_REPLACE_PENDING) != 0) flagBuilder.append("RECEIVER_REPLACE_PENDING | ");
        if ((flags & Intent.FLAG_RECEIVER_FOREGROUND) != 0) flagBuilder.append("RECEIVER_FOREGROUND | ");
        if ((flags & Intent.FLAG_RECEIVER_NO_ABORT) != 0) flagBuilder.append("RECEIVER_NO_ABORT | ");
        if ((flags & Intent.FLAG_RECEIVER_VISIBLE_TO_INSTANT_APPS) != 0) flagBuilder.append("RECEIVER_VISIBLE_TO_INSTANT_APPS | ");

        if (flagBuilder.length() > 0) {
            // Remove the trailing " | " delimiter
            flagBuilder.setLength(flagBuilder.length() - 3);
        }

        return flagBuilder.toString();
    }

    /**
     * Returns a formatted string representing the details of the specified {@link Bundle}.
     * <p>
     * This is a convenience method that initiates the dump process with no indentation.
     * </p>
     *
     * @param bundle the {@link Bundle} to be dumped
     * @return a string containing the formatted details of the Bundle, or "Bundle is null" if the bundle is null
     */
    public static String dumpBundle(Bundle bundle) {
        return dumpBundle(bundle, 0);
    }

    /**
     * Recursively dumps the contents of the given {@link Bundle} into a formatted string.
     * <p>
     * Each key-value pair in the Bundle is printed on a separate line. If a value is itself a Bundle,
     * its contents are dumped recursively with increased indentation.
     * </p>
     *
     * @param bundle      the {@link Bundle} to be dumped
     * @param indentLevel the current indentation level used for formatting nested content
     * @return a formatted string representing the contents of the Bundle
     */
    private static String dumpBundle(Bundle bundle, int indentLevel) {
        if (bundle == null) {
            return "Bundle is null";
        }

        StringBuilder sb = new StringBuilder();
        // Build an indentation string with 4 spaces per indent level
        String indent = new String(new char[indentLevel]).replace("\0", "    ");

        for (String key : bundle.keySet()) {
            Object value = bundle.get(key);
            if (value instanceof Bundle) {
                // Dump nested Bundle recursively
                sb.append(String.format("%s['%s']: Bundle[\n%s%s]\n", indent, key, dumpBundle((Bundle) value, indentLevel + 1), indent));
            } else {
                sb.append(String.format("%s['%s']: %s\n", indent, key, value != null ? value.toString() : "null"));
            }
        }
        return sb.toString();
    }

    /**
     * Displays a dialog showing the details of the specified {@link Intent}.
     * <p>
     * The dialog contains a title ("Intent Details:"), the dumped contents of the Intent in a monospace font,
     * and an OK button that dismisses the dialog. The dialog is animated to slide in from the bottom.
     * </p>
     *
     * @param context the context used to create and display the dialog
     * @param intent  the {@link Intent} whose details are to be displayed; if null, the method returns immediately
     */
    public static void showDialog(Context context, Intent intent) {
        if (intent == null) {
            return;
        }
        // Create a new dialog without a title bar.
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);

        // Create a vertical LinearLayout to hold the dialog contents.
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(20, 50, 20, 50);
        layout.setBackgroundColor(0xffefeff5);

        // Create and add a TextView for the dialog title.
        TextView title = new TextView(context);
        title.setText("Intent Details: ");
        title.setTextSize(16);
        title.setTextColor(0xff000000);
        title.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        title.setPadding(0, 0, 0, 40);
        title.setGravity(Gravity.CENTER);
        title.setBackgroundColor(0xffefeff5);
        layout.addView(title);

        // Create and add a TextView that displays the dumped Intent details in a monospace font.
        TextView message = new TextView(context);
        message.setText(dumpIntent(context, intent));
        message.setTypeface(Typeface.MONOSPACE);
        message.setTextSize(12);
        message.setTextColor(0xff000000);
        message.setPadding(0, 0, 0, 30);
        message.setGravity(Gravity.START);
        message.setBackgroundColor(0xffefeff5);
        layout.addView(message);

        // Create and add an OK button that dismisses the dialog when clicked.
        Button positiveButton = new Button(context);
        positiveButton.setText("OK");
        positiveButton.setTextColor(0xff000000);
        positiveButton.setOnClickListener(v -> dialog.dismiss());
        layout.addView(positiveButton);

        // Set the custom layout as the dialog's content.
        dialog.setContentView(layout);

        // Configure the dialog window to fill the width and be positioned at the bottom without dimming the background.
        Window window = dialog.getWindow();
        if (window != null) {
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            window.setBackgroundDrawableResource(android.R.color.transparent);
            WindowManager.LayoutParams wlp = window.getAttributes();
            wlp.gravity = Gravity.BOTTOM;
            wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            window.setAttributes(wlp);
        }

        dialog.show();

        // Animate the dialog's layout to slide in from below.
        layout.setTranslationY(2000); // Start off-screen below the view.
        layout.setAlpha(0f);
        ObjectAnimator translateYAnimator = ObjectAnimator.ofFloat(layout, "translationY", 0);
        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(layout, "alpha", 1f);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(translateYAnimator, alphaAnimator);
        animatorSet.setDuration(300);  // Duration of the animation in milliseconds.
        animatorSet.setStartDelay(100);  // Delay before starting the animation.
        animatorSet.start();
    }
}

