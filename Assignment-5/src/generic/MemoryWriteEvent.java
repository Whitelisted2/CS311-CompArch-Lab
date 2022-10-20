package generic;

public class MemoryWriteEvent extends Event {

	int addressToWriteTo;
	int value;
	
	public MemoryWriteEvent(long eventTime, Element requestingElement, Element processingElement, int address, int value) {
		super(eventTime, EventType.MemoryWrite, requestingElement, processingElement);
		this.addressToWriteTo = address;
		this.value = value;
	}

	public int getAddressToWriteTo() {
		return addressToWriteTo;
	}

	public void setAddressToWriteTo(int addressToWriteTo) {
		this.addressToWriteTo = addressToWriteTo;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	// -----------------------------------------------
	public boolean checkEqual(int Value) {
		if(this.value == Value) {
			return true;
		} else {
			return false;
		}
	}

	public String toString() {
		return "Memory Write event, Get and Set Address";
	}
}
