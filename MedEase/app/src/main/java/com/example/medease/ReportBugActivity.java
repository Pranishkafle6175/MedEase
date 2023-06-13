package com.example.medease;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.medease.databinding.ActivityChangeInformationBinding;
import com.example.medease.databinding.ReportBugBinding;

public class ReportBugActivity extends AppCompatActivity {

    ReportBugBinding binding;
    Uri imageUri;
    ActivityResultLauncher<Intent> activityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ReportBugBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if(result.getResultCode() == RESULT_OK && result.getData() != null){
                    imageUri = result.getData().getData();
                    binding.screenshot.setImageURI(imageUri);
                }
            }
        });

        binding.addScreenshot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gallery = new Intent(Intent.ACTION_GET_CONTENT);
                gallery.setType("image/*");
                activityResultLauncher.launch(gallery);
            }
        });
binding.sendReport.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        sendMail();
        binding.bugTitle.setText("");
        binding.bugDescription.setText("");
binding.screenshot.setImageURI(null);
    }
});

    }
    private void sendMail(){
      //  String receiver=new String("project.meadease@gmail.com");
        String title=binding.bugTitle.getText().toString();
        String description=binding.bugDescription.getText().toString();

        Intent intent=new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_EMAIL,new String[]{"project.medease@gmail.com"});
        intent.putExtra(Intent.EXTRA_SUBJECT,title);
        intent.putExtra(Intent.EXTRA_TEXT,description);
        intent.putExtra(Intent.EXTRA_STREAM,imageUri);

        intent.setType("message/rfc822");
        startActivity(Intent.createChooser(intent,"Report issue"));

    }
}
