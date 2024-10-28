package org.example;
import java.io.*;
import java.util.*;
import java.nio.file.*;

public class CommandLineInterpreter {
    public void cd(String current_dir , String argu) {
        String Dircectory = "";
        if(argu.equals("..")) {
            int index =current_dir.lastIndexOf('\\');
            if(index > 0) {
                Dircectory = current_dir.substring(0,index);

            }
            else{
                System.out.println("there is something wrong");
            }
            Main.curren_dir = Dircectory ;
        }
        else if (current_dir.lastIndexOf("\\") == -1){
            Dircectory = current_dir+'\\'+argu ;
            File dir = new File(Dircectory);
            if(dir.isDirectory()){
                Main.curren_dir= Dircectory ;
            }
            else{
                System.out.println("NO dirctoy with this name");
            }

        }

    }
    public String[] ls(String  specification, String path){
        if(path.isEmpty()) {
            path = Main.curren_dir;
        }
        File currentDirectory = new File(path);
        if(!currentDirectory.exists()){
            System.out.println("current directory not exist");
        } if(!currentDirectory.isDirectory()){
            System.out.println("current path is not directory");
        }
        File[] files =null;
        List<String> visibleFiles = new ArrayList<>();
        if(currentDirectory.exists()&&currentDirectory.isDirectory()){
            System.out.println("the content of the current directory: "+currentDirectory);
            files=currentDirectory.listFiles();
            if(files!= null){
                if(specification.equals("-r")){
                    for(int i= files.length-1; i>=0 ;i--) {
                        if(files[i].getName().startsWith(".")) continue;
                        System.out.println(files[i].getName());
                        visibleFiles.add(files[i].getName());
                    }
                }
                else{
                    for(File  x: files){
                        if(x.getName().startsWith(".")&&!specification.equals("-a")) continue;
                        System.out.println(x.getName());
                        visibleFiles.add(x.getName());
                    }
                }

            }
        }
        return visibleFiles.toArray(new String[0]);
    }
    public String touch(String path){
        File file ;
        if(Paths.get(path).getParent()==null){
            file = new File(Main.curren_dir,path);
        }else{
            file = new File(path);
        }
        try {
            Path parentPath= file.toPath().getParent();
            if(parentPath!=null||Files.notExists(parentPath)){
                Files.createDirectories(parentPath);
                System.out.println("Parent directory exists: " + parentPath);
            }
            if(file.exists()){
                file.setLastModified(System.currentTimeMillis());
                System.out.println("file exists");
            }else{
                Files.createFile(file.toPath());
                System.out.println("file created");
            }

        }catch(IOException x){
            System.out.println("error happening while creating new file "+ x);
        }
        return file.getName();}
}
