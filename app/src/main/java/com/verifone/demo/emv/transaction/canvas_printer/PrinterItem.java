package com.verifone.demo.emv.transaction.canvas_printer;

/**
 * Created by Simon on 2019/2/27.
 */


/**
 * this is for ALL items defined
 *
 *
 */
public enum PrinterItem {

    // parameters: type, description (file name for Logo), string value, integer value, print style of description, print style of value
    // print style of description, print style of value, default font size 16, alignment left
    LOGO        (PrinterItemType.LOGO_ASSETS, new PrinterElement("verifone_logo.png"), new PrinterElement() ),
    TITLE       (PrinterItemType.STRING, new PrinterElement("Verifone X900", 32 , PrinterDefine.PStyle_align_center), new PrinterElement()),
    SUBTITLE    (PrinterItemType.STRING, new PrinterElement("", 20, PrinterDefine.PStyle_align_right ), new PrinterElement()),
    COPY_NOTE   (PrinterItemType.STRING, new PrinterElement("", 20, PrinterDefine.PStyle_align_left ), new PrinterElement("merchant copy", 16)),
    //TERMINAL INFORMATION
    MERCHANT_NAME(PrinterItemType.STRING, new PrinterElement("MERCHANT N: ",25,PrinterDefine.Font_default), new PrinterElement("", 25)),
    MERCHANT_ID (PrinterItemType.STRING, new PrinterElement("MER ID: ",25,PrinterDefine.Font_default), new PrinterElement("", 25)),
    TERMINAL_ID (PrinterItemType.STRING, new PrinterElement("TERMINAL ID: ",25,PrinterDefine.Font_default),  new PrinterElement("", 25)),
    Merchant_Adress (PrinterItemType.STRING, new PrinterElement("Merchant Address:",25,PrinterDefine.Font_Bold),  new PrinterElement("", 25)),
    TERMINAL_mode (PrinterItemType.STRING, new PrinterElement("Terminal Mode:",25,PrinterDefine.Font_Bold),  new PrinterElement("", 25)),
    Currency_type (PrinterItemType.STRING, new PrinterElement("Currency Type:",25,PrinterDefine.Font_Bold),  new PrinterElement("", 25)),
    Comm_type (PrinterItemType.STRING, new PrinterElement("Comm Type:",25,PrinterDefine.Font_Bold),  new PrinterElement("", 25)),
    terminalinfo(PrinterItemType.STRING, new PrinterElement("TERMINAL INFORMATION", 32 , PrinterDefine.PStyle_align_center,PrinterDefine.Font_Bold), new PrinterElement()),
    SEQNUM(PrinterItemType.STRING, new PrinterElement("SEQ No.:",25,PrinterDefine.Font_Bold),  new PrinterElement("", 25)),

    //TERMINAL SETUP
    terminalsetup (PrinterItemType.STRING, new PrinterElement("TERMINAL SETUP", 32 , PrinterDefine.PStyle_align_center,PrinterDefine.Font_Bold), new PrinterElement()),
    Mode (PrinterItemType.STRING, new PrinterElement("Terminal Mode",25,PrinterDefine.Font_Bold),  new PrinterElement("", 25) ),
    Ipaddress (PrinterItemType.STRING, new PrinterElement("IP Address",25,PrinterDefine.Font_Bold),  new PrinterElement("", 25) ),
    Port (PrinterItemType.STRING, new PrinterElement("Port",25,PrinterDefine.Font_Bold),  new PrinterElement("", 25) ),


    Detailreport (PrinterItemType.STRING, new PrinterElement("DETAIL REPORT", 30 , PrinterDefine.PStyle_align_center,PrinterDefine.Font_Bold), new PrinterElement()),
    Summryreport (PrinterItemType.STRING, new PrinterElement("SUMMRY REPORT", 30 , PrinterDefine.PStyle_align_center,PrinterDefine.Font_Bold), new PrinterElement()),
    Settlement (PrinterItemType.STRING, new PrinterElement("SETTLEMENT REPORT", 30 , PrinterDefine.PStyle_align_center,PrinterDefine.Font_Bold), new PrinterElement()),
    Endoffday (PrinterItemType.STRING, new PrinterElement("END OFF DAY", 30 , PrinterDefine.PStyle_align_center,PrinterDefine.Font_Bold), new PrinterElement()),

    Txn_Total (PrinterItemType.STRING, new PrinterElement("", 24 , PrinterDefine.PStyle_align_center,PrinterDefine.Font_Bold), new PrinterElement()),
    Dashenbach (PrinterItemType.STRING, new PrinterElement("TRANSACTION BACH REPORT ", 24 , PrinterDefine.PStyle_align_center,PrinterDefine.Font_Bold), new PrinterElement()),

    PURCHASE (PrinterItemType.STRING, new PrinterElement("PURCHASE:             ",25),  new PrinterElement("",25 )),
    REVERSAL (PrinterItemType.STRING, new PrinterElement("REVERSAL:             ",25),  new PrinterElement( "",25)),
    REFUND (PrinterItemType.STRING, new PrinterElement("REFUNDS:                 ",25),  new PrinterElement( "",25)),
    PRE_AUTH (PrinterItemType.STRING, new PrinterElement("PRE_AUTH:              ",25),  new PrinterElement( "",25)),
    PRE_AUTHCOMPLATION (PrinterItemType.STRING, new PrinterElement("PRE_AUTHCOMP: ",25),  new PrinterElement("",25 )),
    CASHADVANCE (PrinterItemType.STRING, new PrinterElement("CASHADVANCE:         ",25),  new PrinterElement( "",25)),
    PCASHBACK (PrinterItemType.STRING, new PrinterElement("P/CASHBACK:            ",25),  new PrinterElement( "",25)),
    TOTALNET (PrinterItemType.STRING, new PrinterElement("NET TOTAL:              ",25,PrinterDefine.Font_Bold),  new PrinterElement( "",25)),


    OPERATOR_ID (PrinterItemType.STRING, new PrinterElement("Operator ID",25),  new PrinterElement( )),
    Communicationfail (PrinterItemType.STRING, new PrinterElement("COMMUNICATION FAILURE", 30 , PrinterDefine.PStyle_align_center,PrinterDefine.Font_Bold), new PrinterElement()),
    responceerror (PrinterItemType.STRING, new PrinterElement("AUTHORIZATION FAILURE", 30 , PrinterDefine.PStyle_align_center,PrinterDefine.Font_Bold), new PrinterElement()),
    responcenull (PrinterItemType.STRING, new PrinterElement("RESPONSE NULL/FAILURE", 30 , PrinterDefine.PStyle_align_center,PrinterDefine.Font_Bold), new PrinterElement()),

    HOST        (PrinterItemType.STRING, new PrinterElement("HOST", 25),  new PrinterElement()),
    TRANS_TYPE  (PrinterItemType.STRING, new PrinterElement("", 25 ,PrinterDefine.PStyle_align_left),
            new PrinterElement("", 25, PrinterDefine.PStyle_align_center , PrinterDefine.Font_Bold )),
    CARD_ISSUE  (PrinterItemType.STRING, new PrinterElement("Card Issue", 25),  new PrinterElement() ),
    CARD_NO     (PrinterItemType.STRING, new PrinterElement("CARD_No.:",  25,PrinterDefine.Font_Bold),  new PrinterElement("", 25)),
    CARD_TYPE   (PrinterItemType.STRING, new PrinterElement("Card Type", 25),  new PrinterElement() ),
    CARD_HOLDER (PrinterItemType.STRING, new PrinterElement("", 25 ,PrinterDefine.PStyle_align_left), new PrinterElement("", 25, PrinterDefine.PStyle_align_center , PrinterDefine.Font_default )),
    CARD_VALID  (PrinterItemType.STRING, new PrinterElement("Card Expire:", 25,PrinterDefine.Font_default),  new PrinterElement() ),
    BATCH_NO    (PrinterItemType.STRING, new PrinterElement("BATCH #", 25),  new PrinterElement() ),
    DATE_TIME   (PrinterItemType.STRING, new PrinterElement("", 30,PrinterDefine.Font_Bold),  new PrinterElement("", 30)),
    REFER_NO    (PrinterItemType.STRING, new PrinterElement("REFER", 25),  new PrinterElement() ),
    TRACK_NO    (PrinterItemType.STRING, new PrinterElement("TRACE #", 25),  new PrinterElement() ),
    AUTH_NO     (PrinterItemType.STRING, new PrinterElement("AUTH #", 25),  new PrinterElement() ),
    AMOUNT      (PrinterItemType.STRING, new PrinterElement("AMOUNT:", 25, PrinterDefine.Font_Bold),
            new PrinterElement(/*"", 32, PrinterDefine.Font_Bold*/) ),
    BALANCE     (PrinterItemType.STRING, new PrinterElement("BALANCE:", 25,PrinterDefine.Font_Bold),  new PrinterElement() ),
    TIP         (PrinterItemType.STRING, new PrinterElement("TIP"),  new PrinterElement() ),
    TOTAL       (PrinterItemType.STRING, new PrinterElement("TOTAL:"),  new PrinterElement() ),
    REFERENCE   (PrinterItemType.STRING, new PrinterElement("REFERENCE",25),  new PrinterElement() ),
    E_SIGN        (PrinterItemType.IMG_BCD, new PrinterElement("CUSTOMER SIGNATURE:",25),  new PrinterElement() ),
    RE_PRINT_NOTE(PrinterItemType.STRING, new PrinterElement("RE-PRINT",25 , PrinterDefine.PStyle_align_center),  new PrinterElement() ),
    TC   (PrinterItemType.STRING, new PrinterElement("TC",25),  new PrinterElement() ),

    COMMENT_1   (PrinterItemType.STRING,1, new PrinterElement("", 16, PrinterDefine.PStyle_align_center),  new PrinterElement() ),
    COMMENT_2   (PrinterItemType.STRING,1, new PrinterElement("", 16, PrinterDefine.PStyle_align_center),  new PrinterElement() ),
    COMMENT_3   (PrinterItemType.STRING,1, new PrinterElement("", 16, PrinterDefine.PStyle_align_center),  new PrinterElement() ),

    FLEXIBLE_1  (PrinterItemType.STRING, new PrinterElement(),  new PrinterElement() ),
    FLEXIBLE_2  (PrinterItemType.STRING, new PrinterElement(),  new PrinterElement() ),
    FLEXIBLE_3  (PrinterItemType.STRING, new PrinterElement(),  new PrinterElement() ),
    FLEXIBLE_4  (PrinterItemType.STRING, new PrinterElement(),  new PrinterElement() ),
    FLEXIBLE_5  (PrinterItemType.STRING, new PrinterElement(),  new PrinterElement() ),

    TVR (PrinterItemType.STRING, new PrinterElement("TVR:",25,PrinterDefine.Font_default),  new PrinterElement( " ",25)),
    TRNSNO (PrinterItemType.STRING, new PrinterElement("TRN-#:",25,PrinterDefine.Font_Bold),  new PrinterElement(" ",25,PrinterDefine.PStyle_align_center)),
    ARPC (PrinterItemType.STRING, new PrinterElement("ARPC:",25,PrinterDefine.Font_Bold),  new PrinterElement( )),
    CASHIER (PrinterItemType.STRING, new PrinterElement("Cashier:",25,PrinterDefine.Font_Bold),  new PrinterElement( )),
    CUP  (PrinterItemType.STRING, new PrinterElement("", 25 ,PrinterDefine.PStyle_align_left),
            new PrinterElement("", 25, PrinterDefine.PStyle_align_center , PrinterDefine.Font_default )),

    //directorylist
    directorylist (PrinterItemType.STRING, new PrinterElement("User Manual Directory List   ", 28 , PrinterDefine.PStyle_align_center,PrinterDefine.Font_Bold), new PrinterElement()),
    Admin (PrinterItemType.STRING, new PrinterElement("Administrator Menu",25,PrinterDefine.Font_Bold),  new PrinterElement( )),
    Support (PrinterItemType.STRING, new PrinterElement("Support Menu",25,PrinterDefine.Font_Bold),  new PrinterElement( )),
    Supervisor (PrinterItemType.STRING, new PrinterElement("Supervisor Menu",25,PrinterDefine.Font_Bold),  new PrinterElement( )),
    Cashier1 (PrinterItemType.STRING, new PrinterElement("Cashier Menu",25,PrinterDefine.Font_Bold),  new PrinterElement( )),
    line1 (PrinterItemType.STRING, new PrinterElement("............................................",25,PrinterDefine.PStyle_align_center,PrinterDefine.Font_Bold),  new PrinterElement( )),


    CUS (PrinterItemType.STRING, new PrinterElement("CUS",25),  new PrinterElement( )),
    AID (PrinterItemType.STRING, new PrinterElement("AID:",25,PrinterDefine.Font_default),  new PrinterElement("", 25)),
    RRN (PrinterItemType.STRING, new PrinterElement("RRN",25,PrinterDefine.Font_Bold),  new PrinterElement("", 25)),
    CID (PrinterItemType.STRING, new PrinterElement("CID",25,PrinterDefine.Font_Bold),  new PrinterElement("", 25)),
    TSI (PrinterItemType.STRING, new PrinterElement("TSI",25,PrinterDefine.Font_Bold),  new PrinterElement("", 25)),
    Appcode(PrinterItemType.STRING, new PrinterElement("App Code:",25,PrinterDefine.Font_Bold), new PrinterElement("", 25,PrinterDefine.PStyle_align_center)),
    respocode(PrinterItemType.STRING, new PrinterElement("Response Code:",25,PrinterDefine.Font_Bold), new PrinterElement("", 25)),
    r_resp_msg(PrinterItemType.STRING, new PrinterElement(" ",25,PrinterDefine.PStyle_align_left), new PrinterElement("", 25, PrinterDefine.PStyle_align_center , PrinterDefine.Font_default )),
    Approstatus (PrinterItemType.STRING, new PrinterElement("", 28 ,PrinterDefine.PStyle_align_left), new PrinterElement("", 28, PrinterDefine.PStyle_align_center , PrinterDefine.Font_Bold )),

//    GUIDE1        (PrinterItemType.LOGO_ASSETS, new PrinterElement("guide/2-main.png"), new PrinterElement() ),
//    GUIDE2        (PrinterItemType.LOGO_ASSETS, new PrinterElement("guide/2-main.png"), new PrinterElement() ),
//    GUIDE3        (PrinterItemType.LOGO_ASSETS, new PrinterElement("guide/2-main.png"), new PrinterElement() ),
//    GUIDE4        (PrinterItemType.LOGO_ASSETS, new PrinterElement("guide/2-main.png"), new PrinterElement() ),

    BARCODE_1     (PrinterItemType.BARCODE, new PrinterElement("Barcode for refund", 48, PrinterDefine.PStyle_align_center), new PrinterElement("123456789", 32, PrinterDefine.PStyle_align_center ) ),
    BARCODE_2     (PrinterItemType.BARCODE, new PrinterElement("Barcode for refund", 48, PrinterDefine.PStyle_align_center), new PrinterElement("123456789", 32, PrinterDefine.PStyle_align_center ) ),

    QRCODE_1     (PrinterItemType.QRCODE, new PrinterElement("https://www.globalbankethiopia.com/", 150, PrinterDefine.PStyle_align_center), new PrinterElement("https://www.globalbankethiopia.com/", 150, PrinterDefine.PStyle_align_center) ),
    //QRCODE_2     (PrinterItemType.QRCODE, new PrinterElement("123456789", 180, PrinterDefine.PStyle_align_center), new PrinterElement("123456789", 180, PrinterDefine.PStyle_align_center) ),
//"https://play.google.com/store/apps/details?id=com.cr2.DashenBank&hl=en"  amolie  http://bit.ly/3Mmf21Q
    CUT         (PrinterItemType.STRING, 1, new PrinterElement("----------x----------x----------"),  new PrinterElement() ),
    LINE        (PrinterItemType.LINE, new PrinterElement("",2),  new PrinterElement() ),
    FEED        (PrinterItemType.FEED, new PrinterElement("",2),  new PrinterElement() ), // pixel for feed

    FEED_LINE   (PrinterItemType.FEED, 1,  new PrinterElement("",20),  new PrinterElement() ); // pixel for feed


    public PrinterItemType type;
    public PrinterElement title;
    public PrinterElement value;
    /**
     * 1 for paper, 2 for show, 3 for paper and show as the default
     */
    public int printerMode;

    public boolean isForceMultiLines;

    private PrinterItemType df_type;
    private PrinterElement df_title;
    private PrinterElement df_value;
    private boolean df_isForceMultiLines;

    private void set(PrinterItemType type, PrinterElement title, PrinterElement value, boolean isForceMultiLines, int printerMode){
        if( title.style == -1 ){
            title.style = PrinterDefine.PStyle_align_left;
        }
        if( value.style == -1 ){
            value.style = PrinterDefine.PStyle_align_right;
        }

        this.df_type = type;
        this.df_title = title;
        this.df_value = value;
        this.df_isForceMultiLines = isForceMultiLines ;

        this.type = df_type;
        this.title = df_title;
        this.value = df_value;
        this.isForceMultiLines = this.df_isForceMultiLines;

        this.printerMode = printerMode;
    }

    PrinterItem(PrinterItemType type, PrinterElement title, PrinterElement value){
        set(type, title, value, false , 3);
    }
    PrinterItem(PrinterItemType type, PrinterElement title, PrinterElement value, boolean isForceMultiLines ){
        set(type, title,value, isForceMultiLines , 3);
    }
    PrinterItem(PrinterItemType type, int printerMode, PrinterElement title, PrinterElement value){
        set(type, title, value, false , printerMode);
    }

    public void copy( PrinterItem printerItem ){
        this.title = new PrinterElement( printerItem.title);
        this.value = new PrinterElement( printerItem.value);
        this.type = printerItem.type;
        this.isForceMultiLines = printerItem.isForceMultiLines;
    }

    public void restore(){
        type = df_type;
        title = df_title;
        value = df_value;
        isForceMultiLines = df_isForceMultiLines;
    }


}