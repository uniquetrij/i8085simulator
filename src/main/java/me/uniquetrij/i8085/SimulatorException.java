package me.uniquetrij.i8085;



public final class SimulatorException extends RuntimeException
{
    private boolean stopped;
    private boolean invalid;
    public SimulatorException(){}
    public SimulatorException(String message){super(message);}
    public SimulatorException(String message,Throwable cause){super(message,cause);}
    public SimulatorException(Throwable cause){super(cause);}

    public SimulatorException(String operation,String symbol)
    {
        super("Invalid Simulation."
             +"\nOperation Not Allowed: "+operation
             +"\nFor Symbol: "+symbol
             +"\nInstruction Syntax: "+Operation.syntax(operation.toString()),
             new Throwable("OPERATION NOT ALLOWED"));
    }

    public SimulatorException(DataEx brkpt)
    {
        super("Execution Halted Internally."
             +"\nAt Brake Point: "+brkpt
             +"\nRun to continue...",
             new Throwable("BRAKE POINT"));
        stopped=true;
    }

    public SimulatorException(int opcode,DataEx address)
    {
        super("Error in OPCODE: "+Integer.toHexString(opcode)
             +"At memory address: "+address
             +"\nNo such OPCODE exists.",
             new Throwable("INVALID OPCODE"));
        invalid=true;
    }

    public boolean hasStoppedAtBrakePoint()
    {
        return stopped;
    }

    public boolean isInvalidOpcode()
    {
        return invalid;
    }

}