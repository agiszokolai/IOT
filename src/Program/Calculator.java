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
        String[] octetsIp = ipv4.split("\\.");

        // ip cím binaris átalakítása
        for (int i = 0; i < ipv4Binary.length; i++) {
            ipv4Binary[i] = BinaryResolution(Integer.parseInt(octetsIp[i]));
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
            if(i >= 17 && i != 24){
                System.out.print("\t"+netmasks[i]+"\t\t");
            }
            else System.out.print(netmasks[i]+"\t\t");
            if(i == 7 || i == 15 || i == 23 ){
                System.out.println("\r");
            }
        }
        //netmask értéke
        String netmask;

        do {
            System.out.print("\nAdj meg egy helyes netmaskot: ");
            netmask = scanner.next();
        } while (!(Arrays.asList(netmasks).contains(netmask)));

        //netmask oktetekre tagolása
        String[] octetsNetmask = netmask.split("\\.");

        //A netmask binaris alakja
        String[] binaryNetmask = new String[4];

        for (int i = 0; i < binaryNetmask.length; i++) {
            binaryNetmask[i] = BinaryResolution(Integer.parseInt(octetsNetmask[i]));
        }

        //A network cím bináris alakja
        String[] binaryNetwork = new String[4];
        for (int i = 0; i < binaryNetwork.length; i++) {
            binaryNetwork[i] = (NetworkAddress(ipv4Binary[i],binaryNetmask[i]));
        }

        // A network cím oktet értékei
        String[] octetsNetwork = new String[4];
        for (int i = 0; i < 4; i++) {
            octetsNetwork[i] = DecimalResolution(binaryNetwork[i]);
        }

        //változó az egyesek számára a netmask bináris alakjában
        int ones = numberOfOnes(binaryNetmask);

        //Broadcast cím bináris alakja
        String[] binaryBroadcast = new String[4];

        //netmaskban levo egyesek darabszama utan feltolteni a network address binary szamat egyesekkel
        int karakter = 0;

        for (int i = 0; i < binaryNetwork.length; i++) {
            StringBuilder value = new StringBuilder();

            for (int j = 0; j < 8; j++) {
                karakter++;

                if (karakter > ones) {
                    value.append("1");

                } else value.append(binaryNetwork[i].charAt(j));
            }
            binaryBroadcast[i] = String.valueOf(value);
        }

        //Broadcast cím oktet értékei
        String[] octetBroadcast = new String[4];
        for (int i = 0; i < binaryBroadcast.length; i++) {
           octetBroadcast[i] = String.valueOf(Integer.parseInt(binaryBroadcast[i],2));
        }

        //1st host
        String[] firstHost = Arrays.copyOf(octetsNetwork, octetsNetwork.length);
        int helper = Integer.parseInt(firstHost[3]);
        firstHost[3] = String.valueOf(helper+1);

        //last host
        String[] lastHost = Arrays.copyOf(octetBroadcast, octetBroadcast.length);
        helper = Integer.parseInt(lastHost[3]);
        lastHost[3] = String.valueOf(helper-1);

        printDatas(octetsIp,ipv4Binary,octetsNetmask,binaryNetmask,octetsNetwork,binaryNetwork,octetBroadcast,binaryBroadcast, firstHost,lastHost);

    }
    public static void printDatas(String[] octetsIp, String[] ipv4Binary, String[] octetsNetmask, String[] binaryNetmask,
                                  String[] octetsNetwork, String[] binaryNetwork, String[] octetBroadcast, String[] binaryBroadcast,
                                  String[] firstHost, String[] lastHost){
        System.out.print("\nA megadott IP cím: ");
        decimalPrint(octetsIp);
        System.out.print("\nIP Binary: ");
        binaryPrint(ipv4Binary);
        System.out.println("\nAz IP cím '"+ipClass(octetsIp)+"' osztályú");
        System.out.print("\nA megadott netmask cím: ");
        decimalPrint(octetsNetmask);
        System.out.print("\nNetmask Binary: ");
        binaryPrint(binaryNetmask);
        System.out.println("\nCIDR: "+ numberOfOnes(binaryNetmask));

        System.out.print("\nNetwork Address: ");
        decimalPrint(octetsNetwork);
        System.out.print("\nBinary Network Address: ");
        binaryPrint(binaryNetwork);

        System.out.print("\n\nBroadcast Address: ");
        decimalPrint(octetBroadcast);
        System.out.print("\nBinary Broadcast Address: ");
        binaryPrint(binaryBroadcast);

        System.out.print("\n\n1st host: ");
        decimalPrint(firstHost);
        System.out.print("\nLast host: ");
        decimalPrint(lastHost);

        System.out.println("\nNumber of  Hosts: "+ numberOfAllHosts(numberOfZeros(binaryNetmask)));
        System.out.println("Number of Usable Hosts: "+ numberOfUsableHosts(numberOfZeros(binaryNetmask)));
    }

    public static void binaryPrint(String [] binary){
        for (int i = 0; i < 4; i++) {
            System.out.print(binary[i]+" ");
        }
    }

    public static void decimalPrint(String [] decimal){
        for (int i = 0; i < 4; i++) {
            if(i == 3){
                System.out.print(decimal[i]);
            }else System.out.print(decimal[i]+".");
        }
    }

    /**
     * Metódus az IP cím osztályának kiderítéséhez
     * @param octets Az IP cím octetjei
     * @return Egy karaktert ad vissza ami az IP cím osztályát jelöli
     */
    public static char ipClass(String[] octets){
        char ch = 0;
        if(Integer.parseInt(octets[0]) >= 0 && Integer.parseInt(octets[0]) <= 127){
            ch = 'A';
        }
        if(Integer.parseInt(octets[0]) >= 128 && Integer.parseInt(octets[0]) <= 191){
            ch = 'B';
        }
        if(Integer.parseInt(octets[0]) >= 192 && Integer.parseInt(octets[0]) <= 223){
            ch = 'C';
        }
        if(Integer.parseInt(octets[0]) >= 224 && Integer.parseInt(octets[0]) <= 239){
            ch = 'D';
        }
        if(Integer.parseInt(octets[0]) >= 240 && Integer.parseInt(octets[0]) <= 255){
            ch = 'E';
        }
        return ch;
    }

    /**
     * Metódus a network cím kiszámolására
     *
     * @param binIP Az IPv4 cím binaris alakja
     * @param binMask A netmask cím bináris alakja
     *
     * @return Egy string ami a network address bináris alakját tartalmazza
     */
    public static String NetworkAddress(String binIP, String binMask) {
        StringBuilder tmp = new StringBuilder();

        for (int i = 0; i < 8; i++) {
            if(Integer.parseInt(String.valueOf(binIP.charAt(i))) < Integer.parseInt(String.valueOf(binMask.charAt(i)))){
                tmp.append(binIP.charAt(i));
            }else tmp.append(binMask.charAt(i));
        }
        return tmp.toString();
    }

    /**
     * Metódus az összes host kiszámítására
     * @param nulls A binaris címből megkapott nullák száma
     * @return Egy szám ami az összes host számát jelölli
     */
    public static int numberOfAllHosts(int nulls){
        return (int)Math.pow(2,nulls);
    }

    /**
     * Metódus a használható hostok kiszámítására
     * @param nulls A binaris netmask címből megkapott nullák száma
     * @return Egy szám ami a használható hostok számát jelölli
     */
    public static int numberOfUsableHosts(int nulls){
        return (int)Math.pow(2,nulls)-2;
    }

    /**
     * Metódus a bináris netmask címben lévő nullák megszámolására
     * @param array A bináris cím tömbje
     * @return Egy szám ami a nullák számát jelöli
     */
    public static int numberOfZeros(String [] array){
        int db = 0;
        for (String s : array) {
            for (int j = 0; j < 8; j++) {
                if (s.charAt(j) == '0') {
                    db++;
                }
            }
        }
        return db;
    }

    /**
     * Metódus a bináris netmask címben lévő egyesek megszámolására
     * @param array  A bináris cím tömbje
     * @return Egy szám ami az egyesek számát jelöli
     */
    public static int numberOfOnes(String [] array){
        int db = 0;
        for (String s : array) {
            for (int j = 0; j < 8; j++) {
                if (s.charAt(j) == '1') {
                    db++;
                }
            }
        }
        return db;
    }
    /**
     * Metódus az oktetek binárissá alakítására
     * @param octet A megadott cím oktetekre tagolt alakja
     * @return egy string-é alakított szám ami a bináris alakot jelöli oktetenként
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

    /**
     * Metódus az bináris alak oktetekké alakítására
     * @param binary A megadott cím bináris alakja
     * @return egy string-é alakított szám ami az octet alakot jelöli
     */
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
     * @param ipv4 Megadott ipv4 cím
     * @return boolean Eldönti, hogy a megadott String helyes-e vagy sem
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
     * @param str Az oktet
     * @return boolean Eldönti, hogy az adott oktet helyes-e vagy sem
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