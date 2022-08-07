import parts.*;

public class Main {
   public static void main(String[] args) {

        double p = 0.5;                           // p declared here ... 

        int time = 0;                             // driving loop will begin soon!
        Infiltrator me_cell = new Infiltrator();

        while(me_cell.is_win == false) {
            
            SensorCell curr_wCell = new SensorCell(p); // current cell
            SensorCell cell_a = new SensorCell(p); // cell diagonally bottom-left
            SensorCell cell_s = new SensorCell(p); // cell below
            SensorCell cell_d = new SensorCell(p); // cell diagonally bottom right
        }
        
   } 
}
