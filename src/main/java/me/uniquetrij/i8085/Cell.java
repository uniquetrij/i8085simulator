package me.uniquetrij.i8085;

public final class Cell extends Register
{
    public Cell()
    {
        update();
    }
    void update()
    {
        if(super.data!=null)
            data=super.data;

        else data=new Data();
    }
}
