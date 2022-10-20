package processor.pipeline;

import generic.Instruction;

public class OF_EX_LatchType {

    boolean EX_enable;
    Instruction instruction;
    boolean EX_busy;

    public OF_EX_LatchType() {
        Instruction nop = new Instruction();
        nop.setOperationType(Instruction.OperationType.nop);
        EX_enable = true;
        instruction = nop;
        EX_busy = false;
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
