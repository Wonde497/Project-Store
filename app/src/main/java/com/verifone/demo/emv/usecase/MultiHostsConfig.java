package com.verifone.demo.emv.usecase;

import android.util.Log;

import com.verifone.demo.emv.Public_data.ISO8583msg;
import com.verifone.demo.emv.basic.HostInformation;
import com.verifone.demo.emv.basic.MultiHosts;
import com.verifone.demo.emv.caseA.ISO8583u;
import com.verifone.demo.emv.caseA.ISO8583u128;
import com.verifone.demo.emv.caseB.ISO8583CaseB;

/**
 * Created by Simon on 2019/1/25.
 */

public class MultiHostsConfig {
    private static String TAG = "MultiHostsConfig";
//public static Context context;
    public static MultiHosts multiHosts = null;

    public enum Category {
        Default,
        VISA,
        Bitmap128,
        Dashen,
    }

   /* public MultiHostsConfig(Context con) {
context=con;
    }*/

    public static void initialize() {

        multiHosts = new MultiHosts();


        int index = -1;

        HostInformation hostInformation;


        // Default
        hostInformation = new HostInformation<>(new ISO8583u(), Category.Default, "Default");

        hostInformation.setMerchant("", "", "");
        //  hostInformation.setHost("172.26.208.123", 9098);
        hostInformation.setHost(ISO8583msg.ipadd, ISO8583msg.portnum);
        hostInformation.AIDList = null;
        hostInformation.cardBinList = null; // both the AIDList & cardBinList, so all cards can be match for this host. this should be the last ONE in the list.
        hostInformation.setKeysIndex(10, 10, 10, 10, 1);

        hostInformation.prn_card_mask = "*";
        hostInformation.prn_card_mask_range = new int[]{6, -5};

        index = multiHosts.append(hostInformation);
        Log.d(TAG, "new host " + hostInformation.description +
                "setting @:" + index);

        // VISA
        hostInformation = new HostInformation<>(new ISO8583CaseB(), Category.VISA, "VISA");
        hostInformation.setMerchant("", "", "");
        //  hostInformation.setHost("172.26.208.123", 9098);
        hostInformation.setHost(ISO8583msg.ipadd, ISO8583msg.portnum);
        hostInformation.AIDAppend("A000000003");
        hostInformation.CardBINAppend("439225");
        hostInformation.CardBINAppend("11000241");
        hostInformation.setKeysIndex(20, 20, 20, 20, 2);

        hostInformation.prn_card_mask = "#";
        hostInformation.prn_card_mask_range = new int[]{2, -5};


        index = multiHosts.append(hostInformation);
        Log.d(TAG, "new host " + hostInformation.description +
                "setting @:" + index);

        // Dashen
        hostInformation = new HostInformation<>(new ISO8583CaseB(), Category.Dashen, "Dashen");
        hostInformation.setMerchant("", "", "");
        //  hostInformation.setHost("172.26.208.123", 9098);
        hostInformation.setHost(ISO8583msg.ipadd, ISO8583msg.portnum);
        hostInformation.AIDAppend("A000000025");
        hostInformation.CardBINAppend("439225");
        hostInformation.CardBINAppend("11000241");
        hostInformation.setKeysIndex(20, 20, 20, 20, 2);

        hostInformation.prn_card_mask = "#";
        hostInformation.prn_card_mask_range = new int[]{2, -5};


        index = multiHosts.append(hostInformation);
        Log.d(TAG, "new host " + hostInformation.description +
                "setting @:" + index);

        // 128 bitmap
        hostInformation = new HostInformation<>(new ISO8583u128(), Category.Bitmap128, "128 Bitmap");
        hostInformation.setMerchant("", "", "");
      //  hostInformation.setHost("172.26.208.123", 9098);
        hostInformation.setHost(ISO8583msg.ipadd, ISO8583msg.portnum);
        hostInformation.AIDAppend("A000000004");
        hostInformation.CardBINAppend("233605");
        hostInformation.setKeysIndex(20, 20, 20, 20, 2);

        hostInformation.prn_card_mask = "X";
        hostInformation.prn_card_mask_range = new int[]{4, -5};

        index = multiHosts.append(hostInformation);
        Log.d(TAG, "new host " + hostInformation.description +
                "setting @:" + index);
        // Default

    }

    public static HostInformation get(int index) {
        return multiHosts.get(index);
    }

    public static void update(int index, HostInformation hostInformation) {
        if( null == multiHosts){
            initialize();
        }
        multiHosts.update(index, hostInformation);
    }

    public static HostInformation getHost(int index, String cardBin, String aid) {
        if( null == multiHosts){
            initialize();
        }
        return multiHosts.getHost(index, cardBin, aid);
    }
}
