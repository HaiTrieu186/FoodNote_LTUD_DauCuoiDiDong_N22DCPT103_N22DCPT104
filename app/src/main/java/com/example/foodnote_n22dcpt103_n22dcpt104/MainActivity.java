package com.example.foodnote_n22dcpt103_n22dcpt104;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    TextView tvTitle;
    ViewPager2 viewPager;
    BottomNavigationView bottomNavigation;
    ViewPager2Adapter viewPager2Adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        initUI();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void initUI() {
        // ánh xạ view
        tvTitle= findViewById(R.id.tvTitle);
        viewPager= findViewById(R.id.view_pager);
        bottomNavigation= findViewById(R.id.bottom_navigation);

        // Thiết lập title và lời chào
        setGreeting();

        // Thiết lập viewpager và bottom navigation
        viewPager2Adapter= new ViewPager2Adapter(this);
        viewPager.setAdapter(viewPager2Adapter);

        // Bọn em tắt vuốt ngang để ngăn chặn xung đôt với recycle view horizontal
        viewPager.setUserInputEnabled(false);

        bottomNavigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();

                if (id == R.id.menu_home) {
                    viewPager.setCurrentItem(0);
                    return true;
                } else if (id == R.id.menu_meal) {
                    viewPager.setCurrentItem(1);
                    return true;
                } else if (id == R.id.menu_list) {
                    viewPager.setCurrentItem(2);
                    return true;
                } else if (id == R.id.menu_fridge) {
                    viewPager.setCurrentItem(3);
                    return true;
                }

                return false;
            }
        });

//        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
//            @Override
//            public void onPageSelected(int position) {
//                super.onPageSelected(position);
//
//                if (position==0){
//                    bottomNavigation.getMenu().findItem(R.id.menu_home).setChecked(true);
//                } else
//                if (position==1){
//                    bottomNavigation.getMenu().findItem(R.id.menu_meal).setChecked(true);
//                } else
//                if (position==2){
//                    bottomNavigation.getMenu().findItem(R.id.menu_list).setChecked(true);
//                } else {
//                    bottomNavigation.getMenu().findItem(R.id.menu_fridge).setChecked(true);
//                }
//            }
//        });

    }

    private void setGreeting() {
        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);
        String greeting;

        if (timeOfDay >= 5 && timeOfDay < 11) {
            greeting = "Chào buổi sáng tốt lành.";
        } else if (timeOfDay >= 11 && timeOfDay < 14) {
            greeting = "Chúc buổi trưa vui vẻ.";
        } else if (timeOfDay >= 14 && timeOfDay < 18) {
            greeting = "Chào buổi chiều.";
        } else if (timeOfDay >= 18 && timeOfDay < 22) {
            greeting = "Chúc buổi tối ấm áp.";
        } else {
            greeting = "Chúc bạn ngủ ngon.";
        }
        tvTitle.setText(greeting);
    }
}