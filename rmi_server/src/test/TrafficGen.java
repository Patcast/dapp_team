package test;

import hotel.BookingInterface;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TrafficGen extends Thread{

    private static final String SERVER_URL = "yonira.westeurope.cloudapp.azure.com";
    private static final int PORT = 8080;

    private static final AtomicInteger messageSent = new AtomicInteger(0);

    public static void main(String[] args) {
        int frequency = Integer.parseInt(args[0]);
        long milliseconds = 1000 / frequency;
        int nanoseconds = (1000000 / frequency) % 1000;

        long startTime = System.currentTimeMillis();

        while (true) {
            long time = System.currentTimeMillis();
            long targetTime = time + 1000; // next time
            double timeElapsed = (time - startTime) / 1000.0f;
            GeneratorGeneration generation = new GeneratorGeneration(frequency, milliseconds, nanoseconds);
            generation.start();

            System.out.print("Messages sent: " + messageSent.get() + " Time elapsed: " + timeElapsed + "s\r");

            while (targetTime > System.currentTimeMillis()) {}
        }
    }

    private final int threadNr;

    public TrafficGen(int threadNr) {
        this.threadNr = threadNr;
    }

    @Override
    public void run() {
        try {
            Registry registry = LocateRegistry.getRegistry(SERVER_URL, PORT);
            BookingInterface bookingInterface = (BookingInterface) registry.lookup("Hotel");

            bookingInterface.getRooms();
            messageSent.incrementAndGet();
        } catch (Exception e) {
            System.out.print("Remote exception thread " + threadNr + "\r");
        }
    }

    public static class GeneratorGeneration extends Thread {
        private final int frequency;
        private final long milliseconds;
        private final int nanoseconds;

        public GeneratorGeneration(int frequency, long milliseconds, int nanoseconds) {
            this.frequency = frequency;
            this.milliseconds = milliseconds;
            this.nanoseconds = nanoseconds;
        }

        @Override
        public void run() {
            for (int i = 0; i < frequency; i++) {
                TrafficGen thread = new TrafficGen(i);
                thread.start();
                try {
                    Thread.sleep(milliseconds, nanoseconds);
                } catch (InterruptedException ignore) { }
            }
        }
    }
}
