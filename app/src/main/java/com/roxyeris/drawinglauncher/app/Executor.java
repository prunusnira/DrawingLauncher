package com.roxyeris.drawinglauncher.app;

/**
 * Created by Arin on 2016-08-11.
 */

import android.content.Context;

import com.roxyeris.drawinglauncher.data.Code;

/**
 * 필요 기능
 *
 * 1. Job의 종류 판별 (key는 ':'를 사용하여 작업 종류와 실행 명령이 구분됨)
 * 2. 내부 작업 - 런처자체에서 실행
 * 3. 앱 실행
 * 4. 시스템 기능 설정
 */
public class Executor {
    //private CategoryManager cm = CategoryManager.getInstance();
    private String job;
    private String arg;
    private Context ctx;
    private Executable exec;

    public Executor(String key, Context ctx) {
        this.ctx = ctx;
        String[] ext = extractor(key);
        job = ext[0];
        arg = ext[1];
    }

    public String[] extractor(String key) {
        return key.split(":");
    }

    /** 추가 리팩토링이 필요하지만 기존 코드와의 호환성을 위해 어쩔 수 없는 선택 ㅠㅠ... **/
    public void start() {
        if(job.equals(Code.MODE_APP)) {
            exec = new ExecApp(ctx);
        }
        else if(job.equals(Code.MODE_SYSTEM)) {
            exec = new ExecSystem(ctx);
        }
        else if(job.equals(Code.MODE_LAUNCHER)) {
            exec = new ExecFunction(ctx);
        }
        exec.execute(arg);
    }
}
