package com.MateuszLebioda;

public class File extends newFile {
    public File(String path){
        super(path + ".f");
        cat = false;
    }

    @Override
    public String draw(int paragraphSize) {
        paragraphSize++;
        String paragraph = "\n";
        for(int i=0;i<paragraphSize;i++){
            paragraph = paragraph + "   ";
        }
        return paragraph + "-" + this.path;
    }
}
