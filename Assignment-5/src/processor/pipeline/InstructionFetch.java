package processor.pipeline;

import configuration.Configuration;
import generic.*;
import processor.Clock;
import processor.Processor;

public class InstructionFetch implements Element {

    Processor containingProcessor;
    IF_EnableLatchType IF_EnableLatch;
    IF_OF_LatchType IF_OF_Latch;
    EX_IF_LatchType EX_IF_Latch;

    public InstructionFetch(Processor containingProcessor,
                            IF_EnableLatchType iF_EnableLatch,
                            IF_OF_LatchType iF_OF_Latch,
                            EX_IF_LatchType eX_IF_Latch) {
        this.containingProcessor = containingProcessor;
        this.IF_EnableLatch = iF_EnableLatch;
        this.IF_OF_Latch = iF_OF_Latch;
        this.EX_IF_Latch = eX_IF_Latch;
    }

    public void performIF() {
        //System.out.println("YessI");
        if (IF_EnableLatch.isIF_enable()) {
            if (IF_EnableLatch.isIF_busy()) {
                IF_OF_Latch.setOF_enable(false);
                return;
            }
            Simulator.getEventQueue().addEvent(new MemoryReadEvent(Clock.getCurrentTime() + Configuration.mainMemoryLatency, this, containingProcessor.getMainMemory(), containingProcessor.getRegisterFile().getProgramCounter()));
            IF_EnableLatch.setIF_busy(true);
            IF_OF_Latch.setOF_enable(false);
        }

    }

    @Override
    public void handleEvent(Event e) {
        if (IF_OF_Latch.isOF_busy()) {
            e.setEventTime(Clock.getCurrentTime() + 1);
            Simulator.getEventQueue().addEvent(e);
        } else {
            MemoryResponseEvent event = (MemoryResponseEvent) e;
            int currentPC = containingProcessor.getRegisterFile().getProgramCounter();
            int newInstruction = event.getValue();
            Simulator.ins_count++;
            IF_OF_Latch.setInstruction(newInstruction);
            containingProcessor.getRegisterFile().setProgramCounter(currentPC + 1);
            IF_OF_Latch.setOF_enable(true);
            IF_EnableLatch.setIF_busy(false);
        }
    }
}
