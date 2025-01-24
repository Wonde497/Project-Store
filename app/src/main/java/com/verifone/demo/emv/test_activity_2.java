package com.verifone.demo.emv;



import android.content.Intent;
import android.os.Bundle;



import android.support.v7.app.AppCompatActivity;


import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;


import java.util.ArrayList;

import cn.verifone.simon.verifone_x9_demo_emv.Global.R;

public class test_activity_2 extends AppCompatActivity {


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

        dbHandler = new DBHandler(test_activity_2.this);

        dbHandler.TABLE_NAME="users";

        // getting our course array

        // list from db handler class.

        courseModalArrayList = dbHandler.readCourses();

        Log.d("EMVDemo db res",  courseModalArrayList.get(0).toString());


        // on below line passing our array lost to our adapter class.

        courseRVAdapter = new CourseRVAdapter(courseModalArrayList, test_activity_2.this);

        coursesRV = findViewById(R.id.idRVCourses);



        // setting layout manager for our recycler view.

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(test_activity_2.this, RecyclerView.VERTICAL, false);

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
            startActivity(new Intent(test_activity_2.this, MenuActivity.class));
            finish();
        }
    }
}