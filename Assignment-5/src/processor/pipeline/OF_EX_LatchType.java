package processor.pipeline;

import generic.Instruction;

public class OF_EX_LatchType {
	
	boolean EX_enable;
	Instruction instruction;
	boolean NOP;
	boolean isBusy;

	boolean isNOP;
	int instPC;

	public OF_EX_LatchType()
	{
		EX_enable = false;
		NOP = false;

		isNOP = false;
		isBusy = false;

	}

	public boolean isEX_enable() {
		return EX_enable;
	}

	public void setEX_enable(boolean eX_enable) {
		EX_enable = eX_enable;
	}

	public void setInstruction(Instruction instruction) {
		this.instruction = instruction;
	}

	public Instruction getInstruction() {
		return this.instruction;
	}
	
	public boolean getIsNOP() {
		return NOP;
	}
	
	public void setIsNOP(boolean is_NOP) {
		NOP = is_NOP;
	}

	public boolean checkPC (int pc) {
		return instPC == pc;
	}

	public String toString() {
		return "OF_EX_LatchType";
	}

}
