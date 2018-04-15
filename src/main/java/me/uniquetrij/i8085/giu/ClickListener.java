package me.uniquetrij.i8085.giu;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

public abstract class ClickListener extends MouseAdapter implements ActionListener
{
    private final static int clickInterval = (Integer)Toolkit.getDefaultToolkit().getDesktopProperty("awt.multiClickInterval");

    MouseEvent lastEvent;
    Timer timer;

    public ClickListener()
    {
        this(clickInterval);
    }

    public ClickListener(int delay)
    {
        timer = new Timer( delay, this);
    }

    @Override
    public void mouseClicked (MouseEvent evt)
    {
        if(evt.getButton()==3)
        {
            rightClick(evt);
            timer.stop();
            return;
        }

        if (evt.getClickCount() > 2) return;
        lastEvent = evt;

        if (timer.isRunning())
        {
            timer.stop();
            doubleClick( lastEvent );
        }
        else
        {
            timer.restart();
        }
    }

    @Override
    public void actionPerformed(ActionEvent evt)
    {
        timer.stop();
        singleClick( lastEvent );
    }

    public abstract void singleClick(MouseEvent evt);
    public abstract void doubleClick(MouseEvent evt);
    public abstract void rightClick(MouseEvent evt);

    public static void main(String[] args)
    {
        JFrame frame = new JFrame( "Double Click Test" );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        frame.addMouseListener( new ClickListener()
        {
            public void singleClick(MouseEvent e)
            {
//                System.out.println("single");
            }

            public void doubleClick(MouseEvent e)
            {
//                System.out.println("double");
            }

            @Override
            public void rightClick(MouseEvent evt)
            {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        });
        frame.setSize(200, 200);
        frame.setVisible(true);
     }
}
