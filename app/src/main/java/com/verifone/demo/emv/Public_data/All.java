package com.verifone.demo.emv.Public_data;

public abstract class All {

    public static int deposit = 0;
    public static int settlment = 0;
    public static int refund = 0;
    public static int manualcardentry = 0;
    public static int magestrip = 0;
    public static int pre_auth = 0;
    public static int purchasecashback = 0;

}
/*public void hex_dump( final String label, final int ptr, int size )
    {
       //#ifdef _DEBUG
        short _PER_LINE=16;

        String str[_PER_LINE + 1], String hex[_PER_LINE * 3 + 1];
        int i = 0, j = 0;
        final char buf = final char ptr;

        if( !compare( label, "Dump", 4 ) )
        {
            Log.d(TAG, "%s"+label );
            //LOG_PRINTF(("%s", label));
        }
        else
        {
            Log.d(TAG, "%s ptr:%p sizeof: %d" +label+"," +buf+"," +size );
           // LOG_PRINTF( ( "%s ptr:%p sizeof: %d", label, buf, size ) );
        }

        for( i = j = 0; size-- > 0; ++buf )
        {
            sprintf( hex + i * 3, "%02X ", buf );

            if( isprint(buf ) ) str[i] = buf;
      else str[i] = '.';

            str[++i] = 0;

            if( i >= _PER_LINE )
            {
                Log.d(TAG, "%4d: %s %s"+j +hex +str);
                //LOG_PRINTF( ( "%4d: %s %s", j, hex, str ) );
                j += _PER_LINE;
                i = 0;
            }
        }

        if( i )
        {
            while( i < _PER_LINE ) sprintf( hex + i++ * 3, "   " );
            Log.d(TAG, "%4d: %s %s"+j +hex +str);
            //LOG_PRINTF( ( "%4d: %s %s", j, hex, str ) );
        }

//#endif // _DEBUG
    }
int SPDH_PackEIOPSubField(String pucOutputBuffer, String cOptionalDataFieldType, String pszOptionalDataFieldData )
        {

            String pucPtr = pucOutputBuffer; // points to current output location, start at beginning of output buffer
            // check the input pointer is valid
            if( pucOutputBuffer == null)
            {
                //LOG_PRINTF(("SPDH_PackHeader error - NULL pointer argument passed"));
                //return null;
            }
            // if no data to pack, return here without packing
            if( (pszOptionalDataFieldData).length() == 0 )
                //return pucPtr;

                // pack field seperator - 0x1c
                pucPtr="0x1E";
            int pucPtr1 = Integer.parseInt(pucPtr);
            pucPtr1++;   // increment output pointer

            // pack field type character
            pucPtr1 = Integer.parseInt(cOptionalDataFieldType);
            pucPtr1++;   // increment output pointer

            // pack field data
            pucPtr1=Integer.parseInt(pszOptionalDataFieldData);
            pucPtr1 += (pszOptionalDataFieldData).length();  // increment output pointer by length of data copied

            // return pointer to current output position so that the next call to this function writes to the end of the message
            return pucPtr1;
        }

    //======================================================================================================================================
    int SPDH_PackSubField(String pucOutputBuffer, String cOptionalDataFieldType, String pszOptionalDataFieldData )
    {

        String pucPtr = pucOutputBuffer; // points to current output location, start at beginning of output buffer
        // check the input pointer is valid
        if( pucOutputBuffer == null)
        {
            //LOG_PRINTF(("SPDH_PackHeader error - NULL pointer argument passed"));
            //return null;
        }
        // if no data to pack, return here without packing
        if( (pszOptionalDataFieldData).length() == 0 )
            //return pucPtr;

            // pack field seperator - 0x1c
            pucPtr="0x1c";
        int pucPtr1 = Integer.parseInt(pucPtr);
        pucPtr1++;   // increment output pointer

        // pack field type character
        pucPtr1 = Integer.parseInt(cOptionalDataFieldType);
        pucPtr1++;   // increment output pointer

        // pack field data
        pucPtr1=Integer.parseInt(pszOptionalDataFieldData);
        pucPtr1 += (pszOptionalDataFieldData).length();  // increment output pointer by length of data copied

        // return pointer to current output position so that the next call to this function writes to the end of the message
        return pucPtr1;
    }

    void enable_trans(int position) {
    short retVal;
            strcpy(file_name6, "POSTRAN6");

    if (position == 1) {
        BatRec6.manual_enabled = 1;
    }

    if (position == 2) {
        BatRec6.refund_enabled = 1;
    }

    if (position == 3) {
        BatRec6.pre_auth_enabled = 1;
    }

    if (position == 4) {
        BatRec6.pre_auth_com_enabled = 1;
    }

    if (position == 5) {
        BatRec6.purchase_enabled = 1;
    }

    if (position == 6) {
        BatRec6.deposit_enabled = 1;
    }

    if (position == 7) {
        BatRec6.settlement_enabled = 1;
    }
    if (position == 8) {
        BatRec6.mag_enabled = 1;
    }

    if (createDB6() == 1)//DB CreatUJed or Exists
    {
        LOG_PRINTF(("DB Created or Exists"));
    } else//DB NOT EXIT
    {
        LOG_PRINTF(("DB Creation Error"));
    }
    retVal = db_open(&DBFile, file_name6, SIZE_BATCH_KEY6, O_RDWR);
    if (retVal < 0) {
        LOG_PRINTF(("Error opening the database file"));
    }
    if ((retVal = db_write(&DBFile, &BatKey6, (char*) &BatRec6, SIZE_BATCH_REC6, DB_CURRENT)) <= 0) {

        LOG_PRINTF(("Add error %d", retVal));
                db_close(&DBFile);
    } else {/*
		LOG_PRINTF(("Added Record with invoice no %s",BatRec.cashier_id));*/

    //db_close(&DBFile);
//}
    //db_close(&DBFile);


          //  }



/*
//     void disable_trans(int position) {
//     short retVal;
//     strcpy(file_name6, "POSTRAN6");
//
//     if (position == 1) {
//     BatRec6.manual_enabled = 0;
//     }
//
//     if (position == 2) {
//     BatRec6.refund_enabled = 0;
//     }
//
//     if (position == 3) {
//     BatRec6.pre_auth_enabled = 0;
//     }
//
//     if (position == 4) {
//     BatRec6.pre_auth_com_enabled = 0;
//     }
//
//     if (position == 5) {
//     BatRec6.purchase_enabled = 0;
//     }
//
//     if (position == 6) {
//     BatRec6.deposit_enabled = 0;
//     }
//
//     if (position == 7) {
//     BatRec6.settlement_enabled = 0;
//     }
//     if (position == 8) {
//     BatRec6.mag_enabled = 0;
//     }
//     if (position == 9) {
//     BatRec6.cash_enabled = 0;
//     }
//
//
//
//
//     if (createDB6() == 1)//DB CreatUJed or Exists
//     {
//     LOG_PRINTF(("DB Created or Exists"));
//     } else//DB NOT EXIT
//     {
//     LOG_PRINTF(("DB Creation Error"));
//     }
//     retVal = db_open(&DBFile, file_name6, SIZE_BATCH_KEY6, O_RDWR);
//     if (retVal < 0) {
// LOG_PRINTF(("Error opening the database file"));
// }
// if ((retVal = db_write(&DBFile, &BatKey6, (char*) &BatRec6, SIZE_BATCH_REC6, DB_CURRENT)) <= 0) {
//
// LOG_PRINTF(("Add error %d", retVal));
// db_close(&DBFile);
// } else {/*
// LOG_PRINTF(("Added Record with invoice no %s",BatRec.cashier_id));

         //   db_close(&DBFile);
       // }
        //db_close(&DBFile);


      //  }
public int dec_to_hex(int decimalNumber, String hexadecimalNumber)
    {
        if(decimalNumber==9)
        {
            hexadecimalNumber=String.valueOf("09");
            return 1;
        }
        else if(decimalNumber==10)
        {
            hexadecimalNumber=String.valueOf("0A");
            return 1;
        }
        else if(decimalNumber==11)
        {
           hexadecimalNumber=String.valueOf("0B");
            return 1;
        }
        else if(decimalNumber==12)
        {
            hexadecimalNumber=String.valueOf("0C");
            return 1;
        }
        else if(decimalNumber==13)
        {
            hexadecimalNumber=String.valueOf("0D");
            return 1;
        }
        else if(decimalNumber==14)
        {
            hexadecimalNumber=String.valueOf("0E");
            return 1;
        }
        else if(decimalNumber==15)
        {
            hexadecimalNumber=String.valueOf("0F");
            return 1;
        }
        else if(decimalNumber==16)
        {
            hexadecimalNumber=String.valueOf("10");
            return 1;
        }
        else if(decimalNumber==17)
        {
            hexadecimalNumber=String.valueOf("11");
            return 1;
        }
        else if(decimalNumber==18)
        {
            hexadecimalNumber=String.valueOf("12");
            return 1;
        }
        else if(decimalNumber==19)
        {
            hexadecimalNumber=String.valueOf("13");
            return 1;
        }
        else if(decimalNumber==20)
        {
            hexadecimalNumber=String.valueOf("14");
            return 1;
        }
        else if(decimalNumber==21)
        {
            hexadecimalNumber=String.valueOf("15");
            return 1;
        }
        else if(decimalNumber==22)
        {
            hexadecimalNumber=String.valueOf("16");
            return 1;
        }
        else if(decimalNumber==23)
        {
            hexadecimalNumber=String.valueOf("17");
            return 1;
        }
        else if(decimalNumber==24)
        {
            hexadecimalNumber=String.valueOf("18");
            return 1;
        }
        else if(decimalNumber==25)
        {
            hexadecimalNumber=String.valueOf("19");
            return 1;
        }
        else if(decimalNumber==26)
        {
           hexadecimalNumber=String.valueOf("1A");
            return 1;
        }
        else if(decimalNumber==27)
        {
            hexadecimalNumber=String.valueOf("1B");
            return 1;
        }
        else if(decimalNumber==28)
        {
            hexadecimalNumber=String.valueOf("1C");
            return 1;
        }
        else if(decimalNumber==29)
        {
            hexadecimalNumber=String.valueOf("1D");
            return 1;
        }
        else if(decimalNumber==30)
        {
           hexadecimalNumber=String.valueOf("1E");
            return 1;
        }
        else if(decimalNumber==31)
        {
            hexadecimalNumber=String.valueOf("1F");
            return 1;
        }
        else if(decimalNumber==32)
        {
            hexadecimalNumber=String.valueOf("20");
            return 1;
        }

        return 1;
    }
    public String hex_to_bin(String hex)
    {
        if(hex.equals("A"))
            return "10";
        if(hex.equals("B"))
            return "11";
        if(hex.equals("C"))
            return "12";
        if(hex.equals("D"))
            return "13";
        if(hex.equals("E"))
            return "14";
        if(hex.equals("F"))
            return "15";

        else {
            return "hex";
        }
    }
    public String convert_hex(String bin)
    {

        if(bin.equals("10"))
            return "A";
        if(bin.equals("11"))
            return "B";
        if(bin.equals("12"))
            return "C";
        if(bin.equals("13"))
            return "D";
        if(bin.equals("14"))
            return "E";
        if(bin.equals("15"))
            return "F";
        else {
            return "bin";
        }
    }

       */