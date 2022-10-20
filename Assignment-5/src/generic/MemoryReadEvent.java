package generic;

import processor.memorysystem.MainMemory;
import processor.pipeline.InstructionFetch;

public class MemoryReadEvent extends Event {

	int addressToReadFrom;
	
	public MemoryReadEvent(long eventTime, InstructionFetch instructionFetch, MainMemory mainMemory, int address) {
		super(eventTime, EventType.MemoryRead, instructionFetch, mainMemory);
		this.addressToReadFrom = address;
	}

	public int getAddressToReadFrom() {
		return addressToReadFrom;
	}

	public void setAddressToReadFrom(int addressToReadFrom) {
		this.addressToReadFrom = addressToReadFrom;
	}

	// ---------------------------------------
	public boolean checkEqual(int addr) {
		if(addr == addressToReadFrom) {
			return true;
		} else {
			return false;
		}
	}
	public String toString() {
		return "Memory Read event, Set and Get Address";
	}
}
