package com.MateuszLebioda;

public class Catalog extends newFile {
    public Catalog(String path) {
        super(path);
        cat = true;
    }

    public boolean add(newFile file) {
        for (newFile aList : list) {
            if (aList.path.equals(file.path))
                return false;
        }
        list.add(file);
        return true;
    }

    public newFile getFileByPath(String path) {
        for (newFile aList : list) {
            if (aList.path.equals(path))
                return aList;
        }
        return null;
    }

    @Override
    public boolean delByPath(String path) {
        int x = 0;
        boolean exist = false;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).path.equals(path)) {
                x = i;
                exist = true;
            }
        }
        if(exist){
            list.remove(x);
            return true;
        }return false;
    }

    @Override
    public String draw(int paragraphSize) {
        String paragraph = "\n";
        paragraphSize++;
        for (int i = 0; i < paragraphSize; i++) {
            paragraph = paragraph + "   ";
        }
        paragraph = paragraph.concat("*" + this.path);

        for (newFile list : list) {
            paragraph = paragraph.concat(list.draw(paragraphSize));
        }
        return paragraph;
    }
}
