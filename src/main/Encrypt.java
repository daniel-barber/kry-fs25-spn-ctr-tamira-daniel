package src.main;

import java.util.HashMap;
import java.util.Map;


public class Encrypt {
    public static void main(String[] args) {
        int r = 4; // Anzahl Runden
        int n = 4; // Bitlänge S-Box
        int m = 4; // Anzahl S-Boy pro Runde
        int s = 32; // Schlüssellänge

          final char [] sBox = {
                'E', '4', 'D', '1', '2', 'F', 'B', '8', '3','A', '6', 'C','5', '9', '0','7'
        };
          final int [] bitPermutation = {
                0,4,8,12,1,5,9,13,2,6,10,14,3,7,11,15
          };

        String key = FileHandler.readTextFile("key").replaceAll(" ", "");
        int klartext = 0b0001001010001111;
        int chiffre =  0b1010111010110100;

        int [] roundKeys = calculateRoundKeys(key,r);
        int result = encrypt(roundKeys, klartext, sBox, bitPermutation);
        System.out.println("Encryption of " + Integer.toBinaryString(klartext) + ": " + Integer.toBinaryString(result));
        result = decrypt(roundKeys, chiffre, sBox, bitPermutation);
        System.out.println("Decryption of " + Integer.toBinaryString(chiffre) +  ": " +Integer.toBinaryString(result));

       /*         for (int roundKey : roundKeys) {
                    System.out.println(Integer.toBinaryString(roundKey));
                }*/
    }

    public static int [] calculateRoundKeys(String text, int rounds) {
        String [] subString = new String[rounds+1];
        int [ ] result = new int[rounds+1];
        int length = 16;
        int start = 0;
        for (int i = 0; i <= rounds; i++) {
             start = 4*i;
             subString[i] = text.substring(start, start+length);
             result[i] = Integer.parseInt(subString[i],2);
        }
        return result;
    }

    public static int encrypt(int [] roundKeys, int klartext, char [] sBox, int [] bitPermutation) {
        int result = klartext;
        for(int i = 0; i < roundKeys.length; i++) {
            if(i == 0){ // initial Weissschritt
                result = result ^ roundKeys[i];
            }
            else if  (i==roundKeys.length-1){ //last round
                result = wordSubstitution(result, sBox);
                result = result ^ roundKeys[i];
            } else {// other rounds
                result = wordSubstitution(result, sBox);
                result = permutation(result, bitPermutation);
                result = result ^ roundKeys[i];
            }
        }

        return result;
    }

    public static int decrypt(int [] roundKeys, int chiffre, char [] sBox, int [] bitPermutation) {
        char [] inverseSBox = inverseSBox(sBox);
        int [] newRoundKeys = calRoundKeys(roundKeys, bitPermutation);
        return encrypt(newRoundKeys, chiffre, inverseSBox,bitPermutation);
    }

    private static int[] calRoundKeys(int[] roundKeys, int [] bitPermutation) {
        int[] result = new int[roundKeys.length];
        for (int i = 0; i < roundKeys.length; i++) {
            if (i == 0) {
                result[i] = roundKeys[roundKeys.length-1];
            }
            else if (i == roundKeys.length-1){
                result[i] = roundKeys[0];
            }
            else {
                result[i] = permutation(roundKeys[i], bitPermutation);
            }
        }
        return result;
    }

    private static char[] inverseSBox(char[] sBox) {
        char[] inverse = new char[sBox.length];

        for (int i = 0; i < sBox.length; i++) {
            int value = Character.digit(sBox[i], 16); // Convert hex char ('E', '4', etc.) to integer (0-15)
            inverse[value] = Character.forDigit(i, 16); // Store original index at the correct position
        }

        return inverse;
    }





    private static int permutation(int bitmuster, int[] bitPermutation) {
        int result = 0;

        // Loop through all 16 bits
        for (int i = 0; i < 16; i++) {
            int bit = (bitmuster >> i) & 1; // Extract bit at position i

            if (bit == 1) {
                result |= (1 << bitPermutation[i]); // Move bit to new position
            }
        }

        return result;
    }


    private static int wordSubstitution(int bitmuster, char [] sBox) {
        int result = 0;
        for (int i = 0; i < 4; i++) {
            int nibble = (bitmuster >> (i * 4)) & 0xF; // Extract 4-bit segment
            int substituted = Character.digit(sBox[nibble], 16); // S-Box lookup

            result |= (substituted << (i * 4)); // Place back in the correct position
        }


        return result;
    }


}
