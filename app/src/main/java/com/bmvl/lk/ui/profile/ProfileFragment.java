package com.bmvl.lk.ui.profile;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bmvl.lk.OnBackPressedListener;
import com.bmvl.lk.R;



/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment implements OnBackPressedListener {


    public ProfileFragment() {
        // Required empty public constructor
    }
    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View MyView = inflater.inflate(R.layout.activity_profile, container, false);
       // MyActionBar.setTitle(R.string.profile);

        TextView FIO = MyView.findViewById(R.id.FIO);
        String Name = "Имя", Surname = "Фамилия", Patronic = "Отчество";
        FIO.setText(String.format("Личный кабинет пользователя %s %s %s", Name ,Surname, Patronic));
        return MyView;
    }

    @Override
    public void onBackPressed() {

    }
}
