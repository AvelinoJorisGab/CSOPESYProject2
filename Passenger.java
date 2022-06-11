class Passenger extends Thread {
    public int id;
    public volatile boolean passengerRunning = true;

    Passenger(int id){
        this.id = id;
    }

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

    public void wander(){
        try{
            System.out.println("Passenger [" + id + "] is wandering.");
            sleep(Math.round(Math.random() * 1000));
        }
        catch(InterruptedException e){
            e.printStackTrace();
        }
    }

    public void board(){
        try{
            Main.mutex1.acquire();
            System.out.println("Passenger ["+ id +"] is now boarding.");
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

    public void unboard(){
        try{
            Main.mutex2.acquire();
            System.out.println("Passenger ["+ id +"] is now unboarding.");
            Main.unboarders += 1;
            if(Main.unboarders == Main.capacity){
                Main.allAshore.release();
                Main.unboarders = 0;
            }
            Main.mutex2.release();
            Main.completed += 1;
            if(Main.completed == Main.numPassengers){
                System.out.println("All passengers completed!");
                System.exit(0);
            }
        }
        catch(InterruptedException e){
            e.printStackTrace();
        }
    }
}
