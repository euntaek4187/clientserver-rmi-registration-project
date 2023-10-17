package token;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
public class TokenManager {
    private static final long TOKEN_EXPIRY_DURATION = TimeUnit.MINUTES.toMillis(10);
    private static Map<String, TokenInfo> tokens = new HashMap<>();
    private static class TokenInfo {
        String token;
        long expiryTime;
        String studentID;
        TokenInfo(String token, long expiryTime, String studentID) {
            this.token = token;
            this.expiryTime = expiryTime;
            this.studentID = studentID;
        }
    }
    public static String createToken(String studentID) {
        String token = UUID.randomUUID().toString();
        long expiryTime = System.currentTimeMillis() + TOKEN_EXPIRY_DURATION;
        tokens.put(token, new TokenInfo(token, expiryTime, studentID));
        return token;
    }
    public static boolean isValidToken(String token) {
        TokenInfo tokenInfo = tokens.get(token);
        if (tokenInfo == null) return false;
        if (System.currentTimeMillis() > tokenInfo.expiryTime) {
            tokens.remove(token);
            return false;
        }
        return true;
    }
    public static boolean invalidateToken(String token) {
        if (tokens.containsKey(token)) {
            tokens.remove(token);
            return true;
        } else return false;
    }
    public static String getID(String token) {
        TokenInfo tokenInfo = tokens.get(token);
        return tokenInfo.studentID;
    }
}