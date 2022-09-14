package com.example.sdfapp.view;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.sdfapp.R;

public class RaidFragment extends Fragment {
    ImageView raidImage;
    TextView raidName;
    TextView raidDate;

    public RaidFragment() {
        //layout
        super(R.layout.raid_fragment);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initRaidFragment(view);
    }

    private void initRaidFragment(View view) {
        raidImage = view.findViewById(R.id.raidImage);
        raidName = view.findViewById(R.id.raidTitle);
        raidDate = view.findViewById(R.id.dateText);
    }
}
