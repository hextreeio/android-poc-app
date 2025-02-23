package io.hextree.poc;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.File;

import io.hextree.poc.utils.AttackProvider;
import io.hextree.poc.utils.FilesUtil;
import io.hextree.poc.utils.IntentUtils;

public class MainActivity extends AppCompatActivity {

    static String TAG = "ProofOfConcept";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // ---- Log output with debug()
        debug(IntentUtils.dumpIntent(this, getIntent()));

        // IntentUtils.showDialog(this, getIntent());

        // ---- Button 1 click handler
        findViewById(R.id.button_1).setOnClickListener(v -> {
            ((TextView)findViewById(R.id.text_debug)).append("Hello\n");
            // ************* PROOF OF CONCEPT CODE HERE *************
            // Intent intent = new Intent();
            // intent.setFlags(Intent.FLAG_DEBUG_LOG_RESOLUTION);
            // startActivity(intent);
            // ******************************************************
        });

        // ---- Working with Files
        // FilesUtil.copyFileFromAssetToInternal(this, "example.txt", "example.txt");
        // FilesUtil.writeFile(this, "example2.txt", "dynamically writing file");

        // ---- Working with FileProvider (ContentProvider)
        // Uri attackUri = FileProvider.getUriForFile(this, "io.hextree.attackprovider", new File(getFilesDir(), "example.txt"));
        // Uri fileUri = FileProvider.getUriForFile(this, "io.hextree.fileprovider", new File(getFilesDir(), "example.txt"));

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    void debug(String msg) {
        ((TextView)findViewById(R.id.text_debug)).append(msg+"\n");
        Log.i(TAG, msg);
    }
}