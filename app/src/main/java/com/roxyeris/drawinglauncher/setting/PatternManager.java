package com.roxyeris.drawinglauncher.setting;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.gesture.Gesture;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.roxyeris.drawinglauncher.R;
import com.roxyeris.drawinglauncher.data.PatternMatching;

/**
 * Created by arinpc on 2016-08-24.
 */
public class PatternManager extends AppCompatActivity
        implements View.OnClickListener, AdapterView.OnItemClickListener {
    private ListView patternList;
    private TextView emptyView;
    private Button addPatternBtn;
    private PatternManagerAdapter adapter;
    private PatternMatching pm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_patternmanager);

        initVar();
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    private void initVar() {
        patternList = (ListView) findViewById(R.id.pattern_list);
        emptyView = (TextView) findViewById(R.id.emptyview);
        addPatternBtn = (Button) findViewById(R.id.btn_pat_add);
        adapter = new PatternManagerAdapter(this);
        pm = PatternMatching.getInstance();

        patternList.setAdapter(adapter);
        patternList.setOnItemClickListener(this);
        addPatternBtn.setOnClickListener(this);
        adapter.notifyDataSetChanged();

        patternList.setEmptyView(emptyView);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_pat_add) {
            startActivity(new Intent(this, AddPattern.class));
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        final Gesture g = (Gesture) adapter.getItem(position);
        final String t = adapter.getItemTitle(position);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(t);
        builder.setMessage(getString(R.string.set_pat_item_dlg_con));
        builder.setPositiveButton(getString(R.string.set_pat_item_dlg_pos),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        pm.removeGesture(t, g);
                        adapter.notifyDataSetChanged();

                        Intent intent = new Intent(PatternManager.this, AddPattern.class);
                        intent.putExtra("job", t);
                        startActivity(intent);
                    }
                });
        builder.setNegativeButton(getString(R.string.set_pat_item_dlg_neu),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        pm.removeGesture(t, g);
                        adapter.notifyDataSetChanged();
                    }
                });
        builder.setNeutralButton(getString(R.string.cancel),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        builder.create().show();
    }
}
