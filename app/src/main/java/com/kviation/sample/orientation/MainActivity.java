package com.kviation.sample.orientation;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity implements Orientation.Listener {

  private Orientation mOrientation;
  private AttitudeIndicator mAttitudeIndicator;


  TextView x;
  TextView y;
  TextView z;
  TextView xg;
  TextView yg;
  TextView zg;
  String filename="accelgyro.txt";
  File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), filename);

  private static final int REQUEST_EXTERNAL_STORAGE = 1;
  private static String[] PERMISSIONS_STORAGE = {
          Manifest.permission.READ_EXTERNAL_STORAGE,
          Manifest.permission.WRITE_EXTERNAL_STORAGE
  };


  public static void verifyStoragePermissions(Activity activity) {
    // Check if we have write permission
    int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

    if (permission != PackageManager.PERMISSION_GRANTED) {
      // We don't have permission so prompt the user
      ActivityCompat.requestPermissions(
              activity,
              PERMISSIONS_STORAGE,
              REQUEST_EXTERNAL_STORAGE
      );
    }
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);

    x=(TextView)findViewById(R.id.tvx);
    y=(TextView)findViewById(R.id.tvy);
    z=(TextView)findViewById(R.id.tvz);
    xg=(TextView)findViewById(R.id.gx);
    yg=(TextView)findViewById(R.id.gy);
    zg=(TextView)findViewById(R.id.gz);

    mOrientation = new Orientation(this);



  }

  public void rec(View v)
  {
    mOrientation.startListening(this);
  }

  public void stop(View v)
  {
    mOrientation.stopListening();
  }
  public void reset(View v)
  {
    mOrientation.stopListening();
    x.setText("0.0");
    y.setText("0.0");
    z.setText("0.0");
    xg.setText("0.0");
    yg.setText("0.0");
    zg.setText("0.0");
  }

  public void data(View v)
  {
    String url=Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/accelgyro.txt";
    File file = new File(url);
    Intent intent = new Intent(Intent.ACTION_VIEW);
    String mimeType= MimeTypeMap.getSingleton().getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(url));
    intent.setDataAndType(Uri.fromFile(file), mimeType);
    Intent intent1 = Intent.createChooser(intent, "Open With");
    startActivity(intent1);

  }

  @Override
  public void onOrientationChanged(float dx,float dy,float dz,float gx,float gy,float gz) {

    verifyStoragePermissions(this);
      x.setText(Float.toString(dx));
      y.setText(Float.toString(dy));
      z.setText(Float.toString(dz));
    xg.setText(Float.toString(gx));
    yg.setText(Float.toString(gy));
    zg.setText(Float.toString(gz));


    String content = "ACCEL: "+Float.toString(dx)+", "+Float.toString(dy)+", "+Float.toString(dz)+" GYRO: "+Float.toString(gx)+", "+Float.toString(gy)+", "+Float.toString(gz);



    FileOutputStream os;
    try {
      os = new FileOutputStream(file, true);
      os.write(content.getBytes());
      os.write("\n".getBytes());
      os.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
