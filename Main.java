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

        // Output result ke file
        File resultsFile = new File("Results.txt");
        BufferedWriter writer = new BufferedWriter(new FileWriter(resultsFile, false));

        // BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(System.out));
        
        DictionaryRuleBased drb = new DictionaryRuleBased();
        DictionaryBruteForce dbf = new DictionaryBruteForce();

        String hashedPassword;
        String status;          // Later will be used as counter for success rate %
        System.out.println("Program is running");
        while ((hashedPassword = hashReader.readLine()) != null) {

            // Start dictionary rule-based attack
            long startTime = System.nanoTime();
            String ruleBasedResult = drb.crackPassword(hashedPassword, dictionaryFile);
            long ruleBasedTime = System.nanoTime() - startTime;

            if (ruleBasedResult != null) { status = "success"; }
            else { status = "failed"; }

            // Output rule-based attack
            writer.write("Hashed Password: " + hashedPassword + "\n");
            writer.write("Dictionary Rule-Based Attack:\n");
            writer.write("[status: " + status + "] " + "Result: " + (ruleBasedResult != null ? ruleBasedResult : "-") + " | Time: " + ruleBasedTime / 1_000_000_000 + "s (" + ruleBasedTime / 1_000_000 + " ms)\n");

            // Start dictionary brute force hybrid attack
            startTime = System.nanoTime();
            String bruteForceResult = dbf.crackPassword(hashedPassword, dictionaryFile);
            long bruteForceTime = System.nanoTime() - startTime;

            if (bruteForceResult != null) { status = "success"; }
            else { status = "failed"; }

            // Output brute force hybrid attack
            writer.write("Brute Force Hybrid Attack:\n");
            writer.write("[status: " + status + "] " + "Result: " + (bruteForceResult != null ? bruteForceResult : "-") + " | Time: " + bruteForceTime / 1_000_000_000 + "s (" + bruteForceTime / 1_000_000 + " ms)\n\n");

        }

        // Close streams
        hashReader.close();
        writer.flush();
        writer.close();

    }
}
