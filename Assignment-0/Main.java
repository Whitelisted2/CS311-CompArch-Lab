import parts.*;

public class Main {
   public static void main(String[] args) {

        double p = 0.5;                           // p declared here ... 
        int width = 5;                            // width of DMZ

        int time = 0;                             // driving loop will begin soon!
        Infiltrator me_cell = new Infiltrator();

        while(me_cell.is_win == false && me_cell.is_caught == false) {

            // sensor does work (recalibrating):
            SensorCell cell_wCurr = new SensorCell(p); // current cell
            SensorCell cell_a = new SensorCell(p);     // cell diagonally bottom-left
            SensorCell cell_s = new SensorCell(p);     // cell below
            SensorCell cell_d = new SensorCell(p);     // cell diagonally bottom right

            // print block for verification
            System.out.println("_______________________________________________");
            System.out.println("curr depth: "+ me_cell.depth_curr + "  time(before move):"+time);
            System.out.println("  curr sensor: " + cell_wCurr.is_on);
            System.out.println("a: " + cell_a.is_on + ", s: " + cell_s.is_on + ", d: " + cell_d.is_on);
            
            
            // border case where infil. is at the lowermost row of sensors
            if(me_cell.depth_curr == width){
                if(!cell_wCurr.is_on){
                    me_cell.depth_curr++; // this line not required actually ... 
                    time+=10;
                    System.out.println("!!!!reached other side :)");
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
        System.out.println("Total time taken, t = " + time);
        System.out.println("Width of this case, w = "+ width);
        System.out.println("Probability of a sensor being ON, p = "+ p);

        // p,w,t : output format
        

   } 
}
