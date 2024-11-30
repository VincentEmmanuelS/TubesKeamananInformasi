import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class DictionaryBruteForce {

    public boolean crackPassword(String password) {
        try (BufferedReader read = new BufferedReader(new FileReader("Dictionary.txt"))) {
            String keyword;
            while ((keyword = read.readLine()) != null) {
                // Bandingin
            }
        } catch (IOException e) {
            System.out.println("Error reading dictionary file: " + e.getMessage());
        }

        return false;
    }

}
