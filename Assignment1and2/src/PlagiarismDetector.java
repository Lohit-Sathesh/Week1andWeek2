import java.util.*;

public class PlagiarismDetector {

    private int N = 5;

    private HashMap<String, Set<String>> index = new HashMap<>();

    public List<String> extractNGrams(String text) {

        String[] words = text.split("\\s+");

        List<String> ngrams = new ArrayList<>();

        for (int i = 0; i <= words.length - N; i++) {

            StringBuilder gram = new StringBuilder();

            for (int j = i; j < i + N; j++)
                gram.append(words[j]).append(" ");

            ngrams.add(gram.toString().trim());
        }

        return ngrams;
    }

    public void indexDocument(String docId, String text) {

        List<String> grams = extractNGrams(text);

        for (String g : grams) {

            index.putIfAbsent(g, new HashSet<>());

            index.get(g).add(docId);
        }
    }

    public void analyzeDocument(String docId, String text) {

        List<String> grams = extractNGrams(text);

        HashMap<String, Integer> matches = new HashMap<>();

        for (String g : grams) {

            if (index.containsKey(g)) {

                for (String d : index.get(g)) {

                    matches.put(d, matches.getOrDefault(d, 0) + 1);
                }
            }
        }

        for (String d : matches.keySet()) {

            int count = matches.get(d);

            double similarity = (double) count / grams.size() * 100;

            System.out.println(
                    "Match with " + d +
                            " → " + similarity + "%"
            );
        }
    }

    public static void main(String[] args) {

        PlagiarismDetector pd = new PlagiarismDetector();

        pd.indexDocument("essay_089",
                "machine learning is transforming artificial intelligence systems");

        pd.indexDocument("essay_092",
                "machine learning is transforming artificial intelligence systems and research");

        pd.analyzeDocument("essay_123",
                "machine learning is transforming artificial intelligence systems today");
    }
}