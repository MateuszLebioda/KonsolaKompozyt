package com.MateuszLebioda;

public class Catalog extends newFile {
    public Catalog(String path) {
        super(path);
        cat = true;
    }

    public newFile getCatalog(String path) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).path.equals(path)){
                return this;
            }
        }
        return null;
    }

    public String add(newFile file) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).path.equals(file.path))
                return "Nie mozna utworzyc katalogu. Katalog o takiej nazwie juz istnieje.";
        }
        list.add(file);
        return "Katalog zosta pomyslnie utworzony.";
    }

    public newFile getFileByPath(String path) {
        for(int i = 0; i < list.size(); i++) {
            if (list.get(i).path.equals(path))
                return list.get(i);
        }
        return null;
    }

    @Override
    public void delByPath(String path) {
        int x = 0;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).path.equals(path))
                x = i;
        }
        list.remove(x);
    }

    @Override
    public String draw(int paragraphSize) {
        String paragraph = "";
        paragraphSize++;
        for (int i = 0; i < paragraphSize; i++) {
            paragraph = paragraph + "   ";
        }
        paragraph = paragraph.concat("*" + this.path.concat("\n"));

        for (newFile list : list) {
            paragraph = paragraph.concat(list.draw(paragraphSize));
        }
        return paragraph;
    }
}
