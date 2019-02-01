package com.roxyeris.drawinglauncher.category;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.roxyeris.drawinglauncher.R;
import com.roxyeris.drawinglauncher.app.ExecApp;
import com.roxyeris.drawinglauncher.app.Executable;
import com.roxyeris.drawinglauncher.data.AppListAdapter;
import com.roxyeris.drawinglauncher.data.Code;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by arinpc on 2016-10-18.
 */

public class AppCategoryActivity extends AppCompatActivity
        implements View.OnClickListener, AdapterView.OnItemClickListener{
    private AppCategory category;
    private AppCategoryAdapter adapter;
    private ListView list;
    private TextView title;
    private Button add;
    private TextView emptyView;
    private CategoryManager cm = CategoryManager.getInstance();
    private ArrayList<ApplicationInfo> apps;
    private String catName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_applistmanager);

        initialize();
    }

    public void initialize() {
        Intent intent = getIntent();
        catName = intent.getStringExtra("name");
        category = cm.getCategory(catName);

        list = (ListView) findViewById(R.id.appmgr_list);
        add = (Button) findViewById(R.id.appmgr_btnadd);
        title = (TextView) findViewById(R.id.appmgr_title);
        adapter = new AppCategoryAdapter(this, category);

        emptyView = (TextView) findViewById(R.id.emptyview);

        title.setText(getString(R.string.appmgr_title) + catName);

        add.setOnClickListener(this);
        list.setOnItemClickListener(this);
        list.setAdapter(adapter);
        list.setEmptyView(emptyView);

        // Get app list
        apps = new ArrayList<>();

        List<ApplicationInfo> appsAll = getPackageManager()
                .getInstalledApplications(PackageManager.GET_META_DATA);

        for(int i = 0; i < appsAll.size(); i++) {
            if(checkUserApp(appsAll.get(i))) {
                apps.add(appsAll.get(i));
            }
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.appmgr_btnadd) {
            // 앱이 아닌 인텐트를 통으로 추가 가능하게...
            // AlertDialog.Builder
            selectApp();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        adapter.add(getString(R.string.appmgr_remove));
        AlertDialog.Builder dlg = new AlertDialog.Builder(this);
        dlg.setTitle(this.adapter.getItemString(i));
        dlg.setAdapter(adapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                category.removeApp((Executable)AppCategoryActivity.this.adapter.getItem(i));
                AppCategoryActivity.this.adapter.notifyDataSetChanged();
                cm.save();
            }
        });
        dlg.create().show();
    }

    private void selectApp() {
        AppListAdapter adapter = new AppListAdapter(this, apps);

        adapter.notifyDataSetChanged();

        // List dialog open
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(getText(R.string.add_dlgapp_title));
        dialog.setAdapter(adapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                handlerApp.sendEmptyMessage(i);
            }
        });
        adapter.notifyDataSetChanged();
        dialog.setCancelable(false);
        dialog.create().show();
    }

    private boolean checkUserApp(ApplicationInfo app) {
        return getPackageManager().getLaunchIntentForPackage(app.packageName) != null;
    }

    private Handler handlerApp = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message message) {
            int id = message.what;
            ApplicationInfo selected = apps.get(id);

            Executable info = new ExecApp(getApplicationContext());
            info.setIcon(selected.loadIcon(getPackageManager()));
            info.setPackage(selected.loadLabel(getPackageManager()).toString());
            info.setType(Code.EXEC_APP);

            category.addApp(info);
            adapter.notifyDataSetChanged();
            cm.save();

            return false;
        }
    });
}
