import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class DictionaryRuleBased {

    /* VARIABLES */
    private static final int MAX_PASSWORD_LENGTH = 32;
    private static final char[] charset = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*-_=+.>,<?".toCharArray();

    public boolean crackPassword(String password) {
        try (BufferedReader read = new BufferedReader(new FileReader("Dictionary.txt"))) {
            String keyword;
            while ((keyword = read.readLine()) != null) {
                
                // Do nothing (passthrough)
                if (keyword.equals(password)) {
                    return true;
                }

                // Lowercase all letters
                if (keyword.toLowerCase().equals(password)) {
                    return true;
                }

                // Uppercase all letters
                if (keyword.toUpperCase().equals(password)) {
                    return true;
                }

                // Capitalize the first letter and lower the rest
                if (capitalizeFirstLetter(keyword).equals(password)) {
                    return true;
                }

                // Lower first found chacater, uppercase the rest
                if (lowerFirstUpperRest(keyword).equals(password)) {
                    return true;
                }

                // Toggle the case of all characters in word (p@ssW0rd -> P@SSw0RD)
                if (toggleCase(keyword).equals(password)) {
                    return true;
                }
                
                // Toggle the case of characters at position N (N starts from 0) 
                for (int i = 0; i < keyword.length(); i++) {
                    if (toggleCaseAt(keyword, i).equals(password)) {
                        return true;
                    }
                }

                // Reverse the entire word
                if (reverseWord(keyword).equals(password)) {
                    return true;
                }

                // Duplicate entire word
                if (duplicateWord(keyword).equals(password)) {
                    return true;
                }

                // Append duplicated word N times (N starts from 0)
                // Note: max length for password is 32, and min is 8. So if the password length is 8, max append we can do is 3 times.
                for (int i = 1; i < 4; i++) {
                    if (appendDuplicatedWord(keyword, i).equals(password)) {
                        return true;
                    }
                }

                // Duplicate word reverse front (DrowssaPPassworD)
                if (duplicateWordReverseFront(keyword).equals(password)) {
                    return true;
                }
                
                // Duplicate word reverse end (PassworDDrowssaP)
                if (duplicateWordReverseEnd(keyword).equals(password)) {
                    return true;
                }

                // Rotate the word left (p@ssW0rd -> @ssW0rdp)
                if (rotateLeft(keyword).equals(password)) {
                    return true;
                }

                // Rotate the word right (p@ssW0rd -> dp@ssW0r)
                if (rotateRight(keyword).equals(password)) {
                    return true;
                }

                // Append character X to end


                // Append character X to front


                // Delete first character
                if (deleteFirstChar(keyword).equals(password)) {
                    return true;
                }

                // Delete last character
                if (deleteLastChar(keyword).equals(password)) {
                    return true;
                }

                // Delete character at position N (N starts from 0)
                for (int i = 0; i < keyword.length(); i++) {
                    if (deleteCharAt(keyword, i).equals(password)) {
                        return true;
                    }
                }

            }
        } catch (IOException e) {
            System.out.println("Error reading dictionary file: " + e.getMessage());
        }

        return false;
    }

    /* METHODS */

    private String checkLength(String duplicate, String original) {
        if (duplicate.length() <= MAX_PASSWORD_LENGTH) {
            return duplicate;
        }
        else {
            return original;
        }
    }

    private String capitalizeFirstLetter(String s) {
        if (s == null || s.isEmpty()) {
            return s;
        }
        return s.substring(0,1).toUpperCase() + s.substring(1).toLowerCase();
    }

    private String lowerFirstUpperRest(String s) {
        if (s == null || s.isEmpty()) {
            return s;
        }
        return s.substring(0,1).toLowerCase() + s.substring(1).toUpperCase();
    }

    private String toggleCase(String s) {
        StringBuilder sb = new StringBuilder(s.length());

        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);

            if (Character.isLowerCase(c)) {
                sb.append(Character.toUpperCase(c));
            }
            else {
                sb.append(Character.toLowerCase(c));
            }
        }

        return sb.toString();
    }

    private String toggleCaseAt(String s, int idx) {
        char c = s.charAt(idx);
        StringBuilder sb = new StringBuilder(s);

        if (Character.isLowerCase(c)) {
            sb.setCharAt(idx, Character.toUpperCase(c));
        }
        else { 
            sb.setCharAt(idx, Character.toLowerCase(c));
        }

        return sb.toString();
    }

    private String reverseWord(String s) {
        return new StringBuilder(s).reverse().toString();
    }
    
    private String duplicateWord(String s) {
        String duplicated = s + s;

        return this.checkLength(duplicated, s);
    }

    private String appendDuplicatedWord(String s, int times) {
        String duplicated = s;
        
        for (int i = 0; i < times; i++) {
            duplicated += s;
        }

        return this.checkLength(duplicated, s);
    }

    private String duplicateWordReverseFront(String s) {
        String duplicated = new StringBuilder(s).reverse().toString() + s;

        return this.checkLength(duplicated, s);
    }

    private String duplicateWordReverseEnd(String s) {
        String duplicated = s + new StringBuilder(s).reverse().toString();

        return this.checkLength(duplicated, s);
    }

    private String rotateLeft(String s) {
        return s.substring(1) + s.charAt(0);
    }

    private String rotateRight(String s) {
        return s.charAt(s.length()-1) + s.substring(0, s.length()-1);
    }

    // Method buat append X character

    private String deleteFirstChar(String s) {
        String deleted = s.substring(1);

        if (deleted.length() < 8) {
            return s;
        }
        else {
            return deleted;
        }
    } 

    private String deleteLastChar(String s) {
        String deleted = s.substring(0, s.length()-1);

        if (deleted.length() < 8) {
            return s;
        }
        else {
            return deleted;
        }
    }

    private String deleteCharAt(String s, int idx) {
        if (idx < 0 || idx >= s.length()) {
            return s;
        } 
        
        return s.substring(0, idx) + s.substring(idx + 1);
    }
}
