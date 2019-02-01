package com.roxyeris.drawinglauncher.data;

/**
 * Created by arinpc on 2016-08-12.
 */
public class Code {
    public static final String MODE_APP = "APP";
    public static final String MODE_LAUNCHER = "LAUNCHER";
    public static final String MODE_SYSTEM = "SYSTEM";
    public static final int SDK_VERSION = android.os.Build.VERSION.SDK_INT;

    public static final int EXEC_APP = 0x5000;
    public static final int EXEC_LAUNCHER = 0x5001;
    public static final int EXEC_SYSTEM = 0x5002;
    public static final int EXEC_INTENT = 0x5003;

    /**
     * 런처동작 정의
     * 1. 앱 서랍 열기
     * 2. 런처 옵션 열기
     * 3. 새 패턴 추가
     * 4. 패턴관리
     */
    public static final int LAF_OPEN_DRAWER = 0x1000;
    public static final int LAF_OPEN_SETTING = 0x1001;
    public static final int LAF_ADD_PATTERN = 0x1002;
    public static final int LAF_MANAGE_PATTERN = 0x1003;

    /**
     * 시스템 동작
     * 1. 와이파이
     * 2. gps
     * 3. 블루투스
     * 4. 비행기
     * 5. 손전등
     * 6. 다이얼러
     */
    public static final int SYS_WIFI = 0x2000;
    public static final int SYS_GPS = 0x2001;
    public static final int SYS_BT = 0x2002;
    public static final int SYS_AIRPLANE = 0x2003;
    public static final int SYS_LIGHT = 0x2004;
    public static final int SYS_DIALER = 0x2005;

    /**
     * 설정 인텐트 코드
     * 1. 배경화면 설정
     */
    public static final int SET_REQ_WALLPAPER = 0x3000;
}
