package me.uniquetrij.i8085;


public final class AssemblerException extends RuntimeException
{
    private int ln;
    public AssemblerException(){}
    public AssemblerException(String message){super(message);}
    public AssemblerException(String message,Throwable cause){super(message,cause);}
    public AssemblerException(Throwable cause){super(cause);}

    public AssemblerException(int ln,Exception e)
    {
        super("In line "+ln+"\n"+e,new Throwable(e.getCause()));
        this.ln=ln;
    }

    public AssemblerException(int ln,String line)
    {
        super("In line "+ln
             +"\nInstruction: "+line
             +"\nOrigin exceed memory limits: dec 0 to 65535, hex 0000 to FFFF"
             +"\nTry redefining origin in this range",
             new Throwable("MEMORY OVERFLOW"));
        this.ln=ln;
    }

    public AssemblerException(int ln)
    {
        super("In line "+(ln+1)
             +"\nEND of assembly undefined"
             +"\nExpected: END"
             +"\nEND must be the last line of assembly."   ,
             new Throwable("END NOT SET"));
        this.ln=ln;
    }

    public AssemblerException(boolean isasmd)
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
