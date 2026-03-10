import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class FlashSaleInventory {

    // product stock table
    private ConcurrentHashMap<String, Integer> stockTable = new ConcurrentHashMap<>();

    // waiting list FIFO
    private HashMap<String, LinkedHashMap<Integer, Integer>> waitingList = new HashMap<>();

    // initialize product
    public void addProduct(String productId, int stock) {
        stockTable.put(productId, stock);
        waitingList.put(productId, new LinkedHashMap<>());
    }

    // check stock
    public int checkStock(String productId) {
        return stockTable.getOrDefault(productId, 0);
    }

    // purchase item
    public synchronized String purchaseItem(String productId, int userId) {

        int stock = stockTable.get(productId);

        if (stock > 0) {
            stockTable.put(productId, stock - 1);
            return "Success, " + (stock - 1) + " units remaining";
        }

        LinkedHashMap<Integer, Integer> queue = waitingList.get(productId);
        queue.put(userId, queue.size() + 1);

        return "Added to waiting list, position #" + queue.size();
    }

    public static void main(String[] args) {

        FlashSaleInventory system = new FlashSaleInventory();

        system.addProduct("IPHONE15_256GB", 100);

        System.out.println(system.checkStock("IPHONE15_256GB"));

        System.out.println(system.purchaseItem("IPHONE15_256GB", 12345));
        System.out.println(system.purchaseItem("IPHONE15_256GB", 67890));
    }
}