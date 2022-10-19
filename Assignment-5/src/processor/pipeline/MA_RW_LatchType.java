package processor.pipeline;

import generic.Instruction;

public class MA_RW_LatchType {
	
	boolean RW_enable;
	Instruction instruction;
	int load_result;
	int alu_result;
	boolean nop;
	
	public MA_RW_LatchType()
	{
		RW_enable = false;
		nop = false;
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

	public void setInstruction(Instruction inst) {
		instruction = inst;
	}

	public void setldResult(int result) {
		load_result = result;
	}

	public int getldResult() {
		return load_result;
	}

	public int getaluResult() {
		return alu_result;
	}

	public void setaluResult(int result) {
		alu_result = result;
	}
	
	public boolean getIsNop() {
		return nop;
	}
	
	public void setIsNop(boolean isNop) {
		nop = isNop;
	}
}
