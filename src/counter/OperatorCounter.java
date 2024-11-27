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
}
