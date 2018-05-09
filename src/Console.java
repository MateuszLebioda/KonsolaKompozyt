import com.MateuszLebioda.Catalog;
import com.MateuszLebioda.File;
import com.MateuszLebioda.newFile;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


public class Console extends JComponent implements KeyListener{
    private JPanel panel1;
    private JEditorPane editorPane1;
    String content = "Microsoft Windows [Version 10.0.16299.371]" + "\n" + "(c) 2017 Microsoft Corporation. All rights reserved." + "\n\n" + "c:\\>";
    String globalPath = "c:";
    newFile hardDisc;
    String command;

    Console(){
        Font font=null;
        createDefaultCatalog();
        editorPane1.addKeyListener(this);
        editorPane1.setBackground(Color.BLACK);
        editorPane1.setForeground(Color.WHITE);
        editorPane1.setVisible(true);
        try {
            font = Font.createFont(Font.TRUETYPE_FONT,getClass().getResourceAsStream("resources/windows_command_prompt.ttf"));
            font = font.deriveFont(Font.PLAIN,16);
        }catch (Exception e){
            System.out.println("Blad z czcionka");
        }
        editorPane1.setFont(font);
        editorPane1.setText(content);
    }

    public static void main(String[] args) {
        JFrame panel = new JFrame();
        panel.setBounds(10,10,800,450);

        panel.setContentPane(new Console().panel1);
        panel.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        panel.setVisible(true);

    }

    @Override
    public void keyPressed(KeyEvent e) {

        switch(e.getKeyCode()){

            case KeyEvent.VK_ENTER:{
                e.consume();
                command = prepareCommand(content);

                /*** change directory*/
                if(CheckIf("cd")) {

                    newFile temp = getFileByPath(command,"cd");
                    content = content.concat(command);
                    if (temp != null) {
                        globalPath = command.substring("cd".length()).replace(" ", "");
                    } else {
                        content = content.concat("\n" + "The system cannot find the drive specified..");
                    }
                    setTextAndPath();

                }else if(CheckIf("md")){
                    String path = command.substring("md".length());
                    path = path.replace(" ", "");
                    String[] PathSplit = path.split("\\\\");

                    if(PathSplit[0].equals("e:")||PathSplit[0].equals("c:")) {
                        if (createCatalogByPath(path, "md")) {
                            content = content.concat(command);
                        }else {
                            content = content.concat(command + "\nThe system cannot find the path specified.");
                        }
                    }else {
                        String path1 = globalPath.concat( "\\" + path);
                        if (createCatalogByPath(path1, "md")) {
                            content = content.concat(command);
                        }else {
                            content = content.concat(command + "\nThe system cannot find the path specified.");
                        }
                    }
                        setTextAndPath();

                }else if(CheckIf("fd")){
                    String path = command.substring("fd".length());
                    path = path.replace(" ", "");
                    String[] PathSplit = path.split("\\\\");

                    if(PathSplit[0].equals("e:")||PathSplit[0].equals("c:")) {
                        if (createCatalogByPath(path, "md")) {
                            content = content.concat(command);
                        }else {
                            content = content.concat(command + "\nThe system cannot find the path specified.");
                        }
                    }else {
                        String path1 = globalPath.concat( "\\" + path);
                        if (createFileByPath(path1, "fd")) {
                            content = content.concat(command);
                        }else {
                            content = content.concat(command + "\nThe system cannot find the path specified.");
                        }
                    }
                    setTextAndPath();

                }else if(CheckIf("dir")){
                    String path = command.substring("dir".length());
                    if("".equals(path)){
                        String[] PathSplit = globalPath.split("\\\\");
                        newFile temp = search(PathSplit,PathSplit.length);
                        if(temp!=null){
                            content = content.concat(command + "\n" +  search(PathSplit,PathSplit.length).draw(-1));
                        }else{
                            content = content.concat(command + "\n" + "The system cannot find the drive specified..");
                        }
                        setTextAndPath();

                    }else{
                        path = path.replace(" ","");
                        String[] PathSplit = path.split("\\\\");
                        newFile temp = search(PathSplit,PathSplit.length);
                        if(temp!=null){
                            content = content.concat(command + "\n" +  search(PathSplit,PathSplit.length).draw(-1));
                        }else{
                            content = content.concat(command + "\n" + "The system cannot find the drive specified..");
                        }

                        setTextAndPath();
                    }
                }else if(CheckIf("dir")){
                    String path = command.substring("copy".length());
                    String[] PathSplit1 = path.split(" ");
                    if(PathSplit1.length!=3){
                        content = content.concat(command + "\n" + "The system cannot find the drive specified..");
                    }else{
                        String[] PathSplit = PathSplit1[1].split("\\\\");

                        newFile copied = search(PathSplit,PathSplit.length);
                        PathSplit = PathSplit1[2].split("\\\\");
                        newFile target = search(PathSplit,PathSplit.length);
                        target.add(copied);

                        content = content.concat(command);
                    }
                    setTextAndPath();

                }
                else if(CheckIf("cls")) {
                    content = "Microsoft Windows [Version 10.0.16299.371]" + "\n" + "(c) 2017 Microsoft Corporation. All rights reserved.\n";
                    setTextAndPath();
                }else if(command.length()>=4&&command.equals("help")){
                    content = content.concat(command.concat( "\nCLS - Clears the screen." +
                                                    "\nDIR - Displays a list of files and subdirectories in a directory.." +
                                                    "\nCD - Displays the name of or changes the current directory." +
                                                    "\nMD - Creates a directory." +
                                                    "\nFD - Creates a file.") +
                                                    "\nCOPY - Copies one file to another location." +
                                                    "\nMOVE - Moves one file from one directory to another directory.");
                    setTextAndPath();
                }
                else {
                    content = content + command  + "\n\'" + command + "\'" +  "is not recognized as an internal or external command, \n operable program or batch file.";
                    setTextAndPath();
                }
                break;

            }case KeyEvent.VK_BACK_SPACE:{
                if(!editorPane1.getText().substring(0, content.length()-1).equals(content)){
                    editorPane1.setText(content);
                    e.consume();
                }
                break;
            }
        }
    }

    private String prepareCommand(String content){
        String command = editorPane1.getText().substring(content.length());
        command.toLowerCase();
        return command;
    }

    private void setTextAndPath(){
        content = content.concat("\n" + globalPath + "\\>");
        editorPane1.setText(content);
    }
    private boolean CheckIf(String command){
        if(this.command.length() >= command.length() && this.command.substring(0,command.length()).equals(command)){
            return true;
        }return false;
    }

    private newFile search(String path[],int iterations){
        newFile temp = hardDisc;
        for(int i =0;i<iterations;i++){
            if(temp.getFileByPath(path[i])!=null){
                temp = temp.getFileByPath(path[i]);
            }else{
                return null;
            }
        }
        return temp;
    }

    private newFile getFileByPath(String commend,String size){
        String path = commend.substring(size.length());
        path = path.replace(" ", "");
        String[] PathSplit = path.split("\\\\");
        newFile temp = search(PathSplit, PathSplit.length);
        return temp;
    }

    private boolean createCatalogByPath(String path, String size) {

        String[] PathSplit = path.split("\\\\");
        newFile temp = hardDisc;
        boolean x;
        for (int i = 0; i < PathSplit.length; i++) {
            x = false;
            for (int k = 0; k < temp.list.size(); k++) {
                if (temp.list.get(k).path.equals(PathSplit[i]))
                    x = true;
            }
            if (x) {
                temp = temp.getFileByPath(PathSplit[i]);
                if(!temp.cat) return false;
            } else {
                Catalog catalog = new Catalog(PathSplit[i]);
                temp.add(catalog);
                temp = catalog;
            }
        }
        return true;
    }
    private boolean createFileByPath(String path, String size) {
        String[] PathSplit = path.split("\\\\");

        newFile temp = hardDisc;
        boolean x;
        for (int i = 0; i < PathSplit.length; i++) {
            x = false;
            for (int k = 0; k < temp.list.size(); k++) {
                if (temp.list.get(k).path.equals(PathSplit[i]))
                    x = true;
            }
            if (x) {
                temp = temp.getFileByPath(PathSplit[i]);
                if(!temp.cat) return false;
            } else {
                File catalog = new File(PathSplit[i]);
                temp.add(catalog);
                temp = catalog;
            }
        }
        return true;
    }

    private void createDefaultCatalog(){
        hardDisc = new Catalog("");
        hardDisc.add(new Catalog("e:"));
        hardDisc.add(new Catalog("c:"));
        hardDisc.getFileByPath("e:").add(new com.MateuszLebioda.File("file"));
        hardDisc.getFileByPath("e:").add(new com.MateuszLebioda.File("file2"));
        hardDisc.getFileByPath("e:").add(new Catalog("dir1"));

        hardDisc.getFileByPath("c:").add(new Catalog("dir1"));
        hardDisc.getFileByPath("c:").getFileByPath("dir1").add(new com.MateuszLebioda.File("file3"));
        hardDisc.getFileByPath("c:").getFileByPath("dir1").add(new com.MateuszLebioda.File("file4"));
        hardDisc.getFileByPath("c:").getFileByPath("dir1").add(new Catalog("catalog"));
        hardDisc.getFileByPath("c:").getFileByPath("dir1").getFileByPath("catalog").add(new com.MateuszLebioda.File("file"));
        hardDisc.getFileByPath("c:").getFileByPath("dir1").getFileByPath("catalog").add(new Catalog("catalog11"));
        hardDisc.getFileByPath("c:").add(new Catalog("dir2"));
        hardDisc.getFileByPath("c:").getFileByPath("dir2").add(new com.MateuszLebioda.File("file4"));
        hardDisc.getFileByPath("c:").getFileByPath("dir2").add(new com.MateuszLebioda.File("file5"));
    }

    @Override
    public void keyReleased(KeyEvent e) {}
    @Override
    public void keyTyped(KeyEvent e) {}
}

