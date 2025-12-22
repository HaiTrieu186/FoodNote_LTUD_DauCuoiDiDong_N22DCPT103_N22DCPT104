package com.example.foodnote_n22dcpt103_n22dcpt104;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.foodnote_n22dcpt103_n22dcpt104.fragments.FridgeFragment;
import com.example.foodnote_n22dcpt103_n22dcpt104.fragments.HomeFragment;
import com.example.foodnote_n22dcpt103_n22dcpt104.fragments.ListFragment;
import com.example.foodnote_n22dcpt103_n22dcpt104.fragments.MealFragment;

public class ViewPager2Adapter  extends FragmentStateAdapter {
    private final String[] titles= new String[]{"Meal","List","Home","Fridge"};

    public ViewPager2Adapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:{
                return new HomeFragment();
            }
            case 1:{
                return new MealFragment();
            }
            case 2:{
                return new ListFragment();
            }
            case 3:{
                return new FridgeFragment();
            }
        }
        return new HomeFragment();
    }

    @Override
    public int getItemCount() {
        return titles.length;
    }
}
