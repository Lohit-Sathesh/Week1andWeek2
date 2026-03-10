import java.util.*;

class Event {

    String url;
    String userId;
    String source;

    Event(String url, String userId, String source) {
        this.url = url;
        this.userId = userId;
        this.source = source;
    }
}

public class RealTimeAnalytics {

    HashMap<String, Integer> pageViews = new HashMap<>();

    HashMap<String, Set<String>> uniqueVisitors = new HashMap<>();

    HashMap<String, Integer> trafficSources = new HashMap<>();

    public void processEvent(Event e) {

        // page views
        pageViews.put(e.url,
                pageViews.getOrDefault(e.url, 0) + 1);

        // unique visitors
        uniqueVisitors.putIfAbsent(e.url, new HashSet<>());

        uniqueVisitors.get(e.url).add(e.userId);

        // traffic sources
        trafficSources.put(e.source,
                trafficSources.getOrDefault(e.source, 0) + 1);
    }

    public void getDashboard() {

        System.out.println("Top Pages:");

        PriorityQueue<Map.Entry<String, Integer>> pq =
                new PriorityQueue<>((a, b) -> b.getValue() - a.getValue());

        pq.addAll(pageViews.entrySet());

        for (int i = 0; i < 10 && !pq.isEmpty(); i++) {

            var entry = pq.poll();

            String page = entry.getKey();
            int views = entry.getValue();

            int unique = uniqueVisitors.get(page).size();

            System.out.println(
                    (i + 1) + ". " + page +
                            " - " + views +
                            " views (" + unique + " unique)"
            );
        }

        System.out.println("\nTraffic Sources:");

        int total = trafficSources.values().stream().mapToInt(i -> i).sum();

        for (String src : trafficSources.keySet()) {

            int count = trafficSources.get(src);

            double percent = (double) count / total * 100;

            System.out.println(src + ": " + percent + "%");
        }
    }

    public static void main(String[] args) {

        RealTimeAnalytics analytics = new RealTimeAnalytics();

        analytics.processEvent(new Event("/article/breaking-news","user1","google"));
        analytics.processEvent(new Event("/article/breaking-news","user2","facebook"));
        analytics.processEvent(new Event("/sports/championship","user3","direct"));

        analytics.getDashboard();
    }
}