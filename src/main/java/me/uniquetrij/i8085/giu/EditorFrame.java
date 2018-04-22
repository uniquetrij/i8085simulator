package me.uniquetrij.i8085.giu;

import me.uniquetrij.i8085.Assembler;
import me.uniquetrij.i8085.AssemblerException;
import me.uniquetrij.i8085.Data;
import me.uniquetrij.i8085.DataEx;
import me.uniquetrij.i8085.ProgramCounter;
import me.uniquetrij.i8085.Simulator;
import me.uniquetrij.i8085.SimulatorException;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.*;
import java.util.StringTokenizer;
import javax.swing.*;
import javax.swing.text.Element;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;

/**
 *
 * @author Uniquetrij
 */
@SuppressWarnings("serial")
public class EditorFrame extends JFrame implements Editable, Serializable {
    //<editor-fold defaultstate="collapsed" desc="instance members">

    private JMenuBar menuBar;
    private JMenu fileMenu;
    private JMenuItem newFile;
    private JMenuItem openFile;
    private JMenuItem saveFile;
    private JMenuItem saveAsFile;
    private JMenuItem exitFile;
    private JMenu editMenu;
    private JMenuItem undoEdit;
    private JMenuItem redoEdit;
    private JMenuItem cutEdit;
    private JMenuItem copyEdit;
    private JMenuItem pasteEdit;
    private JMenuItem delEdit;
    private JMenuItem selectAllEdit;
    private JMenu assamblerMenu;
    private JMenuItem assambleAssambler;
    private JMenuItem loadAssambler;
    private JMenuItem listAssambler;
    private JMenuItem fontAssambler;
    private JMenu executeMenu;
    private JMenuItem runExecute;
    private JMenuItem executeStep;
    private JMenuItem stopExecute;
    private JToolBar toolBar;
    private JButton newTool;
    private JButton openTool;
    private JButton saveTool;
    private JButton undoTool;
    private JButton redoTool;
    private JMenu searchMenu;
    private JMenuItem findSearch;
    private JMenuItem replaceSearch;
    private JMenuItem gotoSearch;
    private JButton assambleTool;
    private JButton loadTool;
    private JButton runTool;
    private JButton stepTool;
    private JButton stopTool;
    private JSplitPane splitPane;
    private EditorPane editorPane;
    private File file;
    private UndoManager undoManager;
    private JScrollPane errorPane;
    private JTextArea errorText;
    private int fontsize;
    private String userpath;
    private DataEx origin;
    private boolean stepped;
    private Thread thread;
    //</editor-fold>

    public EditorFrame() {
        this(System.getProperty("user.home") + "/i8085asm/assembly Programs", 13);
    }

    public EditorFrame(String userpath, int fontsize) {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(EditorFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        this.fontsize = fontsize;
        this.userpath = userpath;
        initComponents();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {
        stepped = false;
        thread = new Thread(new Runnable() {

            @Override
            public void run() {
                Simulator.run();
            }
        });

        menuBar = new JMenuBar();
        fileMenu = new JMenu();
        newFile = new JMenuItem();
        openFile = new JMenuItem();
        saveFile = new JMenuItem();
        saveAsFile = new JMenuItem();
        exitFile = new JMenuItem();
        editMenu = new JMenu();
        undoEdit = new JMenuItem();
        redoEdit = new JMenuItem();
        cutEdit = new JMenuItem();
        copyEdit = new JMenuItem();
        pasteEdit = new JMenuItem();
        delEdit = new JMenuItem();
        selectAllEdit = new JMenuItem();
        searchMenu = new JMenu();
        findSearch = new JMenuItem();
        replaceSearch = new JMenuItem();
        gotoSearch = new JMenuItem();
        assamblerMenu = new JMenu();
        assambleAssambler = new JMenuItem();
        loadAssambler = new JMenuItem();
        listAssambler = new JMenuItem();
        fontAssambler = new JMenuItem();
        executeMenu = new JMenu();
        runExecute = new JMenuItem();
        executeStep = new JMenuItem();
        stopExecute = new JMenuItem();
        toolBar = new JToolBar();
        newTool = new JButton();
        openTool = new JButton();
        saveTool = new JButton();
        undoTool = new JButton();
        redoTool = new JButton();
        assambleTool = new JButton();
        loadTool = new JButton();
        runTool = new JButton();
        stepTool = new JButton();
        stopTool = new JButton();
        splitPane = new JSplitPane();
        editorPane = new EditorPane(fontsize);
        file = new File(userpath);
        undoManager = new UndoManager();
        errorPane = new JScrollPane();
        errorText = new JTextArea();
        errorText.setBackground(new Color(0xe2ddbb));

        if (!file.exists()) {
            file.mkdirs();
        }

        if (file.isDirectory()) {
            int i = 0;
            File tempfile;
            do {
                tempfile = new File(file.getAbsolutePath() + "/Untitled " + (++i) + ".i85");
            } while (tempfile.exists());
            file = tempfile;
        } else {
            userpath = userpath.substring(0, userpath.lastIndexOf('/'));
        }

        //<editor-fold defaultstate="collapsed" desc="Menu Bar">
        //<editor-fold defaultstate="collapsed" desc="File">
        fileMenu.setMnemonic('F');
        fileMenu.setText("File");
        //<editor-fold defaultstate="collapsed" desc="New">
        newFile.setAccelerator(javax.swing.KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_MASK));
        newFile.setText("New");
        newFile.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent evt) {
                newActionPerformed(evt);
            }
        });
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Open">
        openFile.setAccelerator(javax.swing.KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));
        openFile.setText("Open");
        openFile.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent evt) {
                openActionPerformed(evt);
            }
        });
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Save">
        saveFile.setAccelerator(javax.swing.KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
        saveFile.setText("Save");
        saveFile.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveActionPerformed(evt);
            }
        });
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Save As">
        saveAsFile.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, InputEvent.SHIFT_MASK | InputEvent.CTRL_MASK));
        saveAsFile.setText("Save As");
        saveAsFile.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent evt) {
                saveAsActionPerformed(evt);
            }
        });
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Exit">
        exitFile.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, InputEvent.CTRL_MASK));
        exitFile.setText("Exit");
        exitFile.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent evt) {
                exitActionPerformed(evt);
            }
        });
        //</editor-fold>

        fileMenu.add(newFile);
        fileMenu.add(openFile);
        fileMenu.add(saveFile);
        fileMenu.add(saveAsFile);
        fileMenu.add(new JPopupMenu.Separator());
        fileMenu.add(exitFile);
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Edit Menu">
        editMenu.setMnemonic('E');
        editMenu.setText("Edit");
        //<editor-fold defaultstate="collapsed" desc="Undo">
        undoEdit.setAccelerator(javax.swing.KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_MASK));
        undoEdit.setText("Undo");
        undoEdit.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent evt) {
                undoActionPerformed(evt);
            }
        });
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Redo">
        redoEdit.setAccelerator(javax.swing.KeyStroke.getKeyStroke(KeyEvent.VK_Y, InputEvent.CTRL_MASK));
        redoEdit.setText("Redo");
        redoEdit.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent evt) {
                redoActionPerformed(evt);
            }
        });
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Cut">
        cutEdit.setAccelerator(javax.swing.KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_MASK));
        cutEdit.setText("Cut");
        cutEdit.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent evt) {
                cutActionPerformed(evt);
            }
        });
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Copy">
        copyEdit.setAccelerator(javax.swing.KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_MASK));
        copyEdit.setText("Copy");
        copyEdit.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent evt) {
                copyActionPerformed(evt);
            }
        });
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Paste">
        pasteEdit.setAccelerator(javax.swing.KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_MASK));
        pasteEdit.setText("Paste");
        pasteEdit.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent evt) {
                pasteActionPerformed(evt);
            }
        });
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Delete">
        delEdit.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_DELETE, 0));
        delEdit.setText("Delete");
        delEdit.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent evt) {
                delActionPerformed(evt);
            }
        });
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Select All">
        selectAllEdit.setAccelerator(javax.swing.KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_MASK));
        selectAllEdit.setText("Select All");
        selectAllEdit.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent evt) {
                selectAllActionPerformed(evt);
            }
        });
        //</editor-fold>

        editMenu.add(undoEdit);
        editMenu.add(redoEdit);
        editMenu.add(new JPopupMenu.Separator());
        editMenu.add(cutEdit);
        editMenu.add(copyEdit);
        editMenu.add(pasteEdit);
        editMenu.add(delEdit);
        editMenu.add(new JPopupMenu.Separator());
        editMenu.add(selectAllEdit);

        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="Search Menu">
        searchMenu.setMnemonic('S');
        searchMenu.setText("Search");
        //<editor-fold defaultstate="collapsed" desc="Find">
        findSearch.setAccelerator(javax.swing.KeyStroke.getKeyStroke(KeyEvent.VK_F, InputEvent.CTRL_MASK));
        findSearch.setText("Find");
        findSearch.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent evt) {
                findActionPerformed(evt);
            }
        });
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Replace">
        replaceSearch.setAccelerator(javax.swing.KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_MASK));
        replaceSearch.setText("Replace");
        replaceSearch.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent evt) {
                replaceActionPerformed(evt);
            }
        });
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Go To">
        gotoSearch.setAccelerator(javax.swing.KeyStroke.getKeyStroke(KeyEvent.VK_G, InputEvent.CTRL_MASK));
        gotoSearch.setText("Go To...");
        gotoSearch.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent evt) {
                gotoActionPerformed(evt);
            }
        });
        //</editor-fold>
        searchMenu.add(findSearch);
        searchMenu.add(replaceSearch);
        searchMenu.add(new JPopupMenu.Separator());
        searchMenu.add(gotoSearch);
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Assambler Menu">
        assamblerMenu.setMnemonic('A');
        assamblerMenu.setText("Assambler");
        //<editor-fold defaultstate="collapsed" desc="Assamble">
        assambleAssambler.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F3, 0));
        assambleAssambler.setText("Assamble");
        assambleAssambler.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent evt) {
                assambleActionPerformed(evt);
            }
        });
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Load">
        loadAssambler.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F4, 0));
        loadAssambler.setText("Load assembly");
        loadAssambler.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent evt) {
                loadActionPerformed(evt);
            }
        });
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="List">
        listAssambler.setAccelerator(javax.swing.KeyStroke.getKeyStroke(KeyEvent.VK_L, InputEvent.CTRL_MASK));
        listAssambler.setText("Show Listing");
        listAssambler.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent evt) {
                listActionPerformed(evt);
            }
        });
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Font">
        //fontAssambler.setAccelerator(javax.swing.KeyStroke.getKeyStroke(KeyEvent.VK_L,InputEvent.CTRL_MASK));
        fontAssambler.setText("Font Size");
        fontAssambler.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent evt) {
                fontAssamblerActionPerformed(evt);
            }
        });
        //</editor-fold>
        assamblerMenu.add(assambleAssambler);
        assamblerMenu.add(loadAssambler);
        assamblerMenu.add(new JPopupMenu.Separator());
        assamblerMenu.add(listAssambler);
        assamblerMenu.add(new JPopupMenu.Separator());
        assamblerMenu.add(fontAssambler);
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Execute Menu">
        executeMenu.setMnemonic('x');
        executeMenu.setText("Execute");
        //<editor-fold defaultstate="collapsed" desc="Run">
        runExecute.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F7, 0));
        runExecute.setText("Run");
        runExecute.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent evt) {
                runActionPerformed(evt);
            }
        });
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Step">
        executeStep.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F8, 0));
        executeStep.setText("Step");
        executeStep.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent evt) {
                stepActionPerformed(evt);
            }
        });
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Stop">
        stopExecute.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F9, 0));
        stopExecute.setText("Stop");
        stopExecute.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent evt) {
                stopActionPerformed(evt);
            }
        });
        //</editor-fold>

        executeMenu.add(runExecute);
        executeMenu.add(new JPopupMenu.Separator());
        executeMenu.add(executeStep);
        executeMenu.add(stopExecute);
        //</editor-fold>

        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(searchMenu);
        menuBar.add(assamblerMenu);
        menuBar.add(executeMenu);

        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="Tool Bar">
        //<editor-fold defaultstate="collapsed" desc="New">
        newTool.setText("New");
        newTool.setFocusable(false);
        newTool.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        newTool.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        newTool.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newActionPerformed(evt);
            }
        });
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Open">
        openTool.setText("Open");
        openTool.setFocusable(false);
        openTool.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        openTool.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        openTool.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openActionPerformed(evt);
            }
        });
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Save">
        saveTool.setText("Save");
        saveTool.setFocusable(false);
        saveTool.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        saveTool.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        saveTool.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveActionPerformed(evt);
            }
        });
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Undo">
        undoTool.setText("Undo");
        undoTool.setFocusable(false);
        undoTool.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        undoTool.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        undoTool.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                undoActionPerformed(evt);
            }
        });
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Redo">
        redoTool.setText("Redo");
        redoTool.setFocusable(false);
        redoTool.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        redoTool.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        redoTool.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                redoActionPerformed(evt);
            }
        });
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Assamble">
        assambleTool.setText("Assamble");
        assambleTool.setFocusable(false);
        assambleTool.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        assambleTool.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        assambleTool.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                assambleActionPerformed(evt);
            }
        });
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Load">
        loadTool.setText("Load");
        loadTool.setFocusable(false);
        loadTool.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        loadTool.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        loadTool.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadActionPerformed(evt);
            }
        });
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Run">
        runTool.setText("Run");
        runTool.setFocusable(false);
        runTool.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        runTool.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        runTool.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runActionPerformed(evt);
            }
        });
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Step">
        stepTool.setText("Step");
        stepTool.setFocusable(false);
        stepTool.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        stepTool.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        stepTool.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stepActionPerformed(evt);
            }
        });
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Stop">
        stopTool.setText("Stop");
        stopTool.setFocusable(false);
        stopTool.setEnabled(false);
        stopExecute.setEnabled(false);
        stopTool.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        stopTool.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        stopTool.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stopActionPerformed(evt);
            }
        });
        //</editor-fold>

        //toolBar.add(new JToolBar.Separator());
        toolBar.add(newTool);
        toolBar.add(openTool);
        toolBar.add(saveTool);
        toolBar.add(new JToolBar.Separator());
        toolBar.add(undoTool);
        toolBar.add(redoTool);
        toolBar.add(new JToolBar.Separator());
        toolBar.add(assambleTool);
        toolBar.add(loadTool);
        toolBar.add(new JToolBar.Separator());
        toolBar.setRollover(true);
        toolBar.setFloatable(false);
        toolBar.add(runTool);
        toolBar.add(stepTool);
        toolBar.add(stopTool);
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Editor Area">
        splitPane.setSize(400, 600);
        splitPane.setDividerSize(10);
        splitPane.setLastDividerLocation(400);
        splitPane.setOneTouchExpandable(true);
        splitPane.setTopComponent(editorPane);
        splitPane.setRightComponent(errorPane);
        splitPane.setDividerLocation(1.0);
        splitPane.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        splitPane.addHierarchyBoundsListener(new HierarchyBoundsListener() {

            @Override
            public void ancestorMoved(HierarchyEvent evt) {
            }

            @Override
            public void ancestorResized(HierarchyEvent evt) {
                splitPaneAncestorResized(evt);
            }
        });

        errorText.setEditable(false);
        errorText.setFont(new Font(Font.MONOSPACED, 0, 13));
        errorPane.setViewportView(errorText);
        JTextPane editor = editorPane.getEditor();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            editor.read(br, null);
        } catch (Exception e) {
        }
        editorPane.setSaved();
        editorPane.linesUpdate(0, false);
        editorPane.getEditor().getDocument().addUndoableEditListener(undoManager);
        editorPane.addPropertyChangeListener("changed", new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                setAssambled(false);
            }
        });
        editorPane.addPropertyChangeListener("undo/redoable", new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                setUndoRedoable(undoManager.canUndo(), undoManager.canRedo());
            }
        });
        //editorPane.setSaved();

        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="this">
        setJMenuBar(menuBar);
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {

            @Override
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });
        setMinimumSize(new Dimension(400, 400));
        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(toolBar, GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE).addComponent(splitPane, GroupLayout.Alignment.TRAILING));
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(toolBar, GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(splitPane, javax.swing.GroupLayout.DEFAULT_SIZE, 500, Short.MAX_VALUE)));
        pack();
        setTitle(file.getName() + " - Intel 8085 assembly Editor");

        setLocationRelativeTo(null);
        setAssambled(false);
        setUndoRedoable(false, false);
        //setUndoable(false);
        //</editor-fold>

    }// </editor-fold>

    private void splitPaneAncestorResized(HierarchyEvent evt) {
        splitPane.setDividerLocation(1.0);
        splitPane.setLastDividerLocation((int) (splitPane.getDividerLocation() * 0.8));
    }

    private void setAssambled(boolean asmd) {
        listAssambler.setEnabled(asmd);
        loadAssambler.setEnabled(asmd);
        loadTool.setEnabled(asmd);

        if (asmd == false) {
            splitPane.setDividerLocation(1.0);
            errorText.setText("[CHANGED]Assamble Again. Last assembly Report:\n" + errorText.getText());
            errorText.setForeground(Color.BLUE);
            errorText.setCaretPosition(0);

            runTool.setEnabled(false);
            runExecute.setEnabled(false);
            stepTool.setEnabled(false);
            executeStep.setEnabled(false);
            runExecute.setEnabled(false);
        }
    }

    @SuppressWarnings("fallthrough")
    private void newActionPerformed(ActionEvent evt) {
        if (!editorPane.isSaved()) {
            int answer = JOptionPane.showConfirmDialog(this, "Do you want to save changes to " + file.getName() + "?", "Confirm Save", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
            switch (answer) {
                case JOptionPane.YES_OPTION:
                    saveFile();
                case JOptionPane.NO_OPTION:
                    break;
                case JOptionPane.CANCEL_OPTION:
                    return;
            }
        }
        /*
         * int i=0;
         * file = new File(userpath);
         * File tempfile;
         * do
         * tempfile=new File(file.getAbsolutePath()+"\\Untitled "+ ++i +".i85");
         * while(tempfile.exists());
         * file=tempfile;
         * editorPane.getEditor().setText("");
         * editorPane.setSaved();
         * editorPane.linesUpdate(0, false);
         * setTitle(file.getName() + " - i8085sim assembly Editor");
         *
         */
        EditorFrame frame = new EditorFrame(userpath, fontsize);
        frame.setLocationRelativeTo(this);
        frame.setVisible(true);

        dispose();
    }

    private void openActionPerformed(ActionEvent evt) {
        if (!editorPane.isSaved()) {
            int answer = JOptionPane.showConfirmDialog(this, "Do you want to save changes to " + file.getName() + "?", "Confirm Save", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
            switch (answer) {
                case JOptionPane.YES_OPTION:
                    saveFile();
                case JOptionPane.NO_OPTION:
                    break;
                case JOptionPane.CANCEL_OPTION:
                    return;
            }
        }

        FileOpener fileOpener = new FileOpener(editorPane.getEditor());
        if (fileOpener.getFile() == null) {
            return;
        }

        file = fileOpener.getFile();
        /*
         *
         * JTextPane editor = editorPane.getEditor();
         * try (BufferedReader br = new BufferedReader(new FileReader(file)))
         * {
         * editor.read(br, null);
         * }
         * catch (Exception e)
         * {
         * }
         * editorPane.setSaved();
         * editorPane.linesUpdate(0, false);
         * setTitle(file.getName() + " - i8085sim assembly Editor");
         * editorPane.getEditor().getDocument().addUndoableEditListener(undoManager);
         *
         * setUndoRedoable(false,false);
         *
         */
        EditorFrame frame = new EditorFrame(file.getAbsolutePath(), fontsize);
        frame.setLocationRelativeTo(this);
        frame.setVisible(true);
        dispose();

    }

    private void saveActionPerformed(ActionEvent evt) {
        if (file.getName().indexOf("Untitled ") == 0 && !file.exists()) {
            saveAsActionPerformed(evt);
        } else {
            saveFile();
        }
    }

    private void saveAsActionPerformed(ActionEvent evt) {
        FileSaver fileSaver = new FileSaver(editorPane.getEditor(), file);
        if (fileSaver.getFile() == null) {
            return;
        }
        file = fileSaver.getFile();
        saveFile();
    }

    public void saveFile() {
        JTextPane editor = editorPane.getEditor();
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            bw.write(editor.getText());
        } catch (Exception e) {
        }
        editorPane.setSaved();
        setTitle(file.getName() + " - i8085sim assembly Editor");
    }

    private void exitActionPerformed(ActionEvent evt) {
        dispose();
    }

    private void undoActionPerformed(ActionEvent evt) {
        try {
            undoManager.undo();
        } catch (CannotUndoException e) {
        }
        setUndoRedoable(undoManager.canUndo(), undoManager.canRedo());
    }

    private void redoActionPerformed(ActionEvent evt) {
        try {
            undoManager.redo();
        } catch (CannotRedoException e) {
        }
        setUndoRedoable(undoManager.canUndo(), undoManager.canRedo());
    }

    private void setUndoRedoable(boolean undoable, boolean redoable) {
        undoEdit.setEnabled(undoable);
        redoEdit.setEnabled(redoable);
        undoTool.setEnabled(undoable);
        redoTool.setEnabled(redoable);
    }

    private void cutActionPerformed(ActionEvent evt) {
        editorPane.getEditor().cut();
        setUndoRedoable(undoManager.canUndo(), undoManager.canRedo());
    }

    private void copyActionPerformed(ActionEvent evt) {
        editorPane.getEditor().copy();
        setUndoRedoable(undoManager.canUndo(), undoManager.canRedo());
    }

    private void pasteActionPerformed(ActionEvent evt) {
        editorPane.getEditor().paste();
        setUndoRedoable(undoManager.canUndo(), undoManager.canRedo());
    }

    private void delActionPerformed(ActionEvent evt) {
        editorPane.getEditor().replaceSelection("");
        setUndoRedoable(undoManager.canUndo(), undoManager.canRedo());
    }

    private void selectAllActionPerformed(ActionEvent evt) {
        editorPane.getEditor().selectAll();
    }

    private void findActionPerformed(ActionEvent evt) {
        FindReplace findReplace = new FindReplace(editorPane.getEditor(), false);
        findReplace.setVisible(true);
    }

    private void replaceActionPerformed(ActionEvent evt) {
        FindReplace findReplace = new FindReplace(editorPane.getEditor(), true);
        findReplace.setVisible(true);
    }

    private void gotoActionPerformed(ActionEvent evt) {
        int lncount = editorPane.getLineCount();
        int ln = -1;
        do {
            try {
                ln = Integer.parseInt(JOptionPane.showInputDialog(this, "Line Number:", "Go To...", JOptionPane.PLAIN_MESSAGE));
            } catch (NumberFormatException e) {
            }
            if (ln > 0 && ln <= lncount) {
                break;
            }
            JOptionPane.showConfirmDialog(this, "Enter line in range 1 - " + lncount, "Warning", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE);
        } while (true);
        selectLine(ln);
    }

    public void selectLine(int ln) {
        try {
            JTextPane editor = editorPane.getEditor();
            Element e = editor.getDocument().getRootElements()[0].getElement(ln - 1);
            editor.select(e.getStartOffset(), e.getEndOffset() - 1);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println(e);
        }
    }

    private void assambleActionPerformed(ActionEvent evt) {
        try {
            new Assembler();
            Assembler.assamble(editorPane.getEditor().getText());
        } catch (AssemblerException e) {
            errorText.setForeground(Color.RED);
            splitPane.setDividerLocation(0.8);
            errorText.setText(e.toString());
            errorText.setCaretPosition(0);
            editorPane.resetChanged();
            selectLine(e.getLineNumber());
            return;
        }
        splitPane.setDividerLocation(0.90);
        errorText.setText("assembly Successful.\nYou can now Load program into memory.");
        errorText.setForeground(new Color(0x117113));
        errorText.setCaretPosition(0);
        editorPane.resetChanged();
        setAssambled(true);
    }

    private void loadActionPerformed(ActionEvent evt) {
        try {
            new Simulator();
            Simulator.simulate(Assembler.getCodeList());
            origin = Simulator.getProgramCounter();
            //int answer=JOptionPane.showConfirmDialog(this, "assembly loaded into memory.\nDo you want to run simulation?", "assembly Loaded Successfully", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            //if(answer==JOptionPane.YES_OPTION)
            //runActionPerformed(evt);
            errorText.setForeground(new Color(0x117113));
            splitPane.setDividerLocation(0.90);
            errorText.setText("assembly loaded into memory.\nRun/Step to execute program.");
            errorText.setCaretPosition(0);
            editorPane.resetChanged();
            runTool.setEnabled(true);
            runExecute.setEnabled(true);
            stepTool.setEnabled(true);
            executeStep.setEnabled(true);
            runExecute.setEnabled(true);
        } catch (SimulatorException | AssemblerException e) {
            errorText.setForeground(Color.RED);
            splitPane.setDividerLocation(0.8);
            errorText.setText(e.toString());
            errorText.setCaretPosition(0);
            editorPane.resetChanged();
        }
    }

    private void listActionPerformed(ActionEvent evt) {
        EditorPane ep = new EditorPane();
        ep.getEditor().setText(Assembler.getList());
        ep.getEditor().setFocusable(false);
        JFrame frame = new JFrame(file.getName() + " - Intel 8085 Assambler Listing");
        frame.add(ep);
        frame.setSize(500, 400);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void fontAssamblerActionPerformed(ActionEvent evt) {
        int size = -1;
        do {
            try {
                size = Integer.parseInt(JOptionPane.showInputDialog(this, "Size", "Select Font Size", JOptionPane.PLAIN_MESSAGE));
            } catch (NumberFormatException e) {
            }
            if (size >= 10 && size <= 30) {
                break;
            }
            JOptionPane.showConfirmDialog(this, "Enter line in range 10 - 30", "Warning", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE);
        } while (true);
        editorPane.setFontSize(size);
    }

    private void runActionPerformed(ActionEvent evt) {
        thread = new Thread(new Runnable() {

            @Override
            public void run() {
                Simulator.run();

            }
        });
        //new ValueEditor("Start Execution @ Address",origin.value(),true,this);
        Simulator.startAt(Assembler.getOrigin());
        StringTokenizer stk = new StringTokenizer(editorPane.getBrakePoints(), "*");
        while (stk.hasMoreTokens()) {
            Assembler.setBrakePoint(Integer.parseInt(stk.nextToken()));
        }
        do {
            try {
                thread.start();
            } catch (SimulatorException e) {
                if (e.isInvalidOpcode()) {
                    JOptionPane.showConfirmDialog(this, e, "Simulatior Warning", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE);
                }
                if (e.hasStoppedAtBrakePoint()) {
                    if (JOptionPane.showConfirmDialog(this, e + "\nRun now?", "Simulatior Warning", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION) {
                        continue;
                    }
                }
            }
            break;
        } while (true);

    }

    private void stepActionPerformed(ActionEvent evt) {
        editorPane.getEditor().setFocusable(false);
        undoTool.setEnabled(false);
        undoEdit.setEnabled(false);
        redoTool.setEnabled(false);
        redoEdit.setEnabled(false);
        runTool.setEnabled(false);
        runExecute.setEnabled(false);
        stopTool.setEnabled(true);
        stopExecute.setEnabled(true);
        String str;
        try {
            str = Assembler.getAddressLinesList().trim();
            String adr = ProgramCounter.ProgramCounter.data().toString();
            if (stepped == true) {
                undoManager.undo();
            }
            Simulator.step();
            stepped = true;
            str = str.substring(0, str.indexOf(adr) - 1);
            str = str.substring(str.lastIndexOf("*") + 1);
//            System.out.println(str);
            errorText.setForeground(Color.MAGENTA);
            splitPane.setDividerLocation(0.95);
            errorText.setText("Executed Line : " + str);
            errorText.setCaretPosition(0);
            //removeHilight(stepped);
            hilight(Integer.parseInt(str));
        } catch (SimulatorException e) {
            if (e.isInvalidOpcode()) {
                JOptionPane.showConfirmDialog(this, e, "Simulatior Warning", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE);
            }
        } catch (StringIndexOutOfBoundsException e) {
            stepped = false;
            stopActionPerformed(evt);
        }
    }

    private void stopActionPerformed(ActionEvent evt) {
        errorText.setText("Execution Terminated");
        undoTool.setEnabled(true);
        undoEdit.setEnabled(true);
        redoTool.setEnabled(true);
        redoEdit.setEnabled(true);
        //removeHilight(stepped);
        if (stepped == true) {
            undoManager.undo();
        }
        editorPane.getEditor().setFocusable(true);
        stopTool.setEnabled(false);
        stopExecute.setEnabled(false);
        runTool.setEnabled(true);
        runExecute.setEnabled(true);
        stepped = false;
        Simulator.startAt(Assembler.getOrigin());

    }

    private void hilight(int line) {
        StyledDocument doc = editorPane.getEditor().getStyledDocument();
        Element e = doc.getRootElements()[0].getElement(line - 1);
        SimpleAttributeSet as = new SimpleAttributeSet();
        StyleConstants.setBackground(as, new Color(0x40f98a));
        StyleConstants.setForeground(as, Color.BLACK);
        doc.setCharacterAttributes(e.getStartOffset(), e.getEndOffset() - e.getStartOffset(), as, false);

    }

    private void removeHilight(int line) {
        if (line == -1) {
            return;
        }
        StyledDocument doc = editorPane.getEditor().getStyledDocument();
        Element e = doc.getRootElements()[0].getElement(line - 1);
        SimpleAttributeSet as = new SimpleAttributeSet();
        StyleConstants.setBackground(as, Color.WHITE);
        StyleConstants.setForeground(as, Color.BLACK);
        doc.setCharacterAttributes(e.getStartOffset(), e.getEndOffset() - e.getStartOffset(), as, false);
    }

    @SuppressWarnings("fallthrough")
    public boolean formWindowClosing(WindowEvent evt) {
        if (!editorPane.isSaved()) {
            int answer = JOptionPane.showConfirmDialog(this, "Save Changes to " + file.getName() + "?", "Warning", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
            switch (answer) {
                case JOptionPane.YES_OPTION:
                    saveActionPerformed(null);
                case JOptionPane.NO_OPTION:
                    break;
                case JOptionPane.CANCEL_OPTION:
                default:
                    return false;
            }
        }
        editorPane.setSaved();
        dispose();
        return true;
    }

    private boolean isSaved() {
        return editorPane.isSaved();
    }

    public Thread getThread() {
        return thread;
    }

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                new EditorFrame(System.getProperty("user.home") + "/i8085asm/assembly Programs", 13).setVisible(true);
            }
        });
    }

    @Override
    public void editValue() {
    }

    @Override
    public void setValue(Data data) {
    }

    @Override
    public void setValue(DataEx d) {
        Simulator.startAt(d);
    }
}
