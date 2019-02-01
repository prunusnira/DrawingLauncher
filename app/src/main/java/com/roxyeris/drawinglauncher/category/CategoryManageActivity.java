package com.roxyeris.drawinglauncher.category;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.roxyeris.drawinglauncher.R;

import java.util.List;

/**
 * Created by arinpc on 2016-10-18.
 */

public class CategoryManageActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    private List<String> categorySet;
    private CategoryManager cm;
    private CategoryManagerAdapter adapter;
    private ListView list;
    private Button add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_categorymanager);

        initialize();
    }

    public void initialize() {
        cm = CategoryManager.getInstance();
        cm.load();
        categorySet = cm.getCategoryNameList();
        adapter = new CategoryManagerAdapter(this, categorySet);
        list = (ListView) findViewById(R.id.catmgr_list);
        add = (Button) findViewById(R.id.catmgr_addCategory);

        list.setAdapter(adapter);

        add.setOnClickListener(this);
        list.setOnItemClickListener(this);

        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.catmgr_addCategory) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle(getString(R.string.catmgr_add_dlg_t));

            LinearLayout linear = new LinearLayout(this);
            linear.setOrientation(LinearLayout.VERTICAL);
            TextView tv = new TextView(this);
            tv.setText(getString(R.string.catmgr_add_dlg_s));
            final EditText edt = new EditText(this);
            edt.setSingleLine();
            linear.addView(tv);
            linear.addView(edt);

            dialog.setView(linear);
            dialog.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    boolean done = cm.creaeNewCategory(edt.getText().toString());
                    if(done) {
                        categorySet.clear();
                        categorySet.addAll(cm.getCategoryNameList());
                        adapter.notifyDataSetChanged();
                        cm.save();
                        dialogInterface.dismiss();
                    }
                    else {
                        AlertDialog.Builder dlg = new AlertDialog.Builder(CategoryManageActivity.this);
                        dlg.setTitle(getString(R.string.system_msg));
                        dlg.setMessage(getString(R.string.catmgr_already));
                        dlg.setNeutralButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                        dlg.setCancelable(false);
                        dlg.create().show();
                    }
                }
            });
            dialog.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            dialog.setCancelable(false);
            dialog.create().show();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if(!categorySet.get(i).equals("All Apps")) {
            final String current = categorySet.get(i);
            AlertDialog.Builder dlg = new AlertDialog.Builder(this);
            dlg.setTitle(getString(R.string.catmgr_selection_title) + " " + categorySet.get(i));

            final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1);

            adapter.add(getString(R.string.catmgr_selection_edit));
            adapter.add(getString(R.string.catmgr_selection_remove));

            dlg.setAdapter(adapter, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int j) {
                    if(j == 0) {
                        Intent move = new Intent(CategoryManageActivity.this, AppCategoryActivity.class);
                        move.putExtra("name", current);
                        startActivity(move);
                    }
                    else if(j == 1) {
                        AlertDialog.Builder dlg2 = new AlertDialog.Builder(CategoryManageActivity.this);
                        dlg2.setTitle(R.string.catmgr_remove_cat);
                        dlg2.setMessage(R.string.catmgr_remove_cat_d);
                        dlg2.setCancelable(false);
                        dlg2.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                cm.removeCategory(current);
                                categorySet.clear();
                                categorySet.addAll(cm.getCategoryNameList());
                                CategoryManageActivity.this.adapter.notifyDataSetChanged();
                                cm.save();
                                dialogInterface.dismiss();
                            }
                        });
                        dlg2.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                        dlg2.create().show();
                    }
                }
            });
            dlg.create().show();
        }
        else {
            AlertDialog.Builder dlg = new AlertDialog.Builder(this);
            dlg.setTitle(getString(R.string.system_msg));
            dlg.setMessage(getString(R.string.catmgr_allapp_sel));
            dlg.setNeutralButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            dlg.setCancelable(false);
            dlg.create().show();
        }
    }
}
