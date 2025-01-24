package com.verifone.demo.emv;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class menu_items {
    public static HashMap<String, List<String>> getData_supervisor() {
        HashMap<String, List<String>> expandableDetailList = new HashMap<String, List<String>>();

        List<String> manage_users = new ArrayList<String>();
        manage_users.add("Register Cashier");
        manage_users.add("Enable/Disable Cashier");
        manage_users.add("Delete Cashier");
        manage_users.add("Change Supervisor PIN");
        manage_users.add("View Cashier");

        List<String> reprint = new ArrayList<String>();
        reprint.add("Last Receipt");
        reprint.add("Specific Receipt");

        List<String> reports = new ArrayList<String>();
        reports.add("Detail Report");
        reports.add("Summary Report");
        reports.add("Sales Report");
        reports.add("Void Report");
        reports.add("Cash Advance Report");

        expandableDetailList.put("Manual Card Entry", new ArrayList<String>());
        expandableDetailList.put("Settlement", new ArrayList<String>());
        expandableDetailList.put("Reprint", reprint);
        expandableDetailList.put("Reports", reports);
        expandableDetailList.put("Manage Users", manage_users);
        expandableDetailList.put("Delete All Data", new ArrayList<String>());

        return expandableDetailList;
    }

    public static HashMap<String, List<String>> getData_admin() {
        HashMap<String, List<String>> expandableDetailList = new HashMap<String, List<String>>();

        List<String> settings = new ArrayList<String>();
        settings.add("Register Support");
        settings.add("View Support");
        settings.add("Enable/Disable Support");
        settings.add("Change Support Pin");
        settings.add("Change Admin Pin");
        settings.add("Delete Support");
        expandableDetailList.put("Settings", settings);

        return expandableDetailList;
    }

    public static HashMap<String, List<String>> getData_support() {
        HashMap<String, List<String>> expandableDetailList = new HashMap<String, List<String>>();

        List<String> manage_users = new ArrayList<String>();
        List<String> manage_transaction = new ArrayList<String>();
        List<String> Communication_setting = new ArrayList<String>();
        manage_users.add("Register Supervisor");
        manage_users.add("Enable/Disable Supervisor");
        manage_users.add("Change Supervisor Pin");
        manage_users.add("Enable Disable Cashier");

        manage_transaction.add("manual card entry");
        manage_transaction.add("refund");
        manage_transaction.add("pre Authentication");
        manage_transaction.add("settlement");
        manage_transaction.add("magnetic stripe & fallback");
        manage_transaction.add("Pre-Auth Completion");
        manage_transaction.add("Purchase Cashback");
        manage_transaction.add("Deposit");

        Communication_setting.add("Communication configuration");
        Communication_setting.add("terminal configuration");
        Communication_setting.add("view terminal info");
        Communication_setting.add("terminal setup rep");
        Communication_setting.add("com setup rep");



        expandableDetailList.put("Manage Users", manage_users);
        expandableDetailList.put("manage_transaction", manage_transaction);
        expandableDetailList.put("Communication Settings", Communication_setting);
        return expandableDetailList;
    }

    public static HashMap<String, List<String>> getData_cashier() {
        HashMap<String, List<String>> expandableDetailList = new HashMap<String, List<String>>();
        List<String> reprint = new ArrayList<String>();
        reprint.add("Last Reciept");
        reprint.add("Specific Reciept");

        List<String> Report = new ArrayList<String>();
       Report.add("Summary Report");
        Report.add("Detail Report");
        Report.add("Summary Report");
        List<String> manage_users = new ArrayList<String>();
        manage_users.add("Change cashier pin");
        manage_users.add("Logout");

        expandableDetailList.put("Manual Dawnload", new ArrayList<String>());
        expandableDetailList.put("Settlement", new ArrayList<String>());
        expandableDetailList.put("Reset Timeout", new ArrayList<String>());
        expandableDetailList.put("Manage Users", manage_users);

        return expandableDetailList;
    }
}
