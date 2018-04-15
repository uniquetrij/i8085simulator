package me.uniquetrij.i8085;

public final class RegisterC extends Register
{
    //static Data data=new Data();
    public static final RegisterC RegisterC=new RegisterC();
    private RegisterC(){data=new Data();}

    void update()
    {
        if(super.data!=null)
        {
            data=super.data;
            fire();
        }

        BCpair.BCpair.propagate(new DataEx(RegisterB.RegisterB.data(),data,true));
    }

    void fire()
    {
        propertyChangeSupport.firePropertyChange("value", 2,data);
    }

    public Data data()
    {
        return data;
    }
}
