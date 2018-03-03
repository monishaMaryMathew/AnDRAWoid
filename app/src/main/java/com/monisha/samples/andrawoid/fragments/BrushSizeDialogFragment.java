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
import android.widget.SeekBar;
import android.widget.TextView;

import com.monisha.samples.andrawoid.R;
import com.monisha.samples.andrawoid.activities.CanvasActivity;

/**
 * Created by Monisha on 2/24/2018.
 */

public class BrushSizeDialogFragment extends DialogFragment {

    public interface BrushSizeDialogFragmentListener {
        public void onBrushSizeDialogOkButtonClick(int brushSize);
        public int getInitialBrushSize();
    }

    BrushSizeDialogFragmentListener mBrushSizeDialogFragmentListener;
    public int brushSize = 20;
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final SeekBar seekBar;
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.brush_size_selector_layout, null);
        final TextView textView = (TextView) view.findViewById(R.id.brush_size_value);
        seekBar = (SeekBar) view.findViewById(R.id.brush_size_seekbar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                brushSize = i;
                textView.setText("Current brush size: " + i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekBar.setProgress(brushSize);
        builder.setView(view)
                .setPositiveButton(R.string.button_select, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Read the seekbar selection value and pass it on
                        mBrushSizeDialogFragmentListener.onBrushSizeDialogOkButtonClick(brushSize);
                    }
                })
                .setNegativeButton(R.string.button_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        BrushSizeDialogFragment.this.getDialog().cancel();
                    }
                })
        .setTitle(R.string.select_brush_size);
        Dialog dialog = builder.create();
        return dialog;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mBrushSizeDialogFragmentListener = (CanvasActivity) context;
        brushSize = mBrushSizeDialogFragmentListener.getInitialBrushSize();
    }
}
