package com.example.academy;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.academy.tabs.CertificateTab;
import com.example.academy.tabs.CoursesTab;

public class PagerAdapter extends FragmentStatePagerAdapter {
    int tab_numbers;
    public PagerAdapter(@NonNull FragmentManager fm, int tab_numbers) {
        super(fm);
        this.tab_numbers = tab_numbers;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                CertificateTab certificateTab = new CertificateTab();
                return certificateTab;
            case 1:
                CoursesTab coursesTab = new CoursesTab();
                return coursesTab;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tab_numbers;
    }
}
