package parts;

public class Clock {
   public int the_time;
   public Clock(){
        the_time = 0;
   }
   public void addTime(int t){
        this.the_time += t;
   }
}
