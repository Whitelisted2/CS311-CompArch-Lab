package processor.pipeline;

import generic.Instruction;

public class MA_RW_LatchType {
	
	boolean RW_enable;
	boolean nop;
	Instruction instruction;
	int ldResult;
	int aluResult;
	
	// constructor
	public MA_RW_LatchType()
	{
		RW_enable = false;
		nop = false;
	}

	public MA_RW_LatchType(boolean rW_enable)
	{
		RW_enable = rW_enable;
		nop = false;
	}

	public MA_RW_LatchType(boolean rW_enable, Instruction instruction)
	{
		RW_enable = rW_enable;
		this.instruction = instruction;
		nop = false;
	}

	public MA_RW_LatchType(boolean rW_enable, Instruction instruction, int LdResult)
	{
		RW_enable = rW_enable;
		this.instruction = instruction;
		this.ldResult = LdResult;
		nop = false;
	}

	public MA_RW_LatchType(boolean rW_enable, Instruction instruction, int LdResult, int aLuResult)
	{
		RW_enable = rW_enable;
		this.instruction = instruction;
		this.ldResult = LdResult;
		this.aluResult = aLuResult;
		nop = false;
	}

	// enable
	public boolean isRW_enable() {
		return RW_enable;
	}

	public void setRW_enable(boolean rW_enable) {
		RW_enable = rW_enable;
	}

	// instructor
	public Instruction getInstruction()
	{
		return instruction;
	}

	public void setInstruction(Instruction iNstruction)
	{
		instruction = iNstruction;
	}

	// ldResult
	public int getldResult()
	{
		return ldResult;
	}

	public void setldResult(int LdResult)
	{
		ldResult = LdResult;
	}

	// aluResult 
	public int getaluResult()
	{
		return aluResult;
	}

	public void setaluResult(int aLuResult)
	{
		aluResult = aLuResult;
	}

	public boolean getIsNOP(){
		return nop;
	}
	public void setIsNOP(boolean Nop){
		nop = Nop;
	}

}
