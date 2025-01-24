package com.verifone.demo.emv;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.verifone.demo.emv.terminalfunctions.Terminal_Mode;

import cn.verifone.simon.verifone_x9_demo_emv.Global.R;
public class Base_Screen1 extends AppCompatActivity {
    static String TAG="REG_TERID";
    View Ttitel;
    private EditText terminalid;
    private TextView ttitel;
    FrameLayout frame;
    Button register;
    private DBHandler dbHandler;
    private DBHandler.terminal_functions terminal_fun;
    int row_cnt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_screen1);

        Log.d(TAG, "Register terminal onCreate method go to initScreen2");

         chk_TID();
        if (row_cnt==0) {
            initScreen2();
            Log.d(TAG, "Register terminal onCreate method initScreen2 Done");

        }
        else if (row_cnt>0) {
            Log.d(TAG, "TID aleardy Registered go Next Activity ");
            //startActivity(new Intent(Base_Screen1.this, Base_Screen2.class));
            startActivity(new Intent(Base_Screen1.this, Base_Screen2.class));
            finish();
        }
            }
    public void chk_TID()
    {
        String selection = null;
        String[] selectionArgs =null;

        Bundle bundle = new Bundle();
       bundle.putString("terminalid", "terminalid");

        dbHandler = new DBHandler(Base_Screen1.this);
        DBHandler.terminal_functions terminal_fun=dbHandler.new terminal_functions();

        row_cnt=terminal_fun.row_countTid(selection,selectionArgs);

        Log.d(TAG,"Check TID method user cnt: " + row_cnt);

              }
              private void initScreen2() {

                terminalid = (EditText) findViewById(R.id.Tvregterminalid);
                ttitel = (TextView) findViewById(R.id.toolbar);
                register = (Button) findViewById(R.id.idBtnregterminalid);
                terminalid.setFilters(new InputFilter[] { new InputFilter.LengthFilter(8) });

                register.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Log.d(TAG, "Register button clicked initScreen2");

                        String strtermid=terminalid.getText().toString();

                        if (strtermid.length() < 8 )
                        {
                            Toast.makeText(register.getContext(), "Terminal_id Must not be less than 8 characters.", Toast.LENGTH_LONG).show();
                            return;
                        }
                        else if (strtermid.isEmpty()){

                            Toast.makeText(register.getContext(), "Please enter Terminal_id is not empity.", Toast.LENGTH_SHORT).show();
                            return;

                        }

                        String TABLE_NAME_TID = "TID";
                        String ID_COL = "id";
                        String TERMID_COL = "terminal_id";
                        dbHandler.query1="CREATE TABLE " + TABLE_NAME_TID + " ("
                                 +ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                                + TERMID_COL + " TEXT)";

                        dbHandler.TABLE_NAME_TID="TID";

                        DBHandler.terminal_functions terminal_fun=dbHandler.new terminal_functions();
                        terminal_fun.regterminal_id(strtermid);

                        Toast.makeText(register.getContext(),"Terminal has been Regiser succefully.", Toast.LENGTH_SHORT).show();



                       // startActivity(new Intent(Base_Screen1.this, Base_Screen2.class));
                       // finish();

                        onBackPressed();
                    }

                });

                //dbHandler = new DBHandler(Base_Screen1.this);

                  terminalid.setOnKeyListener(new View.OnKeyListener()
                  {
                      public boolean onKey(View v, int keyCode, KeyEvent event)
                      {
                          if (event.getAction() == KeyEvent.ACTION_DOWN)
                          {
                              switch (keyCode)
                              {
                                  case KeyEvent.KEYCODE_DPAD_CENTER:
                                  case KeyEvent.KEYCODE_ENTER:
                                      register.performClick();
                                      return true;
                                  default:
                                      break;
                              }
                          }
                          return false;
                      }
                  });

                Log.d(TAG, "out off initScreen1");
            }
    @Override
    public void onBackPressed() {

        //Log.d(TAG, "onBackPressed Method  Back");
            //if(row_cnt==0) {
               // chk_TID();
           // }
            //else if (row_cnt>0) {

                Log.d(TAG, "onBackPressed method Terminal aleardy Registered go base_screen2");
               //startActivity(new Intent(Base_Screen1.this, Base_Screen2.class));
                startActivity(new Intent(register.getContext(), Base_Screen2.class));
                finish();
            //}
            //else
                //finish();
        }
    }


