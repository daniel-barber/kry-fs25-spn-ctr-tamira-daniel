package src.main;

import java.util.HashMap;
import java.util.Map;

public class Encrypt {
    public static void main(String[] args) {
        int r = 4; // Anzahl Runden
        int n = 4; // Bitlänge S-Box
        int m = 4; // Anzahl S-Boy pro Runde
        int s = 32; // Schlüssellänge

        String key = FileHandler.readTextFile("key").replaceAll(" ", "");
        Map <Character, Character> box = new HashMap<>();
        box.put('0','E');
        box.put('1','4');
        box.put('2','D');
        box.put('3','1');
        box.put('4','2');
        box.put('5','F');
        box.put('6','B');
        box.put('7','8');
        box.put('8','3');
        box.put('9','A');
        box.put('A','6');
        box.put('B','C');
        box.put('C','5');
        box.put('D','9');
        box.put('E','0');
        box.put('F','7');

        Map <Integer, Integer> bitPermutation = new HashMap<>();
        bitPermutation.put(0, 0);
        bitPermutation.put(1, 4);
        bitPermutation.put(2, 8);
        bitPermutation.put(3, 12);
        bitPermutation.put(4, 1);
        bitPermutation.put(5, 5);
        bitPermutation.put(6, 9);
        bitPermutation.put(7, 13);
        bitPermutation.put(8, 2);
        bitPermutation.put(9, 6);
        bitPermutation.put(10, 10);
        bitPermutation.put(11, 14);
        bitPermutation.put(12, 3);
        bitPermutation.put(13, 7);
        bitPermutation.put(14, 11);
        bitPermutation.put(15, 15);

        String [] roundKeys = calculateRoundKeys(key,r);
        for (String roundKey : roundKeys) {
            System.out.println(roundKey);
        }
    }

    public static String [] calculateRoundKeys(String text, int rounds) {
        String [] result = new String[rounds+1];
        int length = 16;
        int start = 0;
        for (int i = 0; i <= rounds; i++) {
             start = 4*i;
             result[i] = text.substring(start, start+length);
        }
        return result;
    }

    public static String encrypt(Map<Character, Character> sBox, String [] roundKeys, Map <Integer, Integer> bitPermutation) {
        String result = "";

        return result;
    }


}
