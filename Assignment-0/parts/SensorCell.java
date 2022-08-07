package parts;
import java.util.Random;

public class SensorCell {
    public boolean is_on;
    // by constructor, the cell(s) generated will be ON with probability 'p'. Else, OFF.
    public SensorCell(double p){
        Random rnum = new Random();
        double r = rnum.nextDouble();
        if(r <= p){
            this.is_on = true;
        } else{
            this.is_on = false;
        }
    } 
}
