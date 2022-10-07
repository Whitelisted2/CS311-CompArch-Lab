package processor.pipeline;

import generic.Instruction;

public class OF_EX_LatchType {
	
	boolean EX_enable;
	Instruction instruction;
	boolean nop;
	
	// constructors
	public OF_EX_LatchType() {  // constructor with no parameters, default enable = false
		EX_enable = false;
		nop = false;
	}

	public OF_EX_LatchType(boolean eX_enable) {  // constructor that sets enable as well
		EX_enable = eX_enable;
	}

	public OF_EX_LatchType(boolean eX_enable, Instruction instruction) {  // constructor that sets enable as well
		EX_enable = eX_enable;
		this.instruction = instruction;
	}

	// enable
	public boolean isEX_enable() { // is EX enabled ?
		return EX_enable;
	}

	public void setEX_enable(boolean eX_enable) { // set EX to be enabled
		EX_enable = eX_enable;
	}

	// instruction
	public void setInstruction(Instruction instruction) { // set instruction that was just executed
		this.instruction = instruction;
	}

	public Instruction getInstruction() { // get instruction that was just executed
		return this.instruction;
	}

	public boolean getIsNOP(){
		return nop;
	}
	public void setIsNOP(boolean Nop){
		nop = Nop;
	}
}
