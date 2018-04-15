package me.uniquetrij.i8085;

public final class Flags extends Register
{
    public static final int CY=0;
    public static final int P=2;
    public static final int AC=4;
    public static final int Z=6;
    public static final int S=7;

    public static final Flags Flags=new Flags();
    private Flags(){data=new Data();}

    //static Data data=new Data();

    void update()
    {
        if(super.data!=null)
        {
            data=super.data;
            fire();
        }

        ProgramStatusWord.ProgramStatusWord.propagate(new DataEx(RegisterA.RegisterA.data(),data,true));
    }

    public void set(int index)
    {
        super.set(new Data(data.value()|(int)Math.pow(2,index)));
        String property="";
        switch(index)
        {
            case S : property="sign";break;
            case Z : property="zero";break;
            case AC: property="auxcarry";break;
            case P : property="parity";break;
            case CY: property="carry";break;
            default: return;
        }
        propertyChangeSupport.firePropertyChange(property, 0,1);
        fire();
    }

    public boolean get(int index)
    {
        return (data.value()&(int)Math.pow(2,index))>0;
    }

    public void reset(int index)
    {
        super.set(new Data(data.value()&~(int)Math.pow(2,index)));
        String property="";
        switch(index)
        {
            case S : property="sign";break;
            case Z : property="zero";break;
            case AC: property="auxcarry";break;
            case P : property="parity";break;
            case CY: property="carry";break;
            default: return;
        }
        propertyChangeSupport.firePropertyChange(property, 1,0);
        fire();
    }

    @Override
    public void reset()
    {
        super.reset();
        int[]index={CY,P,AC,Z,S};
        for(int i:index)
            reset(i);
    }

    void fire()
    {
        propertyChangeSupport.firePropertyChange("value", -1,data);
    }

    public Data data()
    {
        return data;
    }

    public String toString()
    {
        String s="";
        s+=" S: "+(get(S)?1:0)+"\n";
        s+=" Z: "+(get(Z)?1:0)+"\n";
        s+=" X: "+"-\n";
        s+="AC: "+(get(AC)?1:0)+"\n";
        s+=" X: "+"-\n";
        s+=" P: "+(get(P)?1:0)+"\n";
        s+=" X: "+"-\n";
        s+="CY: "+(get(CY)?1:0)+"\n";
        return s;
    }

}