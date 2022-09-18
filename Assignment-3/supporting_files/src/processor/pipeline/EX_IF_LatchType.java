package processor.pipeline;

public class EX_IF_LatchType {
	
	boolean IsBranch_enable;
	int PC;

	// constructor
	public EX_IF_LatchType()
	{
		IsBranch_enable = false;
	}

	public EX_IF_LatchType(boolean isBranch_enable) // we may want to set multiplexer to 0, so no need to change pC
	{
		IsBranch_enable = isBranch_enable;
	}

	public EX_IF_LatchType(boolean isBranch_enable, int pC)
	{
		IsBranch_enable = isBranch_enable;
		PC = pC;
	}

	// enable
	public boolean getIsBranch_enable()
	{
		return IsBranch_enable;
	}
	
	public void setIsBranch_enable(boolean isBranch_enable)
	{
		IsBranch_enable = isBranch_enable;
	}

	public void setIsBranch_enable(boolean isBranch_enable, int pC)
	{
		IsBranch_enable = isBranch_enable;
		PC = pC;
	}

	// PC
	public int getPC()
	{
		return PC;
	}

	public void setPC(int pC)
	{
		PC = pC;
	}
}
