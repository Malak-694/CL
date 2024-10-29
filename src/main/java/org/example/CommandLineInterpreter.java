package org.example;
import java.io.*;
import java.util.*;
import java.nio.file.*;

public class CommandLineInterpreter {
    Scanner scanner = new Scanner(System.in);

    public String pwd(){
        return Main.curren_dir;
    }
    public String mkdir(String DircPath){
        File Directory;
        if(Paths.get(DircPath).isAbsolute()){
            Directory=new File(DircPath);
        }
        else{
            Directory=new File(Main.curren_dir,DircPath);
        }
        if(Directory.exists()){
            System.out.println("directory already exists: "+Directory.getAbsolutePath());
        }
        else{
            if (Directory.mkdirs()) {
                System.out.println("directory is created: "+Directory.getAbsolutePath());
            } else {
                System.out.println("failed to create directory at: " +Directory.getAbsolutePath());
            }
        }
        return Directory.getName();


    }
    public void mv(String... paths){
        if (paths.length < 2) {
            System.out.println("error, mv command requires at least two arguments");
            return;
        }

        File dest = new File(paths[paths.length-1]);
        if (paths.length > 2 && (!dest.exists() || !dest.isDirectory())) {
            System.out.println("error, last argument must be an existing directory when moving multiple files.");
            return;
        }
        boolean force = false;
        if (paths[0].equals("-f")||paths[0].equals("--force")){
            force = true;
            paths = Arrays.copyOfRange(paths, 1, paths.length);
        }
        for(int i=0; i<paths.length-1; i++) {
            File source_dirc = new File(paths[i]);
            if (!source_dirc.exists()) {
                System.out.println("error, source file or directory doesn't exist: "+source_dirc.getAbsolutePath());
                continue;
            }

            File target=(dest.isDirectory())? new File(dest, source_dirc.getName()): dest;

            if (source_dirc.isDirectory() && !target.isDirectory()) {
                System.out.println("error, can't move a directory to a non-directory target");
                continue;
            }
            if (target.exists()){
                if (!target.canWrite() && !force){
                    System.out.print("File " +target.getName() +" is not writable. Overwrite? (y/n): ");
                    String response = scanner.nextLine().trim();
                    if (!response.equalsIgnoreCase("y")) {
                        System.out.println("skipped: " + source_dirc.getAbsolutePath());
                        continue;
                    }
                }
                else if(!force) {
                    System.out.print("file " +target.getName() + "already exists. Overwrite? (y/n): ");
                    String response = scanner.nextLine().trim();
                    if(!response.equalsIgnoreCase("y")) {
                        System.out.println("Skipped: " + source_dirc.getAbsolutePath());
                        continue;
                    }
                }
            }
            if (target.exists() && !target.canWrite()){
                if(!target.delete()){
                    System.out.println("error, failed to delete unwritable file "+ target.getAbsolutePath());
                    continue;
                }
            }

            if (!source_dirc.isFile() && !source_dirc.getAbsolutePath().equals(target.getAbsolutePath())){
                System.out.println("error, only regular files can be moved across file systems.");
                continue;
            }
            if (source_dirc.renameTo(target)){
                System.out.println("moved "+source_dirc.getAbsolutePath() + " to " + target.getAbsolutePath());
            }
            else{
                System.out.println("failed to move " +source_dirc.getAbsolutePath()+ " to " +target.getAbsolutePath());
            }
        }
    }

    public void cd(String current_dir , String argu) throws IOException {
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
        else {
            if(argu.contains("/")){
                argu = argu.replace('/' , '\\') ;
            }
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


    public void appendRedirect(String s ){
        String[] splited_command = s.split(" ") ;
        String[] result = new String[0];
        boolean flag = false;
        if(splited_command.length <3){
            System.out.println("write : <command> >> <file>");

        }
        else if (splited_command[0].equals("cat")){
            System.out.println("write : <command> >> <file>");
        }
        else if (splited_command[0].equals("ls")){
            result = ls("", "");
        }else if (splited_command[0].equals("echo")){
            if (splited_command.length != 4){
                flag = true ;
            }
            String text = splited_command[1];
            if(!text.equals(">>") && splited_command[2].equals(">>")){
                result = new String[]{text};
            }
            else {
                System.out.println("write : echo text >> <file>");
            }

        }
        if(result.length > 0){
            File f = new File(splited_command[splited_command.length-1]);
            if(!f.exists()) {
                if (flag == true) {
                    System.out.println("write : echo text >> <file>");
                } else {
                    System.out.println("No file with this name exists");
                }
            }
            else {
                FileWriter fw = null ;
                try {
                    fw = new FileWriter(splited_command[splited_command.length-1] , true);

                    for (String string : result) {
                        fw.write(string);
                        fw.write('\n');
                    }
                    fw.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }else {
            System.out.println("wrong command");
        }
    }

    public void redirect(String s ){
        String[] splited_command = s.split(" ") ;
        String[] result = new String[0];
        boolean flag = false;
        if(splited_command.length <3){
            System.out.println("write : <command> > <file>");

        }
        else if (splited_command[0].equals("cat")){
            System.out.println("write : <command> > <file>");
        }
        else if (splited_command[0].equals("ls")){
            result = ls("", "");
        }else if (splited_command[0].equals("echo")){
            if (splited_command.length != 4){
                flag = true ;
            }
            String text = splited_command[1];
            if(!text.equals(">>") && splited_command[2].equals(">")){
                result = new String[]{text};
            }
            else {
                System.out.println("write : echo text > <file>");
            }

        }
        if(result.length > 0){
            File f = new File(splited_command[splited_command.length-1]);
            if (flag == true) {
                System.out.println("write : echo text >> <file>");
            }
            else {
                FileWriter fw = null ;
                try {
                    fw = new FileWriter(splited_command[splited_command.length-1] , true);

                    for (String string : result) {
                        fw.write(string);
                        fw.write('\n');
                    }
                    fw.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }else {
            System.out.println("wrong command");
        }
    }

    public String[] sort( String name  ){
        String[] conntent = ls("",name);
        Arrays.sort(conntent);
        return conntent ;
    }

    public  String[] uniq( String[] arr){
        String[] uniq_array = Arrays.stream(arr)
                .distinct()
                .toArray(String[]::new);

        return uniq_array ;
    }

    public String[] grep(String[] arr , String key){
        List<String> result = new ArrayList<>();
        for(String elemnt : arr ){
            if(elemnt.contains(key)){
                result.add(elemnt );
            }
        }
        return result.toArray(new String[0]) ;
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
            //System.out.println("the content of the current directory: "+currentDirectory);
            files=currentDirectory.listFiles();
            if(files!= null){
                if(specification.equals("-r")){
                    for(int i= files.length-1; i>=0 ;i--) {
                        if(files[i].getName().startsWith(".")) continue;
                        // System.out.println(files[i].getName());
                        visibleFiles.add(files[i].getName());
                    }
                }
                else{
                    for(File  x: files){
                        if(x.getName().startsWith(".")&&!specification.equals("-a")) continue;
                        //  System.out.println(x.getName());
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
                //System.out.println("Parent directory exists: " + parentPath);
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
    //////////////////////////
    public void rm(String... filenames) {
        for (String filename : filenames) {
            File file = new File(filename);

            if (file.exists() && file.isFile()) {
                if (file.delete()) {
                    System.out.println("Deleted file: " + filename);
                } else {
                    System.out.println("Failed to delete file: " + filename);
                }
            } else {
                System.out.println("File not found or not a file: " + filename);
            }
        }
    }

    public List<String> cat(String... filenames) {
        List<String> content = new ArrayList<>();

        if (filenames.length == 0) {
            System.out.println("Enter text... (type 'done' to finish the input process):");
            Scanner scanner = new Scanner(System.in);
            try {
                String line;
                while (scanner.hasNextLine() && !(line = scanner.nextLine()).equalsIgnoreCase("done")) {
                    System.out.println(line);
                    content.add(line);
                }
            } catch (NoSuchElementException e) {
                System.out.println("There is no line founded,input process ended");
            }
        } else {
            for (String filename : filenames) {
                File file = new File(filename);
                if (file.exists() && file.isFile()) {
                    try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                        String line;
                        while ((line = reader.readLine()) != null) {
                            System.out.println(line);
                            content.add(line);
                        }
                    } catch (IOException e) {
                        System.out.println("Error reading file");
                    }
                } else {
                    System.out.println("File not found or it is not a file");
                }
            }
        }
        return content;
    }

    public  void help() {
        System.out.println("Available commands & Their usage:");
        System.out.println("  pwd        -> Prints the working directory");
        System.out.println("  mkdir      -> Creates a directory with each given name");
        System.out.println("  mv         -> Moves one or more files/directories to a directory.");
        System.out.println("  cd         -> Changes the current directory");
        System.out.println("  ls –a      -> display all contents even entries starting with .");
        System.out.println("  ls –r      -> reverse order)");
        System.out.println("  rm         -> Remove one or more files");
        System.out.println("  cat        -> Display the contents of a file");
        System.out.println("  rmdir      -> Remove a directory");
        System.out.println("  ls         -> List files in the current or specified directory");
        System.out.println("  help       -> Show available commands");
        System.out.println("  exit       -> Exit the CLI");
    }
}


