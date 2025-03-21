package src.main;

import java.util.Random;

public class Encryption {
    private static final int CHUNK_SIZE = 16;

    // Encrypt plaintext
    public static String encrypt(String plaintext) {
        String padded = convertToPaddedBitString(plaintext);
        StringBuilder encryptedData = new StringBuilder();

        // generate random key
        int initialKey = generateRandomKey();
        encryptedData.append(toBitString(initialKey));

        // Create SPN instance
        SPN spn = new SPN();

        // process padded input into 16 bit chunks
        for (int i = 0; i < padded.length() / CHUNK_SIZE; i++) {
            int offset = i * CHUNK_SIZE;
            String chunk = padded.substring(offset, offset + CHUNK_SIZE);

            // derive dynamic key
            int dynamicKey = (initialKey + i) % (1 << CHUNK_SIZE);

            // encrypt dynamic key using spn then xor with plaintext chunk
            int encryptedChunk = spn.encrypt(dynamicKey) ^ Integer.parseInt(chunk, 2);
            encryptedData.append(toBitString(encryptedChunk));
        }
        return encryptedData.toString();
    }

    // Decrypt ciphertext
    public static String decrypt(String ciphertext) {
        if (ciphertext.length() % CHUNK_SIZE != 0) {
            throw new IllegalArgumentException("Ciphertext length must be a multiple of " + CHUNK_SIZE);
        }

        // extract initial key
        int initialKey = Integer.parseInt(ciphertext.substring(0, CHUNK_SIZE), 2);
        StringBuilder decryptedBits = new StringBuilder();
        SPN spn = new SPN();
        String encryptedChunks = ciphertext.substring(CHUNK_SIZE);

        for (int i = 0; i < encryptedChunks.length() / CHUNK_SIZE; i++) {
            int offset = i * CHUNK_SIZE;
            String chunk = encryptedChunks.substring(offset, offset + CHUNK_SIZE);
            int dynamicKey = (initialKey + i) % (1 << CHUNK_SIZE);

            // reverse encryption
            int decryptedChunk = spn.encrypt(dynamicKey) ^ Integer.parseInt(chunk, 2);
            decryptedBits.append(toBitString(decryptedChunk));
        }
        return convertFromPaddedBitString(decryptedBits.toString());
    }

    // generate random key
    private static int generateRandomKey() {
        Random random = new Random();
        return random.nextInt(1 << CHUNK_SIZE);
    }

    // convert int to 16-bit string
    private static String toBitString(int value) {
        return String.format("%16s", Integer.toBinaryString(value)).replace(' ', '0');
    }

    // convert plaintext to padded bit string
    private static String convertToPaddedBitString(String text){
        StringBuilder bitString = new StringBuilder();
        for (char c : text.toCharArray()) {
            String binary = String.format("%8s", Integer.toBinaryString(c)).replace(' ', '0');
            bitString.append(binary);
        }
        // append 1 to mark end of original text
        bitString.append('1');

        // append 0 to fill upt to correct length
        int paddingNeeded = (CHUNK_SIZE - (bitString.length() % CHUNK_SIZE)) % CHUNK_SIZE;
        bitString.append("0".repeat(paddingNeeded));
        return bitString.toString();
    }

    // convert padded bit string to plaintext
    private static String convertFromPaddedBitString(String bitString){
        int lastOneIndex = bitString.lastIndexOf('1');
        if (lastOneIndex == -1) {
            throw new IllegalArgumentException("Invalid padded bit string: no padding marker found.");
        }
        String trimmed = bitString.substring(0, lastOneIndex);
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < trimmed.length(); i+= 8) {
            String byteString = trimmed.substring(i, Math.min(i + 8, trimmed.length()));
            text.append((char) Integer.parseInt(byteString, 2));
        }
        return text.toString();
    }
}
