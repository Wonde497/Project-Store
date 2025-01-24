package com.verifone.demo.emv.Utilities;
/*
 *  author: Derrick
 *  Time: 2019/5/17 14:08
 */

public class ConfigParam {

    private static ConfigParam instance;

    private ConfigParam(){

    }

    public static synchronized ConfigParam getInstance(){
        if (instance == null){
            instance = new ConfigParam();
        }
        return instance;
    }

    private String IS_SHOW_SCAN_BORDER = "1";           //是否显示扫码边框, 默认显示
    private String PRINT_TIMES = "1";                   //打印次数， 默认2次
    private String ONLY_SWIPE = "1";
    // 是否只令非接接触卡支持刷卡交易  默认1 为
    private String SUPPORT_TAP = "1";
    private String SUPPORT_INSERT = "1";
    private String SUPPORT_SWIPE = "1";

    public String getSUPPORT_TAP() {
        return SUPPORT_TAP;
    }

    public void setSUPPORT_TAP(String SUPPORT_TAP) {
        this.SUPPORT_TAP = SUPPORT_TAP;
    }

    public String getSUPPORT_INSERT() {
        return SUPPORT_INSERT;
    }

    public void setSUPPORT_INSERT(String SUPPORT_INSERT) {
        this.SUPPORT_INSERT = SUPPORT_INSERT;
    }

    public String getSUPPORT_SWIPE() {
        return SUPPORT_SWIPE;
    }

    public void setSUPPORT_SWIPE(String SUPPORT_SWIPE) {
        this.SUPPORT_SWIPE = SUPPORT_SWIPE;
    }

    public void setONLY_SWIPE(String ONLY_SWIPE) {
        this.ONLY_SWIPE = ONLY_SWIPE;
    }

    public String getONLY_SWIPE() {
        return ONLY_SWIPE;
    }

    public String getIS_SHOW_SCAN_BORDER() {
        return IS_SHOW_SCAN_BORDER;
    }

    public void setIS_SHOW_SCAN_BORDER(String IS_SHOW_SCAN_BORDER) {
        this.IS_SHOW_SCAN_BORDER = IS_SHOW_SCAN_BORDER;
    }

    public String getPRINT_TIMES() {
        return PRINT_TIMES;
    }

    public void setPRINT_TIMES(String PRINT_TIMES) {
        this.PRINT_TIMES = PRINT_TIMES;
    }
}
