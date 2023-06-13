package com.example.medease;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.medease.databinding.ActivityDoctorAppointmentBinding;
import com.example.medease.databinding.ActivityFitnessBinding;

public class Fitness extends AppCompatActivity implements SensorEventListener {

    ActivityFitnessBinding binding;
    private SensorManager sensorManager;
    private Sensor stepCounterSensor;
    //private SensorManager sensoranager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityFitnessBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
//        sensoranager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
//
//        if (sensoranager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER) != null) {
//            binding.fitnesstextview.setText("STEP COUNTER sensor supports");
//        } else {
//            binding.fitnesstextview.setText("no STEP COUNTER sensor supports");
//        }

        Log.i("Oncreate Method", "Oncreate method");

        sensorManager = (SensorManager) this.getSystemService(Context.SENSOR_SERVICE);

        if (sensorManager != null) {
            stepCounterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

            if (stepCounterSensor != null) {
                sensorManager.registerListener(this, stepCounterSensor, SensorManager.SENSOR_DELAY_FASTEST);
                Log.i("Step counter sensor found", "Sensor registered");
            } else {
                Log.i("Step counter sensor not found", "Sensor not available");
            }
        } else {
            Log.i("Sensor Manager not found", "Sensor manager not available");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Register the sensor listener again
        if (sensorManager != null && stepCounterSensor != null) {
            Log.i("Onresume Medthod","Onresume Medthod");
            sensorManager.registerListener(this, stepCounterSensor, SensorManager.SENSOR_DELAY_FASTEST);
        }else{
            Log.i("error in Onrsume Method","On resume Method error");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Unregister the sensor listener to avoid battery drain
        if (sensorManager != null) {
            sensorManager.unregisterListener(this);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Log.i("Stepcounter","Counter Outside");
        if (event.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
            Log.i("Stepcounter","Counter Changed");
            Log.i("Step count", String.valueOf(event.values[0]));
            binding.fitnesstextview.setText(String.valueOf(event.values[0]));
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do nothing
    }
}
