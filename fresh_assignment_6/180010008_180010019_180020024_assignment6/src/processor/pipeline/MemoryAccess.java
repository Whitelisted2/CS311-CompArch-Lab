package processor.pipeline;

import generic.*;
import generic.Instruction.OperationType;
import processor.Clock;
import processor.Processor;
import processor.memorysystem.Cache;
import generic.Event.EventType;

public class MemoryAccess implements Element{
    Processor containingProcessor;
    EX_MA_LatchType EX_MA_Latch;
    MA_RW_LatchType MA_RW_Latch;
    IF_OF_LatchType IF_OF_Latch;
    OF_EX_LatchType OF_EX_Latch;

    Cache cache;

    public MemoryAccess(Processor containingProcessor, EX_MA_LatchType eX_MA_Latch, MA_RW_LatchType mA_RW_Latch, IF_OF_LatchType iF_OF_Latch, OF_EX_LatchType oF_EX_Latch, Cache cache) {
        this.containingProcessor = containingProcessor;
        this.EX_MA_Latch = eX_MA_Latch;
        this.MA_RW_Latch = mA_RW_Latch;
        this.IF_OF_Latch = iF_OF_Latch;
        this.OF_EX_Latch = oF_EX_Latch;
        this.cache = cache;
    }

    public void performMA() {
        //System.out.println("YessMA");
        if(EX_MA_Latch.isMA_enable() && !EX_MA_Latch.isMA_busy()) {
            if(EX_MA_Latch.isNop) {
                MA_RW_Latch.isNop = true;
            }
            else {
                MA_RW_Latch.isNop = false;
                MA_RW_Latch.insPC = EX_MA_Latch.insPC;
                Instruction instruction = EX_MA_Latch.getInstruction();
                int ALU_output = EX_MA_Latch.getALU_result();
                MA_RW_Latch.setALU_Output(ALU_output);
                MA_RW_Latch.setInstruction(instruction);
                if (instruction != null) {

                    OperationType Operation_Type = instruction.getOperationType();
                    if (Operation_Type.toString().equals("store")) {
                        //System.out.println(instruction);
                        int SOP = containingProcessor.getRegisterFile().getValue(instruction.getSourceOperand1().getValue());
                        //containingProcessor.getMainMemory().setWord(ALU_output, res_st);
                        //System.out.println("Value to Store "+(SOP));
                        EX_MA_Latch.setMA_busy(true);
                        Simulator.storeresp = Clock.getCurrentTime();
                        Simulator.getEventQueue().addEvent(
                                new MemoryWriteEvent(
                                        Clock.getCurrentTime() + this.cache.latency,
                                        this,
                                        this.cache,
                                        ALU_output,
                                        SOP)
                        );
                        EX_MA_Latch.setMA_enable(false);
                        //return;
                    } else if (Operation_Type.toString().equals("load")) {
                        //System.out.println(instruction);
                        //System.out.println(MA_RW_Latch.getInstruction());
                        //int res_ld = containingProcessor.getMainMemory().getWord(ALU_output);
                        //MA_RW_Latch.setLoad_Output(res_ld);
                        //MA_RW_Latch.isLoad = true;
                        EX_MA_Latch.setMA_busy(true);
                        Simulator.getEventQueue().addEvent(
                                new MemoryReadEvent(
                                        Clock.getCurrentTime(),
                                        this,
                                        this.cache, ALU_output)
                        );
                        EX_MA_Latch.setMA_enable(false);
                        //return;
                    }
                    MA_RW_Latch.setInstruction(instruction);
                    //MA_RW_Latch.setRW_enable(true);
                    //EX_MA_Latch.setMA_enable(false);
                }
                else {
                    //System.out.println("bubble");
                }
            }
            EX_MA_Latch.setMA_enable(false);
            if(EX_MA_Latch.getInstruction().getOperationType().toString().equals("end")) {
                EX_MA_Latch.setMA_enable(false);
            }
            MA_RW_Latch.setRW_enable(true);
        }
    }

    @Override
    public void handleEvent(Event e) {
        if(e.getEventType() == EventType.MemoryResponse) {
            MemoryResponseEvent event = (MemoryResponseEvent) e ;
            MA_RW_Latch.setALU_Output(event.getValue());
            MA_RW_Latch.insPC = EX_MA_Latch.insPC;
            MA_RW_Latch.setRW_enable(true);
            EX_MA_Latch.setMA_busy(false);
        }
        else {
            EX_MA_Latch.setMA_busy(false);
        }

        // IF_OF_Latch.setOF_enable(true);
        // IF_EnableLatch.isBusy = false;
    }

}
