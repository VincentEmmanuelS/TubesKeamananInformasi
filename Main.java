/** Tugas Besar Keamanan Informasi
 *  Analisis Perbandingan Dictionary Brute Force Hybrid Attack dan Rule-Based Dictionary Attack
 * 
 * @author Vincent Emmanuel Suwardy / 6182201067
 * @author Lintang Kastara Erlangga / 6182201097
 * @author Michael William Iswadi / 6182201019
 * @author Alexander Vinchent Nathanael / 6182201089
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
            else if (password.length() > 21) {
                write.write("Password hanya memiliki maksimal 21 karakter!\n");
            } 
            else {
                valid = true;
                break;
            }

            write.write("Type your password: ");
            write.flush();
        }

        /* Dictionary Brute Force Hybrid Attack */
        long startBruteForce = System.currentTimeMillis();
        DictionaryBruteForce bruteForce = new DictionaryBruteForce();
        bruteForce.crackPassword(password);
        long endBruteForce = System.currentTimeMillis();

        // output hasil brute force

        /* Dictionary Rule-Based Attack */
        long startRuleBased = System.currentTimeMillis();
        DictionaryRuleBased ruleBased = new DictionaryRuleBased();
        ruleBased.crackPassword(password);
        long endRuleBased = System.currentTimeMillis();

        // output hasil rule based

        write.flush();
        write.close();
    }
}