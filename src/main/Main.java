package src.main;

public class Main {
    public static void main(String[] args) {

        // test the SPN
        var spn = new SPN("00010001001010001000110000000000", 5);
        int x = Integer.parseInt("0001001010001111", 2);
        int expectedY = Integer.parseInt("1010111010110100", 2);
        int y = spn.encrypt(x);

        System.out.println("x                       : " + SPN.formatBinary(x));
        System.out.println("y (expected ciphertext) : " + SPN.formatBinary(expectedY));
        System.out.println("actual ciphertext       : " + SPN.formatBinary(y));

        if (y == expectedY) {
            System.out.println("Test PASSED: Encryption matches the expected ciphertext.");
        } else {
            System.out.println("Test FAILED: Encryption does not match the expected ciphertext.");
        }
        System.out.println();



        // test Encryption
        String plaintext = "hello";
        String encrypted = Encryption.encrypt(plaintext);
        String decrypted = Encryption.decrypt(encrypted);
        System.out.println("plaintext            : " + plaintext);
        System.out.println("encrypted plaintext  : " + encrypted);
        System.out.println("decrypted ciphertext : " + decrypted);

        if (plaintext.equals(decrypted)) {
            System.out.println("Test PASSED: decryption matches the expected plaintext.");
        } else {
            System.out.println("Test FAILED: decryption does not match the expected plaintext.");
        }
        System.out.println();



        // decrypt the chiffretext received by Bob
        String chiffretext = "00000100110100100000101110111000000000101000111110001110011111110110000001010001010000111010000000010011011001110010101110110000";
        String decryptedChiffretext = Encryption.decrypt(chiffretext);
        System.out.println("Chiffretext           : " + chiffretext);
        System.out.println("Decrypted Chiffretext : " + decryptedChiffretext);

    }
}
