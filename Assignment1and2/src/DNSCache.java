import java.util.*;

class DNSEntry {

    String domain;
    String ipAddress;
    long expiryTime;

    DNSEntry(String domain, String ip, long ttl) {
        this.domain = domain;
        this.ipAddress = ip;
        this.expiryTime = System.currentTimeMillis() + ttl * 1000;
    }

    boolean isExpired() {
        return System.currentTimeMillis() > expiryTime;
    }
}

public class DNSCache {

    private int capacity = 100;

    private LinkedHashMap<String, DNSEntry> cache =
            new LinkedHashMap<>(capacity, 0.75f, true);

    private int hits = 0;
    private int misses = 0;

    public String resolve(String domain) {

        if (cache.containsKey(domain)) {

            DNSEntry entry = cache.get(domain);

            if (!entry.isExpired()) {
                hits++;
                return "Cache HIT → " + entry.ipAddress;
            }

            cache.remove(domain);
        }

        misses++;

        String ip = queryUpstreamDNS(domain);

        cache.put(domain, new DNSEntry(domain, ip, 300));

        return "Cache MISS → " + ip;
    }

    private String queryUpstreamDNS(String domain) {

        // simulate DNS lookup
        return "172.217." + new Random().nextInt(255) + "." + new Random().nextInt(255);
    }

    public void getCacheStats() {

        int total = hits + misses;

        double hitRate = (total == 0) ? 0 : (double) hits / total * 100;

        System.out.println("Hits: " + hits);
        System.out.println("Misses: " + misses);
        System.out.println("Hit Rate: " + hitRate + "%");
    }

    public static void main(String[] args) throws Exception {

        DNSCache dns = new DNSCache();

        System.out.println(dns.resolve("google.com"));
        System.out.println(dns.resolve("google.com"));

        Thread.sleep(2000);

        dns.getCacheStats();
    }
}