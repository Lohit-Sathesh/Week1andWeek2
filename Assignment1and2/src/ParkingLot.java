import java.util.*;

class ParkingSpot {

    String plate;
    long entryTime;
    String status;

    ParkingSpot() {
        status = "EMPTY";
    }
}

public class ParkingLot {

    int SIZE = 500;

    ParkingSpot[] table = new ParkingSpot[SIZE];

    int occupied = 0;

    public ParkingLot() {
        for (int i = 0; i < SIZE; i++)
            table[i] = new ParkingSpot();
    }

    int hash(String plate) {
        return Math.abs(plate.hashCode()) % SIZE;
    }

    public void parkVehicle(String plate) {

        int index = hash(plate);

        int probes = 0;

        while (table[index].status.equals("OCCUPIED")) {

            index = (index + 1) % SIZE;

            probes++;
        }

        table[index].plate = plate;

        table[index].entryTime = System.currentTimeMillis();

        table[index].status = "OCCUPIED";

        occupied++;

        System.out.println(
                "Assigned spot #" + index +
                        " (" + probes + " probes)"
        );
    }

    public void exitVehicle(String plate) {

        int index = hash(plate);

        while (!table[index].status.equals("EMPTY")) {

            if (plate.equals(table[index].plate)) {

                long duration =
                        System.currentTimeMillis() -
                                table[index].entryTime;

                double hours = duration / (1000.0 * 3600);

                double fee = hours * 5;

                table[index].status = "DELETED";

                occupied--;

                System.out.println(
                        "Spot #" + index +
                                " freed. Fee: $" + fee
                );

                return;
            }

            index = (index + 1) % SIZE;
        }
    }

    public void getStatistics() {

        double occupancy =
                (double) occupied / SIZE * 100;

        System.out.println("Occupancy: " + occupancy + "%");
    }

    public static void main(String[] args) {

        ParkingLot lot = new ParkingLot();

        lot.parkVehicle("ABC1234");

        lot.parkVehicle("ABC1235");

        lot.parkVehicle("XYZ9999");

        lot.exitVehicle("ABC1234");

        lot.getStatistics();
    }
}