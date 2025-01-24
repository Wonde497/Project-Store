package com.verifone.demo.emv.Utilities;

import android.content.Context;
import android.util.Log;
import android.util.Pair;

import com.verifone.demo.emv.basic.HostInformation;

import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.CertificateFactory;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManagerFactory;

import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by chong.z on 2018/5/22.
 */

public class HttpsComm {
    private static final String TAG = "HttpsComm";
    private static String SERVER_CER_NAME = "cacert.pem";
    private static String DEBUG_CER_NAME = "testOnly.cer";

    private static final int COMM_OK = 0;
    private static final int CONNNET_ILLEGAL_ARGUMENT = 10;
    private static final int CONNECT_ILLEGAL_URL = 11;
    private static final int CERT_NOT_FOUND = 12;
    private static final int RECV_TIMEOUT = 20;
    private static final int CONNECT_EXCEPTION = 21;
    private static final int NO_DATA = 22;
    private static final int IO_EXCEPTION = 23;
    private static final int NULL_POINTER_EXCEPTION = 24;
    private static final int NO_RESPONSE = 25;
    private static final int RESPONSE_CODE_INCORRECT = 26;
    private static final int RESPONSE_FAILED = 27;

    private Context context;
    private OkHttpClient client;
    private Request request;

    public HttpsComm(Context context) {
        this.context = context;
        Log.d(TAG, "HttpsComm");
    }

    /**
     * Load SSL certification
     * @return status
     */
    private int httpsLoadCert(int timeOutMillisecond, boolean isDebug) {
        try {
            InputStream keyStream;
            if (isDebug)
                keyStream = context.getAssets().open(DEBUG_CER_NAME);
            else
                keyStream = context.getAssets().open(SERVER_CER_NAME);
            client = getHttpClient(timeOutMillisecond, keyStream);
        } catch (IOException e) {
            Log.i(TAG,"ssl certificate not found, name is " + SERVER_CER_NAME);
            e.printStackTrace();
            return CERT_NOT_FOUND;
        }
        if (client == null) {
            Log.w(TAG,"httpClient == null");
            return CERT_NOT_FOUND;
        }

        return COMM_OK;
    }

    /**
     * Build http request
     * @param hostInfo CommData
     * @return status
     */
    private int httpsBuildRequest(HostInformation hostInfo) {
        //String test = "600601000060320043000108000020000000c0001600014930303030303031383130323331303036303531303030320011000004300030002953657175656e6365204e6f313633323639563332392d3039352d3030330003303132";

        if (hostInfo == null) {
            Log.i(TAG,"commParamIn = null");
            return CONNNET_ILLEGAL_ARGUMENT;
        }

        String url = "https://" + hostInfo.getHostIp() + ":" + hostInfo.getPort() + hostInfo.getUrlPath();
        Log.i(TAG,"url: " + url);

        //commParamIn.setSendData(Utils.hexStr2Bytes(test));

        // prepare data
        byte[] sendDataPackage = new byte[64 + 2];
//        byte[] sendDataPackage = new byte[hostInfo.getSendData().length + 2];
//        sendDataPackage[0] = (byte) ((hostInfo.getSendData().length >> 8) & 0xFF);
//        sendDataPackage[1] = (byte) (hostInfo.getSendData().length & 0xFF);
//        System.arraycopy(hostInfo.getSendData(),0, sendDataPackage,2, hostInfo.getSendData().length);

//        Log.i(TAG,"https send length: " + sendDataPackage.length);
//        Log.i(TAG,"https send data(hex): " + Utils.byte2HexStr(sendDataPackage));

        // prepare http request
        try {
            MediaType TEXT = MediaType.parse("x-ISO-TPDU/x-auth");
            RequestBody body = RequestBody.create(TEXT, sendDataPackage);

            request = new Request.Builder()
                    .url(url)
                    .header("User-Agent", "Donjin Http 0.1")
                    .header("Cache-Control", "no-cache")
                    .header("Content-Type", "x-ISO-TPDU/x-auth")
                    .header("Accept", "*/*")
                    .post(body)
                    .build();
        } catch (Exception e) {
            Log.i(TAG,"SyncPost: illegal URL");
            e.printStackTrace();
            return CONNECT_ILLEGAL_URL;
        }

        return COMM_OK;
    }

    private Pair<Integer, byte[]> httpsPost() {
        Response response;

        //send & recv
        try {
            Log.i(TAG,"client.newCall execute");
            //block here
            response = client.newCall(request).execute();
            Log.i(TAG,"client.newCall done");
        } catch (ConnectException e) {
            Log.i(TAG,"SyncPost() ConnectException : " + e);
            e.printStackTrace();
            return Pair.create(CONNECT_EXCEPTION, null);
        } catch (SocketTimeoutException e) {
            Log.i(TAG,"SyncPost() SocketTimeoutException : " + e);
            e.printStackTrace();
            return Pair.create(RECV_TIMEOUT, null);
        } catch (IOException e) {
            Log.i(TAG,"SyncPost() IOException : " + e);
            e.printStackTrace();
            return Pair.create(IO_EXCEPTION, null);
        } catch (NullPointerException e) {
            Log.i(TAG,"SyncPost() NullPointerException : " + e);
            e.printStackTrace();
            return Pair.create(NULL_POINTER_EXCEPTION, null);
        }
        if (response == null) {
            Log.i(TAG,"SyncPost() response is null");
            return Pair.create(NO_RESPONSE, null);
        }

        // deal response
        if (response.isSuccessful()) {
            Log.i(TAG,"SyncPost() Http response code: " + response.code());

            //print headers for debug
            StringBuilder stringBuilder = new StringBuilder();
            Headers responseHeaders = response.headers();
            for (int i = 0; i < responseHeaders.size(); i++) {
                stringBuilder.append(responseHeaders.name(i) + ": " + responseHeaders.value(i) + "\n");
            }
            Log.i(TAG,"SyncPost() Https response heads: \n" + stringBuilder.toString());

            if (response.code() != 200) {
                Log.w(TAG,"SyncPost() response.code != 200: " + response.code());
                return Pair.create(RESPONSE_CODE_INCORRECT, null);
            }

            Log.i(TAG,"message : " + response.message());
            byte[] data;
            try {
                data = response.body().bytes(); //get only once
            } catch (IOException e) {
                e.printStackTrace();
                Log.w(TAG,"response.body().string() error");
                return Pair.create(NO_DATA, null);
            }

            if (data == null) {
                Log.w(TAG,"response.body().string() = null");
                return Pair.create(NO_DATA, null);
            }

            //debug message
            Log.i(TAG,"body len = " + data.length);
            Log.i(TAG,"body(hex) : \n" + Utility.byte2HexStr(data));

            //remove 2 bytes headers that indicate 8583 pack length
            byte[] recvData = new byte[data.length - 2];
            System.arraycopy(data,2, recvData,0, data.length - 2);

            return Pair.create(COMM_OK, recvData);
        } else {
            Log.i(TAG,"SyncPost() response.isSuccessful()=false!");
            return Pair.create(RESPONSE_FAILED, null);
        }
    }

    private static OkHttpClient getHttpClient(int timeOutMillisecond, InputStream... certificates) {
        long timeout = timeOutMillisecond - 1;
        try {
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null);
            int index = 0;
            for (InputStream certificate : certificates) {
                String certificateAlias = Integer.toString(index++);
                keyStore.setCertificateEntry(certificateAlias, certificateFactory.generateCertificate(certificate));
                try {
                    if (certificate != null)
                        certificate.close();
                } catch (IOException e) {
                }
            }
            SSLContext sslContext = SSLContext.getInstance("TLS");
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory
                    .getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);
            sslContext.init(null, trustManagerFactory.getTrustManagers(), new SecureRandom());
            OkHttpClient.Builder builder = new OkHttpClient.Builder().connectTimeout(timeout, TimeUnit.MILLISECONDS)
                    .readTimeout(timeout, TimeUnit.MILLISECONDS).writeTimeout(timeout, TimeUnit.MILLISECONDS);
            builder.sslSocketFactory(sslContext.getSocketFactory());
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
            return builder.build();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void commHttps2Server(HostInformation hostInformation/*, NetCallBackListener listener*/) {
        int ret;
        boolean isDebug = false;
        Pair<Integer, byte[]> pair;
        Log.i(TAG, "正在连接加密网络..... 主机IP: " + hostInformation.getHostIp() + "  主机PORT: " + hostInformation.getPort());

//        listener.onNext(CommData.newInstance(CommStatus.ON_CONNECTING, 0));

        //仅仅为了测试，端口为20000时用tomcat证书
        if (hostInformation.getPort() == 20000)
            isDebug = true;

        ret = httpsLoadCert(hostInformation.getTimeout(hostInformation.TYPE_TIMEOUT_RECEIVE), isDebug);
        if (COMM_OK != ret) {
            Log.e(TAG, "SSL证书错误");
//            listener.onError(new ProException(NetWork_SSL_Exception));
            return;
        }
//
//        listener.onNext(CommData.newInstance(CommStatus.ON_CONNECTED, 0));
//        listener.onNext(CommData.newInstance(CommStatus.ON_SENTED, 0));
//        listener.onNext(CommData.newInstance(CommStatus.ON_RECEIVING, null));

//        if (hostInformation.getSendData() == null) {
//            Log.e(TAG, "发送数据为空");
////            listener.onError(new ProException(NetWork_Send_Exception));
//            return;
//        }

        ret = httpsBuildRequest(hostInformation);
        if (CONNNET_ILLEGAL_ARGUMENT == ret) {
            Log.e(TAG, "SSL参数错误");
//            listener.onError(new ProException(NetWork_Send_Exception));
            return;
        } else if (CONNECT_ILLEGAL_URL == ret) {
            Log.e(TAG, "地址不合法");
//            listener.onError(new ProException(NetWork_Send_Exception));
            return;
        }

        pair = httpsPost();
        int retVal = pair.first;
        byte[] retData = pair.second;

        switch (retVal) {
            case COMM_OK:
                Log.i(TAG, "接收数据成功");
//                listener.onNext(CommData.newInstance(CommStatus.ON_RECEIVED, retData));
                break;
            case RECV_TIMEOUT:
                Log.e(TAG, "接收超时");
//                listener.onError(new ProException(NetWork_Timeout_Exception));
                break;
            case CONNECT_EXCEPTION:
                Log.e(TAG, "CONNECT_EXCEPTION");
//                listener.onError(new ProException(NetWork_Connect_Exception));
                break;
            case NO_DATA:
                Log.e(TAG, "NO_DATA");
//                listener.onError(new ProException(NetWork_Receive_Exception));
                break;
            case IO_EXCEPTION:
                Log.e(TAG, "IO_EXCEPTION");
//                listener.onError(new ProException(NetWork_Connect_Exception));
                break;
            case NULL_POINTER_EXCEPTION:
                Log.e(TAG, "NULL_POINTER_EXCEPTION");
//                listener.onError(new ProException(NetWork_Connect_Exception));
                break;
            case NO_RESPONSE:
                Log.e(TAG, "NO_RESPONSE");
//                listener.onError(new ProException(NetWork_Receive_Exception));
                break;
            case RESPONSE_CODE_INCORRECT:
                Log.e(TAG, "RESPONSE_CODE_INCORRECT");
//                listener.onError(new ProException(NetWork_Receive_Exception));
                break;
            case RESPONSE_FAILED:
                Log.e(TAG, "RESPONSE_FAILED");
//                listener.onError(new ProException(NetWork_Receive_Exception));
                break;
            default:
                Log.e(TAG, "ProException: 网络连接失败");
//                listener.onError(new ProException(NetWork_Connect_Exception));
                break;
        }
    }

}

