import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class DictionaryRuleBased {

    public boolean crackPassword(String password) {
        try (BufferedReader read = new BufferedReader(new FileReader("Dictionary.txt"))) {
            String keyword;
            while ((keyword = read.readLine()) != null) {
                
                // Do nothing (passthrough)


                // Lowercase all letters


                // Uppercase all letters


                // Capitalize the first letter and lower the rest


                // Lower first found chacater, uppercase the rest


                // Toggle the case of all characters in word (p@ssW0rd -> P@SSw0RD)

                
                // [!] Toggle the case of characters at position N (N starts from 0) 


                // Reverse the entire word


                // Duplicate entire word


                // Append duplicated word N times (N starts from 0)


                // Duplicate word reverse


                // Rotate the word left (p@ssW0rd -> @ssW0rdp)


                // Rotate the word right (p@ssW0rd -> dp@ssW0r)


                // Append character X to end


                // Append character X to front


                // Delete first character


                // Delete last character


                // Delete character at position N (N starts from 0)

            }
        } catch (IOException e) {
            System.out.println("Error reading dictionary file: " + e.getMessage());
        }

        return false;
    }
    
}
