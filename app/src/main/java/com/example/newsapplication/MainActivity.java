package com.example.newsapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Things for creating tabs in MainActivity

        //Binding Views
        tabLayout = findViewById(R.id.tabs);
        viewPager = findViewById(R.id.container1);

        // Setting up ViewPager And Adapter
        tabLayout.setupWithViewPager(viewPager);
        VPAdapter vpAdapter = new VPAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        vpAdapter.addFragment(new Login(),"LOGIN");
        vpAdapter.addFragment(new SignUp(),"SIGN UP");


        viewPager.setAdapter(vpAdapter);




    }
}