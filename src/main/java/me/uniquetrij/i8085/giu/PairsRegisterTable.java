/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.uniquetrij.i8085.giu;

import me.uniquetrij.i8085.BCpair;
import me.uniquetrij.i8085.DEpair;
import me.uniquetrij.i8085.Data;
import me.uniquetrij.i8085.DataEx;
import me.uniquetrij.i8085.HLpair;
import me.uniquetrij.i8085.SpecialRegister;
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
public final class PairsRegisterTable extends JScrollPane implements Editable,Serializable
{
    //<editor-fold defaultstate="collapsed" desc="instance members">

    private JTable regtable;
    private String[] regheader;
    private Object[][] regdata;
    private SpecialRegister register;
    private String title;

    private boolean working;
    private boolean viewable;
    private boolean editable;

    public PairsRegisterTable()
    {
        this(13);
    }

    public PairsRegisterTable(int fontsize)
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
                            tooltip="BC Register Pair";
                            break;
                        case 1:
                            tooltip="DE Register Pair";
                            break;
                        case 2:
                            tooltip="HL Register Pair";
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
        regtable.getTableHeader().setToolTipText("Register Pairs");
        for(int i=0;i<regheader.length;i++)
        {
            TableColumn column=regtable.getColumnModel().getColumn(i);
            column.setPreferredWidth(2*(fontsize+15));
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
                if(editable)
                {
                    working=true;
                    editCellDialog(false);
                }
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
            "BC","DE","HL"
        };
        regdata=new String[1][3];

        register=BCpair.BCpair;
        register.addPropertyChangeListener("value",new PropertyChangeListener()
        {

            @Override
            public void propertyChange(PropertyChangeEvent evt)
            {
                //register=BCpair.BCpair;
                setCellData(0,(DataEx)evt.getNewValue());
            }
        });
        regdata[0][0]=register.data().toHexString();


        register=DEpair.DEpair;
        register.addPropertyChangeListener("value",new PropertyChangeListener()
        {

            @Override
            public void propertyChange(PropertyChangeEvent evt)
            {
                //register=DEpair.DEpair;
                setCellData(1,(DataEx)evt.getNewValue());
            }
        });
        regdata[0][1]=register.data().toHexString();

        register=HLpair.HLpair;
        register.addPropertyChangeListener("value",new PropertyChangeListener()
        {

            @Override
            public void propertyChange(PropertyChangeEvent evt)
            {
                //register=HLpair.HLpair;
                setCellData(2,(DataEx)evt.getNewValue());
            }
        });
        regdata[0][2]=register.data().toHexString();
    }

    private void displayCellDialog()
    {
        setRegister();
        new ValueDisplayer(title,register.data().value(),true,this);
        working=false;
    }

    void setRegister()
    {
        switch(regtable.getSelectedColumn())
        {
            case 0:
                register=BCpair.BCpair;
                title="Register Pair: BC";
                break;
            case 1:
                register=DEpair.DEpair;
                title="Register Pair: DE";
                break;
            case 2:
                register=HLpair.HLpair;
                title="Register Pair: HL";
                break;
        }
    }
    private void editCellDialog(boolean regset)
    {
        if(!regset)
            setRegister();
        new ValueEditor(title,register.data().value(),true,this);
        working=false;
    }

    @Override
    public void editValue()
    {
        editCellDialog(true);
    }

    @Override
    public void setValue(DataEx data)
    {
        //setCellData(data);
        register.set(data);
    }

    @Override
    public void setValue(Data data)
    {
    }

    private void setCellData(int index,DataEx data)
    {
        regdata[0][index]=data.toHexString();
        ((AbstractTableModel) regtable.getModel()).fireTableCellUpdated(0, index);
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
            java.util.logging.Logger.getLogger(me.uniquetrij.i8085.giu.PairsRegisterTable.class.getName()).log(java.util.logging.Level.SEVERE,null,ex);
        }
        java.awt.EventQueue.invokeLater(new Runnable()
        {

            @Override
            public void run()
            {
                BCpair.BCpair.set(new DataEx(24));
                PairsRegisterTable mt=new PairsRegisterTable(13);

                /*
                 * RegisterB.RegisterB.set(new Data(10));
                 * RegisterC.RegisterC.set(new Data(20));
                 * RegisterD.RegisterD.set(new Data(30));
                 */


                JFrame frame=new JFrame();
                frame.add(mt);
                frame.setSize(mt.getSize());
                frame.setVisible(true);
            }
        });

    }
}