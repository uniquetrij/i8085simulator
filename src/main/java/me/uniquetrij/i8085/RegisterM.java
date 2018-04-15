package me.uniquetrij.i8085;

public final class RegisterM extends Register
{
    public static final RegisterM RegisterM=new RegisterM();
    private RegisterM(){}

    public Data data()
    {
        return Memory.Memory.data(HLpair.HLpair.data());
    }

    public void set(Data data)
    {
        Memory.Memory.set(HLpair.HLpair.data(),data);
    }

}
