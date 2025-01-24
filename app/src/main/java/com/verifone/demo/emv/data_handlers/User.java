package com.verifone.demo.emv.data_handlers;

public class User
{
    public String lastflag="lastflag",Header="Header",MTI="MTI",BITMAP="BITMAP",
            dateandtime="dateandtime",cashier="cashier",cup="cup",aid="aid",tvr="tvr",Txn_type="Txn_type",
            Current_Date="Current_Date",Current_Time="Current_Time",Card_Holder="Card_Holder",

             field_02="field_02",field_03="field_03",field_04="field_04",field_07="field_07",field_11="field_11",
              field_12="field_12",field_13="field_13", field_14="field_14",field_22="field_22",
              field_24="field_24",field_25="field_25",field_35="field_35",field_37="field_37",
             field_38="field_38",field_39="field_39",field_41="field_41",field_42="field_42",
    field_49="field_49",field_55="field_55",sign="signature";

    public String appcode,responsecode,appstatus,aidname,aidlable;
    private String name;
    private String password;
    private String type;
    private String status;

    public String transactionnum="";
    public String  merchname="";
    public String merchid="";
    public String termid="";
    public String balance="";
    public String timeoutdb="";
    private String cashierstatus;


    //................................................
    private String terminal_id;
    private String merchant_id;
    private String merchant_name;
    private String merchant_address;
    private String mode;
    private String currency;
    private String commtype;

    private String actiontype;

    private String masterkey="";
    private String keyloader="";
    private String keyloaderpass="";
//.............................

    public String getMasterkey() {
        return masterkey;
    }
    public void setMasterkey(String masterkey) {
        this.masterkey = masterkey;
    }

    public String getkeyloader() {
        return keyloader;
    }
    public void setkeyloader(String keyloader) { this.keyloader = keyloader; }

    public String getkeyloaderpass() {
        return keyloaderpass;
    }
    public void setkeyloaderpass(String keyloaderpass) {
        this.keyloaderpass = keyloaderpass;
    }
    public String getAidlable() {
        return aidlable;
    }
    public void setAidlable(String aidlable) {
        this.aidlable = aidlable;
    }
    public String getLastflag() {
        return lastflag;
    }
    public void setLastflag(String lastflag) {
        this.lastflag = lastflag;
    }

    public String getDateandtime() { return dateandtime; }
    public void setDateandtime(String dateandtime) { this.dateandtime = dateandtime; }

    public String getCashier() {
        return cashier;
    }
    public void setCashier(String cashier) {
        this.cashier = cashier;
    }


    public String getCup() {
        return cup;
    }
    public void setCup(String cup) {
        this.cup = cup;
    }

    public String getAid() {
        return aid;
    }
    public void setAid(String aid) {
        this.aid = aid;
    }

    public String getAppcode() {
        return appcode;
    }
    public void setAppcode(String appcode) {
        this.appcode = appcode;
    }

    public String getAppstatus() {
        return appstatus;
    }
    public void setAppstatus(String appstatus) {
        this.appstatus = appstatus;
    }

    public String getResponsecode() {
        return responsecode;
    }
    public void setResponsecode(String responsecode) {
        this.responsecode = responsecode;
    }

    public String getTvr() {
        return tvr;
    }
    public void setTvr(String tvr) {
        this.tvr = tvr;
    }

    //////000000000000000000000000000000000
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getTpe() {
        return type;
    }
    public void setTpe(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public String getCashierstatus() {
        return cashierstatus;
    }
    public void setCashierstatus(String cashierstatus) {
        this.cashierstatus = cashierstatus;
    }
    //...................................................................................

    public String getTerminal_id() {
        return terminal_id;
    }
    public void setTerminal_id(String terminal_id) {
        this.terminal_id = terminal_id;
    }
    public String getMerchant_id() { return merchant_id; }
    public void setMerchant_id(String merchant_id) {
        this.merchant_id = merchant_id;
    }

    public String getMerchant_name() {
        return merchant_name;
    }
    public void setMerchant_name(String merchant_name) {
        this.merchant_name = merchant_name;
    }

    public String getMerchant_address() { return merchant_address; }
    public void setMerchant_address(String merchant_address) { this.merchant_address = merchant_address;}

    public String getMode() { return mode; }
    public void setMode(String mode) { this.mode = mode;}

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public String getCommtype() { return commtype; }
    public void setCommtype(String commtype) { this.commtype = commtype; }

    public String getTransactionnum() {
        return transactionnum;
    }

    public void setTransactionnum(String transactionnum) {
        this.transactionnum = transactionnum;
    }
    public String getMerchname() { return merchname; }

    public void setMerchname(String merchname) { this.merchname = merchname; }

    public String getMerchid() {
        return merchid;
    }
    public void setMerchid(String merchid) {
        this.merchid = merchid;
    }

    public String getTermid() {
        return termid;
    }
    public void setTermid(String termid) {
        this.termid = termid;
    }


    public void setBalance(String balance) {
        this.balance = balance;
    }
    public String getBalance() {
        return balance;
    }

    public String getActiontype() {
        return actiontype;
    }
    public void setActiontype(String actiontype) {
        this.actiontype = actiontype;
    }

    public String getTimeoutdb() {
        return timeoutdb;
    }
    public void setTimeoutdb(String timeoutdb) {
        this.timeoutdb = timeoutdb;
    }

 //SET FIELDS.......................................................................
 public String getHeader() {
     return Header;
 }
    public void setHeader(String Header) {
        this.Header = Header;
    }

    public String getMTI() {
        return MTI;
    }

    public void setMTI(String MTI) {
        this.MTI = MTI;
    }

    public String getBITMAP() {
        return BITMAP;
    }
    public void setBITMAP(String BITMAP) {
        this.BITMAP = BITMAP;
    }

    public String getCard_Holder() { return Card_Holder; }
    public void setCard_Holder(String Card_Holder) { this.Card_Holder = Card_Holder; }

    public void setTxn_type(String Txn_type) {
    this.Txn_type = Txn_type;
}
    public String getTxn_type() {
        return Txn_type;
    }

    public void setCurrent_Date(String Current_Date) {
        this.Current_Date = Current_Date;
    }
    public String getCurrent_Date() {
        return Current_Date;
    }

    public void setCurrent_Time(String Current_Time) {
        this.Current_Time = Current_Time;
    }
    public String getCurrent_Time() {
        return Current_Time;
    }

    public void setField_02(String Field_02) {
        this.field_02 = Field_02;
    }
    public String getField_02() {
        return field_02;
    }

    public void setField_03(String Field_03) {
        this.field_03 = Field_03;
    }
    public String getField_03() {
        return field_03;
    }

    public void setField_04(String Field_04) {
        this.field_04 = Field_04;
    }
    public String getField_04() {
        return field_04;
    }

    public String getField_07() {
        return field_07;
    }

    public void setField_07(String field_07) {
        this.field_07 = field_07;
    }

    public void setField_11(String Field_11) {
        this.field_11 = Field_11;
    }
    public String getField_11() {
        return field_11;
    }

    public void setField_12(String Field_12) {
        this.field_12 = Field_12;
    }
    public String getField_12() {
        return field_12;
    }

    public void setField_13(String Field_13) {
        this.field_13 = Field_13;
    }
    public String getField_13() {
        return field_13;
    }

    public void setField_14(String Field_14) {
        this.field_14 = Field_14;
    }
    public String getField_14() {
        return field_14;
    }

    public void setField_22(String Field_22) {
        this.field_22 = Field_22;
    }
    public String getField_22() {
        return field_22;
    }

    public void setField_24(String Field_24) {
        this.field_24 = Field_24;
    }
    public String getField_24() {
        return field_24;
    }

    public String getField_25() {
        return field_25;
    }

    public void setField_25(String field_25) {
        this.field_25 = field_25;
    }

    public String getField_35() {
        return field_35;
    }

    public void setField_35(String field_35) {
        this.field_35 = field_35;
    }

    public void setField_37(String Field_37) {
        this.field_37 = Field_37;
    }
    public String getField_37() { return field_37; }

    public void setField_38(String Field_38) {
        this.field_38 = Field_38;
    }
    public String getField_38() {
        return field_38;
    }

    public void setField_39(String Field_39) {
        this.field_39 = Field_39;
    }
    public String getField_39() {
        return field_39;
    }

    public void setField_41(String Field_41) {
        this.field_41 = Field_41;
    }
    public String getField_41() {
        return field_41;
    }

    public void setField_42(String Field_42) {
        this.field_42 = Field_42;
    }
    public String getField_42() {
        return field_42;
    }

    public String getField_49() {
        return field_49;
    }

    public void setField_49(String field_49) {
        this.field_49 = field_49;
    }

    public void setField_55(String Field_55) {
        this.field_55 = Field_55;
    }
    public String getField_55() { return field_55; }



    public void setSign(String Sign) {
        this.sign = Sign;
    }
    public String getSign() {
        return sign;
    }


}
