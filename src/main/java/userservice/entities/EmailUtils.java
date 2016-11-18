package userservice.entities;

// Vault can't handle @ in its user names so we replace it.
public class EmailUtils {
    private final static String REPLACE_SYMBOL = "-at-";
    private final static String INVALID_SYMBOL = "@";

    public static String convert(final String email) {
        return email.replace(INVALID_SYMBOL, REPLACE_SYMBOL);
    }

    public static String revert(final String email) {
        return email.replace(REPLACE_SYMBOL, INVALID_SYMBOL);
    }
}
