package com.example.medease;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.View;

import com.example.medease.databinding.ActivityChangePasswordBinding;
import com.example.medease.databinding.ActivityDoctorMainBinding;
import com.example.medease.ui.DoctorHomeFragment;
import com.example.medease.ui.FindADoctorFragment;
import com.example.medease.ui.HomeFragment;
import com.example.medease.ui.MyAppointmentFragment;
import com.example.medease.ui.SettingsFragment;
import com.example.medease.ui.ShopFragment;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public class DoctorMainActivity extends AppCompatActivity {

    ActivityDoctorMainBinding binding;
    ChipNavigationBar chipNavigationBar;
    private Fragment fragmentInstance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDoctorMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        chipNavigationBar = (ChipNavigationBar) findViewById(R.id.bottom_navigation_doctor);


        chipNavigationBar.setOnItemSelectedListener(item -> {
            switch (item){

                case (R.id.nav_doctor_home):
                    // getting the instance of the fragment class inside the fragment type variable
                    fragmentInstance = new DoctorHomeFragment();
                    break;

                case (R.id.nav_findADoctor):
                    fragmentInstance = new FindADoctorFragment();
                    break;

                case (R.id.nav_myAppointment):
                    fragmentInstance = new MyAppointmentFragment();
                    break;

                case (R.id.nav_settings):
                    fragmentInstance = new SettingsFragment();
                    break;
            }

            if(fragmentInstance != null){

                /*
                * In some cases, you may see getSupportFragmentManager() being called without a reference to a FragmentActivity instance.
                * This can happen when you're calling the method from within a class that extends FragmentActivity or another class
                * that has access to a FragmentManager instance, such as a class that extends Fragment.
                    For example, if you have a class that extends FragmentActivity and you want to add a fragment to the activity,
                     you can call getSupportFragmentManager() directly within the class:
                * */
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_doctor ,fragmentInstance).commit();
            }

        });

        //By defeault before the user presses the navigation view our home fragment should appear
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_doctor,new DoctorHomeFragment()).commit();
    }
}