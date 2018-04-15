/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.uniquetrij.i8085.giu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.JTextComponent;

/**
 *
 * @author Uniquetrij
 */
@SuppressWarnings("serial")
public class FileSaver extends JDialog
{
    //<editor-fold defaultstate="collapsed" desc="instance members">

    private JTextComponent jTextComponent;
    private File file;
    private JFileChooser jFileChooser;
    //</editor-fold>

    public FileSaver(JTextComponent jTextComponent,File file)
    {
        try
        {
            for(UIManager.LookAndFeelInfo info:UIManager.getInstalledLookAndFeels())
                if("Numbus".equals(info.getName()))
                {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
        }
        catch(ClassNotFoundException|InstantiationException|IllegalAccessException|UnsupportedLookAndFeelException ex)
        {
            java.util.logging.Logger.getLogger(FileOpener.class.getName()).log(java.util.logging.Level.SEVERE,null,ex);
        }

        this.jTextComponent=jTextComponent;
        this.file=file;
        initComponents();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents()
    {

        jFileChooser=new JFileChooser();
        jFileChooser.setDialogType(JFileChooser.SAVE_DIALOG);
        jFileChooser.addActionListener(new ActionListener()
        {

            @Override
            public void actionPerformed(ActionEvent evt)
            {
                jFileChooserActionPerformed(evt);
            }
        });
        jFileChooser.setSelectedFile(file);
        jFileChooser.setFileFilter(new FileNameExtensionFilter("i8085sim Files (*.i85)","i85"));
        jFileChooser.addChoosableFileFilter(new FileNameExtensionFilter("assembly Files (*.asm)","asm"));


        GroupLayout layout=new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(jFileChooser,GroupLayout.PREFERRED_SIZE,GroupLayout.DEFAULT_SIZE,GroupLayout.PREFERRED_SIZE).addContainerGap(33,Short.MAX_VALUE)));
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap(GroupLayout.DEFAULT_SIZE,Short.MAX_VALUE).addComponent(jFileChooser,GroupLayout.PREFERRED_SIZE,GroupLayout.DEFAULT_SIZE,GroupLayout.PREFERRED_SIZE).addContainerGap()));

        setTitle("Save As");
        pack();
        setLocationRelativeTo(jTextComponent);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setAlwaysOnTop(true);
        setModal(true);
        setVisible(true);

    }// </editor-fold>

    private void jFileChooserActionPerformed(ActionEvent evt)
    {
        file=new File("");
        if(evt.getActionCommand().toString().equals("ApproveSelection"))
        {
            file=((JFileChooser)evt.getSource()).getSelectedFile();

            String ext=file.getName();
            int inx=ext.lastIndexOf(".");
            inx=inx==-1?ext.length()-1:inx;
            ext=ext.substring(inx+1);

            switch(jFileChooser.getFileFilter().getDescription())
            {
                case "i8085sim Files (*.i85)":
                {
                    if(!"i85".equals(ext))
                        file=new File(file.getAbsolutePath()+".i85");
                }
                break;

                case "assembly Files (*.asm)":
                {
                    if(!"asm".equals(ext))
                        file=new File(file.getAbsolutePath()+".asm");
                }
                break;
            }

            if(file.exists())
            {
                int answer=JOptionPane.showConfirmDialog(this,file.getName()+" already exists.\nDo you want to replace it?","Confirm Save As",JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.WARNING_MESSAGE);
                switch(answer)
                {
                    case JOptionPane.YES_OPTION:
                        break;
                    case JOptionPane.NO_OPTION:
                        return;
                    case JOptionPane.CANCEL_OPTION:
                        file=null;
                        dispose();
                        return;
                }
            }

        }
        dispose();
    }

    public File getFile()
    {
        return file;
    }

    public static void main(String args[])
    {

        java.awt.EventQueue.invokeLater(new Runnable()
        {

            @Override
            public void run()
            {
                new FileSaver(null,null).setVisible(true);
            }
        });
    }
}
