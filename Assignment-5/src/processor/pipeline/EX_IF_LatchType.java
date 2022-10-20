package processor.pipeline;

public class EX_IF_LatchType {

	boolean IS_enable;
	int PC;
	int offset;
	
	public EX_IF_LatchType()
	{
		IS_enable = false;
		offset = 999999;
	}

	public boolean getIS_Enable() {
		return IS_enable;
	}

	public void setIS_Enable(boolean iS_enable, int newPC) {
		IS_enable = iS_enable;
		PC = newPC;
	}

	public void setIS_Enable(boolean iS_enable) {
		IS_enable = iS_enable;
	}

	public int getPC() {
		return PC;
	}

	public void setPC(int pC) {
		this.PC = pC;
	}
}
