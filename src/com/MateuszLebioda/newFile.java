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

    public boolean add(newFile file){return false;}
    public boolean delByPath(String path){return  false;}
    public newFile getFileByPath(String path){return null;}


}
