package parts;

public class Infiltrator {
    public int depth_curr;
    public boolean is_win, is_caught;
    public Infiltrator(){
        depth_curr = 1;
        is_win = false;
        is_caught = false;  // this will be a precaution probably. though shouldn't rly have to be used,
                            // considering the conditions we will place for moving
    }
}
