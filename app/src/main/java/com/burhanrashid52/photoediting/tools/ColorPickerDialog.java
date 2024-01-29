package com.burhanrashid52.photoediting.tools;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.burhanrashid52.photoediting.R;
import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorChangedListener;
import com.flask.colorpicker.OnColorSelectedListener;

public class ColorPickerDialog extends DialogFragment {


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
        View v=LayoutInflater.from(getContext()).inflate(R.layout.color_picker,null);
        ColorPickerView colorPickerView = v.findViewById(R.id.color_picker_view);
        colorPickerView.addOnColorChangedListener(new OnColorChangedListener() {
            @Override public void onColorChanged(int selectedColor) {
                // Handle on color change
                Log.d("ColorPicker", "onColorChanged: 0x" + Integer.toHexString(selectedColor));
            }
        });
        colorPickerView.addOnColorSelectedListener(new OnColorSelectedListener() {
            @Override
            public void onColorSelected(int selectedColor) {
                Toast.makeText(
                        getContext(),
                        "selectedColor: " + Integer.toHexString(selectedColor).toUpperCase(),
                        Toast.LENGTH_SHORT).show();
            }
        });
        builder.setView(v);
        return builder.create();
    }

  /*  @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=LayoutInflater.from(getContext()).inflate(R.layout.color_picker,null);
        ColorPickerView colorPickerView = v.findViewById(R.id.color_picker_view);
        colorPickerView.addOnColorChangedListener(new OnColorChangedListener() {
            @Override public void onColorChanged(int selectedColor) {
                // Handle on color change
                Log.d("ColorPicker", "onColorChanged: 0x" + Integer.toHexString(selectedColor));
            }
        });
        colorPickerView.addOnColorSelectedListener(new OnColorSelectedListener() {
            @Override
            public void onColorSelected(int selectedColor) {
                Toast.makeText(
                     getContext(),
                        "selectedColor: " + Integer.toHexString(selectedColor).toUpperCase(),
                        Toast.LENGTH_SHORT).show();
            }
        });
        return v;
    }*/
}
