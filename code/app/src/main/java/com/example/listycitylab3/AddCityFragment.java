package com.example.listycitylab3;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class AddCityFragment extends DialogFragment {
    interface AddCityDialogListener {
        void addCity(City city);
        void updateCity(City city, int position);
    }

    private static final String ARG_CITY = "arg_city";
    private static final String ARG_POSITION = "arg_position";

    private AddCityDialogListener listener;

    public static AddCityFragment newInstance(@Nullable City city, int position) {
        AddCityFragment fragment = new AddCityFragment();
        Bundle args = new Bundle();
        if (city != null) {
            args.putSerializable(ARG_CITY, city);
            args.putInt(ARG_POSITION, position);
        }
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof AddCityDialogListener) {
            listener = (AddCityDialogListener) context;
        } else {
            throw new RuntimeException(context + " must implement " + "AddCityDialogListener");
        }
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_add_city, null);

        EditText editCityName = view.findViewById(R.id.edit_text_city_text);
        EditText editProvinceName = view.findViewById(R.id.edit_text_province_text);

        City existingCity = null;
        int position = -1;

        if (getArguments() != null) {
            existingCity = (City) getArguments().getSerializable(ARG_CITY);
            position = getArguments().getInt(ARG_POSITION, -1);

            if (existingCity != null) {
                editCityName.setText(existingCity.getName());
                editProvinceName.setText(existingCity.getProvince());
            }
        }

        final int finalPosition = position;
        final City finalExistingCity = existingCity;

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle(existingCity == null ? "Add a city" : "Edit city")
                .setNegativeButton("Cancel", null)
                .setPositiveButton(existingCity == null ? "Add" : "Save", (dialog, which) -> {
                    String cityName = editCityName.getText().toString();
                    String provinceName = editProvinceName.getText().toString();
                    City city = new City(cityName, provinceName);

                    if (finalExistingCity == null) {
                        listener.addCity(city);
                    } else {
                        listener.updateCity(city, finalPosition);
                    }
                })
                .create();
    }
}

