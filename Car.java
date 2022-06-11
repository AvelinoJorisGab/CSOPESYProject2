class Car extends Thread {
    public int id;

    Car(int id){
        this.id = id;
    }

    public void run(){
        while(true){
            try{
                Main.loadingArea[id].acquire();
                load();
                Main.boardQueue.release(Main.capacity);
                Main.allAboard.acquire();
                System.out.println("All aboard car ["+ id +"]!");
                Main.loadingArea[next(id)].release();

                drive();

                Main.unloadingArea[id].acquire();
                unload();
                Main.unboardQueue.release(Main.capacity);
                Main.allAshore.acquire();
                System.out.println("All ashore from car ["+ id +"]!");
                Main.unloadeds += 1;
                if(Main.unloadeds == Main.numCars){
                    System.out.println("All rides completed!");
                    Main.unloadeds = 0;
                }
                Main.unloadingArea[next(id)].release();
            }
            catch(InterruptedException e){
                e.printStackTrace();
            }
        }
    }

    public void load(){
        System.out.println("Car [" + id + "] is now ready for boarding.");
    }

    public void drive(){
        try{
            System.out.println("Car [" + id + "] is running.");
            sleep(3000);
        }
        catch(InterruptedException e){
            e.printStackTrace();
        }
    }

    public void unload(){
        System.out.println("Car [" + id + "] is now ready for unboarding.");
    }

    public int next(int num){
        return (num + 1) % (Main.capacity);
    }
}
