package com.roxyeris.drawinglauncher.category;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import com.roxyeris.drawinglauncher.app.ExecApp;
import com.roxyeris.drawinglauncher.app.Executable;
import com.roxyeris.drawinglauncher.data.Code;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Created by arinpc on 2016-10-18.
 */

public class CategoryManager {
    /**
     * 카테고리 이름: 같은 이름을 방지하기 위해 set으로 관리
     * 단 매번 불러올 때 새로 로딩을 수행
     */
    private static CategoryManager instance;
    private ArrayList<AppCategory> categories;
    private String filepath;
    private File cmFile;
    private Context ctx;
    private JSONArray array;
    //private boolean changed = false;

    private CategoryManager() {
        categories = new ArrayList<>();
    }

    public static CategoryManager getInstance() {
        if(instance == null) {
            instance = new CategoryManager();
        }
        return instance;
    }

    public void initialize(Context ctx) {
        this.ctx = ctx;

        // 파일 열기
        filepath = ctx.getExternalFilesDir(null).getAbsolutePath();
        File dir = new File(filepath);
        dir.mkdirs();
        cmFile = new File(filepath+"/catergory.dlf");
        if(!cmFile.exists()) {
            try {
                cmFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // All Apps category 추가
        addAllAppCategory();

        // 그 외 카테고리 추가가
        load();
    }

    public void save() {
        JSONArray json = new JSONArray();
        for(AppCategory cat:categories) {
            if(!cat.getName().equals("All Apps")) {
                json.put(cat.categoryToJSON());
            }
        }

        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(cmFile));
            bw.write(json.toString());
            bw.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        //setChangedTrue();
    }

    public void load() {
        String json = "";
        // 파일에서 카테고리 열기
        try {
            BufferedReader br = new BufferedReader(
                    new FileReader(cmFile)
            );
            String s;
            while((s = br.readLine()) != null) {
                json += s;
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(json.length() > 0) {
            try {
                array = new JSONArray(json);
                int size = array.length();

                for (int i = 0; i < size; i++) {
                    JSONObject obj = (JSONObject) array.get(i);

                    String catTitle = obj.getString("catname");

                    if(!getCategoryNameList().contains(catTitle)) {
                        JSONArray catArray = new JSONArray(obj.getString("apps"));

                        AppCategory newcat = new AppCategory();
                        newcat.setName(catTitle);

                        int applen = catArray.length();

                        for (int j = 0; j < applen; j++) {
                            JSONObject appjson = catArray.getJSONObject(j);

                            int type = appjson.getInt("type");
                            String pkg = appjson.getString("pkg");
                            String name = appjson.getString("name");

                            ApplicationInfo app = ctx.getPackageManager().getApplicationInfo(appjson.getString("pkg"), PackageManager.GET_META_DATA);
                            Executable info = new ExecApp(ctx);
                            info.setIcon(app.loadIcon(ctx.getPackageManager()));
                            info.setName(name);
                            info.setPackage(pkg);
                            info.setType(type);
                            newcat.addApp(info);
                        }
                        categories.add(newcat);
                    }
                }
            }
            catch (JSONException e) {
                e.printStackTrace();
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public ArrayList<AppCategory> getAllCategory() {
        return categories;
    }

    public AppCategory getCategory(int i) {
        return categories.get(i);
    }

    public AppCategory getCategory(String name) {
        Iterator<AppCategory> itor = categories.iterator();
        AppCategory c = null;

        while(itor.hasNext()) {
            c = itor.next();
            if(c.getName().equals(name)) {
                break;
            }
        }

        return c;
    }

    public boolean creaeNewCategory(String name) {
        // 1. 같은 이름이 catNameSet에 존재하는지 확인
        List<String> namelist = getCategoryNameList();

        if(namelist.contains(name)) {
            return false;
        }
        else {
            // 2. 새로운 AppCategory 생성
            AppCategory newCat = new AppCategory();
            newCat.setName(name);
            categories.add(newCat);
            save();

            // 3. JSONObject화 하여 array에 추가하고 파일 저장
            return true;
        }
    }

    public void removeCategory(String name) {
        categories.remove(getCategory(name));
    }

    public List<String> getCategoryNameList() {
        List<String> list = new ArrayList<>();
        for(AppCategory c:categories) {
            list.add(c.getName());
        }
        return list;
    }

    // 첫 카테고리 - 모든 앱 포함
    public void addAllAppCategory() {
        if(!getCategoryNameList().contains("All Apps")) {
            AppCategory allappCat = new AppCategory();
            allappCat.setName("All Apps");
            allappCat.addAppList(getAllAppData());
            categories.add(allappCat);
        }
    }

    // 앱 데이터 받아오기
    private List<Executable> getAllAppData() {
        List<ApplicationInfo> allapps =
                ctx.getPackageManager().getInstalledApplications(PackageManager.GET_META_DATA);

        List<Executable> returnapps = new ArrayList<>();

        for(int i = 0; i < allapps.size(); i++) {
            ApplicationInfo capp = allapps.get(i);
            if(checkUserApp(capp)) {
                Executable app = new ExecApp(ctx);
                app.setName(capp.loadLabel(ctx.getPackageManager()).toString());
                app.setIcon(capp.loadIcon(ctx.getPackageManager()));
                app.setPackage(capp.packageName);
                app.setType(Code.EXEC_APP);
                returnapps.add(app);
            }
        }

        return sortAppList(returnapps);
    }

    // 전체 앱 카테고리 리셋
    public void resetAllApps() {
        AppCategory allapps = getCategory("All Apps");
        allapps.getAllApps().clear();
        allapps.addAppList(getAllAppData());
    }

    // 이름순으로 앱 정렬
    private ArrayList<Executable> sortAppList(List<Executable> allapp) {
        ArrayList<Executable> sorted = new ArrayList<>();

        // 일단 앱 이름 다 집어넣음
        ArrayList<String> appnameList = new ArrayList<>();
        ArrayList<Executable> data = new ArrayList<>();
        data.addAll(allapp);
        for(Executable app: allapp) {
            appnameList.add(app.getName());
        }
        ArrayList<String> sortedNameList = new ArrayList<>();
        sortedNameList.addAll(appnameList);
        Collections.sort(sortedNameList, String.CASE_INSENSITIVE_ORDER);

        for(String s:sortedNameList) {
            int idx = appnameList.indexOf(s);
            sorted.add(data.get(idx));
            appnameList.remove(idx);
            data.remove(idx);
        }

        return sorted;
    }

    // 사용자 앱 여부 확인
    private boolean checkUserApp(ApplicationInfo app) {
        return ctx.getPackageManager().getLaunchIntentForPackage(app.packageName) != null;
    }

    // 모든 카테고리에서 삭제된 앱 지우기 (현재 미적용)
    public void removeAppFromAllCategory(String pkgName) {
        for(AppCategory cat:categories) {
            List<Executable> apps = cat.getAllApps();

            for(Executable app:apps) {
                if (app.getPackage().equals(pkgName))
                    apps.remove(app);
            }
        }
    }

    /*public void setChangedTrue() {
        changed = true;
    }

    public void setChangedFalse() {
        changed = false;
    }

    public boolean isChanged() {
        return changed;
    }*/
}
