/* Main class
 * 
 * Class yang akan membandingkan kedua algoritma dictionary attack
 * 
 * Input akan berupa list password yang sudah di hash dengan SHA-256 sebelumnya
 * Output akan berupa password string (atau "-" jika tidak berhasil) dan wkatu yang dibutuhkan untuk menyerang
 */

import java.io.*;
import java.security.NoSuchAlgorithmException;

public class Main {
    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {

        // Input berupa hashed password dari file txt
        File hashedPasswordFile = new File("HashedPassword.txt");
        BufferedReader hashReader = new BufferedReader(new FileReader(hashedPasswordFile));

        // Load dictionary
        File dictionaryFile = new File("Dictionary.txt");

        // Buffer buat output
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(System.out));
        DictionaryRuleBased drb = new DictionaryRuleBased();
        DictionaryBruteForce dbf = new DictionaryBruteForce();

        String hashedPassword;
        while ((hashedPassword = hashReader.readLine()) != null) {

            // Start dictionary rule-based attack
            long startTime = System.nanoTime();
            String ruleBasedResult = drb.crackPassword(hashedPassword, dictionaryFile);
            long ruleBasedTime = System.nanoTime() - startTime;
            // Output rule-based attack
            writer.write("Hashed Password: " + hashedPassword + "\n");
            writer.write("Dictionary Rule-Based Attack:\n");
            writer.write("Result: " + (ruleBasedResult != null ? ruleBasedResult : "-") + "| Time: " + ruleBasedTime / 1_000_000 + "ms\n");

            // Start dictionary brute force hybrid attack
            startTime = System.nanoTime();
            String bruteForceResult = dbf.crackPassword(hashedPassword, dictionaryFile);
            long bruteForceTime = System.nanoTime() - startTime;
            // Output brute force hybrid attack
            writer.write("Brute Force Hybrid Attack:\n");
            writer.write("Result: " + (bruteForceResult != null ? bruteForceResult : "-") + "| Time: " + bruteForceTime / 1_000_000 + "ms\n");

        }

        // Close streams
        hashReader.close();
        writer.flush();
        writer.close();

    }
}
