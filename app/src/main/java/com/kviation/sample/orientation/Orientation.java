
package com.kviation.sample.orientation;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.annotation.Nullable;
import android.view.Surface;
import android.view.WindowManager;

import static android.hardware.SensorManager.SENSOR_DELAY_NORMAL;

public class Orientation implements SensorEventListener {

  public interface Listener {
    void onOrientationChanged(float x,float y,float z, float gx,float gy,float gz);
  }

  private static final int SENSOR_DELAY_MICROS = 50 * 1000; // 50ms

  private final WindowManager mWindowManager;

  private final SensorManager mSensorManager;

  @Nullable
  private final Sensor accel;

  private final Sensor gyro;
  private int mLastAccuracy;
  private Listener mListener;
  float dx=0;
  float dy=0;
  float dz=0;
  float gx=0;
  float gy=0;
  float gz=0;
  float lastx=0;

  float lasty=0;
  float lastz=0;
  float lastgx=0;
  float lastgy=0;
  float lastgz=0;

  public Orientation(Activity activity) {
    mWindowManager = activity.getWindow().getWindowManager();
    mSensorManager = (SensorManager) activity.getSystemService(Activity.SENSOR_SERVICE);

    // Can be null if the sensor hardware is not available
    accel = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    gyro= mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
  }

  public void startListening(Listener listener) {
    if (mListener == listener) {
      return;
    }
    mListener = listener;
    if (accel == null || gyro==null) {
      LogUtil.w("One of the sensors not available; will not provide orientation data.");
      return;
    }
    mSensorManager.registerListener(this, accel, SENSOR_DELAY_NORMAL);
    mSensorManager.registerListener(this, gyro, SENSOR_DELAY_NORMAL);
  }

  public void stopListening() {
    mSensorManager.unregisterListener(this);
    mListener = null;
  }

  @Override
  public void onAccuracyChanged(Sensor sensor, int accuracy) {
    if (mLastAccuracy != accuracy) {
      mLastAccuracy = accuracy;
    }
  }

  public void onSensorChanged(SensorEvent event) {

    Sensor sensor = event.sensor;

    if (sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
     

      // get the change of the x,y,z values of the accelerometer
      dx =event.values[0];
      dy =event.values[1];
      dz =event.values[2];

      // if the change is below 2, it is just plain noise
      if (dx < 2)
        dx = 0;
      if (dy < 2)
        dy = 0;
    }
    else if (sensor.getType()==Sensor.TYPE_GYROSCOPE)
    {
   
      gx = event.values[0];
      gy = event.values[1];
      gz = event.values[2];

      // if the change is below 2, it is just plain noise
      if (gx < 2)
        gx = 0;
      if (gy < 2)
        gy = 0;
    }

    mListener.onOrientationChanged(dx,dy,dz,gx,gy,gz);

  }



}
