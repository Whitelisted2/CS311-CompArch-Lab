package generic;

public class Operand {

    OperandType operandType;

    ;
    int value;
    String labelValue;    //only applicable for Label type;

    public OperandType getOperandType() {
        return operandType;
    }
    //Note that Label type is only applicable for functional emulation of assembly file

    public void setOperandType(OperandType operandType) {
        this.operandType = operandType;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getLabelValue() {
        return labelValue;
    }

    public void setLabelValue(String labelValue) {
        this.labelValue = labelValue;
    }

    public String toString() {
        if (operandType == OperandType.Register || operandType == OperandType.Immediate) {
            return "[" + operandType.toString() + ":" + value + "]";
        } else {
            return "[" + operandType.toString() + ":" + labelValue + "]";
        }
    }

    public enum OperandType {Register, Immediate, Label}
}
