/** Tugas Besar Keamanan Informasi
 *  Analisis Perbandingan Dictionary Brute Force Hybrid Attack dan Rule-Based Dictionary Attack
 * 
 *  @author Vincent Emmanuel Suwardy / 6182201067
 *  @author Lintang Kastara Erlangga / 6182201097
 *  @author Michael William Iswadi / 6182201019
 *  @author Alexander Vinchent Nathanael / 6182201089
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        
        BufferedReader read = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter write = new BufferedWriter(new OutputStreamWriter(System.out));

        write.write("Type your password: ");
        write.flush();

        boolean valid = false;
        String password = "";

        while (!valid) {
            password = read.readLine();

            if (password.length() < 8) {
                write.write("Password harus memiliki minimal 8 karakter!\n");
            }
            else if (password.length() > 32) {
                write.write("Password hanya memiliki maksimal 32 karakter!\n");
            } 
            else {
                valid = true;
                break;
            }

            write.write("Type your password: ");
            write.flush();
        }

        String result = "";

        /* Dictionary Brute Force Hybrid Attack */
        long startBruteForce = System.currentTimeMillis();
        DictionaryBruteForce bruteForce = new DictionaryBruteForce();
        boolean bruteForceResult = bruteForce.crackPassword(password);
        long endBruteForce = System.currentTimeMillis();

        // output hasil brute force
        if (bruteForceResult) {
            result = "cracked!";
        }
        else {
            result = "failed to crack";
        }

        write.write("Brute Force Hybrid Attack Result: " + result + " | Time spend: " + (endBruteForce - startBruteForce) + " ms\n");

        /* Dictionary Rule-Based Attack */
        long startRuleBased = System.currentTimeMillis();
        DictionaryRuleBased ruleBased = new DictionaryRuleBased();
        boolean ruleBasedResult = ruleBased.crackPassword(password);
        long endRuleBased = System.currentTimeMillis();

        // output hasil rule based
        if (ruleBasedResult) {
            result = "cracked!";
        }
        else {
            result = "failed to crack";
        }

        write.write("Rule Based Attack Result: " + result + " | Time spend: " + (startRuleBased - endRuleBased) + " ms\n");

        write.flush();
        write.close();
    }
}