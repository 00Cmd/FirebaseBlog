package com.example.macbookair.firebaseblog.Activitys;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.example.macbookair.firebaseblog.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class PostActivity extends AppCompatActivity {
    private ImageButton mSelectImage;
    private EditText title_val,desc_val;
    private Button btnSubm;
    private  Uri imgUri = null;
    private StorageReference mStore;
    private DatabaseReference mReference;
    private ProgressDialog mBar;
    private final static int IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        mStore = FirebaseStorage.getInstance().getReference();
        mReference = FirebaseDatabase.getInstance().getReference().child("Blog");
        find();
        onClick();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == IMAGE_REQUEST) {
            if(resultCode == RESULT_OK) {
                imgUri = data.getData();
                mSelectImage.setImageURI(imgUri);
                //TODO: look up imageCrop, selected images do NOT match the size of imgButton!!!
            }
        }
    }

    private void find() {
        mSelectImage = (ImageButton)findViewById(R.id.btnAddImage);
        title_val = (EditText) findViewById(R.id.addPostTitle);
        desc_val = (EditText) findViewById(R.id.addPostDesc);
        btnSubm = (Button) findViewById(R.id.btnSubmit);
        mBar = new ProgressDialog(this);
    }

    private void onClick() {
        mSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, IMAGE_REQUEST);
            }
        });

        btnSubm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBar.setMessage("Posting to blog....");
                mBar.show();
                final String title = title_val.getText().toString().trim();
                final String desc = desc_val.getText().toString().trim();

                if(!TextUtils.isEmpty(title) && !TextUtils.isEmpty(desc) && imgUri != null) {
                    StorageReference filePath = mStore.child("Blog_images").child(imgUri.getLastPathSegment());

                    filePath.putFile(imgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Uri downloadUrl = taskSnapshot.getDownloadUrl();
                            DatabaseReference newPost = mReference.push();
                            newPost.child("Title").setValue(title);
                            newPost.child("Description").setValue(desc);
                            newPost.child("Image").setValue(downloadUrl.toString());
                            mBar.dismiss();
                        }
                    });
                }
            }
        });
    }
}
