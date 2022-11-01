package processor.pipeline;

import generic.Instruction;

public class MA_RW_LatchType {

    boolean RW_enable;
    Instruction instruction;
    int load_output;
    int alu_output;
    boolean RW_busy;

    public MA_RW_LatchType() {
        Instruction nop = new Instruction();
        nop.setOperationType(Instruction.OperationType.nop);
        RW_enable = true;
        instruction = nop;
        RW_busy = false;
    }

    public boolean isRW_busy() {
        return RW_busy;
    }

    public void setRW_busy(boolean RW_busy) {
        this.RW_busy = RW_busy;
    }

    public boolean isRW_enable() {
        return RW_enable;
    }

    public void setRW_enable(boolean rW_enable) {
        RW_enable = rW_enable;
    }

    public Instruction getInstruction() {
        return instruction;
    }

    public void setInstruction(Instruction cmd) {
        instruction = cmd;
    }

    public int getLoad_Output() {
        return load_output;
    }

    public void setLoad_Output(int output) {
        load_output = output;
    }

    public int getALU_Output() {
        return alu_output;
    }

    public void setALU_Output(int output) {
        alu_output = output;
    }

}
