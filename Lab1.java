
import TSim.*;
import java.util.concurrent.Semaphore;

public class Lab1 {

    public static int simulationSpeed;

    public static void main(String[] args) {
        new Lab1(args);
    }

    public Lab1(String[] args) {
        if (args.length != 2 && args.length != 3) {
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
        try {
            tsi.setSpeed(this.id, this.speed);
            while (true) {

                SensorEvent se = tsi.getSensor(this.id);

                if (se.getXpos() == 15 && se.getYpos() == 3) { //Stations section
                } else if (se.getXpos() == 15 && se.getYpos() == 5) {
                } else if (se.getXpos() == 15 && se.getYpos() == 11) {
                } else if (se.getXpos() == 15 && se.getYpos() == 13) {
                } else if (se.getXpos() == 8 && se.getYpos() == 5 //SC3
                        || se.getXpos() == 10 && se.getYpos() == 7
                        || se.getXpos() == 6 && se.getYpos() == 7
                        || se.getXpos() == 9 && se.getYpos() == 8) {
                    if (takenSC == TAKENSC.SC3) {
                        sc3.release();
                        takenSC = TAKENSC.NONE;
                    } else {
                        testFreeWay(sc3);
                        takenSC = TAKENSC.SC3;
                    }
                } else if (se.getXpos() == 15 && se.getYpos() == 7) { //SC2
                    if (takenSC == TAKENSC.SC2) {
                        sc2.release();
                        takenSC = TAKENSC.NONE;
                    } else {
                        testFreeWay(sc2);
                        takenSC = TAKENSC.SC2;
                        tsi.setSwitch(17, 7, TSimInterface.SWITCH_RIGHT);
                    }
                } else if (se.getXpos() == 16 && se.getYpos() == 8) {
                    if (takenSC == TAKENSC.SC2) {
                        sc2.release();
                        takenSC = TAKENSC.NONE;
                    } else {
                        testFreeWay(sc2);
                        takenSC = TAKENSC.SC2;
                        tsi.setSwitch(17, 7, TSimInterface.SWITCH_LEFT);
                    }
                } else if (se.getXpos() == 13 && se.getYpos() == 9) {
                    if (takenSC == TAKENSC.SC2) {
                        sc2.release();
                        takenSC = TAKENSC.NONE;
                    } else {
                        testFreeWay(sc2);
                        takenSC = TAKENSC.SC2;
                        tsi.setSwitch(15, 9, TSimInterface.SWITCH_RIGHT);
                    }
                } else if (se.getXpos() == 14 && se.getYpos() == 10) {
                    if (takenSC == TAKENSC.SC2) {
                        sc2.release();
                        takenSC = TAKENSC.NONE;
                    } else {
                        testFreeWay(sc2);
                        takenSC = TAKENSC.SC2;
                        tsi.setSwitch(15, 9, TSimInterface.SWITCH_LEFT);
                    }
                } else if (se.getXpos() == 6 && se.getYpos() == 9) { //SC1
                    if (takenSC == TAKENSC.SC1) {
                        sc1.release();
                        takenSC = TAKENSC.NONE;
                    } else {
                        testFreeWay(sc1);
                        takenSC = TAKENSC.SC1;
                        tsi.setSwitch(4, 9, TSimInterface.SWITCH_LEFT);
                    }
                } else if (se.getXpos() == 5 && se.getYpos() == 10) {
                    if (takenSC == TAKENSC.SC1) {
                        sc1.release();
                        takenSC = TAKENSC.NONE;
                    } else {
                        testFreeWay(sc1);
                        takenSC = TAKENSC.SC1;
                        tsi.setSwitch(4, 9, TSimInterface.SWITCH_RIGHT);
                    }
                } else if (se.getXpos() == 5 && se.getYpos() == 11) {
                    if (takenSC == TAKENSC.SC1) {
                        sc1.release();
                        takenSC = TAKENSC.NONE;
                    } else {
                        testFreeWay(sc1);
                        takenSC = TAKENSC.SC1;
                        tsi.setSwitch(3, 11, TSimInterface.SWITCH_LEFT);
                    }
                } else if (se.getXpos() == 3 && se.getYpos() == 13) {
                    if (takenSC == TAKENSC.SC1) {
                        sc1.release();
                        takenSC = TAKENSC.NONE;
                    } else {
                        testFreeWay(sc1);
                        takenSC = TAKENSC.SC1;
                        tsi.setSwitch(3, 11, TSimInterface.SWITCH_RIGHT);
                    }
                } else if (se.getXpos() == 19 && se.getYpos() == 7) { //IN3
                } else if (se.getXpos() == 17 && se.getYpos() == 9) { //IN2
                } else if (se.getXpos() == 2 && se.getYpos() == 9) {
                } else if (se.getXpos() == 1 && se.getYpos() == 11) { //IN1
                }
            }
        } catch (CommandException e) {
            e.printStackTrace();    // or only e.getMessage() for the error
            System.exit(1);
        } catch (InterruptedException e) {
            e.printStackTrace();    // or only e.getMessage() for the error
            System.exit(1);
        }
    }

    private void testFreeWay(Semaphore s) throws CommandException, InterruptedException {
        TSimInterface tsi = TSimInterface.getInstance();
        if (s.tryAcquire() == false) {
            tsi.setSpeed(this.id, 0);
            s.acquire();
            tsi.setSpeed(this.id, this.speed);
        }
    }
}

