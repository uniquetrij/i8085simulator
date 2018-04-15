/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.uniquetrij.i8085.giu;

import me.uniquetrij.i8085.Data;
import me.uniquetrij.i8085.DataEx;
import me.uniquetrij.i8085.Flags;
import me.uniquetrij.i8085.RegisterB;
import me.uniquetrij.i8085.RegisterC;
import me.uniquetrij.i8085.RegisterD;
import me.uniquetrij.i8085.RegisterL;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.Serializable;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.MatteBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

@SuppressWarnings("serial")
public final class FlagsRegisterTable extends JScrollPane implements Editable,Serializable
{
    //<editor-fold defaultstate="collapsed" desc="instance members">

    private JTable regtable;
    private String[] regheader;
    private Object[][] regdata;
    private Flags flags;
    private int index;
    private String title;
    private boolean working;
    private boolean editable;
    private boolean viewable;

    public FlagsRegisterTable()
    {
        this(13);
    }

    public FlagsRegisterTable(int fontsize)
    {
        generateTableParameters();

        DefaultTableCellRenderer cellrenderer=new DefaultTableCellRenderer();
        cellrenderer.setHorizontalAlignment(JLabel.CENTER);

        regtable=new JTable(regdata,regheader)
        {

            @Override
            public boolean isCellEditable(int rowIndex,int colIndex)
            {
                return false;
            }
            /*
             * @Override
             * public String getToolTipText(MouseEvent evt)
             * {
             * Point p=evt.getPoint();
             * DataEx address=new
             * DataEx(convertRowIndexToModel(rowAtPoint(p))*0x10+convertColumnIndexToModel(columnAtPoint(p)));
             * return address.toHexString();
             * }
             *
             */

            @Override
            public Component prepareRenderer(TableCellRenderer renderer,int rowIndex,int colIndex)
            {
                Component c=super.prepareRenderer(renderer,rowIndex,colIndex);
                if(c instanceof JComponent)
                {
                    String tooltip="";
                    switch(colIndex)
                    {
                        case 0:
                            tooltip="Complete Flags Register Byte";
                            break;
                        case 1:
                            tooltip="Sign Flag";
                            break;
                        case 2:
                            tooltip="Zero Flag";
                            break;
                        case 3:
                            tooltip="Undefined";
                            break;
                        case 4:
                            tooltip="Auxiliary Carry Flag";
                            break;
                        case 5:
                            tooltip="Undefined";
                            break;
                        case 6:
                            tooltip="Parity Flag";
                            break;
                        case 7:
                            tooltip="Undefined";
                            break;
                        case 8:
                            tooltip="Carry Flag";
                            break;
                    }
                    JComponent jc=(JComponent)c;
                    jc.setToolTipText(tooltip);
                    if(!isCellSelected(rowIndex,colIndex))
                        if(colIndex%2!=0)
                            jc.setBackground(new Color(250,250,250));
                        else jc.setBackground(Color.WHITE);
                }
                return c;
            }
        };
        regtable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        regtable.setFont(new Font(Font.MONOSPACED,0,fontsize));
        regtable.getTableHeader().setResizingAllowed(false);
        regtable.getTableHeader().setFont(new Font(Font.MONOSPACED,Font.BOLD,fontsize));
        regtable.getTableHeader().setBorder(BorderFactory.createRaisedBevelBorder());
        regtable.getTableHeader().setToolTipText("Flags Registers");
        for(int i=0;i<regheader.length;i++)
        {
            TableColumn column=regtable.getColumnModel().getColumn(i);
            if(i==0)
                column.setPreferredWidth(3*fontsize+15);
            else
                column.setPreferredWidth(fontsize+15);
            column.setCellRenderer(cellrenderer);
        }
        cellrenderer=(DefaultTableCellRenderer)regtable.getTableHeader().getDefaultRenderer();
        cellrenderer.setHorizontalAlignment(JLabel.CENTER);
        regtable.getTableHeader().setDefaultRenderer(cellrenderer);
        regtable.getTableHeader().setEnabled(false);
        regtable.addMouseListener(new ClickListener()
        {

            @Override
            public void singleClick(MouseEvent e)
            {
                if(viewable)
                {
                    working=true;
                    displayCellDialog();
                }
            }

            @Override
            public void doubleClick(MouseEvent e)
            {

            }

            @Override
            public void rightClick(MouseEvent evt)
            {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        });
        regtable.setCursor(new Cursor(Cursor.HAND_CURSOR));
        regtable.setColumnSelectionAllowed(true);
        regtable.setRowSelectionAllowed(true);
        regtable.setRowHeight(fontsize+12);
        regtable.setBorder(new MatteBorder(0,1,1,1,Color.BLACK));

        setViewportView(regtable);
        getViewport().addChangeListener(new ChangeListener()
        {

            @Override
            public void stateChanged(ChangeEvent e)
            {
                MouseEvent me=new MouseEvent(regtable,0,0,0,-1,-1,0,false);
                for(MouseMotionListener ml:regtable.getMouseMotionListeners())
                    ml.mouseMoved(me);
            }
        });

        regtable.addFocusListener(new FocusListener(){

            @Override
            public void focusGained(FocusEvent e)
            {

            }

            @Override
            public void focusLost(FocusEvent e)
            {
                if(!working)
                regtable.clearSelection();
            }
        });

    }

    private void generateTableParameters()
    {
        working=false;
        editable=viewable=true;

        regheader=new String[]
        {
           "Flags","S","Z","X","AC","X","P","X","CY"
        };
        regdata=new String[1][9];

        flags=Flags.Flags;

        flags.addPropertyChangeListener("value",new PropertyChangeListener()
        {

            @Override
            public void propertyChange(PropertyChangeEvent evt)
            {
                //register=RegisterB.RegisterB;
                setCellData((Data)evt.getNewValue());
            }
        });
        regdata[0][0]=flags.data().toHexString();

        flags.addPropertyChangeListener("sign",new PropertyChangeListener()
        {

            @Override
            public void propertyChange(PropertyChangeEvent evt)
            {
                //register=RegisterB.RegisterB;
                setCellData(1,(int)evt.getNewValue());
            }
        });
        regdata[0][1]=flags.get(Flags.S)?"1":"0";

        flags.addPropertyChangeListener("zero",new PropertyChangeListener()
        {

            @Override
            public void propertyChange(PropertyChangeEvent evt)
            {
                //register=RegisterC.RegisterC;
                setCellData(2,(int)evt.getNewValue());
            }
        });
        regdata[0][2]=flags.get(Flags.Z)?"1":"0";

        regdata[0][3]="-";

        flags.addPropertyChangeListener("auxcarry",new PropertyChangeListener()
        {

            @Override
            public void propertyChange(PropertyChangeEvent evt)
            {
                //register=RegisterD.RegisterD;
                setCellData(4,(int)evt.getNewValue());
            }
        });
        regdata[0][4]=flags.get(Flags.AC)?"1":"0";

        regdata[0][5]="-";

        flags.addPropertyChangeListener("parity",new PropertyChangeListener()
        {

            @Override
            public void propertyChange(PropertyChangeEvent evt)
            {
                //register=RegisterE.RegisterE;
                setCellData(6,(int)evt.getNewValue());
            }
        });
        regdata[0][6]=flags.get(Flags.P)?"1":"0";

        regdata[0][7]="-";

        flags.addPropertyChangeListener("carry",new PropertyChangeListener()
        {

            @Override
            public void propertyChange(PropertyChangeEvent evt)
            {
                //register=RegisterH.RegisterH;
                setCellData(8,(int)evt.getNewValue());
            }
        });
        regdata[0][8]=flags.get(Flags.CY)?"1":"0";

    }

    private void displayCellDialog()
    {
        switch(regtable.getSelectedColumn())
        {
            case 0:
                new ValueDisplayer("Register: Flags",flags.data().value(),false,this);
                working=false;
                return;
            case 1:
                index=Flags.S;
                title="Flag: Sign";
                break;
            case 2:
                index=Flags.Z;
                title="Flag: Zero";
                break;
            case 3:
                return;
            case 4:
                index=Flags.AC;
                title="Flag: Auxiliary Carry";
                break;
            case 5:
                return;
            case 6:
                index=Flags.P;
                title="Flag: Parity";
                break;
            case 7:
                return;
            case 8:
                index=Flags.CY;
                title="Flag: Carry";
                break;
        }
        new ValueDisplayer(title,flags.get(index)?1:0,false,this);
        working=false;
    }

    @Override
    public void editValue()
    {

    }

    @Override
    public void setValue(Data data)
    {
        //setCellData(data);
        flags.set(data);
    }

    @Override
    public void setValue(DataEx data)
    {
    }

    private void setCellData(int index,int value)
    {
        regdata[0][index]=value==1?"1":"0";
        ((AbstractTableModel) regtable.getModel()).fireTableCellUpdated(0, index);
    }

    private void setCellData(Data data)
    {
        regdata[0][0]=data.toHexString();
        ((AbstractTableModel) regtable.getModel()).fireTableCellUpdated(0, 0);
    }

    public void setEditable(boolean editable)
    {
        this.editable=editable;
    }

    public void setViewable(boolean viewable)
    {
        this.viewable=viewable;
    }

    public static void main(String args[])
    {
        try
        {
            for(javax.swing.UIManager.LookAndFeelInfo info:javax.swing.UIManager.getInstalledLookAndFeels())
                if("Nimbus".equals(info.getName()))
                {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
        }
        catch(ClassNotFoundException|InstantiationException|IllegalAccessException|javax.swing.UnsupportedLookAndFeelException ex)
        {
            java.util.logging.Logger.getLogger(FlagsRegisterTable.class.getName()).log(java.util.logging.Level.SEVERE,null,ex);
        }
        java.awt.EventQueue.invokeLater(new Runnable()
        {

            @Override
            public void run()
            {
                RegisterL.RegisterL.set(new Data(50));
                FlagsRegisterTable mt=new FlagsRegisterTable(13);
                RegisterB.RegisterB.set(new Data(10));
                RegisterC.RegisterC.set(new Data(20));
                RegisterD.RegisterD.set(new Data(30));

                JFrame frame=new JFrame();
                frame.add(mt);
                frame.setSize(mt.getSize());
                frame.setVisible(true);
            }
        });

    }
}