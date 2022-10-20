package processor.pipeline;

public class IF_OF_LatchType {
	
	boolean OF_enable;
	int instruction;
	boolean isBusy;
	int instPC;

	public IF_OF_LatchType()
	{
		OF_enable = false;
		isBusy = false;
		instPC = -1;
		instruction = -1999;
	}

	public IF_OF_LatchType(boolean oF_enable, boolean IsBusy)
	{
		OF_enable = oF_enable;
		isBusy = IsBusy;
		instPC = -1;
		instruction = -1999;
	}

	public boolean isOF_enable() {
		return OF_enable;
	}

	public void setOF_enable(boolean oF_enable) {
		OF_enable = oF_enable;
	}

	public int getInstruction() {
		return instruction;
	}

	public void setInstruction(int instruction) {
		this.instruction = instruction;
	}

	public boolean checkInst(int instruction) {
		return this.instruction == instruction;
	}

	public boolean checkPC(int pc) {
		return instPC == pc;
	}

	public String toString() {
		return "IF_OF_LatchType";
	}
}
