package processor;

import processor.memorysystem.*;
import processor.pipeline.*;

public class Processor {
    int stalls;
    int wrongInput;
    int freezed_stalls;
    int freezed_wrongInput;

    RegisterFile registerFile;
    MainMemory mainMemory;

    IF_EnableLatchType IF_EnableLatch;
    IF_OF_LatchType IF_OF_Latch;
    OF_EX_LatchType OF_EX_Latch;
    EX_MA_LatchType EX_MA_Latch;
    EX_IF_LatchType EX_IF_Latch;
    MA_RW_LatchType MA_RW_Latch;
    Cache instCache;
    Cache dataCache; 

    InstructionFetch IFUnit;
    OperandFetch OFUnit;
    Execute EXUnit;
    MemoryAccess MAUnit;
    RegisterWrite RWUnit;

    public Processor() {

        stalls = 0;
        wrongInput = 0;
        registerFile = new RegisterFile();
        mainMemory = new MainMemory();

        IF_EnableLatch = new IF_EnableLatchType();
        IF_OF_Latch = new IF_OF_LatchType();
        OF_EX_Latch = new OF_EX_LatchType();
        EX_MA_Latch = new EX_MA_LatchType();
        EX_IF_Latch = new EX_IF_LatchType();
        MA_RW_Latch = new MA_RW_LatchType();

        // size     16,  128,  512, 1024
        // latency   1,    2,    3,    4

        int instCacheSize = 16;
        int dataCacheSize = 16;
        
        int instCacheLatency = getLat(instCacheSize);
        int dataCacheLatency = getLat(dataCacheSize);
        
        instCache = new Cache(this, instCacheLatency, instCacheSize);
        dataCache = new Cache(this, dataCacheLatency, dataCacheSize);
        IFUnit = new InstructionFetch(this, IF_EnableLatch, IF_OF_Latch, EX_IF_Latch, instCache);
        OFUnit = new OperandFetch(this, IF_OF_Latch, OF_EX_Latch, EX_MA_Latch, MA_RW_Latch, IF_EnableLatch);
        EXUnit = new Execute(this, OF_EX_Latch, EX_MA_Latch, EX_IF_Latch, IF_OF_Latch, MA_RW_Latch);
        MAUnit = new MemoryAccess(this, EX_MA_Latch, MA_RW_Latch, IF_OF_Latch, OF_EX_Latch, dataCache);
        RWUnit = new RegisterWrite(this, MA_RW_Latch, IF_EnableLatch, IF_OF_Latch, OF_EX_Latch, EX_MA_Latch);
    }

    public int getLat(int cacheSize){
        int latency;
        switch(cacheSize){
            case 16:
                latency = 1;
                break;
            case 128:
                latency = 2;
                break;
            case 512:
                latency = 3;
                break;
            case 1024:
                latency = 4;
                break;
            default:
                System.out.println("cache size to latency mapping not defined to this!");
                latency = 1;
        }
        return latency;
    }

    public void printState(int memoryStartingAddress, int memoryEndingAddress) {
        System.out.println(registerFile.getContentsAsString());

        System.out.println(mainMemory.getContentsAsString(memoryStartingAddress, memoryEndingAddress));
    }

    public int getFreezed_stalls() {
        return freezed_stalls;
    }

    public void setFreezed_stalls(int freezed_stalls) {
        this.freezed_stalls = freezed_stalls;
    }

    public int getFreezed_wrong_input() {
        return freezed_wrongInput;
    }

    public void setFreezed_wrong_input(int Freezed_wrongInput) {
        this.freezed_wrongInput = Freezed_wrongInput;
    }

    public RegisterFile getRegisterFile() {
        return registerFile;
    }

    public void setRegisterFile(RegisterFile registerFile) {
        this.registerFile = registerFile;
    }

    public MainMemory getMainMemory() {
        return mainMemory;
    }

    public void setMainMemory(MainMemory mainMemory) {
        this.mainMemory = mainMemory;
    }

    public int getStalls() {
        return stalls;
    }

    public void setStalls(int stalls) {
        this.stalls = stalls;
    }

    public int getWrong_input() {
        return wrongInput;
    }

    public void setWrong_input(int WrongInput) {
        this.wrongInput = WrongInput;
    }

    public InstructionFetch getIFUnit() {
        return IFUnit;
    }

    public OperandFetch getOFUnit() {
        return OFUnit;
    }

    public Execute getEXUnit() {
        return EXUnit;
    }

    public MemoryAccess getMAUnit() {
        return MAUnit;
    }

    public RegisterWrite getRWUnit() {
        return RWUnit;
    }

}
