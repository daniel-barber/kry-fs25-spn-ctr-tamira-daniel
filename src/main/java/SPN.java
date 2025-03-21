package main.java;

public class SPN {
    // Parameters
    private final int rounds;
    private final int n = 4; // Bitlänge S-Box
    private final int m = 4; // Anzahl S-Boy pro Runde
    private final int blockSize = n * m;
    private final String key;
    private final int[] roundKeys;

    // S-Box
    private static final int[] sBox = { 0xE, 0x4, 0xD, 0x1, 0x2, 0xF, 0xB, 0x8, 0x3, 0xA, 0x6, 0xC, 0x5, 0x9, 0x0, 0x7 };
    private static final int[] inv_sBox = computeInverseSBox();

    private static int[] computeInverseSBox() {
        int[] inverses = new int[SPN.sBox.length];
        for (int i = 0; i < 16; i++) {
            inverses[SPN.sBox[i]] = i;
        }
        return inverses;
    }

    // Bit permutation
    private static final int[] permutation = { 0, 4, 8, 12, 1, 5, 9, 13, 2, 6, 10, 14, 3, 7, 11, 15 };

    // constructor
    public SPN(String key, int rounds) {
        this.rounds = rounds;
        this.key = key;
        this.roundKeys = generateRoundKeys();
    }

    // default constructor with key and rounds for this exercise
    public SPN(){
        this("00111010100101001101011000111111", 4 + 1); // +1 für die 0te Runde
    }

    // precompute round keys
    private int[] generateRoundKeys() {
        int[] roundKeys = new int[rounds];
        for (int i = 0; i < rounds; i++) {
            int start = 4 * i;
            String roundKeyStr = key.substring(start, start + blockSize);
            roundKeys[i] = Integer.parseInt(roundKeyStr, 2);
        }
        return roundKeys;
    }

    // apply sBox
    private int applySBox(int input, boolean inverse){
        int output = 0;
        for (int i = 0; i < blockSize / 4; i++){
            int nibble = (input >> (i * 4)) & 0xF;
            int mapped = inverse ? inv_sBox[nibble] : sBox[nibble];
            output |= (mapped << (i * 4));
        }
            return output;
    }

    private int applyPermutation(int input){
        int output = 0;
        for (int i = 0; i < blockSize; i++){
            int bit = (input >> i) & 1;
            if (bit == 1){
                output |= (1 << permutation[i]);
            }
        }
        return output;
    }

    // encrypt plaintext
    public int encrypt(int plaintext){
        int state = plaintext;

        // round 1 -> initialer Weissschritt -> XOR mit ersten Rundenschlüssel
        state ^= roundKeys[0];

        // round 1 to 1 before last
        for (int round = 1; round < rounds - 1; round++){
            state = applySBox(state, false);
            state = applyPermutation(state);
            state ^= roundKeys[round];
        }

        // final round
        state = applySBox(state, false);
        state ^= roundKeys[rounds - 1];
        return state;
    }

    public int decrypt (int ciphertext){
        int state = ciphertext;

        // reverse final round
        state ^= roundKeys[rounds - 1];
        state = applySBox(state, true);

        // reverse round 1 to 1 before last
        for (int round = rounds - 2; round >= 1; round--){
            state ^= roundKeys[round];
            state = applyPermutation(state);
            state = applySBox(state, true);
        }

        // reverse round 1
        state ^= roundKeys[0];
        return state;
    }

    // helper function for formatting
    public static String formatBinary(int value) {
        String binary = String.format("%16s", Integer.toBinaryString(value)).replace(' ', '0');
        return binary.replaceAll("(.{4})", "$1 ").trim();
    }

}
