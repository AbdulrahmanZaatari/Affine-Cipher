import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Scanner;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

// Modified Affine Cipher program
public class ModifiedAffineCipher {

    public static int extendedX, extendedY;

    public static void main(String[] args) throws IOException {

        if (args.length == 0) {
            System.out.println("No command");
            return;
        }
        int alpha;
        int beta;
        switch (args[0]) {
            case "encrypt":
                if (args.length != 5) {
                    System.out.println("Usage: ModifiedAffineCipher encrypt [input-text-file] [destination-file] [alpha] [beta]");
                    break;
                }
                String sourceText = args[1];
                String destination = args[2];
                File destinationFile = new File(destination);
                alpha = Integer.parseInt(args[3]);
                beta = Integer.parseInt(args[4]);
                Path sourcePath = Path.of(sourceText);
                encrypt(sourcePath, destinationFile, alpha, beta);
                break;
            case "decrypt":
                if (args.length != 5) {
                    System.out.println("Usage: ModifiedAffineCipher decrypt [encrypted-text-file] [destination-file] [alpha] [beta]");
                    break;
                }
                String encryptedText = args[1];
                String finalOutput = args[2];
                Path encryptedPath = Path.of(encryptedText);
                File decryptedFile = new File(finalOutput);
                alpha = Integer.parseInt(args[3]);
                beta = Integer.parseInt(args[4]);
                decrypt(encryptedPath, decryptedFile, alpha, beta);
                break;
            case "decipher":
                if (args.length != 4) {
                    System.out.println("Usage: ModifiedAffineCipher decipher [encrypted-text-file] [destination-file] [dictionary-file]");
                    break;
                }
                String encrypted = args[1];
                String analysisOutput = args[2];
                String dictionaryPath = args[3];
                File encryptedFile = new File(encrypted);
                File analysisFile = new File(analysisOutput);
                File dictFile = new File(dictionaryPath);
                decipher(encryptedFile, analysisFile, dictFile);
                break;
            default:
                System.out.println("Invalid command");
                break;
        }

    }

    public static int[] extendedGcd(int alpha, int beta) {
        int s = 1, t = 0, u = 0, v = 1;
        while (beta != 0) {
            int quotient = alpha / beta;
            int remainder = alpha % beta;
            alpha = beta;
            beta = remainder;
            int next_s = u;
            u = s - u * quotient;
            s = next_s;
            int next_t = v;
            v = t - v * quotient;
            t = next_t;
        }
        int[] result = {alpha, s, t};
        return result;
    }

    public static boolean isKeyValid(int alpha, int beta) {
        if (alpha % 2 == 0 || (computeGcd(alpha, beta) != 1 && computeGcd(alpha, beta) != alpha) || alpha <= 0 || beta < 0 || alpha >= 128 || beta >= 128) {
            return false;
        }
        return true;
    }

    public static int computeGcd(int alpha, int beta) {
        if (beta == 0) {
            return alpha;
        } else {
            return computeGcd(beta, alpha % beta);
        }
    }

    public static int extendedGcdCalculation(int alpha, int beta) {
        if (alpha == 0) {
            extendedX = 0;
            extendedY = 1;
            return beta;
        }
        int gcd = extendedGcdCalculation(beta % alpha, alpha);
        int tempX = extendedX;
        int tempY = extendedY;

        extendedX = tempY - (beta / alpha) * tempX;
        extendedY = tempX;

        return gcd;
    }

    public static int calculateModInverse(int number) {
        int[] result = extendedGcd(number, 128);
        int inverse = result[1];
        while (inverse < 0) {
            inverse += 128;
        }
        return inverse;
    }

    public static void encrypt(Path sourcePath, File destinationFile, int alpha, int beta) {
        byte[] fileContent;
        if (isKeyValid(alpha, beta)) {
            try {
                fileContent = Files.readAllBytes(sourcePath);
                StringBuilder cipherTextBuilder = new StringBuilder();
                for (byte b : fileContent) {
                    int encipheredChar = (alpha * b + beta) % 128;
                    cipherTextBuilder.append((char) encipheredChar);
                }
                PrintWriter printWriter = new PrintWriter(destinationFile);
                printWriter.print(cipherTextBuilder);
                printWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("The key pair (" + alpha + ", " + beta + ") is invalid, please select another key.");
        }
    }

    public static void decrypt(Path encryptedPath, File destinationFile, int alpha, int beta) throws IOException {
        byte[] fileContent;
        if (isKeyValid(alpha, beta)) {
            fileContent = Files.readAllBytes(encryptedPath);
            FileWriter fileWriter = new FileWriter(destinationFile);
            for (byte b : fileContent) {
                byte decipheredChar = (byte) ((calculateModInverse(alpha) * (b - beta)) % 128);
                while (decipheredChar < 0) {
                    decipheredChar += 128;
                }
                fileWriter.write(decipheredChar);
            }
            fileWriter.close();
        } else {
            System.out.println("The key pair (" + alpha + ", " + beta + ") is invalid, please select another key.");
        }

    }

    public static void decipher(File encryptedFile, File analysisFile, File dictionary) throws IOException {
        byte[] fileContent = Files.readAllBytes(encryptedFile.toPath());
        byte[] optimalDecipheredText = new byte[fileContent.length];
        byte[] temporaryDecipheredText = new byte[fileContent.length];
        HashSet<String> wordSet = new HashSet<>();
        Scanner scanner = new Scanner(dictionary);
        while (scanner.hasNextLine()) {
            String[] wordsInDictionary = scanner.nextLine().split(" ");
            for (String word : wordsInDictionary) {
                wordSet.add(word);
            }
        }
        int highestMatchCount = 0;
        scanner.close();
        int optimalAlpha = 0;
        int optimalBeta = 0;
        int alpha = 1;
        int beta = 0;
        int currentMatchCount = 0;
        while (alpha < 128) {
            while (beta < 128) {
                if (isKeyValid(alpha, beta)) {
                    for (int i = 0; i < fileContent.length; i++) {
                        byte decipheredChar = (byte) ((calculateModInverse(alpha) * (fileContent[i] - beta)) % 128);
                        while (decipheredChar < 0) {
                            decipheredChar += 128;
                        }
                        temporaryDecipheredText[i] = decipheredChar;
                    }
                    String decipheredString = new String(temporaryDecipheredText);
                    String[] words = decipheredString.split(" ");
                    for (String word : words) {
                        if (wordSet.contains(word)) {
                            currentMatchCount++;
                        }
                    }
                    if (currentMatchCount > highestMatchCount) {
                        System.arraycopy(temporaryDecipheredText, 0, optimalDecipheredText, 0, temporaryDecipheredText.length);
                        optimalBeta = beta;
                        optimalAlpha = alpha;
                        highestMatchCount = currentMatchCount;
                    }
                    currentMatchCount = 0;
                    beta++;
                } else {
                    beta++;
                }
            }
            beta = 0;
            alpha += 2;
        }
        FileWriter fileWriter = new FileWriter(analysisFile);
        fileWriter.write(optimalAlpha + " " + optimalBeta + "\n");
        fileWriter.write("Deciphered message:\n");
        for (byte b : optimalDecipheredText) {
            fileWriter.write(b);
        }
        fileWriter.close();
    }
}
>>>>>>> master
