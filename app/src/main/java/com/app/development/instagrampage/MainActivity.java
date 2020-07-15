package com.app.development.instagrampage;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {

    boolean PERMISSION_FLAG;  // one time usable variable, because from the next time you will not need it...haha

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //when the "CLICK PHOTO" button was clicked
        findViewById(R.id.launch_camera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchCamera();
            }
        });

    }

    private void launchCamera() {
        if (PERMISSION_FLAG) {
            //It permission is already granted
            openCamera();
        } else {
            //Permission is granted from before, go for checkPermission, then for requestPermission
            //and then handle the result inside onRequestPermissionsResult() function
            checkPermission();
        }
    }

    private void openCamera() {
        //This is openCamera() function, it will be called for launching camera

        //For now you can skip this function, because all these two lines of codes inside this function
        // is a part of Intent, which we will cover in the next topic
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(Intent.createChooser(intent, "Click photo using.."),102);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==102 && resultCode==RESULT_OK){
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            ImageView imageView = findViewById(R.id.camera_image);
            imageView.setImageBitmap(imageBitmap);
        }
    }

    private void checkPermission() {
        //checking whether required permission is granted from before not
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            //If you enter inside this "if" block, it means permission is not granted from before,
            //Now it's time to decide whether to show "Rationale" or not, and we will ask from system
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                //And if you are inside this "if" block, it means you have to show "Rationale"
                //So make your "Rationale" and show the user
                new AlertDialog.Builder(this)
                        .setTitle("Permission Required")
                        .setMessage("Permission is required to launch the camera, otherwise you will not be able this feature")
                        .setPositiveButton("Ask me", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //when user clicks "ASK ME", the again request the permission
                                // because now your user got convinced to give you the permission
                                requestPermission();
                            }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }}).create().show();
            }
            else {
                //But if you are here in this "else" block, then you don't need to show the "Rationale"
                //if you don't need to show the "Rationale", then directly request the required permission from the user
                requestPermission();
            }
        }
        else
            {
            //When we checked that whether we have required permission or not
            //Then there were two possibilities, either we had permission or we didn't had permission

            //If you are here in this "else" block then you have permission from before, so perform your task
            //And don't forget, what was our task? Our task was to open camera
            //so call the openCamera() function
            PERMISSION_FLAG = true;
            openCamera();
        }
    }

    private void requestPermission() {
        //this function is called, when you don't have permission or user got agreed to give you permission
        //after seeing the "Rationale"
        String[] permission = {Manifest.permission.CAMERA}; //String array of Permissions

        //Now code to request the permission
        ActivityCompat.requestPermissions(this, permission, 101);
    }

    @Override // this method is invoked(called) whenever user takes decision on whether to give permission or to deny
    // that is, it shows the result of your requested permission
    //Either allowed or denied
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 101) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //If you are here , user gave you that asked permission, so perform your task
                // and our task was to open camera, so calling openCamera() function
                PERMISSION_FLAG = true;
                openCamera();
            }else{
                //Boo!!!! User has denied the request, do some other stuffs, except opening the camera
                //because you can't open the camera, since user has denied to open camera
            }
        }
    }
}