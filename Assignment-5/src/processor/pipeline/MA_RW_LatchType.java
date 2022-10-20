package processor.pipeline;

import generic.Instruction;

public class MA_RW_LatchType {
	
	boolean RW_enable;
	Instruction instruction;
	int load_result;
	int alu_result;
	boolean nop;
	
	boolean isNOP;
	int instPC;
	int rs1, rs2, rd, imm, rs1addr, rs2addr;
	String opcode;
	boolean isLoad;
	
	public MA_RW_LatchType()
	{
		RW_enable = false;
		nop = false;

		isNOP = false;
		rs1 = 999999;
		rs2 = 999999;
		rd = 999999;
		imm = 999999;
		opcode = "999999";
		instPC = -1;
		rs1addr = 45;
		rs2addr = 45;
		isLoad = false;
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
	
	public String toString() {
		return "MA_RW_LatchType";
	}
}
