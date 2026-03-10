import java.util.*;

class TrieNode {

    Map<Character, TrieNode> children = new HashMap<>();

    boolean isWord = false;

    int frequency = 0;

    String query;
}

public class AutocompleteSystem {

    TrieNode root = new TrieNode();

    HashMap<String, Integer> queryFrequency = new HashMap<>();

    public void insert(String query, int freq) {

        queryFrequency.put(query, freq);

        TrieNode node = root;

        for (char c : query.toCharArray()) {

            node.children.putIfAbsent(c, new TrieNode());

            node = node.children.get(c);
        }

        node.isWord = true;

        node.frequency = freq;

        node.query = query;
    }

    public List<String> search(String prefix) {

        TrieNode node = root;

        for (char c : prefix.toCharArray()) {

            if (!node.children.containsKey(c))
                return new ArrayList<>();

            node = node.children.get(c);
        }

        PriorityQueue<TrieNode> pq =
                new PriorityQueue<>((a, b) -> a.frequency - b.frequency);

        dfs(node, pq);

        List<String> result = new ArrayList<>();

        while (!pq.isEmpty())
            result.add(pq.poll().query);

        Collections.reverse(result);

        return result;
    }

    void dfs(TrieNode node, PriorityQueue<TrieNode> pq) {

        if (node.isWord) {

            pq.add(node);

            if (pq.size() > 10)
                pq.poll();
        }

        for (TrieNode child : node.children.values())
            dfs(child, pq);
    }

    public static void main(String[] args) {

        AutocompleteSystem ac = new AutocompleteSystem();

        ac.insert("java tutorial", 1234567);
        ac.insert("javascript", 987654);
        ac.insert("java download", 456789);

        System.out.println(ac.search("jav"));
    }
}