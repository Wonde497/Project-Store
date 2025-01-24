package com.verifone.demo.emv;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import android.support.v7.app.AppCompatActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;


import com.verifone.demo.emv.basic.dialog;
import com.verifone.demo.emv.transaction.TransBasic;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;
import cn.verifone.simon.verifone_x9_demo_emv.Global.R;

public class keytransfer extends AppCompatActivity {
    private boolean status2 = true;
    AudioRecord recorder;
    private int sampleRate = 16000 ; // 44100 for music
    private int channelConfig = AudioFormat.CHANNEL_CONFIGURATION_MONO;
    private int audioFormat = AudioFormat.ENCODING_PCM_16BIT;
    private AudioTrack player;
    int minBufSize = AudioRecord.getMinBufferSize(sampleRate, channelConfig, audioFormat);
    private final int format2 = AudioFormat.ENCODING_PCM_16BIT;
    private final int channel_in = AudioFormat.CHANNEL_IN_MONO;
    private final int channel_out = AudioFormat.CHANNEL_OUT_MONO;
    private final int format = AudioFormat.ENCODING_PCM_16BIT;
    private int minBuffer;

    Button listen,send,stop, listDevices;
    ListView listView;
    TextView msg_box,status;
    EditText writeMsg;
    BluetoothAdapter bluetoothAdapter;
    BluetoothDevice[] btArray;
    SendReceive sendReceive;

    static final int STATE_LISTENING = 1;
    static final int STATE_CONNECTING=2;
    static final int STATE_CONNECTED=3;
    static final int STATE_CONNECTION_FAILED=4;
    static final int STATE_MESSAGE_RECEIVED=5;
    public byte[] buffer;
    int bytes;
    int REQUEST_ENABLE_BLUETOOTH=1;

    private static final String APP_NAME = "BTChat";
    private static final UUID MY_UUID=UUID.fromString("8ce255c0-223a-11e0-ac64-0803450c9a66");
    private DBHandler dbHandler;DBHandler.user_functions user_fun;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.keyloadertransfermaster);
        dbHandler = new DBHandler(getApplicationContext());
         sharedPreferences = getSharedPreferences("Shared_data", Context.MODE_PRIVATE);

        findViewByIdes();
        bluetoothAdapter=BluetoothAdapter.getDefaultAdapter();

       checkbluetooth();

        implementListeners();
    }
public boolean checkbluetooth(){
    if(!bluetoothAdapter.isEnabled())
    {
        Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(enableIntent,REQUEST_ENABLE_BLUETOOTH);
        return false;
    }else {
        return true;
    }
}
                private void implementListeners() {

                  listDevices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    if(checkbluetooth()){
                  Set<BluetoothDevice> bt=bluetoothAdapter.getBondedDevices();
                   String[] strings=new String[bt.size()];
                  btArray=new BluetoothDevice[bt.size()];
                  int index=0;

                if( bt.size()>0)
                {
                    for(BluetoothDevice device : bt)
                    {
                        btArray[index]= device;
                        strings[index]=device.getName();
                        index++;
                    }
                    ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,strings)
                    {

                        @Override
                        public View getView(int position, View convertView, ViewGroup parent) {
                            View view =super.getView(position, convertView, parent);

                            TextView textView=(TextView) view.findViewById(android.R.id.text1);
                            /*YOUR CHOICE OF COLOR*/

                            int color = getResources().getColor(R.color.blackgreen2); // If you have a custom color in res/values/colors.xml
// int color = Color.BLUE; // Using a predefined color

                            textView.setTextColor(color);
                            ;

                            return view;
                        }
                    };
                    listView.setAdapter(arrayAdapter);

                }
               }
            }
        });


        listen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkbluetooth()){
                ServerClass serverClass=new ServerClass();
                serverClass.start();
                }
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
               if(checkbluetooth()){
                ClientClass clientClass=new ClientClass(btArray[i]);
                clientClass.start();
                status.setText("Connecting");}
            }
        });

        /*send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              // String string= String.valueOf(writeMsg.getText());
               if(checkbluetooth()&& status.getText().toString().equals("Connected")){
                   //sendReceive.write("message ...".getBytes());
                        if(dbHandler.getkeyloadersize()>0){
                            sendReceive.write(dbHandler.getmasterkeydata().getMasterkey().getBytes());

                        }else {
                            Toast.makeText(keytransfer.this, "masterkey not registerd",Toast.LENGTH_SHORT).show();

                        }
               }
                if(!status.getText().toString().equals("Connected")){
                    Toast.makeText(keytransfer.this, "device not connected",Toast.LENGTH_SHORT).show();
                }
            }

        });*/

    }



    Handler handler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {

            switch (msg.what)
            {
                case STATE_LISTENING:
                    status.setText("Listening");
                    break;
                case STATE_CONNECTING:
                    status.setText("Connecting");
                    break;
                case STATE_CONNECTED:
                    status.setText("Connected");
                    break;
                case STATE_CONNECTION_FAILED:
                    status.setText("Connection Failed");
                    break;
                case STATE_MESSAGE_RECEIVED:
                    byte[] readBuff= (byte[]) msg.obj;
                    String tempMsg=new String(readBuff,0,msg.arg1);
                  //  msg_box.setText(tempMsg);
                    dbHandler.registermasterkey(tempMsg);
                    TransBasic.getInstance(sharedPreferences).masterkeytrans(tempMsg.substring(0, 32),tempMsg.substring(32,64));
                    //  Toast.makeText(keytransfer.this, dbHandler.getmasterkeydata().getMasterkey(),Toast.LENGTH_SHORT).show();
                    Toast.makeText(keytransfer.this, "Key Recieved",Toast.LENGTH_SHORT).show();
                    final dialog dia = new dialog(keytransfer.this, "Key Recieved");
                    dia.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dia.show();

                    break;
            }
            return true;
        }
    });

    private void findViewByIdes() {
        listen=(Button) findViewById(R.id.listen);
       // send=(Button) findViewById(R.id.enter);
        listView=(ListView) findViewById(R.id.listview);
        status=(TextView) findViewById(R.id.status);
       // writeMsg=(EditText) findViewById(R.id.writemsg);
        listDevices=(Button) findViewById(R.id.listDevices);
    }

    private class ServerClass extends Thread
    {
        private BluetoothServerSocket serverSocket;

        public ServerClass(){
            try {
                serverSocket=bluetoothAdapter.listenUsingRfcommWithServiceRecord(APP_NAME,MY_UUID);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void run()
        {
            BluetoothSocket socket=null;

            while (socket==null&&bluetoothAdapter.isEnabled())
            {
                try {
                    Message message=Message.obtain();
                    message.what=STATE_CONNECTING;
                    handler.sendMessage(message);

                    socket=serverSocket.accept();
                } catch (IOException e) {
                    e.printStackTrace();
                    Message message=Message.obtain();
                    message.what=STATE_CONNECTION_FAILED;
                    handler.sendMessage(message);
                }

                if(socket!=null)
                {
                    Message message=Message.obtain();
                    message.what=STATE_CONNECTED;
                    handler.sendMessage(message);

                    sendReceive=new SendReceive(socket);
                    sendReceive.start();

                    break;
                }
            }
        }
    }

    private class ClientClass extends Thread
    {
        private BluetoothDevice device;
        private BluetoothSocket socket;

        public ClientClass (BluetoothDevice device1)
        {
            device=device1;

            try {
                socket=device.createRfcommSocketToServiceRecord(MY_UUID);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void run()
        {
            if(bluetoothAdapter.isEnabled()){
                try {
                socket.connect();
                Message message=Message.obtain();
                message.what=STATE_CONNECTED;
                handler.sendMessage(message);

                sendReceive=new SendReceive(socket);
                sendReceive.start();

            } catch (IOException e) {
                e.printStackTrace();
                Message message=Message.obtain();
                message.what=STATE_CONNECTION_FAILED;
                handler.sendMessage(message);
            }
            }
        }
    }


    private class SendReceive extends Thread
    {
        private final BluetoothSocket bluetoothSocket;
        private final InputStream inputStream;
        private final OutputStream outputStream;

        public SendReceive (BluetoothSocket socket)
        {
            bluetoothSocket=socket;
            InputStream tempIn=null;
            OutputStream tempOut=null;

            try {
                tempIn=bluetoothSocket.getInputStream();
                tempOut=bluetoothSocket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }

            inputStream=tempIn;
            outputStream=tempOut;
        }

        public void run()
        {
            buffer=new byte[1024];

            while (bluetoothAdapter.isEnabled())
            {
                Log.d("EMVDemo", "DEPOSIT111");

                try {
                    bytes=inputStream.read(buffer);
                    handler.obtainMessage(STATE_MESSAGE_RECEIVED,bytes,-1,buffer).sendToTarget();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        public void write(byte[] bytes)
        {
            try {
                outputStream.write(bytes);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
