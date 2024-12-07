public class Test {
    public static void main(String[] args) {
        PasswordHasher ph = new PasswordHasher();
        System.out.println(((16-12)*75)*1*2*3*4);

        for (int i = 0; i <= 4; i++) {
            if (appendEnd("UdinUdinUdin", "IdinIdinIdin", i, ph)) {
                System.out.println("Cracked!");
            }
        }

        System.out.println("Finish");
    }

    private static boolean appendEnd(String s, String pass, int limit, PasswordHasher hashFunction) {
        int MAX_PASSWORD_LENGTH = 16;
        char[] charSet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*?><,.".toCharArray();
        // System.out.println(charSet.length);
    
        if (limit == 0) {
            return false;
        }
    
        for (char c : charSet) {
            String appended = s + c;
    
            if (appended.length() <= MAX_PASSWORD_LENGTH) {
                String hashedAppended = hashFunction.hashPassword(appended);
                // System.out.println(hashedAppended);
                if (hashedAppended.equals(pass)) {
                    return true;
                }
    
                if (appendEnd(appended, pass, limit - 1, hashFunction)) {
                    return true;
                }
            }
        }
    
        return false;
    }
    
}