 package me.uniquetrij.i8085;



public final class ProgramCounter extends SpecialRegister
{
    //static DataEx data=new DataEx();
    public static final ProgramCounter ProgramCounter=new ProgramCounter();
    private ProgramCounter(){data=new DataEx();}

    void update()
    {
        if(super.data!=null)
            propertyChangeSupport.firePropertyChange("value", 12,data=super.data);
            //data=super.data;
    }

    public DataEx data()
    {
        return data;
    }

    public Data obtain()
    {
        Data d=Memory.Memory.data(data);
        set(data.plus(1));
        return d;
    }
}
