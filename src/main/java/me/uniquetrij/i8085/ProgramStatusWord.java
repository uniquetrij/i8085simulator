package me.uniquetrij.i8085;

public final class ProgramStatusWord extends SpecialRegister
{
    //static DataEx data=new DataEx(RegisterA.data,Flags.data);
    public static final ProgramStatusWord ProgramStatusWord=new ProgramStatusWord();
    private ProgramStatusWord(){data=new DataEx();}

    void update()
    {
        if(super.data!=null)
        {
            data=super.data;
            fire();
        }

        RegisterA.RegisterA.propagate(data.hi());
        Flags.Flags.set(data.lo());

    }

    void fire()
    {
        propertyChangeSupport.firePropertyChange("value", 7,data);
    }

    public DataEx data()
    {
        return data;
    }
}
