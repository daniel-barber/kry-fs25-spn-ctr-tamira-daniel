package src.main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;

public class FileHandler {


    static BigInteger[] readChiffreFile(String fileName) {
        try (BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/" + fileName))) {
            String line = reader.readLine();
            if (line == null) {
                throw new RuntimeException(fileName + " is empty.");
            }

            String[] parts = line.split(",");
            BigInteger[] chiffre = new BigInteger[parts.length];

            for (int i = 0; i < parts.length; i++) {
                chiffre[i] = new BigInteger(parts[i]);
            }

            return chiffre;
        } catch (IOException ex) {
            throw new RuntimeException("Couldn't read " + fileName + ": " + ex.getMessage());
        }
    }

    static String readTextFile(String fileName) {
        String input;
        try (
                BufferedReader textReader = new BufferedReader(new FileReader("src/main/resources/" + fileName))) {
            input = textReader.readLine();
        } catch (IOException ex) {
            throw new RuntimeException("couldn't find " + fileName + " in /resources folder or file not in valid format");
        }
        if (!isAscii(input)) {
            throw new RuntimeException(fileName + " contains non-ASCII characters");
        }

        return input;
    }

    public static boolean isAscii(String input) {
        for (int i = 0; i < input.length(); i++) {
            if (input.charAt(i) > 127) {
                return false;
            }
        }
        return true;
    }
}
