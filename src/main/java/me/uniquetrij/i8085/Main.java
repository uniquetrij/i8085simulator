package me.uniquetrij.i8085;





import java.io.*;
public class Main
{
    public static void open(String path)throws IOException
    {
        BufferedReader br=new BufferedReader(new FileReader(path));
        String text,file="";
        int i=0;
        while((text=br.readLine())!=null)
            file+=text+"\n";
        br.close();
        try
        {
            new Assambler();
            Assambler.assamble(file);
        }
        catch(AssamblerException e)
        {
            System.err.println(e);
        }
    }

    public static void load()
    {
        try
        {
            new Simulator();
            Simulator.simulate(Assambler.getCodeList());
        }
        catch(AssamblerException e)
        {
            System.err.println(e);
        }
    }

    public static void run()
    {
        try
        {
            Simulator.run();
        }
        catch(SimulatorException e)
        {
            System.err.println(e);
        }
    }


    public static void showMemory()
    {
        Memory.Memory.print();
    }


    public static void showListing()
    {
        System.out.println(Assambler.getList());
    }

}
