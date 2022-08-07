import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;

import parts.*;

public class Main {
   public static void main(String[] args) throws FileNotFoundException {
        // number of trials conducted for each (p,w) pair 
        int num_trials = 5;

        // hard coding block; remove for loops for this
        // double p = 0.5;                            // p 
        // int width = 30;                            // width
        double[] p_arr = {0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8};
        int[] width_arr = {3, 6, 9, 12, 15, 18, 21, 24, 27, 30, 33};
        
        File file = new File("output.txt");
        PrintStream stream = new PrintStream(file);
        PrintStream console = System.out;
                
        // p values:  0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8
        // w values: 3, 6, 9, 12, 15, 18, 21, 24, 27, 30, 33
        for(double p:p_arr) {
            for(int width:width_arr) {
                for(int tnum = 0; tnum<num_trials; tnum++) {

                    // System.setOut();
                    int time = 0;                             // driving loop will begin soon!
                    Infiltrator me_cell = new Infiltrator();

                    while(me_cell.is_win == false && me_cell.is_caught == false) {

                        // sensor does work (recalibrating):
                        Sensor cell_wCurr = new Sensor(p); // current cell
                        Sensor cell_a = new Sensor(p);     // cell diagonally bottom-left
                        Sensor cell_s = new Sensor(p);     // cell below
                        Sensor cell_d = new Sensor(p);     // cell diagonally bottom right

                        // print block for verification
                        System.out.println("_______________________________________________");
                        System.out.println("curr depth: "+ me_cell.depth_curr + "  time(before move):"+time);
                        System.out.println("  curr sensor: " + cell_wCurr.is_on);
                        if(me_cell.depth_curr != width){
                        System.out.println("a: " + cell_a.is_on + ", s: " + cell_s.is_on + ", d: " + cell_d.is_on);
                        }
                        
                        // border case where infil. is at the lowermost row of sensors
                        if(me_cell.depth_curr == width){
                            if(!cell_wCurr.is_on){
                                me_cell.depth_curr++; // this line not required actually ... 
                                time+=10;
                                
                                System.out.println("\n!!! reached other side !!!");
                                me_cell.is_win = true;
                                break;
                            }else{
                                time+=10;
                                continue;
                            }
                        }

                        // infiltrator does work (moving, or not):
                        if(((!cell_a.is_on) || (!cell_s.is_on) || (!cell_d.is_on)) && !cell_wCurr.is_on){
                            me_cell.depth_curr++;
                        }
                        time+=10;
                    }
                    System.out.println("_______________________________________________");
                    System.out.println("Probability of a sensor being ON, p = "+ p);
                    System.out.println("Width of this case, w = "+ width);
                    System.out.println("Total time taken, t = " + time);

                    // p,w,t : output format
                    System.setOut(stream);
                    System.out.println(p + "," + width + "," + time);
                    System.setOut(console);
                }
            }
        }
    } 
}
