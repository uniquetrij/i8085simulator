package me.uniquetrij.i8085;



public final class RegisterA extends Register
{
    public static final RegisterA RegisterA=new RegisterA();
    private RegisterA(){data=new Data();}

    void update()
    {
        if(super.data!=null)
        {
            data=super.data;
            fire();
        }

        ProgramStatusWord.ProgramStatusWord.propagate(new DataEx(data,Flags.Flags.data(),true));
    }

    void fire()
    {
        propertyChangeSupport.firePropertyChange("value", 0,data);
    }

    public Data data()
    {
        return data;
    }
}


