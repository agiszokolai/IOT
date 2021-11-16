package Program;

import java.util.Arrays;
import java.util.Scanner;

public class Calculator {

    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        // IPV4-es cím binaris alakja
        String[] ipv4Binary = new String[4];

        // IPV4-es cím
        String ipv4;

        // Addig kéri az ip címet amíg az nem helyes
        do {
            System.out.print("IPV4: ");
            ipv4 = scanner.next();
        } while (!checkValidIPV4(ipv4));

        // Tagolja az ip címet
        String[] octets = ipv4.split("\\.");

        // ip cím binaris átalakítása
        for (int i = 0; i < ipv4Binary.length; i++) {
            ipv4Binary[i] = BinaryResolution(Integer.parseInt(octets[i]));
        }
        System.out.print("IP Binary: ");
        for (int i = 0; i < ipv4Binary.length; i++) {
            System.out.print(ipv4Binary[i] + " ");
        }

        //netmaskok listája
        System.out.println("\nNetmask címek:");
        String[] netmasks =
                {"255.255.255.255","255.255.255.254","255.255.255.252", "255.255.255.248", "255.255.255.240", "255.255.255.224",
                "255.255.255.192", "255.255.255.128", "255.255.255.0", "255.255.254.0", "255.255.252.0", "255.255.248.0",
                "255.255.240.0", "255.255.224.0", "255.255.192.0", "255.255.128.0", "255.255.0.0", "255.254.0.0",
                "255.252.0.0", "255.248.0.0", "255.240.0.0", "255.224.0.0", "255.192.0.0", "255.128.0.0", "255.0.0.0","254.0.0.0",
                "252.0.0.0","248.0.0.0","240.0.0.0","224.0.0.0","192.0.0.0","128.0.0.0"};

        for (int i = 0; i < netmasks.length; i++) {

            System.out.print(netmasks[i]+"   ");
            if(i % 7 == 0){
                System.out.println("\r");
            }
        }
        //netmask ertéke
        String netmask;

        do {
            System.out.print("\nAdj meg egy helyes netmaskot: ");
            netmask = scanner.next();
        } while (!(Arrays.asList(netmasks).contains(netmask)));

        //netmask oktetekre tagolása
        String[] octetsOfNetmask = netmask.split("\\.");

        //A netmask binaris alakja
        String[] binaryNetmask = new String[4];

        for (int i = 0; i < binaryNetmask.length; i++) {
            binaryNetmask[i] = BinaryResolution(Integer.parseInt(octetsOfNetmask[i]));
        }

        System.out.print("\nNetmask Binary: ");
        for (int i = 0; i < binaryNetmask.length; i++) {
            System.out.print(binaryNetmask[i]+ " ");
        }

        //A network cím bináris alakja
        String[] BinaryNetwork = new String[4];
        for (int i = 0; i < BinaryNetwork.length; i++) {
            BinaryNetwork[i] = String.valueOf(NetworkAddress(ipv4Binary[i],binaryNetmask[i]));
            //BinaryNetwork[i] = BinaryResolution(Integer.parseInt(octetsNetwork[i]));
        }

        // A network cím oktet értékei
        String[] octetsNetwork = new String[4];
        for (int i = 0; i < 4; i++) {
           // octetsNetwork[i] = String.valueOf(NetworkAddress(ipv4Binary[i],binaryNetmask[i]));
            octetsNetwork[i] = DecimalResolution(BinaryNetwork[i]);
        }

        System.out.print("\nNetwork Address: ");
        for (int i = 0; i < 4; i++) {
            System.out.print(octetsNetwork[i]+".");
        }

        System.out.print("\nBinary Network Address: ");
        for (int i = 0; i < 4; i++) {
            System.out.print(BinaryNetwork[i]+" ");
        }

        //változó az egyesek széméra a netmaskb bináris alakjában
        int ones = numberOfOnes(binaryNetmask);

        //Broadcast cím bináris alakja
        String[] BinaryBroadcast = new String[4];

        //netmaskban levo egyesek darabszama utan feltolteni a network address binary szamat egyesekkel
        int karakter = 0;
        for (int i = 0; i < BinaryNetwork.length; i++) {
            StringBuilder value = new StringBuilder();
            for (int j = 0; j < 8; j++) {
                karakter++;
                if(karakter > ones){
                    value.append("1");
                }else value.append(BinaryNetwork[i].charAt(j));
            }
            BinaryBroadcast[i] = String.valueOf(value);
        }

        System.out.print("\nBinary Broadcast Address: ");
        for (int i = 0; i < 4; i++) {
            System.out.print(BinaryBroadcast[i]+" ");
        }

        //Broadcast cím oktet értékei
        String[] octetBroadcast = new String[4];
        for (int i = 0; i < BinaryBroadcast.length; i++) {
           octetBroadcast[i] = String.valueOf(Integer.parseInt(BinaryBroadcast[i],2));

        }
        System.out.print("\nBroadcast Address: ");
        for (int i = 0; i < 4; i++) {
            System.out.print(octetBroadcast[i]+".");
        }

        //1st host
        String[] firstHost = Arrays.copyOf(octetsNetwork,octetsNetwork.length);
        int helper = Integer.parseInt(firstHost[3]);
        firstHost[3] = String.valueOf(helper+1);

        //last host
        String[] lastHost = Arrays.copyOf(octetBroadcast, octetBroadcast.length);
        helper = Integer.parseInt(lastHost[3]);
        lastHost[3] = String.valueOf(helper-1);

        System.out.print("\n1st host: ");
        for (int i = 0; i < 4; i++) {
            System.out.print(firstHost[i]+".");
        }
        System.out.print("\nLast host: ");
        for (int i = 0; i < 4; i++) {
            System.out.print(lastHost[i]+".");
        }

        System.out.println("\nNumber of  Hosts: "+ numberOfAllHosts(numberOfZeros(binaryNetmask)));
        System.out.println("Number of Usable Hosts: "+ numberOfUsableHosts(numberOfZeros(binaryNetmask)));

    }

    /**
     * Metódus a network cím kiszámolására
     *
     * @param
     * @param
     * @return tmp.toString()
     */
    public static String NetworkAddress(String binIP, String binMask) {
        StringBuilder tmp = new StringBuilder();
        int a = Integer.parseInt(binIP);
        int b = Integer.parseInt(binMask);

        /*for (int i = 0; i < 8; i++) {
            if(a < b){
                tmp.append(String.valueOf(a));
            }else tmp.append(String.valueOf(b));
        }*/
        for (int i = 0; i < 8; i++) {
            if(Integer.parseInt(String.valueOf(binIP.charAt(i))) < Integer.parseInt(String.valueOf(binMask.charAt(i)))){
                tmp.append(binIP.charAt(i));
            }else tmp.append(binMask.charAt(i));
        }
        return tmp.toString();
    }

    /**
     * Metódus az összes host kiszámítására
     * @param nulls
     * @return
     */
    public static int numberOfAllHosts(int nulls){
        int h = (int)Math.pow(2,nulls);
        return h;
    }

    /**
     * Metódus a használható hostok kiszámítására
     * @param nulls
     * @return
     */
    public static int numberOfUsableHosts(int nulls){
        int h = (int)Math.pow(2,nulls)-2;
        return h;
    }

    /**
     * Metódus a bináris alakban lévő címben lévő nullák megszámolására
     * @param array
     * @return
     */
    public static int numberOfZeros(String [] array){
        int db = 0;
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < 8; j++) {
                if(array[i].charAt(j) == '0'){
                    db++;
                }
            }
        }
        return db;
    }

    /**
     * Metódus a bináris alakban lévő címben lévő egyesek megszámolására
     * @param array
     * @return
     */
    public static int numberOfOnes(String [] array){
        int db = 0;
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < 8; j++) {
                if(array[i].charAt(j) == '1'){
                    db++;
                }
            }
        }
        return db;
    }
    /**
     * Metódus az oktetek binárissá alakítására
     * @param octet
     * @return
     */

    public static String BinaryResolution(int octet) {
        int[] binarySystem = {128, 64, 32, 16, 8, 4, 2, 1};
        StringBuilder binaryString = new StringBuilder();

        for (int binary : binarySystem) {
            if (octet - binary >= 0) {
                binaryString.append("1");
                octet -= binary;
                continue;
            }
            binaryString.append("0");
        }
        return binaryString.toString();
    }
    public static String DecimalResolution(String binary) {
        int[] binarySystem = {128, 64, 32, 16, 8, 4, 2, 1};
        int binaryString = 0;

        for (int i = 0; i < 8 ; i++) {
            if (binary.charAt(i) == '1'){
               binaryString+= binarySystem[i];
            }
        }
        return String.valueOf(binaryString);
    }

    /**
     * Metódus az IPV4 cím vizsgálatához
     * @param ipv4
     * @return boolean
     */

    public static boolean checkValidIPV4(String ipv4){

        // fsfsdf
        if (ipv4.length() < 7 || ipv4.length() > 15 ) return false;

        String[] array = ipv4.split("\\.");

        if (array.length != 4) return false;

        for (String element: array) {
            if (!isNumber(element) || Integer.parseInt(element) > 255 || Integer.parseInt(element) < 0) return false;
        }
        return true;
    }

    /**
     * Metódus, az oktetben lévő karakterek vizsgálatához
     * @param str
     * @return
     */
    private static boolean isNumber(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }
}