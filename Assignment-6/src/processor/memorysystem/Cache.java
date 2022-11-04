package processor.memorysystem;

import generic.*;
import processor.*;
import configuration.Configuration;

public class Cache implements Element{ // override fn is in the end of this
    Processor containingProcessor;
    public int latency;
	int cacheSize;

    int msAddr, temp;
    boolean isHit = true;
    CacheLine[] cacheLine;
    int[] index;

    public Cache(Processor ContainingProcessor, int Latency, int cSize) {
        this.containingProcessor = ContainingProcessor;
        this.latency = Latency;
        this.cacheSize = cSize;

        // hmm
        this.temp = (int)(Math.log(cSize/8)/Math.log(2)); // size of index
        this.cacheLine = new CacheLine[cSize/8];
		for(int i = 0; i < cSize/8; i++) 
			this.cacheLine[i] = new CacheLine();
    }

    public int cacheRead(int address) {
        String addrString = Integer.toBinaryString(address); // binary form of addr to search (not 32b)
 
        for(int i = 0; i < 32 - addrString.length(); i++) {
            addrString = "0" + addrString;  // left pad addr string with zeros, make it 32b
        }

        String index = "";
        for(int i = 0; i < temp; i++) {
            index = index + "1";            // temp no. of 1s
        }
        
        int temp_ind;
        if(temp == 0) {
            temp_ind = 0;
        }
        else {
            temp_ind = address & Integer.parseInt(index, 2); // 
        }
        
        // reading
        System.out.println("In the Cache " + address);
        int addrTag = Integer.parseInt(
            addrString.substring(0, addrString.length() - temp), 2
            );

        if(addrTag == cacheLine[temp_ind].tag[0]) {
            cacheLine[temp_ind].LRUctr = 1;
            isHit = true;
            return cacheLine[temp_ind].data[0];
        }
        else if(addrTag == cacheLine[temp_ind].tag[1]) {
            cacheLine[temp_ind].LRUctr = 0;
            isHit = true;
            return cacheLine[temp_ind].data[1];
        } else {
            isHit = false;
            return -1;
        }
    }

    public void cacheWrite(int address, int value) {
        String addrString = Integer.toBinaryString(address); // binary form of addr to search (not 32b)

        for(int i = 0; i < 32 - addrString.length(); i++) {
            addrString = "0" + addrString;                   // make addr 32b
        }
        
        String index = "";
        for(int i = 0; i < temp; i++) {
            index = index + "1";            // temp no. of ones
        }       
            
        int temp_ind;
        if(temp == 0)
            temp_ind = 0;
        else 
            temp_ind = address & Integer.parseInt(index, 2);
        
        // writing
        int tag = Integer.parseInt(addrString.substring(0, addrString.length() - temp),2);
        cacheLine[temp_ind].setData(tag, value);
    }

    public boolean isInCache() {
        return this.isHit;
    }

    public int[] getIndexes() {
        return this.index;
    }

    public CacheLine[] getCaches() {
        return this.cacheLine;
    }

    public Processor getProcessor() {
        return this.containingProcessor;
    }

    public void setProcessor(Processor processor) {
        this.containingProcessor = processor;
    }

    public String toString() {
        return Integer.toString(this.latency) + " : latency";
    }

    public void handleCacheMiss(int address) {
		Simulator.getEventQueue().addEvent(
            new MemoryReadEvent(
                    Clock.getCurrentTime() + Configuration.mainMemoryLatency,
                    this,
                    containingProcessor.getMainMemory(),
                    address
            )
        );           
	}

    @Override
	public void handleEvent(Event e){ // handle moment
        if(e.getEventType() == Event.EventType.MemoryRead) {
            System.out.println("Handling Cache Memory Read!");
            MemoryReadEvent readE = (MemoryReadEvent) e;
            int data = cacheRead(readE.getAddressToReadFrom());
            if(isHit == true){
                Simulator.getEventQueue().addEvent(
                    new MemoryResponseEvent(
                        Clock.getCurrentTime() + this.latency, 
                        this, 
                        readE.getRequestingElement(), 
                        data)
                );
            } else {
                System.out.println("Missed!");
                this.msAddr = readE.getAddressToReadFrom();
                readE.setEventTime(Clock.getCurrentTime() + Configuration.mainMemoryLatency + 1);
                Simulator.getEventQueue().addEvent(readE);
                handleCacheMiss(readE.getAddressToReadFrom());
            }
        } else if(e.getEventType() == Event.EventType.MemoryWrite) {
            System.out.println("Handling Cache Memory Write");
            MemoryWriteEvent writeE = (MemoryWriteEvent) e;
            cacheWrite(writeE.getAddressToWriteTo(), writeE.getValue());
            containingProcessor.getMainMemory().setWord(writeE.getAddressToWriteTo(), writeE.getValue());

            Simulator.getEventQueue().addEvent(
				new ExecutionCompleteEvent(
					Clock.getCurrentTime() + Configuration.mainMemoryLatency, 
					containingProcessor.getMainMemory(), 
					writeE.getRequestingElement()
                )
			); 
		} else if(e.getEventType() == Event.EventType.MemoryResponse){
            MemoryResponseEvent responseE = (MemoryResponseEvent) e;
            cacheWrite(this.msAddr, responseE.getValue());
        }
	}
}