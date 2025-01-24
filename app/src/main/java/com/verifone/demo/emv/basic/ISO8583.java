package com.verifone.demo.emv.basic;

import static com.verifone.demo.emv.transaction.TransBasic.hexToASCII;
import static com.verifone.demo.emv.transaction.TransBasic.responce_code;

import android.util.Log;
import android.util.SparseArray;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

import com.verifone.demo.emv.DBHandler;
import com.verifone.demo.emv.Transactiondata;
import com.verifone.demo.emv.Utilities.Utility;
import com.verifone.demo.emv.transaction.TransBasic;

/**
 * Created by Simon on 2018/8/23.
 */

public abstract class ISO8583 {

    private static final String TAG = "EMVDemo-ISO8583";
    public static String B_Amount1;
    boolean[] validField;
    byte[][] allField;
    EMVTLVParam emvtlvF55 = null;

    //protected String header="6000060000";
    protected String header="";
    protected String tail="";

    public boolean[] unpackValidField;


    protected static final int TYPE_BCD = 1;
    protected static final int TYPE_ASC = 2;
    protected static final int TYPE_BIN = 4 ;
    // 8
    // 16
    protected static final int TYPE_LEN = 32 ;
    protected static final int TYPE_LEN_LEN = 64 ;
    // 128
    protected static final int TYPE_FILL_ = 128 ;
    // 256
    protected static final int TYPE_FILL_SPACE_RIGHT = 256 + TYPE_FILL_ ;
    // 512
    protected static final int TYPE_FILL_SPACE_LEFT = 512 + TYPE_FILL_ ;
    // 1024
    protected static final int TYPE_FILL_ZERO_RIGHT = 1024 + TYPE_FILL_ ;
    // 2048
    protected static final int TYPE_FILL_ZERO_LEFT = 2048  + TYPE_FILL_ ;
    // 4096
    protected static final int TYPE_L_BCD = TYPE_LEN + TYPE_BCD;
    protected static final int TYPE_LL_BCD = TYPE_LEN_LEN + TYPE_BCD;
    protected static final int TYPE_L_ASC = TYPE_ASC + TYPE_LEN ;
    protected static final int TYPE_LL_ASC = TYPE_ASC + TYPE_LEN_LEN ;
    protected static final int TYPE_L_BIN = TYPE_BIN + TYPE_LEN ;
    protected static final int TYPE_LL_BIN = TYPE_BIN + TYPE_LEN_LEN ;
    protected static final int TYPE_ASC_FS = TYPE_ASC + TYPE_FILL_SPACE_RIGHT;

    protected static final int ATTR_INDEX_TYPE = 0;
    protected static final int ATTR_INDEX_LEN_DEFAULT = 1;
    protected static final int BITMAP_FIELD_INDEX_ = 1;

    private int ISO_BIT_MAX;

    protected static String DATA_FORMAT = "DDdd";
    protected static String TIME_FORMAT = "HHmmss";
    public static String daterecit="",timereciet="",Current_Time="",Current_Date="";
    public enum PACKET_TYPE
    {
        PACKET_TYPE_NONE,
        PACKET_TYPE_HEXLEN_BUF,
    }

    public enum ATTRIBUTE {

        //Header,
        //MessageType,
        //BITMAP,
        PrimaryAccountNumber,
        //ProcessingCode
        DateOfExpired,
        CardSN,
        Track2,
        Track3,
        CurrencyCode,
        Amount,
        PinBlock,
        ServiceCode,
        TerminalID,
        MerchantID,
        Time,
        Date,
        Balance,

    }

    protected int [][] attribute_array = null;

    public ISO8583()
    {
        Log.d(TAG, "Constructor");
        ISO_BIT_MAX = getISOBITMAX();
        validField = new boolean[ISO_BIT_MAX +1];
        Arrays.fill(validField, false);
        allField = new byte[ISO_BIT_MAX +1][];
        Arrays.fill(allField, null);
    }

    protected int getISOBITMAX() {
        return 64;
    }

    protected abstract byte[] calculateMac( byte[] packet, int offset, int length );

    protected abstract int getHeaderLen();

    /**
     * \brief set the field of ISO data refer the format #attribute_array
     *
     * //@param field the field index, 0 for message type, 1 for bitmap(bitmap should not be set manually)
     * //@param value the readable String value in ASC/BCD format for the field
     * //@return the field value, include the length header (refer the format ), null for invalid value given.
     *
     * */
    public static class ISO8583_HEADER
    {
        public static String Header="";
        public static String MTI="";
        public static String BITMAP="";
        public static String STAN="";
    }
    public static class ISO8583_FIELD
    {
        //*****************************
        // ISO 8583 VARIABLES
        public static String message_id="";
        public static String field_02="";    /* Primary_Account_Number */
        public static String field_03="";    /* Processing_Code */
        public static String field_06="";   /* Amount card holder billing */
        public static String field_04="";   /* Amount_Transaction */
        public static String field_05="";    /* Amount_Settlement */
        /* skip 06 	/* Amount_Cardholder_Billing */
        public static String field_07="";    /*Transmission_Date_and_Time */
        public static String field_08="";    /* Processing_Code */
        /* skip 08 	/* Amount_Cardholder_Billing_Fee */
        public static String field_09="";    /* Conversion_Rate_Settlement */
        public static String field_10="";   /* Processing_Code */
        /* skip 10 	/* Conversion_Rate_Cardholder_Billing */
        public static String field_11="";    /* Systems_Trace_Audit_Number */
        public static String field_12="";    /* Time_Local_Transaction */
        public static String field_13="";    /* Date_Local_Transaction */
        public static String field_14="";    /* Date_Expiration */
        public static String field_15="";    /* Date Settlement */
        public static String field_16="";    /* Date_Conversion */
        public static String field_17="";    /* Date_Capture */
        public static String field_18="";    /* Merchants Type */
        public static String field_19="";    /* Acquiring_Institution_Country_Code */
        public static String field_20="";    /* Primary_Account_Number_* Extended_Country_Code 	*/
        public static String field_21=""; /* Forwarding_Institution_Country_Code */
        public static String field_22=""; /* Point_of_Service_Entry_Mode */
        public static String field_23=""; /* Card_Sequence_Number */
        public static String field_24=""; /* Network_International_Identifier */
        public static String field_25=""; /* Point_of_Service_Condition_Code */
        public static String field_26=""; /* Point_of_Service_PIN_Capture_Code */
        public static String field_27=""; /* Authorization_Identification_* Response_Length */
        public static String field_28=""; /* Amount_Transaction_Fee */
        public static String field_29=""; /* Amount_Settlement_Fee */
        public static String field_30="";    /* Additional amount */
        public static String field_31="";    /* Amount settlement processsinf fee */
        public static String field_32=""; /* Acquiring_Institution_ID_Code */
        public static String field_33=""; /* Forwarding_Institution_ID_Code */
        public static String field_34=""; /* Primary_Account_Number_Extended */
        public static String field_35=""; /* Track_2_Data */
        public static String field_36="";    /* Track 3 data */
        public static String field_37=""; /* Retrieval_Reference_Number */
        public static String field_38=""; /* Authorization_Identification_Response*/
        public static String field_39=""; /* Response_Code */
        public static String field_40="";    /* Service restriction Code */
        public static String field_41=""; /* Card_Acceptor_Terminal_Identification*/
        public static String field_42=""; /* Card_Acceptor_Identification_Code */
        public static String field_43=""; /* Card_Acceptor_Name_Location */
        public static String field_44=""; /* Card_Acceptor_Name_Location */
        public static String field_45="";    /* Processing_Code */
        public static String field_46="";    /* Processing_Code */
        public static String field_47="";     /* Processing_Code */
        public static String field_48="";     /* Processing_Code */

        public static String field_49="";; /* Currency_Code_Transaction */
        public static String field_50="";; /* Currency_Code_Settlement */
        public static String field_51="";    /* Currency code */
        public static String field_53="";    /* Processing_Code */
        public static String field_54=""; /* Additional_Amounts */
        public static String field_55="";    /* ICC */
        public static String field_56="";   /* Processing_Code */
        public static String field_57="";    /* Processing_Code */
        public static String field_58="";    /* Processing_Code */
        public static String field_59="";    /* Processing_Code */
        public static String field_60="";    /* Processing_Code */
        public static String field_61="";
        public static String field_62="";   /* Processing_Code */
        public static String field_63="";    /* Processing_Code */

        public static String field_64=""; /* Message_Authentication_Code_Field */

        public static String field_65="";    /* Processing_Code */
        public static String field_66="";    /* Processing_Code */
        public static String field_67="";    /* Processing_Code */
        public static String field_68="";    /* Processing_Code */
        public static String field_69="";    /* Processing_Code */
        public static String field_70="";    /* Processing_Code */
        public static String field_71=""; /* Message number */
        public static String field_72=""; /* Message number last */
        public static String field_73="";    /* Processing_Code */
        public static String field_74="";    /* Processing_Code */
        public static String field_75="";    /* Processing_Code */
        public static String field_76="";    /* Processing_Code */
        public static String field_77="";    /* Processing_Code */
        public static String field_78= "";     /* Processing_Code */
        public static String field_79= "";     /* Processing_Code */
        /* skip 73.. 79 */
        public static String field_80= "";  /* Inquiries number */
        public static String field_81= "";  /* Authorizations number */
        public static String field_82= "";     /* Processing_Code */
        public static String field_83= "";     /* Processing_Code */
        public static String field_84= "";     /* Processing_Code */
        public static String field_85= "";    /* Processing_Code */
        public static String field_86= "";     /* Processing_Code */
        public static String field_87= "";     /* Processing_Code */
        public static String field_88= "";     /* Processing_Code */
        public static String field_89= "";     /* Processing_Code */
        public static String field_90= "";     /* Processing_Code */
        public static String field_91= "";     /* Processing_Code */
        public static String field_92= "";     /* Processing_Code */
        public static String field_93= "";     /* Processing_Code */
        public static String field_94= "";    /* Processing_Code */
        public static String field_95= "";     /* Processing_Code */
        public static String field_96= "";     /* Processing_Code */
        public static String field_97= "";     /* Processing_Code */
        public static String field_98= "";     /* Processing_Code */
        public static String  field_99= "";    /* Processing_Code */
        public static String field_100 = "";    // Account Identification 1
        public static String field_101 = "";    // Account Identification 2
        public static String field_102 = "";    // Account Identification 3
        public static String field_103 = "";    // Account Identification 4
        public static String field_104 = "";    // Account Identification 5
        public static String field_105 = "";    // Account Identification 6
        public static String field_106 = "";    // Account Identification 7
        public static String field_107 = "";    // Account Identification 8
        public static String field_108 = "";    // Account Identification 9
        public static String field_109 = "";    // Account Identification 10
        public static String field_110 = "";    // Account Identification 11
        public static String field_111 = "";    // Account Identification 12
        public static String field_112 = "";    // Account Identification 13
        public static String field_113 = "";    // Account Identification 14
        public static String field_114 = "";    // Account Identification 15
        public static String field_115 = "";    // Account Identification 16
        public static String field_116 = "";    // Account Identification 17
        public static String field_117 = "";    // Account Identification 18
        public static String field_118 = "";    // Account Identification 19
        public static String field_119 = "";    // Account Identification 20
        public static String field_120 = "";    // Account Identification 21
        public static String field_121 = "";    // Account Identification 22
        public static String field_122 = "";    // Account Identification 23
        public static String field_123 = "";    // Account Identification 24
        public static String field_124 = "";    // Account Identification 25
        public static String field_125 = "";    // Account Identification 26
        public static String field_126 = "";    // Account Identification 27
        public static String field_127 = "";    // Account Identification 28
        public static String field_128 = "";    // Account Identification 29

// Skipping fields 82 to 134

        public static String field_135=""; // Test Field 1
        public static String field_136=""; // Test Field 2

// Skipping fields 137 to 143

        public static String field_144=""; // Test Field 3
        public static String field_145=""; // Test Field 4


        //..............................Response Fields................................................................................
        public static String r_field_02="";    // Primary_Account_Number
        public static String r_field_03="";    // Processing_Code
        public static String r_field_06="";    // Amount card holder billing
        public static String r_field_04="";    // Transaction Transaction
        public static String r_field_11="";    // STAN
        public static String r_field_12="";    // Local Transaction Time (hhmmss)
        public static String r_field_13="";    // Local Transaction Date (MMDD)
        public static String r_field_14="";    // Expiration Date of Card (YYMM)
        public static String r_field_23="";    // Card_Sequence_Number
        public static String r_field_24="";    // Network_International_Identifier
        public static String r_field_25="";    // Point_of_Service_Condition_Code
        public static String r_field_37="";    // Placeholder for future use
        public static String r_field_38="";    // App code
        public static String r_field_39="";    // Response code
        public static String r_field_41="";    // Card_Acceptor_Terminal_Identification
        public static String r_field_42="";    // Card_Acceptor_Identification_Code
        public static String r_field_55="";    // Integrated Circuit Card (ICC) System Related Data
        public static String r_field_62="";    // Customer Defined Response

    }


    public byte[] setField( int field, String value)
    {
        Log.d(TAG, "setField, field="+field+", value=" + value );
        if( field==55)
        {
            //Transactiondata.ISO8583_FIELD.field_55=value;
           // Log.e(TAG, "field_55 value iso8583: " + Transactiondata.ISO8583_FIELD.field_55);
        }

        if( field > ISO_BIT_MAX)
        {
            return null;
        }

        if( null == attribute_array)
        {
            Log.e(TAG, "no attribute_array set!");
            return null;
        }
        if( value.length() == 0 )
        {
            Log.e(TAG, "invalid value(len:0) of field:" + field);
            return null;
        }
        byte[] bytes;
        int len;
        int wantLen = attribute_array[field][ATTR_INDEX_LEN_DEFAULT];
        int type = attribute_array[field][ATTR_INDEX_TYPE];
        String trimmedValue;
        if( 0 != (type & TYPE_BCD) )
        {
            // is bcd
            trimmedValue = value.replace(" ", "");
            len = trimmedValue.length();

            if( len < wantLen ) {
                if (TYPE_FILL_SPACE_RIGHT == (type & TYPE_FILL_SPACE_RIGHT) ) {

                } else if (TYPE_FILL_SPACE_LEFT == (type & TYPE_FILL_SPACE_LEFT) ) {

                } else if (TYPE_FILL_ZERO_RIGHT == (type & TYPE_FILL_ZERO_RIGHT) ) {

                } else if (TYPE_FILL_ZERO_LEFT == (type & TYPE_FILL_ZERO_LEFT) ) {
                    Log.d(TAG, "reset value for FILL 0 left");
                    char fill[] = new char[wantLen-len];
                    Arrays.fill(fill, '0' );
                    trimmedValue = String.copyValueOf( fill) + trimmedValue;
                    len = trimmedValue.length();

                    Log.d(TAG, "after fill:" + trimmedValue );

                    B_Amount1=trimmedValue;
                    Log.d(TAG, "B_Amount1............"+B_Amount1);
                }
            }

            bytes = Utility.hexStr2Byte( trimmedValue );
        } else if( 0 != (attribute_array[field][ATTR_INDEX_TYPE] & TYPE_ASC) )
        {
            // is ASC
            len = value.length();
            bytes = value.getBytes();
        } else if( 0 != (attribute_array[field][ATTR_INDEX_TYPE] & TYPE_BIN) )
        {
            // is bin
            trimmedValue = value.replace(" ", "");
            len = trimmedValue.length();
            len /= 2;
            bytes = Utility.hexStr2Byte( trimmedValue );
        } else {
            Log.e( TAG, "type " + Integer.toHexString(attribute_array[field][ATTR_INDEX_TYPE]) + " invalided" );
            validField[field] = false;

            return null;
        }

        return setField( field, bytes, len );
    }

    /**
     * \brief set the ISO field given byte , will insert the length refer the  format #attribute_array
     *
     * @param field the field index, 0 for message, 1 for bitmap (should be set automatic)
     * @param value the byte value of the field, will insert the length refer the  format #attribute_array
     * @return the field value, null for invalid value
     */
    public byte[] setField( int field, byte[] value, int len)
    {
        if( field > ISO_BIT_MAX)
        {
            Log.e(TAG, "field > ISO_BIT_MAX!");
            return null;
        }

        if( null == attribute_array)
        {
            Log.e(TAG, "no attribute_array set!");
            return null;
        }
//        String valueTrimed = new String(value);
        byte[] retBytes = null;

        int wantLen = attribute_array[field][ATTR_INDEX_LEN_DEFAULT];
        int type = attribute_array[field][ATTR_INDEX_TYPE];

        byte[] lenHeader = new byte[2];
        if( 0 != (type & TYPE_LEN_LEN ) )
        {
            Log.d(TAG, "herrrrrrrrrr ..............1 ");
            retBytes = value;
             //lenHeader[0] = Utility.HEX2DEC(len/100);
             // lenHeader[1] = Utility.HEX2DEC(len%100);
             //retBytes = new byte[value.length+2];
             //System.arraycopy( lenHeader, 0, retBytes, 0, 2);
             //System.arraycopy( value, 0, retBytes, 2, value.length);


        } else if( 0 != (type & TYPE_LEN ) )
        {
            Log.d(TAG, "herrrrrrrrrr............... 2");
            retBytes = value;
            //lenHeader[0] = Utility.HEX2DEC(len);
            //retBytes = new byte[value.length+1];
            //System.arraycopy(lenHeader, 0, retBytes, 0, 1);
            //System.arraycopy( value, 0, retBytes, 1, value.length );
            //System.arraycopy( value, 0, retBytes, 1, 0);

        } else if( len == wantLen )
        {
            Log.d(TAG, "herrrrrrrrrr len = wantLen................ 3");
            retBytes = value;
        } else if( 0 != (type & TYPE_FILL_) )
        {
            Log.d(TAG, "reset value for FILL type, current len:" + len + ", want:" + wantLen);
            if( len < wantLen )
            {
                if (TYPE_FILL_SPACE_RIGHT == (type & TYPE_FILL_SPACE_RIGHT) ) {

                } else if (TYPE_FILL_SPACE_LEFT == (type & TYPE_FILL_SPACE_LEFT) ) {

                } else if (TYPE_FILL_ZERO_RIGHT == (type & TYPE_FILL_ZERO_RIGHT) ) {

                } else if (TYPE_FILL_ZERO_LEFT == (type & TYPE_FILL_ZERO_LEFT) ) {
                    Log.d(TAG, "reset value for FILL 0 left");
                    retBytes = new byte[wantLen/2];
                    Arrays.fill(retBytes, (byte) 0);
                    System.arraycopy(value,0,retBytes,(wantLen-len)/2, len/2);
                    Log.d(TAG, "before:" + Utility.byte2HexStr(value) + ", after:" + Utility.byte2HexStr(retBytes) );
                    B_Amount1=Utility.byte2HexStr(retBytes);
                    Log.d(TAG, "B_Amount1............"+B_Amount1);
                }
            }
        } else {
            Log.d(TAG, "Ameee herrrrrrrrrr ............... 4");
            retBytes = null;
        }


        if( retBytes == null )
        {
            retBytes = value;
            //Log.e( TAG, "type " + Integer.toHexString(attribute_array[field][ATTR_INDEX_TYPE]) + ", len " + value.length + " of field: " + field+ " invalid :" + wantLen );
            //validField[field] = false;

           allField[field] = retBytes;
            validField[field] = true;

            //Log.d( TAG, "save field:" + field + ", type " +  Integer.toHexString(attribute_array[field][ATTR_INDEX_TYPE]) + ", len:" + value.length + ", value:" + Utility.byte2HexStr(retBytes) );
            //String s = new String(retBytes, StandardCharsets.UTF_8);

            Log.d( TAG, "save field.....1 :" + field + ", type " +  Integer.toHexString(attribute_array[field][ATTR_INDEX_TYPE]) + ", len:" + value.length + ", value:" + Utility.byte2HexStr(retBytes) );

        }
        else {

            allField[field] = retBytes;
            validField[field] = true;
         String s = new String(retBytes, StandardCharsets.UTF_8);

         Log.d( TAG, "save field....2 :" + field + ", type " +  Integer.toHexString(attribute_array[field][ATTR_INDEX_TYPE]) + ", len:" + value.length + ", value:" + Utility.byte2HexStr(retBytes) );

        }

        return retBytes;
    }

    public byte[] setField1( int field, byte[] value, int len)
    {
        if( field > ISO_BIT_MAX)
        {
            Log.e(TAG, "field > ISO_BIT_MAX!");
            return null;
        }

        if( null == attribute_array)
        {
            Log.e(TAG, "no attribute_array set!");
            return null;
        }
//        String valueTrimed = new String(value);
        byte[] retBytes = null;

        int wantLen = attribute_array[field][ATTR_INDEX_LEN_DEFAULT];
        int type = attribute_array[field][ATTR_INDEX_TYPE];

        byte[] lenHeader = new byte[2];
        if( 0 != (type & TYPE_LEN_LEN ) )
        {
            lenHeader[0] = Utility.HEX2DEC(len/100);
            lenHeader[1] = Utility.HEX2DEC(len%100);
            retBytes = new byte[value.length+2];
            System.arraycopy( lenHeader, 0, retBytes, 0, 2);
            System.arraycopy( value, 0, retBytes, 2, value.length);


        } else if( 0 != (type & TYPE_LEN ) )
        {
            lenHeader[0] = Utility.HEX2DEC(len);
            retBytes = new byte[value.length+1];
            System.arraycopy(lenHeader, 0, retBytes, 0, 1);
            System.arraycopy( value, 0, retBytes, 1, value.length );

        } else if( len == wantLen )
        {

            retBytes = value;
        } else if( 0 != (type & TYPE_FILL_) )
        {
            Log.d(TAG, "reset value for FILL type, current len:" + len + ", want:" + wantLen);
            if( len < wantLen )
            {
                if (TYPE_FILL_SPACE_RIGHT == (type & TYPE_FILL_SPACE_RIGHT) ) {

                } else if (TYPE_FILL_SPACE_LEFT == (type & TYPE_FILL_SPACE_LEFT) ) {

                } else if (TYPE_FILL_ZERO_RIGHT == (type & TYPE_FILL_ZERO_RIGHT) ) {

                } else if (TYPE_FILL_ZERO_LEFT == (type & TYPE_FILL_ZERO_LEFT) ) {
                    Log.d(TAG, "reset value for FILL 0 left");
                    retBytes = new byte[wantLen/2];
                    Arrays.fill(retBytes, (byte) 0);
                    System.arraycopy(value,0,retBytes,(wantLen-len)/2, len/2);
                    Log.d(TAG, "before:" + Utility.byte2HexStr(value) + ", after:" + Utility.byte2HexStr(retBytes) );
                    B_Amount1=Utility.byte2HexStr(retBytes);
                    Log.d(TAG, "B_Amount1............"+B_Amount1);
                }
            }
        } else {
            retBytes = null;
        }

     if( retBytes == null )
        {
            retBytes = value;
            //Log.e( TAG, "type " + Integer.toHexString(attribute_array[field][ATTR_INDEX_TYPE]) + ", len " + value.length + " of field: " + field+ " invalid :" + wantLen );
            //validField[field] = false;

           allField[field] = retBytes;
            validField[field] = true;

            //Log.d( TAG, "save field:" + field + ", type " +  Integer.toHexString(attribute_array[field][ATTR_INDEX_TYPE]) + ", len:" + value.length + ", value:" + Utility.byte2HexStr(retBytes) );
            //String s = new String(retBytes, StandardCharsets.UTF_8);

            Log.d( TAG, "save field:" + field + ", type " +  Integer.toHexString(attribute_array[field][ATTR_INDEX_TYPE]) + ", len:" + value.length + ", value:" + Utility.byte2HexStr(retBytes) );

        }
        /*else if( field == 55 )
        {


         allField[field] = retBytes;
         String s = new String(retBytes, StandardCharsets.UTF_8);
         validField[field] = true;

         //Log.d( TAG, "save field:" + field + ", type " +  Integer.toHexString(attribute_array[field][ATTR_INDEX_TYPE]) + ", len:" + value.length + ", value:" + Utility.byte2HexStr(retBytes) );


         Log.d( TAG, "save field ss:" + field + ", type " +  Integer.toHexString(attribute_array[field][ATTR_INDEX_TYPE]) + ", len:" + value.length + ", value:" + s );

        }

         */
        else {

            allField[field] = retBytes;
            validField[field] = true;
         String s = new String(retBytes, StandardCharsets.UTF_8);

         Log.d( TAG, "save field:" + field + ", type " +  Integer.toHexString(attribute_array[field][ATTR_INDEX_TYPE]) + ", len:" + value.length + ", value:" + Utility.byte2HexStr(retBytes) );

        }

        return retBytes;
    }



    public byte[] getField(int field)
    {
        if (null == allField[field])
        {
            return null;
        }
        int len = allField[field].length;
        byte[] tmp;

        if (0 != (attribute_array[field][ATTR_INDEX_TYPE] & TYPE_LEN_LEN)) {
            tmp = new byte[len - 2];
            System.arraycopy(allField[field], 2, tmp, 0, len - 2);
        } else if (0 != (attribute_array[field][ATTR_INDEX_TYPE] & TYPE_LEN)) {
            tmp = new byte[len - 1];
            System.arraycopy(allField[field], 1, tmp, 0, len - 1);
        } else {
            tmp = allField[field];
        }
        return tmp;
    }

    public byte[] getPacket( String header, String tail, PACKET_TYPE type) {
        byte[] h;
        byte[] t;
        if( null != header ){
            h = Utility.hexStr2Byte(header);
        } else {
            h = null;
        }

        if( null != tail ){
            t = Utility.hexStr2Byte(tail);
        } else {
            t = null;
        }

        return getPacket( h, t, type);
    }
    public byte[] getPacket( byte[] header, byte[] tail, PACKET_TYPE type)
    {
        byte[] tmp = new byte[4096];
        byte[] bitmap;
        int offset = 0;
        int len = 0;
        if( null != header )
        {
            //Log.d( TAG, "TMP... "+Utility.byte2HexStr(tmp));
           // len = header.length;
            //System.arraycopy( header, 0 ,tmp, 0, len );
            offset += len ;
        }
       // Log.d( TAG, "TMP...2 "+Utility.byte2HexStr(tmp));
        if( null != emvtlvF55 )
        {
            //
            //setField(55, emvtlvF55.getTlvString());
            Log.d(TAG, "emvtlvF55.getTlvString() :" +emvtlvF55.getTlvString() );

            int len55=emvtlvF55.getTlvString().length();
          // String len55=String.valueOf(emvtlvF55.getTlvString());
            Log.d(TAG, "Field55 length..:" +len55);

            // Log.d(TAG, "Total length F55........... :"+len55);
            // String leng =Integer.toHexString(len55);
            len55=len55/2;
            String leng55=Integer.toString(len55);
            //String leng55 =Integer.toHexString(len55/2);

            if(leng55.length()==2)
            {
                leng55="00"+leng55;

            }else  if(leng55.length()==3)
              {
                leng55="0"+leng55;

            } if(leng55.length()==4)
               {

              }

            Log.d(TAG, "lenhex F55........... :"+leng55);
            Log.d(TAG, "F55 length to hex........... :"+Utility.byte2HexStr(leng55.getBytes()));
            Log.e(TAG, "Total_value of filed_55 : " + leng55 + emvtlvF55.getTlvString());

           //setField(55, leng55 + emvtlvF55.getTlvString());
           // setField(55,  ISO8583.ISO8583_FIELD.field_55);



        }
        // validField
        if( false == validField[1] )
        {
            Log.d(TAG, "calculate the bitmap, size:" + ISO_BIT_MAX );

            //bitmap = new byte[ISO_BIT_MAX>>3];
            bitmap= Utility.hexStr2Byte(ISO8583_HEADER.BITMAP);
           /* for( int i=1; i <= ISO_BIT_MAX; i++ )
            {
                if( validField[i] )
                {
                    bitmap[(i - 1) >> 3 ] |=  1 ;
                }
                if( 0 != ( i & 0x07) )
                {
                    bitmap[(i - 1) >> 3] = (byte) (bitmap[(i - 1) >> 3] << 1);
                }
            }

            */
            Log.d(TAG, "Txn_Menu_Type..........  " +TransBasic.Txn_Menu_Type);
            Log.d(TAG, "TXn_Type..........  " +TransBasic.Txn_type);
            daterecit=new SimpleDateFormat("yy/MM/dd", Locale.getDefault()).format(new Date());
            timereciet=new SimpleDateFormat("HH:mm:ss ", Locale.getDefault()).format(new Date());
            Log.d(TAG, "daterecit "+ daterecit);

            String date=new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date()),
                    time=new SimpleDateFormat("HH:mm:ss aa", Locale.getDefault()).format(new Date());
  //..........................Filed 12 Transaction time.......................
            Current_Time =""+time.charAt(0)+time.charAt(1)+time.charAt(3)+time.charAt(4)+time.charAt(6)+time.charAt(7);;
            Log.d(TAG, "Current_Time  "+Current_Time);
  //..........................Filed 13 Transaction date........................
            Current_Date =""+date.charAt(8)+date.charAt(9)+date.charAt(3)+date.charAt(4)+date.charAt(0)+date.charAt(1);
            Log.d(TAG, "Current_Date  "+Current_Date);

            Log.d(TAG, "date "+ date);
  //............................Header..........................................
            ISO8583_HEADER.Header="30606020153535";
            Log.d(TAG, "Header "+ ISO8583_HEADER.Header);


  //.............................MTI.............................................
            ISO8583_HEADER.MTI="0200";
            Log.d(TAG, "MTI "+ ISO8583_HEADER.MTI);


  //............................BITMAP...........................................
            if(TransBasic.isonline)
            {

                //GBE BITMAP If Online PIN Entered
                //ISO8583_HEADER.BITMAP="7234058020C09200";

                ISO8583_HEADER.BITMAP="7024058020C01204";
                Log.d(TAG, "BITMAP_online..........  " +  ISO8583.ISO8583_HEADER.BITMAP);
                TransBasic.isonline=false;
            }
            else
            {
                //GBE BITMAP If Offline PIN Entered
                //ISO8583_HEADER.BITMAP="7234058020C08200";

                ISO8583_HEADER.BITMAP="7024058020C00204";
                Log.d(TAG, "BITMAP_offline..........  " +  ISO8583_HEADER.BITMAP);
                TransBasic.isonline=false;
            }

            bitmap= ISO8583_HEADER.BITMAP.getBytes();
            Log.d(TAG, "Header "+ISO8583_HEADER.Header +"  MTI "+ ISO8583_HEADER.MTI);

            // Field 03  Prossesing code
            ISO8583_FIELD.field_03="000000";
            Log.d(TAG, "field_03 :" +  ISO8583_FIELD.field_03);

            // Field 22 Point of Service code
            // field_22 = "051" //for GBE
            if(TransBasic.Txn_Menu_Type.equals("ContactLess"))
            {
                ISO8583_FIELD.field_22="0071"; // 051 for GBE
                Log.d(TAG, "field_22 :" + ISO8583_FIELD.field_22);
            }else
            {
                ISO8583_FIELD.field_22="0051";// 051 for GBE
                Log.d(TAG, "field_22 :" + ISO8583_FIELD.field_22);
            }

            // Field 24 Function Code
            // field_24 ="200" For GBE
            ISO8583_FIELD.field_24="0001"; // 200 For GBE
            Log.d(TAG, "field_24 :" + ISO8583_FIELD.field_24);

            // Field 25 Message Reason Code
            // field_24 ="00" For GBE
            ISO8583_FIELD.field_25="00"; // 00 For GBE
            Log.d(TAG, "field_25 :" + ISO8583_FIELD.field_25);


//............................For Transaction type Purchase/Sale ......................................
            if(TransBasic.Txn_type.equals("PURCHASE"))
            {
                setField(0, ISO8583_HEADER.Header );   // Header
                setField(1, ISO8583_HEADER.MTI);       // MTI
                setField(2, ISO8583_HEADER.BITMAP);    // Bitmap (If Online PIN Entered: 7234058020C09200 for GBE, If Offline PIN Entered: 7234058020C08200 for GBE)
                setField(3, ISO8583_FIELD.field_02);   // Card Number(PAN)
                setField(4, ISO8583_FIELD.field_03);   // Processing code (000000 for GBE)
                setField(5, ISO8583_FIELD.field_04);   // Transaction Amount
                setField(6, ISO8583_FIELD.field_11);   // System Audit Trace Number(STAN)
                setField(7, ISO8583_FIELD.field_14);   // Expiry Date
                setField(8, ISO8583_FIELD.field_22);   // Point of Service Code (051 for GBE)
                setField(9, ISO8583_FIELD.field_24);   // Function Code (200 for GBE)
                setField(10, ISO8583_FIELD.field_25);  // Message Reason Code (00 for GBE)
                setField(11, ISO8583_FIELD.field_35);  // Track 2 Data
                setField(12, ISO8583_FIELD.field_41);  // Terminal ID
                setField(13, ISO8583_FIELD.field_42);  // Merchant ID
                setField(14, ISO8583_FIELD.field_55);  // EMV Data
                setField(15, ISO8583_FIELD.field_62);  // Customer Defined Response
            }

//............................For Transaction type Reversal ......................................
            else if(TransBasic.Txn_type.equals("REVERSAL"))
            {
                ISO8583_HEADER.BITMAP="703C05802EC00014";

                ISO8583_FIELD.field_03="020000";
                Log.d(TAG, "processing_code "+ISO8583_FIELD.field_03);

                setField(0, ISO8583_HEADER.Header);    // Header of the ISO 8583 message
                setField(1, ISO8583_HEADER.MTI);       // Message Type Indicator
                setField(2, ISO8583_HEADER.BITMAP);    // Bitmap indicating which data elements are present
                setField(3, ISO8583_FIELD.field_02);   // Primary Account Number (PAN)
                setField(4, ISO8583_FIELD.field_03);   // Processing Code
                setField(5, ISO8583_FIELD.field_04);   // Amount, Transaction
                setField(6, ISO8583_FIELD.field_11);   // Systems Trace Audit Number (STAN)
                setField(7, ISO8583_FIELD.field_12);   // Time, Local Transaction (hhmmss)
                setField(8, ISO8583_FIELD.field_13);   // Date, Local Transaction (MMDD)
                setField(9, ISO8583_FIELD.field_14);   // Date, Expiration (YYMM)
                setField(10, ISO8583_FIELD.field_22);  // Point of Service Entry Mode
                setField(11, ISO8583_FIELD.field_24);  // Function Code
                setField(12, ISO8583_FIELD.field_25);  // Message Reason Code
                setField(13, ISO8583_FIELD.field_35);  // Track 2 Data
                setField(14, ISO8583_FIELD.field_37);  // Retrieval Reference Number (RRN)
                setField(15, ISO8583_FIELD.field_38);  // Authorization Identification Response (Auth ID)
                setField(16, ISO8583_FIELD.field_39);  // Response Code
                setField(17, ISO8583_FIELD.field_41);  // Card Acceptor Terminal Identification (TID)
                setField(18, ISO8583_FIELD.field_42);  // Card Acceptor Identification Code (MID)
                setField(19, ISO8583_FIELD.field_60);  // Additional Data (such as transaction details)
                setField(20, ISO8583_FIELD.field_62);  // Customer Identification (Additional customer-related data)


            }

//............................For Transaction type Settlement ......................................
        else if(TransBasic.Txn_Menu_Type.equals("SETTLEMENT"))
        {
            Log.d(TAG, "Txn_Menu_Type.. "+TransBasic.Txn_Menu_Type);
            ISO8583_HEADER.Header = "30606000000000";    // Header of the ISO 8583 message
            ISO8583_HEADER.BITMAP = "2020010000C00016";  // Bitmap indicating which data elements are present
            ISO8583_HEADER.MTI = "0500";                 // Message Type Indicator
            ISO8583_FIELD.field_03 = "960000";           // Processing Code
            ISO8583_FIELD.field_22 = "";                 // Point of Service Code
            ISO8583_FIELD.field_25 = "";                 // Message Reason Code
            ISO8583_FIELD.field_24 = "0001";             // Function Code
            ISO8583_FIELD.field_04 = "";                 // Transaction Amount

            setField(0, ISO8583_HEADER.Header);     // Set the Header field
            setField(1, ISO8583_HEADER.MTI);        // Set the Message Type Indicator field
            setField(2, ISO8583_HEADER.BITMAP);     // Set the Bitmap field
            setField(3, ISO8583_FIELD.field_03);    // Set the Processing Code field
            setField(11, ISO8583_FIELD.field_11);   // Set the Systems Trace Audit Number (STAN) field
            setField(12, ISO8583_FIELD.field_24);   // Set the Function Code field
            setField(13, ISO8583_FIELD.field_41);   // Set the Card Acceptor Terminal Identification (TID) field
            setField(14, ISO8583_FIELD.field_42);   // Set the Card Acceptor Identification Code (MID) field
            setField(15, ISO8583_FIELD.field_60);   // Set the Additional Data field
            setField(16, ISO8583_FIELD.field_62);   // Set the Customer Identification field
            setField(17, ISO8583_FIELD.field_63);   // Set the Client Debts Data

        }

//............................For Transaction type Pre Auth ......................................
        else if(TransBasic.Txn_type.equals("PRE_AUTH"))
        {
            ISO8583_HEADER.BITMAP="7024058020C01204";
            ISO8583_HEADER.MTI="0100";
            ISO8583_FIELD.field_03="300000";
            setField(0, ISO8583_HEADER.Header);
            setField(1, ISO8583_HEADER.MTI);
            setField(2, ISO8583_HEADER.BITMAP);
            setField(3, ISO8583_FIELD.field_03);

            }

//............................For Transaction type Settlement ......................................
            else  if(TransBasic.Txn_type.equals("PRE_AUTH_COMPLETION"))
            {


            }

//...........................................................
            else if(TransBasic.Txn_type.equals("download"))
            {
                Log.d(TAG, "Txn_Type.. "+TransBasic.Txn_type);

                ISO8583_HEADER.Header="00566020153535";   // Header
                ISO8583_HEADER.BITMAP="2020010000C00004"; // Bitmap
                ISO8583_HEADER.MTI="0800";                // Message Type Indicator
                ISO8583_FIELD.field_03="990000";          // Processing Code
                ISO8583_FIELD.field_22="";                // Point of Service Code
                ISO8583_FIELD.field_25="";                // Message Reason Code
                ISO8583_FIELD.field_24="0001";            // Function Code
                ISO8583_FIELD.field_04="";                // Transaction Amount
                setField(0, ISO8583_HEADER.Header);
                setField(1, ISO8583_HEADER.MTI);
                setField(2, ISO8583_HEADER.BITMAP);
                setField(3, ISO8583_FIELD.field_03);  // Processing code
                setField(11, ISO8583_FIELD.field_11); // STAN
                setField(12, ISO8583_FIELD.field_24); // Function Code
                setField(13, ISO8583_FIELD.field_41); // Terminal ID
                setField(14, ISO8583_FIELD.field_42); // Merchant ID
                setField(15, ISO8583_FIELD.field_62); // Customer Defined Response


            }

//............................For Transaction type Cash Advance......................................
            else if(TransBasic.Txn_type.equals("CASH_ADVANCE"))
            {
                ISO8583_FIELD.field_03="010000";

            }

//............................For Transaction type Refund ......................................
            else if(TransBasic.Txn_type.equals("REFUND"))
            {

                ISO8583_FIELD.field_03="200000";

            }

//...........................For Transaction type Balance INQ ......................................
            else if(TransBasic.Txn_type.equals("BALANCE_INQUIRY"))
            {

                ISO8583_HEADER.MTI="0400";
                ISO8583_FIELD.field_03="310000";
                Log.d(TAG, "processing_code "+ISO8583_FIELD.field_03);


            }

//...........................For Transaction type Purchase with Cash Back ......................................
            else if(TransBasic.Txn_type.equals("PURCHASE_WITH_CASHBACK"))
            {


            }

//...........................For Transaction type Deposit ......................................
            else if(TransBasic.Txn_type.equals("DEPOSIT"))
            {


            }
            else {


            }
        }
        Log.d( TAG, "Add off_set length to tields ");
        for( int i = 0; i<= ISO_BIT_MAX; i++ )
        {
            if( validField[i] )
            {
                len = allField[i].length;
                System.arraycopy( allField[i], 0 ,tmp, offset, len);
                offset += len;

                Log.d( TAG, "set field " + i + ", len:" + len + ", value:" + Utility.byte2HexStr(allField[i]) );
              //  Log.d( TAG, "eachloop " + Utility.byte2HexStr(tmp) );

            } else {
            }
        }

        if( false == validField[ISO_BIT_MAX] && !TransBasic.Txn_type.equals("download") && !TransBasic.Txn_Menu_Type.equals("SETTLEMENT"))
        {
            byte[] mac = calculateMac( tmp, header.length, offset );

            if( null != mac )
            {
                Log.d(TAG, "get mac:" + Utility.byte2HexStr(mac));
                len = 8;
                System.arraycopy( mac, 0 ,tmp, offset, len );
                offset += len ;
            } else {
                Log.e(TAG, "calculate mac fails" );
            }
        }
       // Log.d( TAG, "eachloop1 " + Utility.byte2HexStr(tmp) );

       /* if( null != tail )
        {
         Log.d(TAG, "Amexxxxxx Ezeh negn........0");
            len = tail.length;
            System.arraycopy( tail, 0 ,tmp, 0, len );
            offset += len ;
        }*/
       // Log.d( TAG, "eachloop2 " + Utility.byte2HexStr(tmp) );

        //Log.d( TAG, "TMP...2 "+Utility.byte2HexStr(tmp));
        len = offset;

        if( type == PACKET_TYPE.PACKET_TYPE_HEXLEN_BUF )
        {
            Log.d(TAG, "Amexxxxxx Ezeh negn........01");
            len += 2;
            Log.d(TAG, "len.1..........."+len);
        }
        byte[] packet = new byte[len];
        if( type == PACKET_TYPE.PACKET_TYPE_NONE )
        {
            System.arraycopy(tmp, 0, packet, 0, offset );
        }
        else if( type == PACKET_TYPE.PACKET_TYPE_HEXLEN_BUF )//focus here..........
        {
            Log.d(TAG, "Amexxxxxx Packet....comeeeeeeeeeeee  ");

           System.arraycopy(tmp, 0, packet, 0, offset);
           //packet[0] = (byte)(offset/256);
           //packet[1] = (byte)(offset%256);

            Log.d(TAG, "Len:" + offset + " buf size: " + packet.length );
        } else {
            return null;
        }

        Log.d(TAG, "Packet  ......  "+Utility.byte2HexStr(packet));

        return packet;

    }

    public boolean unpack( byte[] packet ){
        return unpack(packet,0);
    }

    SparseArray<String> isoData;

    public String getUnpack( int field ){
        int a = field;
        if( a >= 200 ) {
            a -= 200;
        }
        if( null != unpackValidField ) {
            if (unpackValidField[a]) {
                return isoData.get(field);
            } else {
                return null;
            }
        } else {
            byte[] value = getField(field);
            if( null != value ){
                return Utility.byte2HexStr(value);
            }
        }
        return null;
    }

    public boolean unpack( byte[] packet, int offset )
    {
        int headerLen = getHeaderLen();
        int index = offset + headerLen; //offset=2
        int fieldOffset = 0;
        if(TransBasic.Txn_type.equals("download") || TransBasic.Txn_Menu_Type.equals("SETTLEMENT"))
        {
            Log.d(TAG, "Txn_type: unpack " + TransBasic.Txn_type);
            Log.d(TAG, "Txn_Menu_Type: unpack " + TransBasic.Txn_Menu_Type);
            index =0;
            headerLen=headerLen/2;
            index = offset + headerLen; //offset=2
        }
        Log.d(TAG, "index: " + index);
        Log.d(TAG, "offset: " + offset);
        Log.d(TAG, "headerLen: " + headerLen);
        Log.d(TAG, "Unpack Responce: " + Utility.byte2HexStr(packet));

        isoData = new SparseArray<>();
        unpackValidField = new boolean[ISO_BIT_MAX+1];

        Log.d(TAG, "message type:" + Utility.byte2HexStr(packet, index, 2));

        isoData.put(0, Utility.byte2HexStr( packet, index, 2) );
        unpackValidField[0] = true;
        index += 2;
        Log.d(TAG, "Index:" + index);
        //Log.d(TAG, "packet Hex: " + Utility.byte2HexStr(packet, index, 32));
        //Log.d(TAG, "packet Ascii :" + hexToASCII(Utility.byte2HexStr(packet, index, 32)));

        int fieldMark = 0;
        fieldOffset = index+8;
        --index;
        for( int field = 1; field<= ISO_BIT_MAX; field++ )
        {
            if(fieldMark == 0 )
            {
             Log.d(TAG, "fieldMark == 0............... ");
                ++index;
                fieldMark = 0x0080;
                Log.d(TAG, "bitmap: " +Integer.toHexString((int)packet[index]));
            }
            if( 0 !=(fieldMark & packet[index]))
            {
                //Log.d(TAG, "fieldMark !=0:..............:" + field);
                unpackValidField[field] = true;
                Log.d(TAG, "Mark Field:.................. " + field);
                int length = attribute_array[field][ATTR_INDEX_LEN_DEFAULT];

                if( 0 != (attribute_array[field][ATTR_INDEX_TYPE] & TYPE_LEN) )
                {
                    Log.d(TAG, "Ezih unpack...............1: "+ field);
                    // one byte length
                    //length = Utility.DEC2INT(packet[fieldOffset]);

                    length = Utility.DEC2INT(packet[fieldOffset]);
                    if(field==55|| field==60)
                    {
                        length = length * 2;
                    }
                    ++fieldOffset;

                 }
                 else if( 0 != (attribute_array[field][ATTR_INDEX_TYPE] & TYPE_LEN_LEN) )
                 {
                    Log.d(TAG, "Ezih unpack...............2: "+ field );
                    length = Utility.DEC2INT(packet[fieldOffset]);

                    length *= 100;
                    ++fieldOffset;
                    length += Utility.DEC2INT(packet[fieldOffset]);
                     if(field==55|| field==60 && !TransBasic.Txn_type.equals("download"))
                     {
                         Log.d(TAG, "Her spacial...............1");
                         length = length * 2;
                     }
                     if(field==62 && TransBasic.Txn_type.equals("download"))
                     {
                         Log.d(TAG, "Her spacial.............2");
                        length = length;
                     }
                    ++fieldOffset;
                }
                Log.d(TAG, "try read field:" + field + ", type:" + Integer.toHexString(attribute_array[field][ATTR_INDEX_TYPE]) + ", Length:" + length );

                if( 0 != (attribute_array[field][ATTR_INDEX_TYPE] & TYPE_BCD) )
                 {
                    Log.d(TAG, "set field...............1:,  "+ field);
                     Log.d(TAG, "Offset "+ fieldOffset+"  length  "+length);
                    // is bcd
                    if( 1 == (length & 1))
                    {
                        Log.d(TAG, "set field...............1&1:, "+ field);
                        ++length;
                    }
                    length = (length>>1);
                    isoData.put(field, Utility.byte2HexStr(packet, fieldOffset, length));
                    fieldOffset += length;

                }
                else if( 0 != (attribute_array[field][ATTR_INDEX_TYPE] & TYPE_ASC))
                {
                    Log.d(TAG, "set field...............2:  "+ field);
                    // is ASC
                    Log.d(TAG, "Offset "+ fieldOffset+"  length  "+length);

                   isoData.put(field, new String( packet, fieldOffset, length) );
                   isoData.put(field + 200, Utility.byte2HexStr(packet, fieldOffset, length));

                    fieldOffset += length;
                } else if( 0 != (attribute_array[field][ATTR_INDEX_TYPE] & TYPE_BIN) )
                {
                    Log.d(TAG, "set field...............3 :"+ field);
                    if( 1 == (length&1) )
                    {
                        ++length;
                    }

                    isoData.put(field, Utility.byte2HexStr(packet, fieldOffset, length));
                    fieldOffset += length;
                }

                Log.d(TAG, "set field:" + field + ", type:" + Integer.toHexString(attribute_array[field][ATTR_INDEX_TYPE]) + ", Length:" + length + ", value:" + isoData.get(field ));

                // Assign values
                if(field==2)
                {
                    ISO8583_FIELD.r_field_02 = isoData.get( field );
                    Log.d(TAG, "field2: " + ISO8583_FIELD.r_field_02);
                }
                else if(field==3)
                {
                    ISO8583_FIELD.r_field_03 = isoData.get( field );
                    Log.d(TAG, "field3: " + ISO8583_FIELD.r_field_03);
                }
                else if(field==4)
                {
                    ISO8583_FIELD.r_field_04 = isoData.get( field );
                    Log.d(TAG, "field4: " + ISO8583_FIELD.r_field_04);
                }
                else if(field==11)
                {
                    ISO8583_FIELD.r_field_11 = isoData.get( field );
                    Log.d(TAG, "field11: " + ISO8583_FIELD.r_field_11);
                }
                else if(field==12)
                {
                    ISO8583_FIELD.r_field_12 = isoData.get( field );
                    Log.d(TAG, "field12: " + ISO8583_FIELD.r_field_12);
                }
                else if(field==13)
                {
                    ISO8583_FIELD.r_field_13 = isoData.get( field );
                    Log.d(TAG, "field13: " + ISO8583_FIELD.r_field_13);
                }
                else if(field==14)
                {
                    ISO8583_FIELD.r_field_24 = isoData.get( field );
                    Log.d(TAG, "field14: " + ISO8583_FIELD.r_field_14);
                }
                else if(field==37)
                 {
                       ISO8583_FIELD.r_field_37 = isoData.get( field );
                     Log.d(TAG, "field37: " + ISO8583_FIELD.r_field_37);
                 }
                else if(field==38)
                {
                    ISO8583_FIELD.r_field_38 = isoData.get(field );
                    Log.d(TAG, "field38: " + ISO8583_FIELD.r_field_38);
                }
                else if(field==39)
                {
                    ISO8583_FIELD.r_field_39 = isoData.get(field );
                    Log.d(TAG, "field39: " + ISO8583_FIELD.r_field_39);
                }
                else if(field==41)
                {
                    ISO8583_FIELD.r_field_41 = isoData.get(field );
                    Log.d(TAG, "field41: " + ISO8583_FIELD.r_field_41);
                }
                else if(field==55)
                {
                    ISO8583_FIELD.r_field_55 = isoData.get(field );
                    Log.d(TAG, "field55: " + ISO8583_FIELD.r_field_55);
                }
                else if(field==62)
                {
                    ISO8583_FIELD.r_field_62 = isoData.get(field );
                    Log.d(TAG, "field62: " + ISO8583_FIELD.r_field_62);
                }
                else
                {

                }
            } else {
                //Log.d(TAG, "Mark Field =0: " + field);
                unpackValidField[field] = false;
            }
            fieldMark = (fieldMark>>1);
        }
        Log.d(TAG, "Index1:" + index);
        return true;
    }

    public byte[] appendF55( int tag, String value ) {
        if( null == emvtlvF55)
        {
            emvtlvF55 = new EMVTLVParam();
        }
        String tlv = emvtlvF55.append(tag, value);
        return Utility.hexStr2Byte(tlv);
    }

    public byte[] appendF55( String value )
    {
        return appendF55(value.getBytes());
    }
    public byte[] appendF55( int tag, byte[] value )
    {
        return appendF(55, tag, value );
    }
    public byte[] appendF55( byte[] TLV )
    {
        return appendF(55, TLV );
    }
    public byte[] appendF( int field, int tag, byte[] value )
    {
        byte[] TLV;
        byte[] bLength = new byte[2 + 3];
        String sTag = Integer.toHexString(tag).toUpperCase();
        int offset = 0;
        int lenTag;
        int lenLen;

        int len = value.length;
        if (len <= 0x7F) {
            bLength[offset] = (byte) len;
            ++offset;
        } else if (len <= 0xFF) {
            bLength[offset] = (byte) 0b10000001;
            ++offset;
            bLength[offset] = (byte) (0b10000000 | (len & 0x7F));
            ++offset;
        } else {
            Log.e(TAG, "invalid length:" + len + " of TAG:" + Integer.toHexString(tag) + " to Field:" + field);
            return null;
        }
        lenTag = sTag.length();
        lenLen = offset;
        lenTag /= 2;

        TLV = new byte[lenTag + lenLen + value.length];

        System.arraycopy( Utility.hexStr2Byte(sTag), 0, TLV, 0, lenTag );
        offset = lenTag;
        System.arraycopy(bLength, 0, TLV, offset, lenLen);
        offset += lenLen;
        System.arraycopy(value, 0, TLV, offset, value.length);
        Log.d(TAG, "TLV:"  + Utility.byte2HexStr(TLV));

        return appendF(field, TLV);
    }
    public byte[] appendF( int field, byte[] TLV ){
        if( TLV.length <= 0 ){
            return null;
        }
        validField[field] = true;
        if( null != allField[field] ){
            byte[] tmp = getField(field);
            byte[] all = new byte[tmp.length+TLV.length];
            System.arraycopy(tmp,0,all,0,tmp.length);
            System.arraycopy(TLV,0,all,tmp.length, TLV.length);
            setField(field, all, all.length);
        } else {
            setField(field, TLV, TLV.length);
        }

        return  TLV;

    }


    public byte[] makePacket(SparseArray<String> data, PACKET_TYPE type) {
        int fieldIndex;
        String fieldValue;
        byte[] tmp;

        for( int i=0; i< data.size(); i++ ) {
            fieldIndex = data.keyAt(i);
            fieldValue = data.valueAt(i);
            tmp = setField( fieldIndex, fieldValue );
            if( tmp == null ){
                Log.e(TAG, "error of index:" + fieldIndex + ", value:" +  fieldValue );
            }
        }

        return getPacket(header, tail, type );
    }

    public abstract int getFieldIndex( ATTRIBUTE attribute );

    public byte[] setValue( ATTRIBUTE attribute, String value ){
        byte[] ret = null;

        int field = getFieldIndex(attribute);
        if( field >= 0 ){
            ret = setField(field, value);
        }
        return ret;
    }

    public String getValue( ATTRIBUTE attribute ){
        String ret = null;

        int field = getFieldIndex(attribute);
        if( field >= 0 ){
            ret = getUnpack(field);
        }
        return ret;
    }

    public byte[] getPacket( PACKET_TYPE type)
    {
        return getPacket(header, tail, type);
    }

    public String bytetostring(byte[] bytes) {
        String s = new String(bytes, StandardCharsets.UTF_8);
        return s;
        // Log.d(TAG, "Packet  ......  "+bytetostring(packet));

    }

}
