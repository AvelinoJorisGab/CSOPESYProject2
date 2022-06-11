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

class Passenger extends Thread {
    public int id;
    public volatile boolean passengerRunning = true;

    /*
    * This is the constructor for the class.
    * @param id = ID of the passenger
    */
    Passenger(int id){
        this.id = id;
    }

    /*
    * This method runs the current 
    * passenger thread.
    * @return   Nothing
    */
    public void run(){
        while(passengerRunning){
            try{
                wander();

                Main.boardQueue.acquire();
                board();

                Main.unboardQueue.acquire();
                unboard();
            }
            catch(InterruptedException e){
                e.printStackTrace();
            }
        }
    }

    /*
    * This method invokes the wander procedure.
    * @return   Nothing
    */
    public void wander(){
        try{
            System.out.println(new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(new Date()) + " Passenger [" + id + "] is wandering.");
            sleep(Math.round(Math.random() * 1000));
        }
        catch(InterruptedException e){
            e.printStackTrace();
        }
    }

    /*
    * This method invokes the board procedure.
    * @return   Nothing
    */
    public void board(){
        try{
            Main.mutex1.acquire();
            System.out.println(new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(new Date()) + " Passenger ["+ id +"] is now boarding.");
            Main.boarders += 1;
            if(Main.boarders == Main.capacity){
                Main.allAboard.release();
                Main.boarders = 0;
            }
            Main.mutex1.release();
        }
        catch(InterruptedException e){
            e.printStackTrace();
        }
    }

    /*
    * This method invokes the unboard procedure.
    * @return   Nothing
    */
    public void unboard(){
        try{
            Main.mutex2.acquire();
            System.out.println(new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(new Date()) + " Passenger ["+ id +"] is now unboarding.");
            Main.unboarders += 1;
            if(Main.unboarders == Main.capacity){
                Main.allAshore.release();
                Main.unboarders = 0;
            }
            Main.mutex2.release();
            Main.completed += 1;
        }
        catch(InterruptedException e){
            e.printStackTrace();
        }
    }
}
