package com.mycompany.myddns;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.util.logging.Level;
import java.util.logging.Logger;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Updater {

    private Updater() {

    }

    public static void checkAndUpdate() {
        String ip = getRouterIP().trim();
        if (!ip.equals(MyDDNS.routerIP)) {
            updateIP(ip);
            if (checkDNSUpdated(ip)) {
                Logger.getLogger(Updater.class.getName()).log(Level.INFO, "IP updated to: {0}", ip);
                MyDDNS.logger.info("IP updated to: " + ip);
            } else {
                String[] temp = {MyDDNS.routerIP, ip};
                Logger.getLogger(Updater.class.getName()).log(Level.WARNING, "DNS failed to update from {0}, to {1}", temp);
                MyDDNS.logger.warning("DNS failed to update from " + MyDDNS.routerIP + ", to " + ip);
            }

            MyDDNS.routerIP = ip;
            return;
        }
        Logger.getLogger(Updater.class.getName()).log(Level.INFO, "IP stayed the same: {0}", MyDDNS.routerIP);
        MyDDNS.logger.info("IP stayed the same: " + MyDDNS.routerIP);

    }

    /**
     * Part of OkHttp https://square.github.io/okhttp/
     *
     * @return IP from the server
     */
    public static String getRouterIP() {
        try {
            return new OkHttpClient()
                    .newCall(new Request.Builder().url("http://checkip.amazonaws.com").build())
                    .execute().body().string();
        } catch (IOException ex) {
            MyDDNS.logger.severe("Can't fetch router IP");
        }
        return "";
    }

    public static void updateIP(String ip) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://www.duckdns.org/update?domains=" + MyDDNS.domain + "&token=" + MyDDNS.token + "&ip=" + ip + "&verbose=true")
                .build();

        try (Response response = client.newCall(request).execute()) {
            MyDDNS.logger.info(response.body().string().trim());

        } catch (IOException e) {
            Logger.getLogger(Updater.class.getName()).log(Level.SEVERE, e.getMessage(), e);
            MyDDNS.logger.severe(e.getMessage());

        }
    }

    public static boolean checkDNSUpdated(String ip) {
        try {
            InetAddress address = InetAddress.getByName("www." + MyDDNS.domain + ".duckdns.org");
            MyDDNS.logger.severe("Checking if DNS updated " + "www." + MyDDNS.domain + ".duckdns.org");
            MyDDNS.logger.severe("Expecting " + ip + " found " + address.getHostAddress());
            return ip.equals(address.getHostAddress());
        } catch (IOException e) {
            MyDDNS.logger.severe("During search for InetAddress search... ");
            MyDDNS.logger.severe(e.getMessage());
        }
        return false;
    }

}
