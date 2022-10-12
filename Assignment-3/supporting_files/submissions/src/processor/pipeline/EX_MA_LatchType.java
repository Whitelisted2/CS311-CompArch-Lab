package processor.pipeline;

import generic.Instruction;

public class EX_MA_LatchType {
	
	boolean MA_enable;
	Instruction instruction;
	int aluResult;
	
	// constructor
	public EX_MA_LatchType()
	{
		MA_enable = false;
	}
	
	public EX_MA_LatchType(boolean mA_enable)
	{
		MA_enable = mA_enable;
	}

	public EX_MA_LatchType(boolean mA_enable, int aLuResult) // for load, only aluResult is used in MA
	{
		MA_enable = mA_enable;
		this.aluResult = aLuResult;
	}

	public EX_MA_LatchType(boolean mA_enable, int aLuResult, Instruction instruction) // for store
	{
		MA_enable = mA_enable;
		this.aluResult = aLuResult;
		this.instruction = instruction;
	}

	// enable
	public boolean isMA_enable() {
		return MA_enable;
	}

	public void setMA_enable(boolean mA_enable) {
		MA_enable = mA_enable;
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

	// instruction
	public Instruction getInstruction()
	{
		return instruction;
	}

	public void setInstruction(Instruction iNstruction)
	{
		instruction = iNstruction;
	}
}
