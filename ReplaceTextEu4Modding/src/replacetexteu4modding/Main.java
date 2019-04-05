/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package replacetexteu4modding;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FilenameFilter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

/**
 *
 * @author Tonnes
 */
public class Main {
    
    String line;
    String line2;
    String oldie = ""; 
    String oldie2 = "";  
    String numberVar;
    
    int counter = 0;
    
    String[] lines;
    String[] files;
    File[] foundFiles;
    
    File directory;
    File filePath;
    BufferedReader buffReader;
    BufferedWriter buffWriter;
    FileWriter writer;
    
    static Main main;
    
    /**
     * Constructor
     */
    public Main() {
//       getOneFilePath();
//       createArrayFromFile();  
        theWholeProcess();
    }
    
    public void theWholeProcess() {
        createStringArrayFromNUMBERSFile();
        for (int i = 0; i < files.length; i++) {
            getOneFilePath("" + i);
            System.out.println(getOneFilePath("" + i));
        }
    }
    
    /**
     * 
     * @param numb
     * @return 
     */
    public File getOneFilePath(String numb) {
        directory = new File("C:\\Users\\Tonnes\\Documents\\Paradox Interactive\\Europa Universalis IV\\mod\\mapMod3\\history\\provinces");
        foundFiles = directory.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.startsWith(numb + " -");
            }
        });
        
        
        //System.out.println(foundFiles[i]);
        filePath = foundFiles[0];
        
        System.out.println(filePath);
        
        return filePath;
    }
    
    /**
     * 
     * @return 
     */
    public String[] createStringArrayFromNUMBERSFile() {
        try {
            buffReader = new BufferedReader(new FileReader("filesNumbers.txt"));
            
            while((line2 = buffReader.readLine()) != null) {
                oldie2 += line2 + "\r\n";
            }
            buffReader.close();
            
            files = oldie2.split("\\r?\\n");
            
        } catch (IOException e) {
            System.out.println(e);
        }
        
        return files;
    }
    
//    /**
//     * 
//     * @param filesArray
//     * @return 
//     */
//    public String getNumber(String[] filesArray) {
//        getOneFilePath(filesArray[counter]);
//        System.out.println(filesArray[counter]);
//        counter++;
//        return numberVar;
//    }
    
//    public File getPath() {
//        File path;
//        return path;
//    }
    
    /**
     * 
     * @return 
     */
    public String[] createArrayFromFile() {
//        filePath = getOneFilePath(getNumber(createStringArrayFromNUMBERSFile()));
//        filePath = getPath();
        try {
            buffReader = new BufferedReader(new FileReader(filePath));
            
            while((line = buffReader.readLine()) != null) {
                oldie += line + "\r\n";
            }
            buffReader.close();
            
            lines = oldie.split("\\r?\\n");
            
            for (int i = 0; i < lines.length; i++) {
                if (lines[i].contains("owner = ") && i < 12) {
                    lines[i] = "owner = DAN";
                }
                if (lines[i].contains("controller = ") && i < 12) {
                    lines[i] = "controller = DAN";
                }  
                //System.out.println(lines[i]); //CHANGE   ----------------------------
            }
            
            //writeToFile(lines, filePath);        

        } catch (IOException e) {
            System.out.println(e);
        }   
        return lines;
    }
    
    /**
     * 
     * @param lines
     * @param filepath 
     */
    public void writeToFile(String[] lines, File filepath) {
        try {
            buffWriter = new BufferedWriter(writer = new FileWriter(filepath));
            for (int i = 0; i < lines.length; i++) {               
                buffWriter.write(lines[i]);   
                buffWriter.newLine();
            }
            buffWriter.flush();
            buffWriter.close();  
        } catch (IOException e) {
            System.out.println(e);
        }
    }
    
    /**
     * 
     * @param args 
     */
    public static void main(String[] args) {
        main = new Main();
    }

}
