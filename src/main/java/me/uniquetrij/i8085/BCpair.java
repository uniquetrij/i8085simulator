package me.uniquetrij.i8085;

public final class BCpair extends SpecialRegister
{
    //static DataEx data=new DataEx(RegisterB.data,RegisterC.data);
    //static DataEx data=new DataEx();
    public static final BCpair BCpair=new BCpair();
    private BCpair(){data=new DataEx();}

    void update()
    {
        if(super.data!=null)
        {
            data=super.data;
            fire();
        }

        RegisterB.RegisterB.propagate(data.hi());
        RegisterC.RegisterC.propagate(data.lo());
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
