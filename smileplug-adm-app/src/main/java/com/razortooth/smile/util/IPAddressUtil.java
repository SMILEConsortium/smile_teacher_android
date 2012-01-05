package com.razortooth.smile.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class IPAddressUtil {

    public static String getIPAddress() {
        String ipaddress = "";

        try {
            Enumeration<NetworkInterface> enumnet = NetworkInterface.getNetworkInterfaces();
            NetworkInterface netinterface = null;

            while (enumnet.hasMoreElements()) {
                netinterface = enumnet.nextElement();

                for (Enumeration<InetAddress> enumip = netinterface.getInetAddresses(); enumip
                    .hasMoreElements();) {
                    InetAddress inetAddress = enumip.nextElement();

                    if (!inetAddress.isLoopbackAddress()) {
                        ipaddress = inetAddress.getHostAddress();

                        break;
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }

        return ipaddress;
    }
}
