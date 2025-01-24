package com.verifone.demo.emv.userfunctions;
import android.support.v4.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v7.widget.RecyclerView;

import com.verifone.demo.emv.Clicklistner;
import com.verifone.demo.emv.DBHandler;
import com.verifone.demo.emv.data_handlers.User;
import com.verifone.demo.emv.widget.RecyclerAdapter;
import android.support.v7.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import cn.verifone.simon.verifone_x9_demo_emv.Global.R;
/**
 * A simple {@link Fragment} subclass.
 * Use the {@link viewusermy#newInstance} factory method to
 * create an instance of this fragment.
 */
/*typedef struct
{
   char device_Type[3];
   char transmission_No[3];
   char terminal_Id[17];
   char employee_ID[7];
   char current_Date[7];
   char current_Time[7];
   char message_Type[2];
   char message_Subtype[2];
   char transaction_Code[3];
   char processing_Flag1[2];
   char processing_Flag2[2];
   char processing_Flag3[2];
   char response_Code[4];
}
SPDH_HEADER_VARS;

//The above 13 lines of declaration are for standard message header parts
//The next 55 lines of declaration are for standard message header parts

typedef struct
{
   char A_customer_Billing_Address[21];
   char B_amount_1[19];
   char C_amount2[19];
   char D_application_Account_Type[2];
   char E_application_Account_Number[20];
   char F_approval_Code[9];
   char G_authentication_Code[9];
   char H_authentication_Key[75];
   char I_data_Encryption_Key[17];
   char J_available_Balance[19];
   char K_business_Date[7];
   char L_check_Type[2];
   char M_communications_Key[75];
   char N_customer_Id[41];
   char O_customer_Id_Type[3];
   char P_draft_Capture_Flag[2];
   char Q_echo_Data[17];
   char R_card_Type[2];
   char S_invoice_Number[11];
   char T_invoice_Number_Original[11];
   char U_language_code[2];
   char V_mail_Download_Key[16];
   char W_mail_Text_Download_Data[958];
   char X_iso_Response_Code[4];
   char Y_customer_Zip_Code[10];
   char Z_address_Verification_status[2];
   char a_optional_Data[251];
   char b_pin_Customer[17];
   char c_pin_Suppervisor[17];
   char d_retailer_Id[13];
   char e_pos_Condition_Code[3];
   char f_pin_Length[201];
   char g_response_Display[49];
   char h_sequence_Number[11];
   char i_sequence_Number_Original[10];
   char j_state_Code[3];
   char k_birth_Date[26];
   char l_totals_Batch[76];
   char m_totals_Day[76];
   char n_totals_Employee[76];
   char o_totals_Shift[76];
   char q_track2_Customer[41];
   char r_track2_Supervisor[41];
   char s_transaction_Description[25];
   char t_pin_Pad_Identifier[17];
   char u_acceptor_Posting_Date[7];
   char _0_amex_Data_Collection[119];
   char _1_ps2000_Data[25];
   char _2_track1_Customer[83];
   char _3_track1_Supervisor[83];
   char _4_industry_Data[172];
   char _6_product_Subfields[1044];
   char _7_product_Subfields[51];
   char _8_product_Subfields[51];
   char _9_product_Subfields[51];
}
SPDH_OPTIONAL_DATA_FIELD_VARS;

typedef struct
{
   char A_host_original_data[13];
   char E_pos_entry_mode[4];
   char I_transaction_currency_code[4];
   char O_emv_request_data[137];
   char P_emv_additional_request_data[65];
   char Q_emv_response_data[65];
   char R_response_data[260];
   char B_CVD[5];
   char H_CVDIindicator[3];
   char j_stateid[3];
   char X_PointofService_data[7];
}
SPDH_FID_6_DATA;*/
public class viewusermy extends Fragment implements Clicklistner {
    TextView t;    private DBHandler dbHandler;

    Button b; //FirebaseDatabase f;
  private RecyclerView mRecyclerView;

    private RecyclerAdapter mRecyclerAdapter;
   // private RecyclerView.LayoutManager mLayoutManager;String s;//MainActivity1 n;
    Button btnAddItem,but;
    ArrayList<User> myList;public String user_type;
    EditText etTitle, etDescription;
    String title = "",description = "";
    ImageView crossImage;  public static List<String> list=new ArrayList<String>();
    // RecyclerView recyclerView;
    //RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;

    @Override
    public void delete(String name) {

    }

    @Override
    public void isenabled(boolean toggle) {

    }

    @Override
    public void createfragment() {

    }


    public interface OnFragmentInteractionListener {

        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public viewusermy() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Tab1Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static viewusermy newInstance(String param1, String param2) {
        viewusermy fragment = new viewusermy();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root=inflater.inflate(R.layout.viewusermy, container, false);
        user_type = getArguments().getString("user_type");
        myList = new ArrayList<>();
        mRecyclerView = (RecyclerView)root.findViewById(R.id.recyclerView);
        mRecyclerAdapter = new RecyclerAdapter(myList,this, root.getContext(),user_type);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mRecyclerAdapter);
        myList.clear();
        String selection="";
        String[] selectionArgs = {};
        dbHandler = new DBHandler(root.getContext());
        DBHandler.user_functions user_fun=dbHandler.new user_functions(); User mLog = new User();
        List<User> userList = new ArrayList<User>();
        //userList= user_fun.viewUsers(selection,selectionArgs,user_type);
        if(userList.size()>0){
            for(int i=0;i<userList.size();i++){
                mLog.setName(userList.get(i).getName());
                mLog.setActiontype("view");
                myList.add(mLog);
            }
            mRecyclerAdapter.notifyData(myList);
        }else{

        }
       /* mLog.setName(userList.get(0).getName());
        mLog.setActiontype("view");
        myList.add(0,mLog);
       // mRecyclerAdapter.notifyData(myList);

       // Toast.makeText(root.getContext(), myList.get(0).getName(), Toast.LENGTH_SHORT).show();

        mLog.setName(userList.get(1).getName());
        mLog.setActiontype("view");
        myList.add(1,mLog);
        mRecyclerAdapter.notifyData(myList);

      //  Toast.makeText(root.getContext(), myList.get(1).getName(), Toast.LENGTH_SHORT).show();*/

        return root;
    }
    public static void lista(String k){
        list.add(k);
    }


}