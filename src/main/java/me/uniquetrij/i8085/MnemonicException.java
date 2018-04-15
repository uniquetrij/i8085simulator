package me.uniquetrij.i8085;





public final class MnemonicException extends RuntimeException
{
    private int pos;
    public MnemonicException(){}
    public MnemonicException(String message){super(message);}
    public MnemonicException(Throwable cause){super(cause);}
    public MnemonicException(String message,Throwable cause){super(message,cause);}

    private static String pointIndex(int index)
    {
        String x="";
        while(index-->0)x+=" ";
        x+='^';
        return x;
    }
    /**All exceptions thrown from class InstructionTokenozer**/
    public MnemonicException(Operation operation,int index,String instruction)
    {
        super("Invalid Mnemonic."
            +"\nUnknown Operation: "+operation
            +"\nIn Instruction: "+instruction
            +"\nAt Index Point: "+MnemonicException.pointIndex(index),
            new Throwable("UNKNOWN OPERATION"));
        pos=index;}

    public MnemonicException(Operation operation,String instruction,char c,int index)
    {
        super("Invalid Mnemonic."
            +"\nSyntax Error: "+operation
            +"\nIn Instruction: "+instruction
            +"\nAt Index Point: "+MnemonicException.pointIndex(index)
            +"\nIllegal Character: "+c
            +"\nInstruction Syntax: "+Operation.syntax(operation.toString()),
            new Throwable("ILLEGAL CHARACTER"));
        pos=index;
    }

    public MnemonicException(Operation operation,String instruction,int index)
    {
        super("Invalid Mnemonic."
            +"\nSyntax Error: "+operation
            +"\nOperation can not be used as label: "+operation
            +"\nIn Instruction: "+instruction
            +"\nAt Index Point: "+MnemonicException.pointIndex(index)
            +"\nInstruction Syntax: "+Operation.syntax(operation.toString()),
            new Throwable("OPERATION OVERLOAD"));
        pos=index;
    }

    public MnemonicException(int index,Operation operation,String instruction)
    {
        super("Invalid Mnemonic."
            +"\nSyntax Error: "+operation
            +"\nIn Instruction: "+instruction
            +"\nAt Index Point: "+MnemonicException.pointIndex(index)
            +"\nExpected: Parameter(s)"
            +"\nInstruction Syntax: "+Operation.syntax(operation.toString()),
            new Throwable("EXPECTED PARAMETER"));
        pos=index;}

    public MnemonicException(int index,String instruction,Operation operation)
    {
        super("Invalid Mnemonic."
            +"\nSyntax Error: "+operation
            +"\nIn Instruction: "+instruction
            +"\nAt Index Point: "+MnemonicException.pointIndex(index)
            +"\nExpected: comma(,)"
            +"\nInstruction Syntax: "+Operation.syntax(operation.toString()),
            new Throwable("EXPECTED COMMA"));
        pos=index;
    }

    public MnemonicException(String instruction,Operation operation,int index)
    {
        super("Invalid Mnemonic."
            +"\nSyntax Error: "+operation
            +"\nIn Instruction: "+instruction
            +"\nAt Index Point: "+MnemonicException.pointIndex(index)
            +"\nEQU and SET must be preceeded by a label without a colon"
            +"\nInstruction Syntax: "+Operation.syntax(operation.toString()),
            new Throwable("EQU SET INSTRUCTION"));
        pos=index;
    }

    public MnemonicException(int index,String instruction)
    {
        super("Invalid Mnemonic."
            +"\nSyntax Error: ORG"
            +"\nIn Instruction: "+instruction
            +"\nAt Index Point: "+MnemonicException.pointIndex(index)
            +"\nORG must not be preceeded by a label"
            +"\nInstruction Syntax: "+Operation.syntax("ORG"),
            new Throwable("ORG INSTRUCTION"));
        pos=index;
    }

    public MnemonicException(String instruction,int index,Operation operation)
    {
        super("Invalid Mnemonic."
            +"\nSyntax Error: "+operation
            +"\nIn Instruction: "+instruction
            +"\nAt Index Point: "+MnemonicException.pointIndex(index)
            +"\nContains: Extra Token(s)"
            +"\nInstruction Syntax: "+Operation.syntax(operation.toString()),
            new Throwable("EXTRA TOKENS"));
        pos=index;
    }

    public MnemonicException(String instruction,String label,int index)
    {
        super("Invalid Mnemonic."
            +"\nImproper Label: "+label
            +"\nSyntax Error: Label cannot be a hex number and cannot begin with a digit"
            +"\nIn Instruction: "+instruction
            +"\nAt Index Point: "+MnemonicException.pointIndex(index)
            +"\nExpected: Proper Label Name",
            new Throwable("IMPROPER LABEL"));
        pos=index;
    }

    public MnemonicException(String instruction,int index,String label)
    {
        super("Invalid Mnemonic."
            +"\nImproper Label: "+label
            +"\nSyntax Error: Register Name cannot be used as Label"
            +"\nIn Instruction: "+instruction
            +"\nAt Index Point: "+MnemonicException.pointIndex(index)
            +"\nExpected: Proper Label Name",
            new Throwable("IMPROPER LABEL"));
        pos=index;
    }

    public MnemonicException(String instruction,int index)
    {
        super("Invalid Mnemonic."
            +"\nSyntax Error: Label Undefined"
            +"\nIn Instruction: "+instruction
            +"\nAt Index Point: "+MnemonicException.pointIndex(index)
            +"\nExpected: Label Name",
            new Throwable("UNDEFINED LABEL"));
        pos=index;
    }

    public MnemonicException(String instruction,String label)
    {
        super("Invalid Mnemonic."
            +"\nLabel Already Defined: "+label
            +"\nIn Instruction: "+instruction
            +"\nAt Index Point: "+MnemonicException.pointIndex(0)
            +"\nUse Different Label Name",
            new Throwable("LABEL OVERLOAD"));
        pos=0;
    }

    /**All exceptions thrown from class Mnemonic**/
    public MnemonicException(Operation operation,String instruction,String symbol,int index)
    {
        super("Invalid Mnemonic."
            +"\nUndefined Symbol: "+symbol
            +"\nSyntax Error: "+operation
            +"\nIn Instruction: "+instruction
            +"\nAt Index Point: "+MnemonicException.pointIndex(index)
            +"\nInstruction Syntax: "+Operation.syntax(operation.toString()),
            new Throwable("UNDEFINED SYMBOL"));
        pos=index;
    }

    public MnemonicException(Operation operation,String instruction,int index,String symbol)
    {
        super("Invalid Mnemonic."
            +"\nOperation Not Allowed: "+operation
            +"\nFor Symbol: "+symbol
            +"\nIn Instruction: "+instruction
            +"\nAt Index Point: "+MnemonicException.pointIndex(index)
            +"\nInstruction Syntax: "+Operation.syntax(operation.toString()),
            new Throwable("OPERATION NOT ALLOWED"));
        pos=index;
    }

    public MnemonicException(Operation operation,String instruction,String symbol,int index,Exception e)
    {
        super("Invalid Mnemonic."
            +"\nUndefined Symbol: "+symbol
            +"\nSyntax Error: "+operation
            +"\nIn Instruction: "+instruction
            +"\nAt Index Point: "+MnemonicException.pointIndex(index)
            +"\n"+e
            +"\nInstruction Syntax: "+Operation.syntax(operation.toString()),
            new Throwable(e.getCause()));
        pos=index;
    }

    public MnemonicException(int index,String instruction,String label)
    {
        super("Invalid Mnemonic."
            +"\nUndefined Label: "+label
            +"\nIn Instruction: "+instruction
            +"\nAt Index Point: "+MnemonicException.pointIndex(index)
            +"\nExpected: Label Name",
            new Throwable("UNDEFINED LABEL"));
        pos=index;
    }

    public int getErrorIndex()
    {
        return pos;
    }
}

