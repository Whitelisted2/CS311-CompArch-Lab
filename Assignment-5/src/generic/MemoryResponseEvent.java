package generic;

public class MemoryResponseEvent extends Event {

	int value;
	
	public MemoryResponseEvent(long eventTime, Element requestingElement, Element processingElement, int value) {
		super(eventTime, EventType.MemoryResponse, requestingElement, processingElement);
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	// ---------------------------------------
	public boolean checkEqual(int Value) {
		if(this.value == Value) {
			return true;
		} else {
			return false;
		}
	}

	public String toString() {
		return "Memory Response event, Set and Get Value";
	}

}
