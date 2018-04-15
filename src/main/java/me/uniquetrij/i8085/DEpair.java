package me.uniquetrij.i8085;

public final class DEpair extends SpecialRegister
{
    //static DataEx data=new DataEx(RegisterD.data,RegisterE.data);
    public static final DEpair DEpair=new DEpair();
    private DEpair(){data=new DataEx();}

    void update()
    {
        if(super.data!=null)
        {
            data=super.data;
            fire();
        }

        RegisterD.RegisterD.propagate(data.hi());
        RegisterE.RegisterE.propagate(data.lo());
    }

    void fire()
    {
        propertyChangeSupport.firePropertyChange("value", 8,data);
    }

    public DataEx data()
    {
        return data;
    }
}
