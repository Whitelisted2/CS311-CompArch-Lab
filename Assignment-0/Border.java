
 
public class Border {
    // border consists of length (infinite) and width.
    public int width, length;
    public Border(int d){
        width = d;
        length = Integer.MAX_VALUE;
        // no actual need to set L ... we'll never use it; this program accounts for infinite L anyway.
    }
}
