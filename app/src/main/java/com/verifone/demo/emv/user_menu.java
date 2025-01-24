package com.verifone.demo.emv;



import android.content.Intent;
import android.os.Bundle;



import android.support.v7.app.AppCompatActivity;


import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;


import com.verifone.demo.emv.basic.BaseActivity;

import java.util.ArrayList;

import cn.verifone.simon.verifone_x9_demo_emv.Global.R;
public class user_menu extends BaseActivity {


    private ArrayList<test_class> courseModalArrayList;
    static String TAG = "Test_ACt2";
    private DBHandler dbHandler;
    private CourseRVAdapter courseRVAdapter;
    private RecyclerView coursesRV;
    boolean homePressed = false;


    @Override

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_test2);

        // initializing our all variables.

        courseModalArrayList = new ArrayList<>();

        dbHandler = new DBHandler(user_menu.this);

        dbHandler.TABLE_NAME="users";

        // getting our course array

        // list from db handler class.

        courseModalArrayList = dbHandler.readCourses();



        // on below line passing our array lost to our adapter class.

        courseRVAdapter = new CourseRVAdapter(courseModalArrayList, user_menu.this);

        coursesRV = findViewById(R.id.idRVCourses);



        // setting layout manager for our recycler view.

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(user_menu.this, RecyclerView.VERTICAL, false);

        coursesRV.setLayoutManager(linearLayoutManager);



        // setting our adapter to recycler view.

        coursesRV.setAdapter(courseRVAdapter);

        /////////////////////////////////////////////////////////////////////////////////

       /* String selection = "name = ?";
        String[] selectionArgs = {"chal"};

        List<user> userList = new ArrayList<user>();
        DBHandler.user_functions user_fun=dbHandler.new user_functions();

        userList= user_fun.viewUsers(selection,selectionArgs);

        for (user obj : userList) { //loop
            Log.d("EMVDemo", "Users: " + obj.getName() + ":" + obj.getPassword() + ":" + obj.getStatus());
        }*/

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        homePressed = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        homePressed = true;
        Log.d(TAG, "Resume");
    }

    @Override
    public void onPause() {
        super.onPause();
        if(homePressed)
        {
            Log.i(TAG, "Pause");
            startActivity(new Intent(user_menu.this, MenuActivity.class));
            finish();
        }
    }
}