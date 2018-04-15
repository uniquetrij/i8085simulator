package me.uniquetrij.i8085;


public final class AssamblerException extends RuntimeException
{
    private int ln;
    public AssamblerException(){}
    public AssamblerException(String message){super(message);}
    public AssamblerException(String message,Throwable cause){super(message,cause);}
    public AssamblerException(Throwable cause){super(cause);}

    public AssamblerException(int ln,Exception e)
    {
        super("In line "+ln+"\n"+e,new Throwable(e.getCause()));
        this.ln=ln;
    }

    public AssamblerException(int ln,String line)
    {
        super("In line "+ln
             +"\nInstruction: "+line
             +"\nOrigin exceed memory limits: dec 0 to 65535, hex 0000 to FFFF"
             +"\nTry redefining origin in this range",
             new Throwable("MEMORY OVERFLOW"));
        this.ln=ln;
    }

    public AssamblerException(int ln)
    {
        super("In line "+(ln+1)
             +"\nEND of assembly undefined"
             +"\nExpected: END"
             +"\nEND must be the last line of assembly."   ,
             new Throwable("END NOT SET"));
        this.ln=ln;
    }

    public AssamblerException(boolean isasmd)
    {
        super("Sourcecode not assambled yet."
             +"\nAssamble sourcecode first",
             new Throwable("SOURCE CODE NULL"));
        ln=-1;
    }

    public int getLineNumber()
    {
        return ln;
    }
}
