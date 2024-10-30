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

    public void mv(String... paths) {
        if (paths.length < 2) {
            System.out.println("error, mv command requires at least two arguments");
            return;
        }

        File dest= new File(paths[paths.length - 1]);
        if (paths.length>2 &&(!dest.exists() || !dest.isDirectory())) {
            System.out.println("error, last argument must be an existing directory when moving multiple files");
            return;
        }

        boolean force = false;
        if (paths[0].equals("-f")||paths[0].equals("--force")) {
            force= true;
            paths= Arrays.copyOfRange(paths, 1, paths.length);
        }
        for(int i=0; i<paths.length-1; i++) {
            File source_dirc = new File(paths[i]);
            if (!source_dirc.exists()){
                System.out.println("error,source file or directory doesn't exist: " + source_dirc.getAbsolutePath());
                continue;
            }

            File target=(dest.isDirectory())? new File(dest, source_dirc.getName()):dest;
            if (target.exists()) {
                if (!target.canWrite() && !force){
                    System.out.print("File " + target.getName() + " is not writable. Overwrite? (y/n): ");
                    String response = scanner.nextLine().trim();
                    if (!response.equalsIgnoreCase("y")){
                        System.out.println("skipped: " + source_dirc.getAbsolutePath());
                        continue;
                    }
                }
                else if(!force){
                    System.out.print("File " + target.getName() +" already exists. Overwrite? (y/n): ");
                    String response = scanner.nextLine().trim();
                    if (!response.equalsIgnoreCase("y")) {
                        System.out.println("skipped: " +source_dirc.getAbsolutePath());
                        continue;
                    }
                }
            }
            if (target.exists()) {
                if (!target.delete()) {
                    System.out.println("error: Failed to delete unwritable file " + target.getAbsolutePath());
                    continue;
                }
            }

            try {
                Files.move(source_dirc.toPath(), target.toPath(), StandardCopyOption.REPLACE_EXISTING);
                System.out.println("Moved " + source_dirc.getAbsolutePath() + " to " + target.getAbsolutePath());
            } catch (IOException e) {
                System.out.println("Failed to move " + source_dirc.getAbsolutePath() + ": " + e.getMessage());
            }
        }
    }


    public void cd(String current_dir, String argu) throws IOException {
        String Dircectory = "";
        if (argu.equals("..")) {
            int index = current_dir.lastIndexOf('\\');
            if (index > 0) {
                Dircectory = current_dir.substring(0, index);

            } else {
                System.out.println("there is something wrong");
            }
            Main.curren_dir = Dircectory;
        } else {
            if (argu.contains("/")) {
                argu = argu.replace('/', '\\');
            }
            Dircectory = current_dir + '\\' + argu;
            File dir = new File(Dircectory);
            if (dir.isDirectory()) {
                Main.curren_dir = Dircectory;
            } else {
                System.out.println("NO dirctoy with this name");
            }

        }

    }


    public void appendRedirect(String s) {
        String[] splited_command = s.split(" ");
        String[] result = new String[0];
        boolean flag = false;
        if (splited_command.length < 3) {
            System.out.println("write : <command> >> <file>");

        } else if (splited_command[0].equals("cat")) {
            if(!splited_command[1].equals(">>")){
                result = cat(splited_command[1]).toArray(String[]::new);
            }
            else {
                result = cat().toArray(String[]::new);
            }
        } else if (splited_command[0].equals("ls")) {
            if(!splited_command[1].equals(">>")){
                result = ls(splited_command[1],"");
            }
            else {
                result = ls("","");
            }
        } else if (splited_command[0].equals("echo")) {
            if (splited_command.length != 4) {
                flag = true;
            }
            String text = splited_command[1];
            if (!text.equals(">>") && splited_command[2].equals(">>")) {
                result = new String[]{text};
            } else {
                System.out.println("write : echo text >> <file>");
            }

        }
        if (result.length > 0) {
            File f = new File(splited_command[splited_command.length - 1]);
            if (!f.isAbsolute()) {
                f = new File(Main.curren_dir, splited_command[splited_command.length - 1]);
            }
            if (f.isDirectory()) {
                System.out.println("Error: Cannot write to directory '" + f.getName() + "'");
                return;
            }
            if (!f.exists()) {
                if (flag) {
                    System.out.println("write : echo text >> <file>");
                } else {
                    System.out.println("No file with this name exists");
                }
            } else {
                FileWriter fw = null;
                try {
                    fw = new FileWriter(f , true);

                    for (String string : result) {
                        fw.write(string);
                        fw.write('\n');
                    }
                    fw.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        } else {
            System.out.println("wrong command");
        }
    }

    public void redirect(String s) {
        String[] splited_command = s.split(" ");
        String[] result = new String[0];
        boolean flag = false;
        if (splited_command.length < 3) {
            System.out.println("write : <command> > <file>");

        } else if (splited_command[0].equals("cat")) {
            if(!splited_command[1].equals(">")){
                 result = cat(splited_command[1]).toArray(String[]::new);
            }
            else {
                result = cat().toArray(String[]::new);
            }
            } else if (splited_command[0].equals("ls")) {
            if(!splited_command[1].equals(">")){
                result = ls(splited_command[1],"");
            }
            else {
                result = ls("","");
            }
        } else if (splited_command[0].equals("echo")) {
            if (splited_command.length != 4) {
                flag = true;
            }
            String text = splited_command[1];
            if (!text.equals(">") && splited_command[2].equals(">")) {
                result = new String[]{text};
            } else {
                System.out.println("write : echo text > <file>");
            }

        }
        if (result.length > 0) {
            File f = new File(splited_command[splited_command.length - 1]);
            if (!f.isAbsolute()) {
                f = new File(Main.curren_dir, splited_command[splited_command.length - 1]);
            }
            if (f.isDirectory()) {
                System.out.println("Error: Cannot write to directory '" + f.getName() + "'");
                return;
            }
            if (flag) {
                System.out.println("write : echo text > <file>");
            } else {
                FileWriter fw = null;
                try {
                    fw = new FileWriter(f);

                    for (String string : result) {
                        fw.write(string);
                        fw.write('\n');
                    }
                    fw.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        } else {
            System.out.println("wrong command");
        }
    }
/////////////
    public String[] sort(String name) {
        String[] conntent = cat(name).toArray(String[]::new);
        Arrays.sort(conntent);
        return conntent;
    }

    public String[] uniq(String[] arr) {
        String[] uniq_array;
        uniq_array = Arrays.stream(arr)
                .distinct()
                .toArray(String[]::new);

        return uniq_array;
    }

    public String[] grep(String[] arr, String key) {
        List<String> result = new ArrayList<>();
        for (String elemnt : arr) {
            if (elemnt.contains(key)) {
                result.add(elemnt);
            }
        }
        return result.toArray(new String[0]);
    }


    public String[] ls(String specification, String path) {
        if (path.isEmpty()) {
            path = Main.curren_dir;
        }
        File currentDirectory = new File(path);
        if (!currentDirectory.exists()) {
            System.out.println("current directory not exist");
        }
        if (!currentDirectory.isDirectory()) {
            System.out.println("current path is not directory");
        }
        File[] files = null;
        List<String> visibleFiles = new ArrayList<>();
        if (currentDirectory.exists() && currentDirectory.isDirectory()) {
            //System.out.println("the content of the current directory: "+currentDirectory);
            files = currentDirectory.listFiles();
            if (files != null) {
                if (specification.equals("-r")) {
                    for (int i = files.length - 1; i >= 0; i--) {
                        if (files[i].getName().startsWith(".")) continue;
                        // System.out.println(files[i].getName());
                        visibleFiles.add(files[i].getName());
                    }
                } else {
                    for (File x : files) {
                        if (x.getName().startsWith(".") && !specification.equals("-a")) continue;
                        //  System.out.println(x.getName());
                        visibleFiles.add(x.getName());
                    }
                }

            }
        }
        return visibleFiles.toArray(new String[0]);
    }

    public String touch(String path) {
        File file;
        if (Paths.get(path).getParent() == null) {
            file = new File(Main.curren_dir, path);
        } else {
            file = new File(path);
        }
        try {
            Path parentPath = file.toPath().getParent();
            if (parentPath != null || Files.notExists(parentPath)) {

                System.out.println("Parent directory not exist");
            }
            if (file.exists()) {
                file.setLastModified(System.currentTimeMillis());
                System.out.println("file exists");
            } else {
                Files.createFile(file.toPath());
                System.out.println("file created");
            }

        } catch (IOException x) {
            System.out.println("error happening while creating new file " + x);
        }
        return file.getName();
    }

    //////////////////////////
    public void rm(String... filenames) {
        for (String filename : filenames) {
            File file = new File(filename);
            if (!file.isAbsolute()) {
                file = new File(Main.curren_dir, filename);
            }

            if (file.exists()) {
                if (file.isDirectory()) {
                    System.out.println("Cannot remove '" + filename + "': Is a directory");
                } else if (file.isFile()) {
                    if (file.delete()) {
                        System.out.println("Deleted file: " + filename);
                    } else {
                        System.out.println("Failed to delete file: " + filename);
                    }
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
                   // System.out.println(line);
                    content.add(line.trim());
                }
            } catch (NoSuchElementException e) {
                System.out.println("There is no line founded,input process ended");
            }
        } else {
            for (String filename : filenames) {
                File file = new File(filename);
                if (!file.isAbsolute()) {
                    file = new File(Main.curren_dir, filename);
                }

                if (file.exists() && file.isFile()) {
                    try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                        String line;
                        while ((line = reader.readLine()) != null) {
                            //System.out.println(line);
                            content.add(line.trim());
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

    public void rmdir(String... dirs) {
        for (String dirPath : dirs) {

            File dir = new File(dirPath);
            if (!dir.isAbsolute()) {
                dir = new File(Main.curren_dir, dirPath);
            }

            if (dir.exists() && dir.isDirectory()) {
                File[] contents = dir.listFiles();
                if (contents != null && contents.length == 0) {
                    if (dir.delete()) {
                        System.out.println("Deleted directory: " + dir.getPath());
                    } else {
                        System.out.println("Failed to delete directory: " + dir.getPath());
                    }
                } else {
                    System.out.println("Directory is not empty or cannot be deleted: " + dir.getPath());
                }
            } else {
                System.out.println("Directory not found or not a directory: " + dir.getPath());
            }
        }
    }


    public void help() {
        System.out.println("Available commands & Their usage:");
        System.out.println("  pwd             -> Prints the working directory");
        System.out.println("  cd <dir>        -> Changes the current directory to <dir>");
        System.out.println("  ls              -> List files in the current or specified directory");
        System.out.println("  ls -a           -> Display all contents, even entries starting with .");
        System.out.println("  ls -r           -> List contents in reverse order");
        System.out.println("  mkdir <dir>     -> Create a new directory named <dir>");
        System.out.println("  rmdir <dir>     -> Remove an empty directory named <dir>");
        System.out.println("  touch <file>    -> Create a new file or update the timestamp of <file>");
        System.out.println("  mv <src> <dest> -> Move or rename a file or directory from <src> to <dest>");
        System.out.println("  rm <file>       -> Remove one or more files");
        System.out.println("  cat <file>      -> Display the contents of <file>");
        System.out.println("  > <file>        -> Redirect output to <file> (overwrites)");
        System.out.println("  >> <file>       -> Append output to <file>");
        System.out.println("  | <command>     -> Pipe the output of one command to another");
        System.out.println("  help            -> Show available commands and usage");
        System.out.println("  exit            -> Exit the CLI");
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
    public String [] pipe(String commnd){
        String[] command_line = Arrays.stream(commnd.split("\\|"))
                .map(String::trim) // Trim each substring
                .toArray(String[]::new);

        String[] result = new String[0];
        int current_c = 0 ;

        while (current_c<command_line.length){
            String[] command = command_line[current_c].split(" ");

            if(command[0].equals("ls")){

                String specification="";
                String path="";
                if(command.length>=2) {
                    if (command[1].equalsIgnoreCase("-a") || command[1].equalsIgnoreCase("-r")) {
                        specification = command[1];
                        if (command.length == 3) path = command[2];
                    } else if (command.length == 2) path = command[1];
                }

                result = ls(specification,path);
            }
            else if (command[0].equalsIgnoreCase("sort")){
                if(command.length!=2){
                    System.out.println("write: sort <<fileName>>");
                    result = new String[0];
                }
                else {
                    File f = new File(command[1]);
                    if(f.exists()){
                        result = sort(command[1]);
                    }
                    else{
                        System.out.println("no file exists with this name");
                    }
                }
            }
            else if(command[0].equalsIgnoreCase("uniq")  && result.length > 0){
                if(command.length>1){
                    System.out.println("use command wrong");
                    result = new String[0];
                }
                else {
                    result = uniq(result);
                }
            }
            else if(command[0].equals("grep")) {
                if(command.length!= 2 && result.length == 0 ){
                    System.out.println("wrong command");
                    result = new String[0];
                } else {
                    result = grep(result , command[1]);
                }
            }
            else if(command[0].equals("more")){
                if(current_c == 0 || command.length>1){
                    System.out.println("wrong command");
                    result = new String[0];
                }

            }
            else if(command[0].equals("cat")){
                if (command.length == 1) {
                    result = cat().toArray(String[]::new);
                } else {
                    String[] filenames = Arrays.copyOfRange(command, 1, command.length);
                    result =  cat(filenames).toArray(String[]::new);
                }

            }

            else if(command[0].equalsIgnoreCase("pwd")){
                System.out.println("Current Directory: "+ pwd());
                break;
            }
            else{
                System.out.println("wrong command");
            }
            current_c++;

        }

        return result;
    }

    }