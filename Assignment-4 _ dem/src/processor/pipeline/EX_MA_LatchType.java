package processor.pipeline;

import generic.Instruction;

public class EX_MA_LatchType {
	
	boolean MA_enable;
	int alu_result;
	boolean nop;
	Instruction instruction;

	public EX_MA_LatchType()
	{
		MA_enable = false;
		nop = false;
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

	public void setInstruction(Instruction inst) {
		instruction = inst;
	}

	public int getaluResult() {
		return alu_result;
	}

	public void setALU_result(int result) {
		alu_result = result;
	}
	
	public boolean getIsNop() {
		return nop;
	}
	
	public void setIsNop(boolean isNop) {
		nop = isNop;
	}


}
