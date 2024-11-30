import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class DictionaryRuleBased {

    /* VARIABLES */
    private static final int MAX_PASSWORD_LENGTH = 32;
    private static final char[] charSet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*-_=+.>,<?".toCharArray();
    private static final char[][] replacements = {
        {'A', '4'}, {'a', '@'}, {'E', '3'}, {'O', '0'},
        {'o', '0'}, {'i', '!'}, {'I', '1'}, {'t', '+'}
    };

    //Possible password: passwordhj*!g*jkl&&*GF$#31jk+41@ -> length 32

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
                for (char c : charSet) {
                    String appendedToEnd = keyword + c;
                    if (appendedToEnd.equals(password)) {
                        return true;
                    }
                }

                // Append character X to front
                for (char c : charSet) {
                    String appendedToFront = c + keyword;
                    if (appendedToFront.equals(password)) {
                        return true;
                    }
                }

                // Insert character X at position N (N starts from 0)


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

                // Overwrite character at position N with symbol
                for (int i = 0; i < keyword.length(); i++) {
                    if (overwriteCharToSymbol(keyword, i).equals(password)) {
                        return true;
                    }
                }

                // Overwrite all possible characters with symbols
                if (overwriteAllCharToSymbol(keyword).equals(password)) {
                    return true;
                }

                // Overwrite symbil at position N with character
                for (int i = 0; i < keyword.length(); i++) {
                    if (overwriteSymbolToChar(keyword, i).equals(password)) {
                        return true;
                    }
                }

                // Overwrite all possible symbols with characters
                if (overwriteAllSymbolToChar(keyword).equals(password)) {
                    return true;
                }

                // Swap front and back character
                if (swapFrontAndBack(keyword).equals(password)) {
                    return true;
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

    // [A, 4], [a, @], [E, 3], [O, 0], [o, 0], [i, !], [I, 1], [t, +]
    private String overwriteCharToSymbol(String s, int idx) {
        char[] chars = s.toCharArray();

        if (idx >= 0 && idx < chars.length) {
            char curr = chars[idx];

            // Cek untuk setiap pair
            for (char[] pair : replacements) {
                if (pair[0] == curr) {
                    chars[idx] = pair[1];
                    break;
                }
            }
        }

        return new String(chars);
    }

    private String overwriteAllCharToSymbol(String s) {
        char[] chars = s.toCharArray();

        for (int i = 0; i < chars.length; i++) {
            char curr = chars[i];

            for (char[] pair : replacements) {
                if (pair[0] == curr) {
                    chars[i] = pair[1];
                    break;
                }
            }
        }

        return new String(chars);
    }

    private String overwriteSymbolToChar(String s, int idx) {
        char[] chars = s.toCharArray();

        if (idx >= 0 && idx < chars.length) {
            char curr = chars[idx];

            // Cek untuk setiap pair
            for (char[] pair : replacements) {
                if (pair[1] == curr) {
                    chars[idx] = pair[0];
                    break;
                }
            }
        }

        return new String(chars);
    }

    private String overwriteAllSymbolToChar(String s) {
        char[] chars = s.toCharArray();

        for (int i = 0; i < chars.length; i++) {
            char curr = chars[i];

            for (char[] pair : replacements) {
                if (pair[1] == curr) {
                    chars[i] = pair[0];
                    break;
                }
            }
        }

        return new String(chars);
    }

    private String swapFrontAndBack(String s) {
        String swap = s.charAt(s.length()-1) + s.substring(1, s.length()-1) + s.charAt(0);
        return swap;
    }

}
