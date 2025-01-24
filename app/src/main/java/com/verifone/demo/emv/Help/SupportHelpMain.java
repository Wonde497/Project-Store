package com.verifone.demo.emv.Help;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;
import android.support.v7.app.AppCompatActivity;

import cn.verifone.simon.verifone_x9_demo_emv.Global.R;

public class SupportHelpMain extends AppCompatActivity {
    List<ListData> helpItems;

    String helptitle, help_text, user_role;
    ImageView helpimage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help_activity_main);

        ListView listViewHelp = findViewById(R.id.listview_help);




        //help items
        helpItems = new ArrayList<>();
        helpItems.add(new ListData(R.drawable.white_logout_icon24px, "How can I logout ?"));
        helpItems.add(new ListData(R.drawable.white_change_password_icon24px, "How can change my PIN ?"));
        helpItems.add(new ListData(R.drawable.white_forgot_password_icon24px, "I forgot my password ?"));

        // Create the adapter
        ListAdapter adapter = new ListAdapter(this, R.layout.help_list, helpItems);

        // Set the adapter to the ListView
        listViewHelp.setAdapter(adapter);

        // Set item click listener
        listViewHelp.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Handle item click here
                ListData selectedItem = helpItems.get(position);
                if (position == 0) {

                    // Intent to LoginHelp Activity
                    Intent intent = new Intent(SupportHelpMain.this, HelpDetailedActivity.class);
                    helptitle = "How can I logout ?";
                    help_text = "logout";
                    user_role = "support";

                    intent.putExtra("user_role", user_role);
                    intent.putExtra("title", selectedItem.getTitle());
                    intent.putExtra("helptype", selectedItem.getHelpType());
                    intent.putExtra("help_text", help_text);
                    startActivity(intent);
                } else if (position == 1) {

                    // Intent to LogoutHelp Activity
                    Intent intent = new Intent(SupportHelpMain.this, HelpDetailedActivity.class);
                    helptitle = "How can change my PIN ?";
                    help_text = "pinchange";
                    user_role = "support";

                    intent.putExtra("user_role", user_role);
                    intent.putExtra("help_text", help_text);
                    intent.putExtra("title", selectedItem.getTitle());
                    intent.putExtra("helptype", selectedItem.getHelpType());
                    startActivity(intent);
                } else if (position == 2) {

                    // Intent to ChangePINHelp Activity
                    Intent intent = new Intent(SupportHelpMain.this, HelpDetailedActivity.class);
                    helptitle = "I forgot my password ?";
                    help_text = "forgotpassword";
                    user_role = "support";

                    intent.putExtra("user_role", user_role);
                    intent.putExtra("title", selectedItem.getTitle());
                    intent.putExtra("helptype", selectedItem.getHelpType());
                    intent.putExtra("help_text", help_text);
                    startActivity(intent);
                }

            }
        });
    }
}
