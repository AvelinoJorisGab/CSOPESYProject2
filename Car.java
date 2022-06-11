/**
* Avelino, Joris Gabriel
* Cayton, Alenna Jaye
* Escarraga, Joaquin
* Ocampo, Andrea Nicole
* Pioquinto, Cherrie Luz
* CSOPESY S11
*/

import java.text.SimpleDateFormat;
import java.util.Date;

/*
* This class creates the Cars for the Multi-car rollercoaster.
*/
class Car extends Thread {
    public int id;
    public boolean ready_to_ride;

    /*
    * This is the constructor for the class.
    * @param id = ID of the car
    */
    Car(int id){
        this.id = id;
        this.ready_to_ride = true;
    }

    /*
    * This method runs the current 
    * Car thread.
    * @return   Nothing
    */
    public void run(){
        while(true){
            try{
                // Step 1: Acquire permission to load passengers only if car is ready. (Prevents starvation)
                if (this.ready_to_ride == true){
                    Main.loadingArea[id].acquire();
                        // Critical Section:
                        // Loads the passengers, and releases the number of permissions for next loads.
                        load();
                        this.ready_to_ride = false;
                        Main.boardQueue.release(Main.capacity);
                        Main.allAboard.acquire();
                        System.out.println(new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(new Date()) + " All aboard car ["+ id +"]!");
                    
                    // Step 2: Release permission to load passengers for next car.
                        Main.loadingArea[next(id)].release();

                    // Step 3: Drive the car around the track for a certain amount of time.
                    drive();

                    // Step 4: Acquire permission to unload passengers.
                    Main.unloadingArea[id].acquire();
                        // Critical Section:
                        // Unoads the passengers, and releases the number of permissions for next unloads.
                        unload();
                        Main.unboardQueue.release(Main.capacity);
                        Main.allAshore.acquire();
                        System.out.println(new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(new Date()) + " All ashore from car ["+ id +"]!");
                        Main.unloadeds += 1;

                        // If all cars are done running, exit the program.
                        if(Main.unloadeds == Main.numCars){
                            System.out.println(new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(new Date()) + " All rides completed!");
                            Main.unloadeds = 0;
                            System.exit(0);
                        }
                    
                    // Step 5: Release permission to unload passengers for next car.
                    Main.unloadingArea[next(id)].release();
                }
            }
            catch(InterruptedException e){
                e.printStackTrace();
            }
        }
    }


    /*
    * This method invokes the load procedure.
    * @return   Nothing
    */
    public void load(){
        System.out.println(new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(new Date()) + " Car [" + id + "] is now ready for boarding.");
    }

    /*
    * This method invokes the drive procedure.
    * @return   Nothing
    */
    public void drive(){
        try{
            System.out.println(new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(new Date()) + " Car [" + id + "] is running.");
            sleep(3000);
        }
        catch(InterruptedException e){
            e.printStackTrace();
        }
    }

    /*
    * This method invokes the unload procedure.
    * @return   Nothing
    */
    public void unload(){
        System.out.println(new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(new Date()) + " Car [" + id + "] is now ready for unboarding.");
    }

    /*
    * This method computes the next car in queue.
    * @return   Nothing
    */
    public int next(int num){
        return (num + 1) % (Main.numCars);
    }
}
