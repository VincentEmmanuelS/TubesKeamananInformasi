/* Hashed Password Generator
 * 
 * Class sementara yang akan digunakan untuk melakukan enkripsi password string ke hashed password dengan SHA-256
 * OutputFile akan selalu di overwrite setiap kali melakukan run (menghindari adanya hashedPassword yang tertinggal ketika list password diganti)
 */

import java.io.*;
import java.security.NoSuchAlgorithmException;

public class HashedPasswordGenerator {
    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {
        File inputFile = new File("Password.txt");
        File outputFile = new File("HashedPassword.txt");

        // BufferedReader reader = new BufferedReader(new FileReader(inputFile));
        // BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));

        // Overwrite file memastikan bahwa file kosong
        BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile, false));
        writer.close();

        // Rewrite hashedPassword dengan membaca Password.txt
        BufferedReader reader = new BufferedReader(new FileReader(inputFile));
        writer = new BufferedWriter(new FileWriter(outputFile, true));

        PasswordHasher passwordHasher = new PasswordHasher();

        String password;
        while ((password = reader.readLine()) != null) {
            if (password.length() >= 8 && password.length() <= 16) {
                String hashedPassword = passwordHasher.hashPassword(password);
                writer.write(hashedPassword);
                writer.newLine();
            }
        }

        reader.close();
        writer.flush();
        writer.close();
    }
}
