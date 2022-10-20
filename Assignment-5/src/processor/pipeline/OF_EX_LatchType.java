package processor.pipeline;

import generic.Instruction;

public class OF_EX_LatchType {
	
	boolean EX_enable;
	Instruction instruction;
	boolean NOP;
	boolean isBusy;

	boolean isNOP;
	int instPC;
	int rs1, rs2, rd, imm, rs1addr, rs2addr;
	String opcode;

	public OF_EX_LatchType()
	{
		EX_enable = false;
		NOP = false;

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
