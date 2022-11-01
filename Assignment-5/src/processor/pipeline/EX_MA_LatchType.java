package processor.pipeline;

import generic.Instruction;

public class EX_MA_LatchType {

    boolean MA_enable;
    int alu_result;
    Instruction instruction;
    int flag;
    boolean MA_busy;

    public EX_MA_LatchType() {
        Instruction nop = new Instruction();
        nop.setOperationType(Instruction.OperationType.nop);
        MA_enable = true;
        instruction = nop;
        flag = 0;
        MA_busy = false;
    }

    public boolean isMA_busy() {
        return MA_busy;
    }

    public void setMA_busy(boolean MA_busy) {
        this.MA_busy = MA_busy;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public boolean isMA_enable() {
        return MA_enable;
    }

    public void setMA_enable(boolean mA_enable) {
        MA_enable = mA_enable;
    }

    public Instruction getInstruction() {
        return instruction;
    }

    public void setInstruction(Instruction instruction) {
        this.instruction = instruction;
    }

    public int getALU_result() {
        return alu_result;
    }

    public void setALU_result(int result) {
        alu_result = result;
    }

}
