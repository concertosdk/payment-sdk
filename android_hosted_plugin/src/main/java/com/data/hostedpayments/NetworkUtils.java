package com.data.hostedpayments;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.LinkAddress;
import android.net.LinkProperties;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.util.Log;



import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.URL;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class NetworkUtils {

    static String ipResponse;
    /**
     *
     * This Method is used to get Local IP Adress IPv4and IPv6
     */
    public static void checkNetworkAndIP(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) {
            Log.d("NetworkCheck", "ConnectivityManager not available.");
            return;
        }

        Network activeNetwork = cm.getActiveNetwork();
        if (activeNetwork == null) {
            Log.d("NetworkCheck", "No active network.");
            return;
        }

        NetworkCapabilities capabilities = cm.getNetworkCapabilities(activeNetwork);
        LinkProperties linkProperties = cm.getLinkProperties(activeNetwork);

        if (capabilities == null || linkProperties == null) {
            Log.d("NetworkCheck", "Could not retrieve network details.");
            return;
        }

        String networkType = "Unknown";
        if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
            networkType = "Wi-Fi";
        } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
            networkType = "Cellular";
        }

        String ipv4 = null;
        String ipv6 = null;

        for (LinkAddress address : linkProperties.getLinkAddresses()) {
            InetAddress inetAddress = address.getAddress();
            if (inetAddress instanceof Inet4Address) {
                ipv4 = inetAddress.getHostAddress();
            } else if (inetAddress instanceof Inet6Address) {
                ipv6 = inetAddress.getHostAddress();
            }
        }

        Log.d("NetworkCheck", "Network Type: " + networkType);
        Log.d("NetworkCheck", "IPv4 Address: " + (ipv4 != null ? ipv4 : "Not available"));
        Log.d("NetworkCheck", "IPv6 Address: " + (ipv6 != null ? ipv6 : "Not available"));
    }

    // Method to get the public IP address
    public static String fetchIP(String ipUrl) {
//        String publicIPService = "http://checkip.amazonaws.com"; // Public service


        try {
            URL url = new URL(ipUrl);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setConnectTimeout(5000);
            urlConnection.setReadTimeout(5000);

            // Read the response
            BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String publicIP = in.readLine(); // Public IP as a string
            in.close();
            return publicIP;
        } catch (Exception e) {
            Log.e("PublicIP", "Failed to fetch from: " + ipUrl, e);
            return null;
        }
    }


    public static String getIpAdd()
    {
        String ipv4Url = "https://api.ipify.org";
        String ipv6Url = "https://api64.ipify.org/";

//        ipResponse = fetchIP(ipv4Url);
//        if (ipResponse != null) {
//            return "IPv4: " + ipResponse;
//        }

       ipResponse = fetchIP(ipv6Url);
       // ipResponse = fetchIPv6();
        if (ipResponse != null) {
            return  ipResponse;
        }

        return "";
    }

    public static String fetchIPv6() {
        String ipv6Url = "https://api64.ipify.org"; // This returns only IPv6 if the connection is IPv6

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(ipv6Url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                String ip = response.body().string().trim();
                // Verify it's IPv6
                if (ip.contains(":")) {
                    Log.d("IPv6", "Public IPv6: " + ip);
                    return ip;
                } else {
                    Log.e("IPv6", "Received IPv4 instead: " + ip);
                }
            }
        } catch (Exception e) {
            Log.e("IPv6", "Error fetching IPv6", e);
        }

        return null;
    }
}
