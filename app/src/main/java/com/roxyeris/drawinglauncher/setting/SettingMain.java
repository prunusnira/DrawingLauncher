package com.roxyeris.drawinglauncher.setting;

import android.app.AlertDialog;
import android.app.WallpaperManager;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.InputType;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.roxyeris.drawinglauncher.R;
import com.roxyeris.drawinglauncher.category.CategoryManageActivity;
import com.roxyeris.drawinglauncher.category.CategoryManager;
import com.roxyeris.drawinglauncher.data.Code;
import com.roxyeris.drawinglauncher.data.PatternMatching;

import java.io.File;
import java.io.IOException;

import colorpicker.AmbilWarnaDialog;

/**
 * Created by arinpc on 2016-08-08.
 */
public class SettingMain extends SettingMenuBase {
    public void setSettingItems() {
        SettingMenuData patternManager = new SettingMenuData();
        patternManager.setIconid(R.drawable.pattern);
        patternManager.setTitle(getText(R.string.set_pattern_t).toString());
        patternManager.setDesc(getText(R.string.set_pattern_s).toString());
        settingItems.add(patternManager);

        SettingMenuData background = new SettingMenuData();
        background.setIconid(R.drawable.background);
        background.setTitle(getText(R.string.set_hscr_bg_t).toString());
        background.setDesc(getText(R.string.set_hscr_bg_s).toString());
        settingItems.add(background);

        SettingMenuData paint = new SettingMenuData();
        paint.setIconid(R.drawable.canvas);
        paint.setTitle(getText(R.string.set_hscr_dr_t).toString());
        paint.setDesc(getText(R.string.set_hscr_dr_s).toString());
        settingItems.add(paint);

        SettingMenuData grid = new SettingMenuData();
        grid.setIconid(R.drawable.grid);
        grid.setTitle(getText(R.string.set_dr_rowcol_t).toString());
        grid.setDesc(getText(R.string.set_dr_rowcol_s).toString());
        settingItems.add(grid);

        SettingMenuData drawerCategory = new SettingMenuData();
        drawerCategory.setIconid(R.drawable.category);
        drawerCategory.setTitle(getText(R.string.set_drawer_category_t).toString());
        drawerCategory.setDesc(getText(R.string.set_drawer_category_s).toString());
        settingItems.add(drawerCategory);

        SettingMenuData rotate = new SettingMenuData();
        rotate.setIconid(R.drawable.orientation);
        rotate.setTitle(getText(R.string.set_rotate_t).toString());
        rotate.setDesc(getText(R.string.set_rotate_s).toString());
        settingItems.add(rotate);

        SettingMenuData reset = new SettingMenuData();
        reset.setIconid(R.drawable.reset);
        reset.setTitle(getText(R.string.set_reset_t).toString());
        reset.setDesc(getText(R.string.set_reset_s).toString());
        settingItems.add(reset);

        SettingMenuData about = new SettingMenuData ();
        about.setIconid (R.drawable.about);
        about.setTitle(getString(R.string.set_about_t));
        about.setDesc (getString(R.string.set_about_s));
        settingItems.add(about);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        switch(i) {
            case 0:
                startActivity(new Intent(this, PatternManager.class));
                break;
            case 1:
                Display d = getWindowManager().getDefaultDisplay();
                Point size = new Point();
                d.getSize(size);

                Intent pick = new Intent(Intent.ACTION_SET_WALLPAPER);
                startActivity(Intent.createChooser(pick, getString(R.string.set_wallpaper)));
                break;
            case 2:
                final ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                        android.R.layout.simple_list_item_1);

                adapter.add(getString(R.string.set_draw_rgba));
                adapter.add(getString(R.string.set_draw_thick));

                AlertDialog.Builder dlg1 = new AlertDialog.Builder(this);
                dlg1.setTitle(R.string.set_draw_t);
                dlg1.setAdapter(adapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(i == 0) {
                            String alpha = Integer.toHexString((int)(pref.getFloat("gestureAlpha", 0.5f)*256));
                            String color = pref.getString("gestureColor", "303F9F");

                            if(alpha.equals("0"))
                                alpha = "00";
                            AmbilWarnaDialog color1 = new AmbilWarnaDialog(SettingMain.this,
                                    Color.parseColor("#"+alpha+color), true,
                                    new AmbilWarnaDialog.OnAmbilWarnaListener() {
                                @Override
                                public void onCancel(AmbilWarnaDialog dialog) {
                                    dialog.getDialog().dismiss();
                                }

                                @Override
                                public void onOk(AmbilWarnaDialog dialog, int color) {
                                    String str = String.format("%08X", (0xFFFFFFFF & color));
                                    String alphaStr = str.substring(0,2);
                                    String colorStr = str.substring(2,8);
                                    edit = pref.edit();
                                    edit.putString("gestureColor", colorStr);
                                    edit.putFloat("gestureAlpha", Integer.parseInt(alphaStr, 16) / 256f);
                                    edit.commit();
                                }
                            });
                            color1.show();
                        }
                        else if(i == 1) {
                            AlertDialog.Builder thkdlg = new AlertDialog.Builder(SettingMain.this);
                            thkdlg.setTitle(R.string.set_draw_thick_t);
                            thkdlg.setMessage(R.string.set_draw_thick_s);

                            View thickv = getLayoutInflater().inflate(R.layout.setting_thickness, null);

                            float thickness = pref.getFloat("gestureThick", 100f);
                            final SeekBar sb = (SeekBar)thickv.findViewById(R.id.sb_thick);
                            final TextView tvThickCurrent = (TextView)thickv.findViewById(R.id.tv_thick_current);
                            sb.setMax(200);
                            sb.setProgress((int)thickness);
                            tvThickCurrent.setText(String.valueOf((int)thickness));

                            sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                                @Override
                                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                                    tvThickCurrent.setText(String.valueOf(progress));
                                }

                                @Override
                                public void onStartTrackingTouch(SeekBar seekBar) {

                                }

                                @Override
                                public void onStopTrackingTouch(SeekBar seekBar) {

                                }
                            });

                            thkdlg.setView(thickv);

                            thkdlg.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    int progress = sb.getProgress();
                                    edit = pref.edit();
                                    edit.putFloat("gestureThick", progress);
                                    edit.commit();
                                }
                            });
                            thkdlg.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            });

                            thkdlg.setCancelable(false);
                            thkdlg.create().show();
                        }
                    }
                });
                dlg1.setNeutralButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                dlg1.setCancelable(false);
                dlg1.create().show();

                break;
            case 3:
                AlertDialog.Builder dlg2 = new AlertDialog.Builder(this);
                dlg2.setTitle(R.string.set_dr_rowcol_t);

                View v = null;
                LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
                v = inflater.inflate(R.layout.setting_rowcol, null);
                final NumberPicker rowPicker = (NumberPicker)v.findViewById(R.id.num_row);
                final NumberPicker colPicker = (NumberPicker)v.findViewById(R.id.num_col);

                rowPicker.setMinValue(3);
                rowPicker.setMaxValue(6);
                colPicker.setMinValue(3);
                colPicker.setMaxValue(6);
                rowPicker.setWrapSelectorWheel(false);
                colPicker.setWrapSelectorWheel(false);

                rowPicker.setValue(pref.getInt("DRAWER_ROW", 6));
                colPicker.setValue(pref.getInt("DRAWER_COL", 5));

                dlg2.setView(v);

                dlg2.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences.Editor editor = pref.edit();
                        int row = rowPicker.getValue();
                        int col = colPicker.getValue();
                        if(row < 3) row = 3;
                        else if(row > 6) row = 6;
                        if(col < 3) col = 3;
                        else if(col > 6) col = 6;
                        editor.putInt("DRAWER_ROW", row);
                        editor.putInt("DRAWER_COL", col);
                        editor.commit();
                    }
                });
                dlg2.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dlg2.create().show();
                break;
            case 4:
                startActivity(new Intent(this, CategoryManageActivity.class));
                break;
            case 5:
                // 다이얼로그 표시
                AlertDialog.Builder dlg4 = new AlertDialog.Builder(this);
                dlg4.setTitle(getString(R.string.set_rotate_t));

                View v2 = null;
                LayoutInflater inflater2 = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
                v2 = inflater2.inflate(R.layout.setting_rotate, null);

                final RadioButton rbVertical = (RadioButton)v2.findViewById(R.id.rb_ver);
                final RadioButton rbHorizon = (RadioButton)v2.findViewById(R.id.rb_hor);
                final ImageView ivVertical = (ImageView)v2.findViewById(R.id.iv_ver);
                final ImageView ivHorizon = (ImageView)v2.findViewById(R.id.iv_hor);

                if(pref.getInt("orientation", 0) == 0)
                    rbVertical.setChecked(true);
                else
                    rbHorizon.setChecked(true);

                ivVertical.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        rbVertical.setChecked(true);
                    }
                });

                ivHorizon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        rbHorizon.setChecked(true);
                    }
                });

                dlg4.setView(v2);
                dlg4.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SharedPreferences.Editor edit = pref.edit();

                        if(rbVertical.isChecked())
                            edit.putInt("orientation", 0);
                        if(rbHorizon.isChecked())
                            edit.putInt("orientation", 1);

                        edit.commit();
                    }
                });
                dlg4.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                dlg4.create().show();
                break;
            case 6:
                // 다이얼로그 표시
                AlertDialog.Builder dlg = new AlertDialog.Builder(this);
                dlg.setTitle(getString(R.string.set_reset_t));
                dlg.setMessage(getString(R.string.set_main_reset_dlg));
                dlg.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        PatternMatching pm = PatternMatching.getInstance();
                        pm.resetLauncher();
                        CategoryManager cm = CategoryManager.getInstance();
                        for (String s:cm.getCategoryNameList()) {
                            cm.removeCategory(s);
                        }
                        edit = pref.edit();
                        edit.putString("gestureColor", "303F9F");
                        edit.putFloat("gestureAlpha", 0.5f);
                        edit.putFloat("gestureThick", 100f);
                        edit.commit();
                        dialogInterface.dismiss();
                    }
                });
                dlg.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                dlg.create().show();
                break;
            case 7:
                AlertDialog.Builder dlg3 = new AlertDialog.Builder(this);
                dlg3.setTitle(R.string.set_about_t);
                dlg3.setMessage(R.string.set_about_d);
                dlg3.setCancelable(false);
                dlg3.setNeutralButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                dlg3.create().show();
                break;
        }
    }
}
