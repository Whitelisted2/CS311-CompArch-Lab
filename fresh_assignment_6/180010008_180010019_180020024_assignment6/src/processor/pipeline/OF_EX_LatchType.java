package processor.pipeline;

import generic.Instruction;

public class OF_EX_LatchType {

    public int insPC;
    boolean EX_enable;
    Instruction instruction;
    boolean EX_busy,isNop;

    public OF_EX_LatchType() {
        Instruction nop = new Instruction();
        nop.setOperationType(Instruction.OperationType.nop);
        EX_enable = false;
        instruction = nop;
        EX_busy = false;
        isNop = false;
        insPC = -1;
    }

    public boolean isEX_busy() {
        return EX_busy;
    }

    public void setEX_busy(boolean eX_busy) {
        EX_busy = eX_busy;
    }

    public boolean isEX_enable() {
        return EX_enable;
    }

    public void setEX_enable(boolean eX_enable) {
        EX_enable = eX_enable;
    }

    public Instruction getInstruction() {
        return this.instruction;
    }

    public void setInstruction(Instruction instruction) {
        this.instruction = instruction;
    }

}
