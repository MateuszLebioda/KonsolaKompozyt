package com.MateuszLebioda;

import java.util.ArrayList;

public abstract class newFile {
    newFile(String path){
        this.path = path;
    }
    public String path;
    public boolean cat;

    public abstract String draw(int i);


    public ArrayList<newFile> list = new ArrayList<>();

    public String add(newFile file){return null;}
    public void delByPath(String path){};
    public newFile getFileByPath(String path){return null;}


}
