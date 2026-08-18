package com.data.hostedpayments;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;


import com.android.volley.BuildConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Console;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Checkout extends AppCompatActivity {

    WebView mWeb;
    ProgressDialog mProgress;
    String weburl;
    private Sha1Encryption hash = new Sha1Encryption();
    String repMsg1,payId,amount,merchantKey;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        Bundle extras = getIntent().getExtras();

        mWeb = findViewById(R.id.webView1);

        mWeb.getSettings().setJavaScriptEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR_MR1) {
            mWeb.getSettings().setDomStorageEnabled(true);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mWeb.setWebContentsDebuggingEnabled(true); // Enable debugging
        }

        weburl = extras.getString("weburl");
        repMsg1 = extras.getString("respData");
        payId = extras.getString("payid");
        context = this;



        if (weburl.equalsIgnoreCase("")) {
            ResponseConfig.startTrxn = false;
            System.out.println("Response Body :"+repMsg1);
            Intent intent = new Intent();
            intent.putExtra("MESSAGE", repMsg1);
            setResult(2, intent);
            finish();
        } else {
            WebSettings settings = mWeb.getSettings();
            settings.setJavaScriptEnabled(true);
            settings.setDefaultTextEncodingName("utf-8");
            settings.setDomStorageEnabled(true);
            settings.setSupportZoom(true);
            settings.setBuiltInZoomControls(true);
            settings.setLoadWithOverviewMode(true);
            settings.setJavaScriptCanOpenWindowsAutomatically(true);
            settings.setSupportMultipleWindows(true);
            mProgress = ProgressDialog.show(this, "Loading", "Please wait...");
            if (BuildConfig.DEBUG) {
                WebView.setWebContentsDebuggingEnabled(true);
            }

            mWeb.setWebViewClient(new WebViewClient() {
                // load url

                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    if (context instanceof Activity) {
                        if (!((Activity) context).isFinishing())
                            mProgress.show();
                    }
                    // Uri request=Uri.parse(url);

                    if (isCustomPaymentScheme(url)) {
                     //   url = "phonepe://pay?tr=2517119163863031075&ver=01&mode=04&orgId=159019&Purpose=01&tn=TEST-INDIAN-BANK&pa=Concerto.vegaaH%40indianbk&pn=ConcertoProdTest&mc=7399&am=1.00&mam=1.00&cu=INR&qrMedium=02";

                        launchAppFromCustomScheme(context, url);
                        return true;
                    }
                        else {
                    view.loadUrl(url);
                    return false;
                        }


                }
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                    String url = request.getUrl().toString();
                    System.out.println(" shouldOverrideUrlLoading -- "+url);
                    if (isCustomPaymentScheme(url)) {
                        //url = "phonepe://pay?tr=2517119163863031075&ver=01&mode=04&orgId=159019&Purpose=01&tn=TEST-INDIAN-BANK&pa=Concerto.vegaaH%40indianbk&pn=ConcertoProdTest&mc=7399&am=1.00&mam=1.00&cu=INR&qrMedium=02";
                        launchAppFromCustomScheme(context, url);
                        return true;
                    }
                    return false;
                }


                public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                    super.onReceivedError(view, request, error);
//                       System.out.println("view = " + view + ", request = " + request + ", error = " + error);
//                       Toast.makeText(Checkout.this, "Error in Loading Data"+error.toString(), Toast.LENGTH_SHORT).show();

                }

                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    super.onPageStarted(view, url, favicon);
                    if (context instanceof Activity) {
                        if (!((Activity) context).isFinishing())
                            mProgress.show();
                    }


                }

                // when finish loading page
                public void onPageFinished(WebView view, final String url) {
                    if (mProgress != null && mProgress.isShowing()) {
                        mProgress.dismiss();
                    }
                    //System.out.println(url);
                    if (url.contains ("?data"))
                    {
                        try {
                            // Create URI from URL
                            URI uri = new URI(url);
                            // Get query from URI
                            String query = uri.getQuery();
                            // Parse query string to get parameters
                            Map<String, String> queryParams = parseQuery(query);
                            // Extract 'data' parameter value
                            String data = queryParams.get("data");
                            // Log or use the data

                            // URL decode and replace spaces with '+'
                        String decodedData = java.net.URLDecoder.decode(data, "UTF-8");
                        decodedData = decodedData.replace(' ', '+');
                            // Decrypt the data
                        String encryptedResponse = decodedData;
                        String merKey = ConstantsVar.merchanKey; // Ensure this key is set correctly
                        String decryptedData = DecryptionUtil.decodeAndDecryptV2(encryptedResponse, merKey);
                            System.out.println("Response Body: "+ decryptedData );
                            // Print or use the decrypted data
                            // Log.d(TAG, "Decrypted Data: " + decryptedData);
                        ResponseConfig.startTrxn = false;
                        Intent intent = new Intent();
                        intent.putExtra("MESSAGE", decryptedData);
                        setResult(2, intent);
                        finish();

                        } catch (Exception e) {
                            Log.e(TAG, "Exception: ", e);
                        }
                    }

//                        String respParameters[] = url.split("\\?");
//                        JSONObject newData = null;
//                        try {
//                            newData = splitResponse(respParameters[1]);
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                        ResponseConfig.startTrxn = false;
//                        Intent intent = new Intent();
//                        intent.putExtra("MESSAGE", newData.toString());
//                        setResult(2, intent);
//                        finish();
//                    }

                }
            });

            mWeb.loadUrl(weburl);
        }

    } private Map<String, String> parseQuery(String query) {
        Map<String, String> params = new HashMap<>();
        if (query != null) {
            String[] pairs = query.split("&");
            for (String pair : pairs) {
                String[] keyValue = pair.split("=");
                if (keyValue.length == 2) {
                    try {
                        params.put(URLDecoder.decode(keyValue[0], StandardCharsets.UTF_8.name()),
                                URLDecoder.decode(keyValue[1], StandardCharsets.UTF_8.name()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return params;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ResponseConfig.startTrxn =false;
        //loadurl=false;
    }

    public String generateHashKey(JSONObject jsonObj, String merchantKey) {
        String pipeSeperatedString = "";
        String hashKey = null;
JSONObject orderdetail;
        String orderid= "";



        try {
              orderdetail = jsonObj.getJSONObject("order");
            orderid= orderdetail.get("orderId").toString();

            //System.out.println("orderId123" + orderid);
            pipeSeperatedString = orderid + "|" + jsonObj.get("terminalId") + "|" + jsonObj.get("password") + "|" + merchantKey + "|" + jsonObj.get("amount") + "|" + jsonObj.get("currency");

            hashKey = hash.SHA256(pipeSeperatedString);
        } catch (Exception ex) {
            // TODO Auto-generated catch block
            ex.printStackTrace();
        }
            return hashKey;

    }



    public JSONObject generateJson(String terminal_Id, String terminal_Pass, String action_Code, String Currency,
                                   String Amount, JSONObject cardOper, String tokenType,
                                   JSONObject orderobject, JSONObject customerdetails, String trans_Id, JSONObject metadata, String ipaddress, JSONObject deviceInfo, JSONObject paymentInstr, JSONObject airlineData)
            throws UnsupportedEncodingException {



        JSONObject testJson = new JSONObject();
//        System.out.println("deviceJson :"+deviceInfo);
        try {
            testJson.put("terminalId", terminal_Id);
            testJson.put("password", terminal_Pass);
            testJson.put("paymentType", action_Code);
            testJson.put("currency", Currency);

            testJson.put("amount", Amount);
            amount = Amount;
           // merchantKey=merchantk;


            testJson.put("order", orderobject);
            testJson.put("customer", customerdetails);
            testJson.put("deviceInfo", deviceInfo.toString());
            testJson.put("additionalDetails", metadata);
            if (airlineData != null && airlineData.length() > 0) {
                testJson.put("airline", airlineData);
            }
           // testJson.put("merchantIp", ipaddress);
            //testJson.put("customerIp", ipaddress);
            testJson.put("paymentInstrument", paymentInstr);


            if(action_Code.equalsIgnoreCase("12") )
            {
                testJson.put("tokenization", cardOper);
                if(cardOper.toString().equalsIgnoreCase("U"))
                {
//                  testJson.put("maskedPAN", cardMsked);
                    testJson.put("cardToken", cardOper.get("cardToken"));
                }
                else if(cardOper.toString().equalsIgnoreCase("D"))
                {
                    testJson.put("cardToken", cardOper.get("cardToken"));
                  //  testJson.put("instrumentType", "CCI");

                }
            }

            else if((action_Code.equalsIgnoreCase("1")) || (action_Code.equalsIgnoreCase("4")))
            {
                testJson.put("customer", customerdetails);
                testJson.put("tokenization", cardOper);
                testJson.put("tokenizationType", tokenType);
            }
            else if(action_Code.equalsIgnoreCase("2") || action_Code.equalsIgnoreCase("3") || action_Code.equalsIgnoreCase("9") || action_Code.equalsIgnoreCase("10") || action_Code.equalsIgnoreCase("6") )
            {
                testJson.put("referenceId", trans_Id);
            }

        }
        catch (JSONException e1)
        {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        return testJson;
    }
    private void launchAppFromCustomScheme(Context context, String upiUri) {
        try {
//            Uri uri = Uri.parse(upiUri);
            Intent intent = new Intent(Intent.ACTION_VIEW);
//            PackageManager packageManager = context.getPackageManager();
            intent.setData(Uri.parse(upiUri));
            // Match scheme and assign package manually
            if (upiUri.startsWith("gpay://")) {
                intent.setPackage("com.google.android.apps.nbu.paisa.user");
            } else if (upiUri.startsWith("phonepe://")) {
                intent.setPackage("com.phonepe.app");
            } else if (upiUri.startsWith("paytmmp://")) {
                intent.setPackage("net.one97.paytm");
            } else if (upiUri.startsWith("cred://")) {
                intent.setPackage("com.dreamplug.androidapp"); // CRED package
            }

//            if (intent.resolveActivity(context.getPackageManager()) != null) {
                context.startActivity(intent);
//            } else {
//                Toast.makeText(context, "No app installed to handle this payment", Toast.LENGTH_LONG).show();
//            }
        }
       catch (Exception e) {
            Toast.makeText(context, "Google Pay is not installed.", Toast.LENGTH_LONG).show();
            e.printStackTrace();

            // Optionally redirect to Play Store
           // redirectToPlayStore(context, "com.google.android.apps.nbu.paisa.user");
        }
    }
    private boolean isCustomPaymentScheme(String url) {
        return url.startsWith("phonepe://") ||
                url.startsWith("paytmmp://") ||
                url.startsWith("gpay://") ||
                url.startsWith("cred://");
    }

}