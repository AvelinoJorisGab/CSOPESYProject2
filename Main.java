import java.util.*;
import java.util.concurrent.*;

public class Main {
    public static int numPassengers;
    public static int capacity;
    public static int numCars;

    public static int boarders = 0;
    public static int unboarders = 0;
    public static int unloadeds = 0;
    public static int completed = 0;

    public static Semaphore mutex1 = new Semaphore(1);
    public static Semaphore mutex2 = new Semaphore(1);
    public static Semaphore boardQueue = new Semaphore(0);
    public static Semaphore unboardQueue = new Semaphore(0);
    public static Semaphore allAboard = new Semaphore(0);
    public static Semaphore allAshore = new Semaphore(0);
    public static Semaphore[] loadingArea;
    public static Semaphore[] unloadingArea;

    public static Passenger passengers[];
    public static Car cars[];

    public static void main(String[] args){
        
        Scanner reader = new Scanner(System.in);
        System.out.print("Please nter amount of passengers to be processed: ");
        numPassengers = reader.nextInt();

        passengers = new Passenger[numPassengers];
        System.out.print("Please enter the capacity for each car: ");
        capacity = reader.nextInt();

        if(numPassengers <= capacity){
            System.out.println("Unable to process! Number of passengers must be greater than the capacit of the car!");
            System.exit(0);
        }

        System.out.print("Please enter the number of cars: ");
        numCars = reader.nextInt();
        cars = new Car[numCars];
        
        loadingArea = new Semaphore[numCars];
        unloadingArea  = new Semaphore[numCars];
        reader.close();

        for(int i=0; i<numCars; i++){
            loadingArea[i] = new Semaphore(0);
            unloadingArea[i] = new Semaphore(0);
        }
        loadingArea[0].release(); 
        unloadingArea[0].release();

        for(int i=0; i<numPassengers; i++){
            passengers[i]=new Passenger(i);
            passengers[i].start();
        }

        for(int i=0; i<numCars; i++){
            cars[i]=new Car(i);
            cars[i].start();
        }

    }
}
