package com.example.medease;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.View;

import com.example.medease.databinding.ActivityShopMainBinding;
import com.example.medease.ui.DoctorHomeFragment;
import com.example.medease.ui.FindADoctorFragment;
import com.example.medease.ui.MyAppointmentFragment;
import com.example.medease.ui.MyCartFragment;
import com.example.medease.ui.SearchShopFragment;
import com.example.medease.ui.SettingsFragment;
import com.example.medease.ui.ShopFragment;
import com.example.medease.ui.ViewOrderFragment;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public class ShopMainActivity extends AppCompatActivity {

    ActivityShopMainBinding binding;
    ChipNavigationBar chipNavigationBar;
    private Fragment fragmentInstance;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityShopMainBinding.inflate(getLayoutInflater());

        View view = binding.getRoot();
        setContentView(view);

        chipNavigationBar = binding.bottomNavigationShop;

        chipNavigationBar.setOnItemSelectedListener(item -> {
            switch (item){

                case (R.id.nav_shop):
                    // getting the instance of the fragment class inside the fragment type variable
                    fragmentInstance = new ShopFragment();
                    break;

                case (R.id.nav_myCart):
                    fragmentInstance = new MyCartFragment();
                    break;

                case (R.id.nav_searchShop):
                    fragmentInstance = new SearchShopFragment();
                    break;

                case (R.id.nav_viewOrders):
                    fragmentInstance = new ViewOrderFragment();
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
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_shop ,fragmentInstance).commit();
            }

        });

        //By defeault before the user presses the navigation view our home fragment should appear
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_shop,new ShopFragment()).commit();

    }
}