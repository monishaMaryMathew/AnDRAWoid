package com.monisha.samples.andrawoid.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.monisha.samples.andrawoid.R;
import com.monisha.samples.andrawoid.activities.CanvasActivity;
import com.skydoves.multicolorpicker.ColorEnvelope;
import com.skydoves.multicolorpicker.MultiColorPickerView;
import com.skydoves.multicolorpicker.listeners.ColorListener;

/**
 * Created by Monisha on 2/28/2018.
 * Ref: https://android-arsenal.com/details/1/6365
 * https://medium.com/@skydoves/how-to-implement-color-picker-in-android-61d8be348683
 */

public class ColorPickerDialogFragment extends DialogFragment {
    private int selectedColor = 0;

    public interface ColorPickerDialogFragmentListener {
        public void onColorSelected(int color);
    }
    private ColorPickerDialogFragmentListener mColorPickerDialogFragmentListener;
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.color_picker_layout, null);

        final RelativeLayout currentColor = (RelativeLayout) view.findViewById(R.id.current_selection);
        MultiColorPickerView multiColorPickerView =(MultiColorPickerView) view.findViewById(R.id.multiColorPickerView);
        multiColorPickerView.addSelector(getActivity().getDrawable(R.drawable.wheel), new ColorListener() {
            @Override
            public void onColorSelected(ColorEnvelope envelope) {
                int color = envelope.getColor();
                int[] rgb = envelope.getRgb();
                String htmlCode = envelope.getHtmlCode();

                // TODO
                selectedColor = color;
                currentColor.setBackgroundColor(selectedColor);
            }
        });
        builder.setView(view)
                .setPositiveButton("Select", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mColorPickerDialogFragmentListener.onColorSelected(selectedColor);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ColorPickerDialogFragment.this.getDialog().cancel();
                    }
                });
        Dialog dialog = builder.create();
        return dialog;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mColorPickerDialogFragmentListener = (CanvasActivity) context;
    }
}
