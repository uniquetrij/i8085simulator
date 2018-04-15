package me.uniquetrij.i8085;

public final class RegisterB extends Register
{
    //static Data data=new Data();
    public static final RegisterB RegisterB=new RegisterB();
    private RegisterB(){data=new Data();}

    void update()
    {
        if(super.data!=null)
        {
            data=super.data;
            fire();
        }

        BCpair.BCpair.propagate(new DataEx(data,RegisterC.RegisterC.data(),true));
    }

    void fire()
    {
        propertyChangeSupport.firePropertyChange("value", 1,data);
    }

    public Data data()
    {
        return data;
    }
}
