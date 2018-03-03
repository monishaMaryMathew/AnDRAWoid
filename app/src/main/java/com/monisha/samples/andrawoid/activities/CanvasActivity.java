package com.monisha.samples.andrawoid.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.monisha.samples.andrawoid.R;
import com.monisha.samples.andrawoid.fragments.BrushSizeDialogFragment;
import com.monisha.samples.andrawoid.fragments.ColorPickerDialogFragment;
import com.monisha.samples.andrawoid.views.CustomView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/*
 * Created by Monisha on 2/18/2018.
 * Ref - https://www.codeproject.com/Articles/1045443/Android-Drawing-App-Tutorial-Pt
 */

public class CanvasActivity extends AppCompatActivity
        implements BrushSizeDialogFragment.BrushSizeDialogFragmentListener,
        ColorPickerDialogFragment.ColorPickerDialogFragmentListener {
    private Toolbar mToolbar_top;
    private Toolbar mToolbar_bottom;
    private CustomView customView;

    private int brushsize = 20;

    private String LOG_CAT = "Logger";

    private final static int REQUEST_WRITE_STORAGE = 111;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); //TODO - change this later when you add code to handle the orientation change
        setContentView(R.layout.activity_canvas);

        customView = (CustomView) findViewById(R.id.custom_view);

        mToolbar_bottom = (Toolbar) findViewById(R.id.toolbar_bottom);
        mToolbar_bottom.inflateMenu(R.menu.menu_drawing);
        mToolbar_bottom.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                handleDrawingIconTouched(item.getItemId());
                return false;
            }
        });
    }

    private void handleDrawingIconTouched(int itemId) {
        switch (itemId) {
            case R.id.action_delete:
                customView.delete(this);
                break;
            case R.id.action_erase:
                customView.erase(this);
                break;
            case R.id.action_undo:
                customView.redo(this);
                break;
            case R.id.action_brush:
                createBrushSizeSelectDialog();
                break;
            case R.id.action_color:
                createColorSelectorDialog();
                break;
            case R.id.action_save:
                shareDrawing();
                break;
            default:
                break;
        }
    }

    private void createColorSelectorDialog() {
        DialogFragment colorSelectorDialogFragment = new ColorPickerDialogFragment();
        colorSelectorDialogFragment.show(getSupportFragmentManager(), "Select Color");
    }

    private void createBrushSizeSelectDialog() {
        DialogFragment brushSizeDialogFragment = new BrushSizeDialogFragment();
        brushSizeDialogFragment.show(getSupportFragmentManager(), "Select Brush Size");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    private void shareDrawing() {
        customView.setDrawingCacheEnabled(true);
        customView.invalidate();
        String path = Environment.getExternalStorageDirectory().toString();
        OutputStream fOut = null;
        File file = new File(path,
                "android_drawing_app.png");
        file.getParentFile().mkdirs();
        if (isWritePermissionGiven()) {
            try {
                file.createNewFile();
            } catch (Exception e) {
                Log.e(LOG_CAT, e.getCause() + e.getMessage());
            }

            try {
                fOut = new FileOutputStream(file);
            } catch (Exception e) {
                Log.e(LOG_CAT, e.getCause() + e.getMessage());
            }

            if (customView.getDrawingCache() == null) {
                Log.e(LOG_CAT, "Unable to get drawing cache ");
            }

            customView.getDrawingCache()
                    .compress(Bitmap.CompressFormat.JPEG, 85, fOut);

            try {
                fOut.flush();
                fOut.close();
            } catch (IOException e) {
                Log.e(LOG_CAT, e.getCause() + e.getMessage());
            }

            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
            shareIntent.setType("image/png");
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(Intent.createChooser(shareIntent, "Share image"));
        }
            else {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_STORAGE);
        }

    }

    private boolean isWritePermissionGiven() {
        return (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
    }

    @Override
    public void onBrushSizeDialogOkButtonClick(int brushSize) {
        //Toast.makeText(this, "Value:" + brushSize, Toast.LENGTH_SHORT).show();
        this.brushsize = brushSize;
        customView.brush(brushsize);
    }

    @Override
    public int getInitialBrushSize() {
        return brushsize;
    }

    @Override
    public void onColorSelected(int color) {
        //Toast.makeText(this, "Color :" + color, Toast.LENGTH_SHORT).show();
        customView.color(this, color);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_WRITE_STORAGE:
                if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    shareDrawing();
                } else {
                    Toast.makeText(this, "To use the save feature, the app needs permission to access the storage.", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }
}

