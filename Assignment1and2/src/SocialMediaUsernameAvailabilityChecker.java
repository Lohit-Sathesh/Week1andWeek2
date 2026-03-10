import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class SocialMediaUsernameAvailabilityChecker {

    // username -> userId
    private final ConcurrentHashMap<String, Integer> usernameMap = new ConcurrentHashMap<>();

    // username -> attempt frequency
    private final ConcurrentHashMap<String, AtomicInteger> attemptFrequency = new ConcurrentHashMap<>();

    // Check username availability in O(1)
    public boolean checkAvailability(String username) {
        attemptFrequency
                .computeIfAbsent(username, k -> new AtomicInteger(0))
                .incrementAndGet();

        return !usernameMap.containsKey(username);
    }

    // Register username
    public boolean registerUsername(String username, int userId) {
        if (usernameMap.putIfAbsent(username, userId) == null) {
            return true;
        }
        return false;
    }

    // Suggest similar usernames
    public List<String> suggestAlternatives(String username) {
        List<String> suggestions = new ArrayList<>();

        for (int i = 1; i <= 5; i++) {
            String suggestion = username + i;
            if (!usernameMap.containsKey(suggestion)) {
                suggestions.add(suggestion);
            }
        }

        String dotVersion = username.replace("_", ".");
        if (!usernameMap.containsKey(dotVersion)) {
            suggestions.add(dotVersion);
        }

        String dashVersion = username.replace("_", "-");
        if (!usernameMap.containsKey(dashVersion)) {
            suggestions.add(dashVersion);
        }

        return suggestions;
    }

    // Get most attempted username
    public String getMostAttempted() {
        String result = null;
        int max = 0;

        for (Map.Entry<String, AtomicInteger> entry : attemptFrequency.entrySet()) {
            int count = entry.getValue().get();
            if (count > max) {
                max = count;
                result = entry.getKey();
            }
        }

        return result;
    }

    // Sample usage
    public static void main(String[] args) {
        SocialMediaUsernameAvailabilityChecker checker = new SocialMediaUsernameAvailabilityChecker();

        checker.registerUsername("john_doe", 1);

        System.out.println(checker.checkAvailability("john_doe")); // false
        System.out.println(checker.checkAvailability("jane_smith")); // true

        System.out.println(checker.suggestAlternatives("john_doe"));

        System.out.println(checker.getMostAttempted());
    }
}