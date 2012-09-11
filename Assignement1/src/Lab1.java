
import TSim.*;
import java.util.concurrent.Semaphore;

public class Lab1 {

    public static int simulationSpeed;

    public static void main(String[] args) {
        new Lab1(args);
    }

    public Lab1(String[] args) {
        if (args.length != 2 || args.length != 3) {
            System.err.println("2 or 3 arguments !!!!!");
            return;
        }

        if (args.length == 3) {
            simulationSpeed = Integer.parseInt(args[2]);
        } else {
            simulationSpeed = 100;
        }

        Train t1 = new Train(1, Integer.parseInt(args[0]), true);
        Train t2 = new Train(2, Integer.parseInt(args[1]), true);

        t1.start();
        t2.start();

    }
}

class Train extends Thread {

    public static Semaphore sc1 = new Semaphore(1);
    public static Semaphore sc2 = new Semaphore(1);
    public static Semaphore sc3 = new Semaphore(1);
    public static Semaphore in1 = new Semaphore(0);
    public static Semaphore in2 = new Semaphore(1);
    public static Semaphore in3 = new Semaphore(0);
    boolean direction;
    int id;
    int speed;
    TAKENSC takenSC = TAKENSC.NONE;
    TAKENIN takenIN;

    enum TAKENSC {

        SC1,
        SC2,
        SC3,
        NONE
    }

    enum TAKENIN {

        IN1,
        IN2,
        IN3,
        NONE
    }

    public Train(int id, int speed, boolean direction) {
        this.direction = direction;
        this.id = id;
        this.speed = speed;

        if (id == 1) {
            takenIN = TAKENIN.IN3;
        } else if (id == 2) {
            takenIN = TAKENIN.IN1;
        }
    }

    @Override
    public void run() {
        TSimInterface tsi = TSimInterface.getInstance();
        while (true) {
            try {
                SensorEvent se = tsi.getSensor(this.id);

                if (se.getXpos() == 15 && se.getYpos() == 3) { //Stations section
                } else if (se.getXpos() == 15 && se.getYpos() == 5) {
                } else if (se.getXpos() == 15 && se.getYpos() == 11) {
                } else if (se.getXpos() == 15 && se.getYpos() == 13) {
                } else if (se.getXpos() == 8 && se.getYpos() == 5) { //SC3
                    if (takenSC == TAKENSC.SC3) {
                        sc3.release();
                        takenSC = TAKENSC.NONE;
                    } else {
                        testFreeWay(sc3);
                        takenSC = TAKENSC.SC3;
                    }
                } else if (se.getXpos() == 10 && se.getYpos() == 7) {
                } else if (se.getXpos() == 6 && se.getYpos() == 7) {
                } else if (se.getXpos() == 9 && se.getYpos() == 8) {
                } else if (se.getXpos() == 15 && se.getYpos() == 7) { //SC2
                } else if (se.getXpos() == 16 && se.getYpos() == 8) {
                } else if (se.getXpos() == 13 && se.getYpos() == 9) {
                } else if (se.getXpos() == 14 && se.getYpos() == 10) {
                } else if (se.getXpos() == 6 && se.getYpos() == 9) { //SC1
                } else if (se.getXpos() == 5 && se.getYpos() == 10) {
                } else if (se.getXpos() == 5 && se.getYpos() == 11) {
                } else if (se.getXpos() == 3 && se.getYpos() == 13) {
                } else if (se.getXpos() == 19 && se.getYpos() == 7) { //IN3
                    if (takenIN == TAKENIN.IN3) {
                        in3.release();
                        takenIN =TAKENIN.NONE;
                    }else{


                        takenIN = TAKENIN.IN3;
                    }
                } else if (se.getXpos() == 17 && se.getYpos() == 9) { //IN2
                } else if (se.getXpos() == 2 && se.getYpos() == 9) {
                } else if (se.getXpos() == 1 && se.getYpos() == 11) { //IN1
                }

            } catch (CommandException e) {
                e.printStackTrace();    // or only e.getMessage() for the error
                System.exit(1);
            } catch (InterruptedException e) {
                e.printStackTrace();    // or only e.getMessage() for the error
                System.exit(1);
            }
        }
    }

    private void testFreeWay(Semaphore s) throws CommandException, InterruptedException {
        TSimInterface tsi = TSimInterface.getInstance();
        if (sc3.tryAcquire() == false) {
            tsi.setSpeed(this.id, 0);
            s.acquire();
            tsi.setSpeed(this.id, this.speed);
        }
    }

    private void chooseFreeWay(Semaphore s, int xin, int yin, int fastestWay) throws CommandException, InterruptedException {
        TSimInterface tsi = TSimInterface.getInstance();
        int otherWay;
        if(fastestWay == TSimInterface.SWITCH_LEFT)
            otherWay = TSimInterface.SWITCH_RIGHT;
        else
            otherWay = TSimInterface.SWITCH_LEFT;

        if (s.tryAcquire() == false) { // Fastest way occuped
            tsi.setSwitch(xin, yin, otherWay);
        } else { // We take the fastest way
            tsi.setSwitch(xin, yin, fastestWay);
        }
    }
}
