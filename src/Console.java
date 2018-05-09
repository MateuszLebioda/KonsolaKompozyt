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
                content = content.concat(command);

                /*** Change directory*/
                if(CheckIf("cd")) {

                    if (getFileByPathByCommend("cd") != null) {
                        globalPath = command.substring("cd".length()).replace(" ", "");
                    } else {
                        content = content.concat("\n" + "The system cannot find the drive specified..");
                    }

                /*** Creates a directory or file*/
                }else if(CheckIf("md")||CheckIf("fd")) {
                    String path = getPathByCommend("md");
                    String[] PathSplit = getSplitPathByCommend("md");

                    if (!(PathSplit[0].equals("e:") || PathSplit[0].equals("c:"))) {
                        path = globalPath.concat("\\" + path);
                    }
                        /*** Create directory */
                    if (command.substring(0, 2).equals("md")) {
                        if (!createCatalogByPath(path)) {
                            content = content.concat("\nThe system cannot find the path specified.");
                        }
                        /*** Create file */
                    } else if (command.substring(0, 2).equals("fd")) {
                        if (!createFileByPath(path, "fd")) {
                            content = content.concat("\nThe system cannot find the path specified.");
                        }
                    }
                }

                /*** Displays a list of files and subdirectories in a directory */
                else if(CheckIf("dir")){

                    String[] PathSplit = getSplitPathByCommend("dir");
                    newFile temp = search(PathSplit);

                    if(getPathByCommend("dir").equals("")){
                        PathSplit = globalPath.split("\\\\");
                        temp = search(PathSplit);
                    }

                    if(temp!=null){
                        content = content.concat(search(PathSplit).draw(-1));
                    }else{
                        content = content.concat("\n" + "The system cannot find the drive specified..");
                    }

                /***Copies one file to another location*/
                }else if(CheckIf("copy")||CheckIf("move")){
                    String path = command.substring("copy".length());
                    String[] PathSplit1 = path.split(" ");
                    if(PathSplit1.length!=3){
                        content = content.concat("\n" + "The system cannot find the drive specified..");
                    }else{
                        String[] PathSplitCopied = PathSplit1[1].split("\\\\");

                        newFile copied = search(PathSplitCopied);
                        /***Move one file to another location*/

                        String []PathSplitTarget = PathSplit1[2].split("\\\\");
                        newFile target = search(PathSplitTarget);

                        if(target.add(copied)&&command.substring(0,"move".length()).equals("move")){
                            deleteCatalog(PathSplitCopied);
                        }else{
                            content = content.concat("\n" + "Catalog or folder with that name already exist");
                        }
                    }


                }
                else if(CheckIf("cls")) {
                    content = "Microsoft Windows [Version 10.0.16299.371]" + "\n" + "(c) 2017 Microsoft Corporation. All rights reserved.\n";


                } else if(CheckIf("help")){
                    content = content + "\n  CLS - Clears the screen." +
                                        "\n  DIR - Displays a list of files and subdirectories in a directory.." +
                                        "\n  CD - Displays the name of or changes the current directory." +
                                        "\n  MD - Creates a directory." +
                                        "\n  FD - Creates a file." +
                                        "\n  COPY - Copies one file to another location." +
                                        "\n  MOVE - Moves one file from one directory to another directory.";


                } else {
                    content = content + "\n\'" + command + "\' " +  "is not recognized as an internal or external command, \n operable program or batch file.";
                }
                setTextAndPath();
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
    private newFile getFileByPathByCommend(String commandName){
        return  search(getSplitPathByCommend(commandName));

    }
    private String[] getSplitPathByCommend(String commandName){
        return getPathByCommend(commandName).split("\\\\");
    }
    private String getPathByCommend(String commandName){
        return command.substring(commandName.length()).replace(" ","");
    }
    private newFile search(String path[]){
        newFile temp = hardDisc;
        for(int i =0;i<path.length;i++){
            if(temp.getFileByPath(path[i])!=null){
                temp = temp.getFileByPath(path[i]);
            }else{
                return null;
            }
        }
        return temp;
    }
    private boolean createCatalogByPath(String path) {
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
                if(temp.add(catalog)) {
                    return true;
                }
            }
        }
        return false;
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
        hardDisc.getFileByPath("e:").add(new com.MateuszLebioda.File("1"));
        hardDisc.getFileByPath("e:").add(new com.MateuszLebioda.File("2"));
        hardDisc.getFileByPath("e:").add(new Catalog("1"));

        hardDisc.getFileByPath("c:").add(new Catalog("1"));
        hardDisc.getFileByPath("c:").getFileByPath("1").add(new com.MateuszLebioda.File("3"));
        hardDisc.getFileByPath("c:").getFileByPath("1").add(new com.MateuszLebioda.File("4"));
        hardDisc.getFileByPath("c:").getFileByPath("1").add(new Catalog("2"));
        hardDisc.getFileByPath("c:").getFileByPath("1").getFileByPath("2").add(new com.MateuszLebioda.File("1"));
        hardDisc.getFileByPath("c:").getFileByPath("1").getFileByPath("2").add(new Catalog("11"));
        hardDisc.getFileByPath("c:").add(new Catalog("2"));
        hardDisc.getFileByPath("c:").getFileByPath("2").add(new com.MateuszLebioda.File("22"));
        hardDisc.getFileByPath("c:").getFileByPath("2").add(new com.MateuszLebioda.File("22"));
    }

    private boolean deleteCatalog(String path[]){
        newFile temp = hardDisc;
        int i=0;
        for(;i<path.length-1;i++){
            if(temp.getFileByPath(path[i])!=null){
                temp = temp.getFileByPath(path[i]);
            }else{
                return false;
            }
        }
        return temp.delByPath(path[i]);
    }

    @Override
    public void keyReleased(KeyEvent e) {}
    @Override
    public void keyTyped(KeyEvent e) {}
}

