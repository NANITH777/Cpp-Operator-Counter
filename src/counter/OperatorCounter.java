package counter; 

import java.io.*;
import java.util.regex.*;

public class OperatorCounter {
    public static void main(String[] args) {
        BufferedReader okuyucu = new BufferedReader(new InputStreamReader(System.in));
        
        try {
            System.out.print("Lütfen C++ dosyasının tam yolunu girin: ");
            String dosyaYolu = okuyucu.readLine();
            
            String icerik = dosyaOku(dosyaYolu);
            icerik = yorumlariKaldir(icerik);
            icerik = stringleriKaldir(icerik);
            icerik = includeDirektifleriniKaldir(icerik);
            
            int[] sayaclar = operatorSay(icerik);
            
            System.out.println("Tekli Operatör Sayısı: " + sayaclar[0]);
            System.out.println("İkili Operatör Sayısı: " + sayaclar[1]);
            System.out.println("Üçlü Operatör Sayısı: " + sayaclar[2]);
            
        } catch (IOException e) {
            System.out.println("Dosya okuma hatası: " + e.getMessage());
        }
    }

    private static String dosyaOku(String dosyaYolu) throws IOException {
        StringBuilder icerik = new StringBuilder();
        try (BufferedReader okuyucu = new BufferedReader(new FileReader(dosyaYolu))) {
            String satir;
            while ((satir = okuyucu.readLine()) != null) {
                icerik.append(satir).append("\n");
            }
        }
        return icerik.toString();
    }

    private static String yorumlariKaldir(String icerik) {
        // Çok satırlı yorumları kaldır
        icerik = icerik.replaceAll("/\\*[^*]*\\*+(?:[^/*][^*]*\\*+)*/", "");
        // Tek satırlı yorumları kaldır
        icerik = icerik.replaceAll("//.*", "");
        return icerik;
    }

    private static String stringleriKaldir(String icerik) {
        // String ifadelerini kaldır
        return icerik.replaceAll("\"[^\"\\\\]*(\\\\.[^\"\\\\]*)*\"", "");
    }

    private static String includeDirektifleriniKaldir(String icerik) {
        // #include ve onun < > kısmını kaldır
        return icerik.replaceAll("#include\\s*<[^>]*>", "");
    }

    private static int[] operatorSay(String icerik) {
        int[] sayaclar = new int[3]; // Tekli, ikili, üçlü operatörler için sayaçlar
        
     // Tekli operatörler (++, --, !, ~, &, *, +, -)
        Pattern tekliDesen = Pattern.compile("(?<![-+!~&*])\\+\\+|--(?![-+])|!(?![=])|~(?![=])|(?<![&])&(?![&=*])|\\*(?![*=])(?!\\s*[a-zA-Z0-9(])|(?<!\\d)[+-]\\d+");
        Matcher tekliEslesme = tekliDesen.matcher(icerik);
        while (tekliEslesme.find()) {
            sayaclar[0]++;
        }

        return sayaclar;
    }
}
