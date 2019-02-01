package com.roxyeris.drawinglauncher.data;

/**
 * Created by Arin on 2016-08-09.
 */

import android.content.Context;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GesturePoint;
import android.gesture.GestureStroke;
import android.gesture.Prediction;
import android.graphics.Path;
import android.os.Environment;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Job의 종류
 * 1. 앱 동작: 서랍열기, 설정
 * 2. 빠른 앱 실행
 *
 * PatternMatching
 * 매칭 패턴을 저장하는 제스처 파일을 읽고 쓰는 클래스
 * 싱글톤으로 사용
 *
 * 하나의 제스처는 하나의 엔티티에만 매핑되도록 설정
 * Gesture.toPath()를 사용하여 60프레임으로 나누어진 기존 형태를 그대로 사용
 */
public class PatternMatching {
    // Pattern path and gesture store
    private String savedPatternPath;
    private File patternFile;
    private GestureLibrary patternStore;
    private Context ctx;

    private static PatternMatching instance;

    public void initialize() {
        savedPatternPath = ctx.getExternalFilesDir(null).getAbsolutePath();
        File dir = new File(savedPatternPath);
        dir.mkdirs();
        patternFile = new File(savedPatternPath+"/pattern.dlf");
        if(patternStore == null) {
            patternStore = GestureLibraries.fromFile(patternFile);
            patternStore.load();
        }
    }

    public Set<String> getPatternEntries() {
        return patternStore.getGestureEntries();
    }

    public Gesture getGesture(String job) {
        return patternStore.getGestures(job).get(0);
    }

    public void saveGesture(String job, Gesture g) {
        patternStore.addGesture(job, g);
        patternStore.save();
    }

    public String compareGesture(Gesture g) {
        ArrayList<Prediction> p = patternStore.recognize(g);

        Collections.sort(p, new Comparator<Prediction>() {
            @Override
            public int compare(Prediction o1, Prediction o2) {
                return o1.score > o2.score ? -1 : 1 ;
            }
        });

        if(p.size() > 0) {
            return p.get(0).name;
        }
        else {
            return "nojob";
        }
    }

    public static PatternMatching getInstance() {
        if(instance == null) {
            instance = new PatternMatching();
        }
        return instance;
    }

    public void removeGesture(String job, Gesture g) {
        patternStore.removeGesture(job, g);
        patternStore.removeEntry(job);
        patternStore.save();
    }

    public void resetLauncher() {
        try {
            patternFile.delete();
            patternFile.createNewFile();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void setCtx(Context mctx) {
        ctx = mctx;
    }
}
