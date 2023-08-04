package ticketsystem.utils;

import java.text.Normalizer;

public class UsernameGenerator {
    public static String generateUsername(String firstName, String email) {

        String baseUsername = (removeAccents(firstName.toLowerCase()))
                .replaceAll(" ", "");

        String uniquePart = String.valueOf(email.hashCode());

        String username = baseUsername + uniquePart;

        if (username.length() > 16) {
            username = username.substring(0, 16);
        }

        return username;
    }

    public static String removeAccents(String input) {
        return Normalizer.normalize(input, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
    }
}
