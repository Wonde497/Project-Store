package com.verifone.demo.emv.caseA;

/**
 * Created by Simon on 2019/1/23.
 */

public class ISO8583uHeader5 extends ISO8583u {

    public ISO8583uHeader5(){
        super();
        //super.header = "6000800000";
        super.header = "";
    }

    @Override
    protected int getHeaderLen() {
        return 5;
    }



}
