package processor.pipeline;

import generic.Instruction;

public class EX_MA_LatchType {
	
	boolean MA_enable;
	int alu_result;
	boolean nop;
	Instruction instruction;
	boolean isBusy;

	boolean isNOP;
	int instPC;
	int rs1, rs2, rd, imm, rs1addr, rs2addr;
	String opcode;

	public EX_MA_LatchType()
	{
		MA_enable = false;
		nop = false;
		isBusy = false;

		isNOP = false;
		rs1 = 999999;
		rs2 = 999999;
		rd = 999999;
		imm = 999999;
		opcode = "999999";
		instPC = -1;
		rs1addr = 45;
		rs2addr = 45;
		isBusy = false;
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
