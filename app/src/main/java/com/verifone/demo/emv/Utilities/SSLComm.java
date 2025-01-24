package com.verifone.demo.emv.Utilities;

import static com.verifone.demo.emv.transaction.TransBasic.hexToASCII;

import android.content.Context;
import android.util.Log;

import com.verifone.demo.emv.basic.HostInformation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.SocketException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.io.*;
import java.net.*;
import javax.net.ssl.*;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

/**
 * Created by Simon on 2019/4/29.
 */

public class SSLComm {

    private static final String TAG = "SSLComm";
    Context context;
    static SSLContext ssl_ctx;
    InputStream keyStream = null;  // for cert
    SSLSocket socket = null;
    SSLContext sslContext;
    String host;
    int port;
    private OutputStream outputStream;
    private InputStream inputStream;
    private int status;

    private static final String CLIENT_KEY_PASSWORD = "verifone";
    private static final String TRUST_KEY_PASSWORD = "verifone";

    enum ACTION {
        connect,
        send,
        receive
    }

    ;

    SSLClient.ACTION action = SSLClient.ACTION.connect;

    public SSLComm(Context context, String assetsCert, String host, int port) {
        try {
            this.context = context;
            this.host = host;
            this.port = port;
            keyStream = context.getAssets().open(assetsCert);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private SSLContext getSSLContext(int timeOutMillisecond, InputStream... certificates) {
       // long timeout = timeOutMillisecond - 1;
        /*Certificate ca=null;

        try {
            //keyStream=
            keyStream = context.getAssets().open("rootcert.pem");
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            ca = certificateFactory.generateCertificate(keyStream);

            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            //clientKeyStore.load(context.getResources().getAssets().ope
            //keyStore.load();
            keyStore.load(null);
            keyStore.setCertificateEntry("0", ca);

            SSLContext sslContext = SSLContext.getInstance("TLS");
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory
                    .getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);
            sslContext.init(null, trustManagerFactory.getTrustManagers(), new SecureRandom());
            return sslContext;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }*/


        try {
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null);
            int index = 0;
            for (InputStream certificate : certificates) {
                String certificateAlias = Integer.toString(index++);
                Log.d(TAG, "certififcate alias "+ certificateAlias);
                keyStore.setCertificateEntry(certificateAlias, certificateFactory.generateCertificate(certificate));
                try {
                    if (certificate != null)
                        certificate.close();
                } catch (IOException e) {
                }
            }
             sslContext = SSLContext.getInstance("TLS");
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory
                    .getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);
            sslContext.init(null, trustManagerFactory.getTrustManagers(), new SecureRandom());
            return sslContext;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        //取得TLS协议的SSLContext实例
       /* try {
            SSLContext sslContext = SSLContext.getInstance("TLS");

            KeyStore clientKeyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            clientKeyStore.load(context.getResources().getAssets().open("keystore/kclient.bks"), CLIENT_KEY_PASSWORD.toCharArray());
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("X509");
            keyManagerFactory.init(clientKeyStore, CLIENT_KEY_PASSWORD.toCharArray());

            //取得BKS类型的密钥库实例，这里特别注意：手机只支持BKS密钥库，不支持Java默认的JKS密钥库 Get keystore instance of BKS type, if you just
            KeyStore trustKeyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustKeyStore.load(context.getResources().getAssets().open("keystore/tclient.bks"), TRUST_KEY_PASSWORD.toCharArray());
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance("X509");
            trustManagerFactory.init(trustKeyStore);

            //初始化SSLContext实例
            sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), null);
            return sslContext;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (UnrecoverableKeyException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }*/
        //return null;
    }
    public boolean connect() {
        Log.d(TAG, "call connect");
        BufferedReader input = null;
        PrintWriter output = null;
        SSLSocketFactory socketFactory = (SSLSocketFactory) getSSLContext(20000, keyStream).getSocketFactory();
        Log.e(TAG, "SSLCOM hostadd "+HostInformation.hostAddr+"    SSLCOMPort   "+ HostInformation.hostPort);
        try {

            socket = (SSLSocket) socketFactory.createSocket(HostInformation.hostAddr, HostInformation.hostPort);
            Log.d(TAG,"Created the socket");
            socket.startHandshake();
            Log.d(TAG, "Created the socket, input, and output!! connect");

            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.d(TAG, "connect return");
        return false;
    }


    public int send(byte[] lastdata) {

        Log.d(TAG, "call send");

        PrintWriter output = null;
        try {
            output = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
            output.write(Utility.byte2HexStr(lastdata));

            Log.e(TAG, "Sent hex output lastdata...  "+output);
            Log.e(TAG, "Sent hex lastdata...  "+Utility.byte2HexStr(lastdata));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "Created the socket, input, and output!! send");

        try {
            if (null != output) {
                output.close();
            }
        } finally {

        }

        Log.d(TAG, "send return");

        return 0;

    }


//orginal...........Recived
  public byte[] receive() {
      Log.d(TAG, "call receive");
      BufferedReader input = null;
      int reader=0;
      try {
          DataInputStream inputData = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
          String incomingData = inputData.readUTF();
          byte[] L=incomingData.getBytes();
          Log.e(TAG, "Receive line :  "+Utility.byte2HexStr(L));
          System.out.println("Received: " + incomingData);
          inputData.close();
      }
      catch (IOException e) {
          e.printStackTrace();
      }
/*
      try {
         // socket.setReceiveBufferSize(3);
          input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
          String line=null;


          while (reader!=1)
          {
            line = input.readLine();
            reader++;
             Log.d(TAG,"line is ......"+line);
             // return null;
          }

              Log.d(TAG,"SSL receive:"+line);

          byte[] L=line.getBytes();
          Log.e(TAG, "Receive line :  "+Utility.byte2HexStr(L));


      } catch (IOException e) {
          e.printStackTrace();
      }
      try {
          if (null != input) {
              Log.d(TAG,"input is closed......");
              input.close();
          }
      } catch (IOException ioe) {
      } finally {

      }
*/
      Log.d(TAG, "receive return");

      return null;

  }
//receive 2
public byte[] receive2() {
    Log.d(TAG, "call receive2");
   // BufferedReader input = null;
    int reader=0;
    char[] array= new char[10];

      try {
         // socket.setReceiveBufferSize(3);
          BufferedReader input= new BufferedReader( new InputStreamReader(socket.getInputStream()));
         // input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
          String line=null;


          //while (reader!=1)
        //  {

            line=input.readLine();
         //   reader++;
             Log.d(TAG,"line is ......"+line);
             // return null;
       //   }
   System.out.println((line));
       //       Log.d(TAG,"SSL receive:"+array);

          byte[] L=line.getBytes();
          Log.e(TAG, "Receive line :  "+Utility.byte2HexStr(L));


      } catch (IOException e) {
          e.printStackTrace();
      }
     /* try {
          if (null != input) {
              Log.d(TAG,"input is closed......");
              input.close();
          }
      } catch (IOException ioe) {
      } finally {

      }*/

    Log.d(TAG, "receive return");

    return null;

}
    public byte[] receive3( int wantLength, int timeoutSecond ){
        status=1;
        Log.e(TAG, "this is receive 3");
        if( status <= 0 ){
            Log.e(TAG, "status is null");
            return null;
        }
        try {
           // Log.e(TAG, "socket timeout");
            socket.setSoTimeout( timeoutSecond*1000 );
         //   Log.e(TAG, "socket timeout2");
            inputStream = socket.getInputStream();
          //  Log.e(TAG, "socket timeout3");
            if( null == inputStream ) {
                Log.i(TAG, "responseis :" + "null");
                return null;
            }
            byte[] tmp = new byte[wantLength];
            int recvLen = inputStream.read(tmp);
            Log.i(TAG, "response length:" + recvLen);
            if( recvLen > 0  ) {
                byte[] ret = new byte[recvLen];

                System.arraycopy(tmp,0, ret, 0, recvLen);
             //   Log.i(TAG, "response final:" + tmp);
                return ret;
            } else if( recvLen == 0 ){
                return null;
            }
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public int send3( byte[] data ) {
        if( status <= 0 ) {
            return 0;
        }

        Log.d(TAG, "SEND3:");
        Log.d(TAG, Utility.byte2HexStr(data));

        try {
            outputStream = socket.getOutputStream();
            if( null == outputStream ){
                return 0;
            }

            outputStream.write( data );
            outputStream.flush();
            return data.length;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }
    public int close() {
       // Log.d(TAG, "call close");
        try {
            socket.close();
        } catch (IOException ioe) {
        } finally {

        }

        Log.d(TAG, "close return");
        return 0;

    }
    public void ssldisconnect(){
        status = 0;
        try {
            if( null != inputStream ) {
                inputStream.close();
                inputStream = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            if( null != outputStream ){
                outputStream.close();
                outputStream = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            if( null != socket ){
                socket.close();
                socket = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        status = 0;
    }

}
