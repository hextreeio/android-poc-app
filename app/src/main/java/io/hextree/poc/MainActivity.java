package io.hextree.poc;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

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

        // Working with Files
        // FilesUtil.copyFileFromAssetToInternal(this, "example.txt", "example.txt");
        //FilesUtil.writeFile(this, "example2.txt", "dynamically writing file");

        // Working with FileProvider
        // Uri attackUri = FileProvider.getUriForFile(this, "io.hextree.attackprovider", new File(getFilesDir(), "example.txt"));
        // Uri fileUri = FileProvider.getUriForFile(this, "io.hextree.fileprovider", new File(getFilesDir(), "example.txt"));

        IntentUtils.showDialog(this, getIntent());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}