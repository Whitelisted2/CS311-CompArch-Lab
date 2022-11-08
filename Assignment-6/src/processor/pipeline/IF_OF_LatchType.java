package processor.pipeline;

public class IF_OF_LatchType {

    int insPC;
    boolean OF_enable;
    int instruction;
    boolean OF_busy;

    public IF_OF_LatchType() {
        OF_enable = false;
        instruction = -1999;
        OF_busy = false;
        insPC = -1;
    }

    public int getInsPC() {
        return insPC;
    }

    public void setInsPC(int insPC) {
        this.insPC = insPC;
    }

    public boolean isOF_busy() {
        return OF_busy;
    }

    public void setOF_busy(boolean oF_busy) {
        OF_busy = oF_busy;
    }

    public boolean isOF_enable() {
        return OF_enable;
    }

    public void setOF_enable(boolean oF_enable) {
        OF_enable = oF_enable;
    }

    public int getInstruction() {
        return instruction;
    }

    public void setInstruction(int instruction) {
        this.instruction = instruction;
    }

}
