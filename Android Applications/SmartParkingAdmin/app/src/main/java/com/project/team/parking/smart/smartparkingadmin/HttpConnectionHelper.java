package com.project.team.parking.smart.smartparkingadmin;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by ChenYu Wu on 4/20/2016.
 */
public class HttpConnectionHelper {
    public static int EXCEPTIONRESPONSECODE = -999;

    public static int DEFAULT_CONNECT_TIME_OUT = 5000;
    public static int DEFAULT_READ_TIME_OUT = 5000;

    private HttpURLConnection mConn;
    private String mErrorMsg;
    private int mResponseCode;
    private String mResponseString;

    public HttpConnectionHelper(String sURL, String connectMethod) throws IOException {
        URL url = new URL(sURL);
        mConn = (HttpURLConnection) url.openConnection();
        mConn.setConnectTimeout(DEFAULT_CONNECT_TIME_OUT);
        mConn.setRequestMethod(connectMethod);
    }

    public HttpConnectionHelper(String sURL, String connectMethod, int connectTimeOut) throws IOException {
        URL url = new URL(sURL);
        mConn = (HttpURLConnection) url.openConnection();
        mConn.setConnectTimeout(connectTimeOut);
        mConn.setRequestMethod(connectMethod);
    }

    public void setRequestProperty(String field, String value) {
        mConn.setRequestProperty(field, value);
    }

    public int request_Input(int readTimeOut) {
        try {
            mConn.setDoInput(true);
            mConn.setReadTimeout(readTimeOut);

            mConn.connect();

            mResponseCode = mConn.getResponseCode();

            InputStream inStream = new BufferedInputStream(mConn.getInputStream());
            mResponseString = getResponseText(inStream);

            return mResponseCode;

        }   catch (IOException e) {
            mErrorMsg = e.getMessage();
            return EXCEPTIONRESPONSECODE;
        }
    }

    public int request_Output(String outData) {
        try {
            mConn.setDoOutput(true);

            OutputStreamWriter outStream = new OutputStreamWriter(mConn.getOutputStream());
            outStream.write(outData);
            outStream.close();

            mConn.connect();

            mResponseCode = mConn.getResponseCode();

            return mResponseCode;

        }   catch (IOException e) {
            mErrorMsg = e.getMessage();
            return EXCEPTIONRESPONSECODE;
        }
    }

    public int request_InOutput(int readTimeOut, String outData) {
        try {
            mConn.setDoInput(true);
            mConn.setDoOutput(true);
            mConn.setReadTimeout(readTimeOut);

            OutputStreamWriter outStream = new OutputStreamWriter(mConn.getOutputStream());
            outStream.write(outData);
            outStream.flush();
            outStream.close();

            mResponseCode = mConn.getResponseCode();

            InputStream inStream = new BufferedInputStream(mConn.getInputStream());
            mResponseString = getResponseText(inStream);

            return mResponseCode;

        }   catch (IOException e) {
            mErrorMsg = e.getMessage();
            return EXCEPTIONRESPONSECODE;
        }
    }

    public String getErrorMsg() {
        return mErrorMsg;
    }

    public int getResponseCode() {
        return mResponseCode;
    }

    public String getResponseString() {
        return mResponseString;
    }

    private String getResponseText(InputStream inStream) {
        return new Scanner(inStream).useDelimiter("\\A").next();
    }
}
