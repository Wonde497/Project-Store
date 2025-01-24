package com.verifone.demo.emv.Help;

import static com.verifone.demo.emv.ClearKeys.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v7.app.AppCompatActivity;


import cn.verifone.simon.verifone_x9_demo_emv.Global.R;

//package com.verifone.demo.emv.Help.databinding.ActivityMainBinding;
public class HelpDetailedActivity extends AppCompatActivity {
    // ActivityDetailedBinding binding;

    ImageView helpimage;
    TextView HelpTitle, HelpDatail,
            helpquestion, helpquestiontext,
            step1text,  step2text, step3text,
            step1, step2,step3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help_activity_detailed1);
        // helpimage = new ImageView(R.id.helpImage1);
        helpimage = (ImageView) findViewById(R.id.helpImage1);
        HelpTitle = (TextView) findViewById(R.id.helpquestion);
        HelpDatail = (TextView) findViewById(R.id.helpdetailtitle1);
        helpquestion = (TextView) findViewById(R.id.helpquestion);
        helpquestiontext = (TextView) findViewById(R.id.helpquestiontext);
        step1text = (TextView) findViewById(R.id.step1text);
        step2text = (TextView) findViewById(R.id.step2text);
        step3text = (TextView) findViewById(R.id.step3text);
        step1 = (TextView) findViewById(R.id.step1);
        step2 = (TextView) findViewById(R.id.step2);
        step3 = (TextView) findViewById(R.id.step3);


        Intent intent = getIntent();
        if (intent != null) {
            String helptype = intent.getStringExtra("helptype");
            String help_text = intent.getStringExtra("help_text");
            String user_role = intent.getStringExtra("user_role");
            HelpDatail.setText(helptype);
            Log.d(TAG, "User Role on help detail:    "+user_role);
            Log.d(TAG, "Help Text on help detail:    "+help_text);
            if (help_text.equals("logout")){

                helpquestion.setText("How Can I Logout ?");
                helpquestiontext.setText("Follow the following steps to logout");
                step1text.setText("Go to your menu and then click menu on the left corner");
                step2text.setText("Click Logout form the list (found on bottom)");
                step3text.setText("Confirm the dialog that you are sure you want to logout");
            } else if (help_text.equals("pinchange")) {

                helpquestion.setText("How can I change my PIN ?");
                helpquestiontext.setText("Follow the following steps to change your PIN");
                step1text.setText("Go to your menu");
                step2text.setText("Click Change My PIN form the list");
                step3text.setText("Fill your current password and new password and then click Change Button");
            } else if (help_text.equals("forgotpassword")) {
                helpquestion.setText("I forgot my password ?");
                if (user_role.equals("supervisor")){
                    helpquestiontext.setText("If you forgot your password please contact your support");
                } else if (user_role.equals("support")) {
                    helpquestiontext.setText("If you forgot your password please contact your admin");
                } else if (user_role.equals( "cashier")) {
                    helpquestiontext.setText("If you forgot your password please contact your supervisor");
                } else if (user_role.equals("admin")) {
                    helpquestiontext.setText("If you forgot your password please contact your admin");
                }

                step1text.setText(" ");
                step2text.setText(" ");
                step3text.setText(" ");
                step1.setText("");
                step2.setText("");
                step3.setText("");
            } else if (help_text.equals("default_login") && user_role.equals("default")) {
                helpquestion.setText("How Can I Login ?");
                helpquestiontext.setText("Follow the following steps to Login");
                step1.setText("Step 1: ");
                step2.setText("Step 2: ");
                step3.setText("Step 3: ");
                step1text.setText("Click Login from menu activity");
                step2text.setText("Fill your user name and password and then select your role from drop down menu ");
                step3text.setText("Click Login button");
            } else if (help_text.equals("default_logout") && user_role.equals("default")) {
                helpquestion.setText("How Can I Logout ?");
                helpquestiontext.setText("Follow the following steps to logout");
                step1.setText("Step 1: ");
                step2.setText("Step 2: ");
                step3.setText("Step 3: ");
                step1text.setText("Go to your menu and then click menu on the left corner");
                step2text.setText("Click Logout form the list (found on bottom)");
                step3text.setText("Confirm the dialog that you are sure you want to logout");
            }else if (help_text.equals("default_pinchange") && user_role.equals("default")) {
                helpquestion.setText("How can I change my PIN ?");
                helpquestiontext.setText("Follow the following steps to change your PIN");
                step1.setText("Step 1: ");
                step2.setText("Step 2: ");
                step3.setText("Step 3: ");
                step1text.setText("Go to your menu");
                step2text.setText("Click Change My PIN form the list");
                step3text.setText("Fill your current password and new password plus confirm new password and then click Change Button");
            }else if (help_text.equals("default_forgotpassword") && user_role.equals("default")) {
                helpquestion.setText("I forgot my PIN ?");
                helpquestiontext.setText("If you forgot your  PIN  please contact your admin");
                step1text.setText(" ");
                step2text.setText(" ");
                step3text.setText(" ");
                step1.setText("");
                step2.setText("");
                step3.setText("");
            }
        }
    }
}
