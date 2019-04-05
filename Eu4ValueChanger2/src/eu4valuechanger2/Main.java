/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package eu4valuechanger2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Arrays;

/**
 *
 * @author Tonnes
 */
public class Main {
    
    String oldie = "";
    String line;
    String oldie2 = "";
    String line2;
    
    //DAN, BRA, NOV, ETH, TUN, TUR, PAP, AZT, SWE, NOR, LIT, MAM, FRA, PRU, MUG
    /**
     * DAN - Dragon Island
     * BRA - North Pole
     * NOV - Islands under "edge" continent
     * ETH - Spiral Islands
     * TUN - Islands in the middle of middle continent
     * TUR - South-East part of middle continent
     * PAP - Left side of edge continent (in-game)
     * AZT - Right side of edge continent (in-game)
     * SWE - Smaller Islands at the east of middle continent
     * NOR - Northern part of middle continent
     * LIT - Western part of middle continent
     * MAM - South-Eastern part of large continent
     * FRA - South-Western part of large continent
     * PRU - North-Western part of large contientn
     * MUG - North-Eastern part of large continent
     * 
     */
    String countryTAG = "DAN";
    String replaceOwner = "owner = ";
    String replaceController = "controller = ";
    String replaceAdd_Core = "add_core = ";
    
    int counter = 0;
    
    String[] files;
    String[] lines;
    String[] linesLonger;
    File[] foundFiles;
    
    File directory;
    File filePath;
    
    BufferedReader buffReader;
    BufferedWriter buffWriter;
    FileWriter writer;
    
    static Main main;
    
    public Main() {  
        createStringArrayFromNUMBERSFile();
        theProcess();
    } 
    
    /**
     * THE PROCESS
     */
    public void theProcess() {       
        while (counter < files.length) {       
            File filePath = getOneFilePath(getNextNumber());
            writeToFile(createArrayFromFile(filePath), filePath);
                       
            System.out.println("Counter: " + counter);
            System.out.println("FilePath: " + filePath);
            System.out.println("lines length: " + lines.length);
        }
    }

    /**
     * Creating a array of numbers of files to change.
     * 
     */
    public void createStringArrayFromNUMBERSFile() {
        oldie2 = "";
        line2 = "";
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
    }    
    
    public String getNextNumber() {
        String number = this.files[counter];
        this.counter++;
        
        return number;
    }
    
    /**
     * Getting one path to a file dependant on the files array.
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
        
        filePath = foundFiles[0];       
        
        return filePath;
    }    
    
    
    /**
     * Getting a path and converting the file to a string array.
     * @param file
     * @return 
     */
    public String[] createArrayFromFile(File file) {
        oldie = "";
        line = "";
        try {
            buffReader = new BufferedReader(new FileReader(file));
            
            while((line = buffReader.readLine()) != null) {
                oldie += line + "\r\n";
            }
            buffReader.close();
            
            lines = oldie.split("\\r?\\n");
            
            linesLonger = Arrays.copyOf(lines, lines.length+3);
            
            linesLonger[linesLonger.length-1] = "";
            linesLonger[linesLonger.length-2] = "";
            linesLonger[linesLonger.length-3] = "";
            
            for (int i = 0; i < linesLonger.length; i++) {
                if (lines.length > 2) {
                    if (linesLonger[i].contains(replaceOwner)) {
                        linesLonger[i] = replaceOwner + countryTAG;
                    } else if (!linesLonger[i].contains(replaceOwner) && i < 12 && !lines[lines.length-1].contains("owner = ")) {
                        linesLonger[linesLonger.length-1] = replaceOwner + countryTAG;
                    }

                    if (linesLonger[i].contains(replaceController)/* && i < 18*/) {
                        linesLonger[i] = replaceController + countryTAG;
                    } else if (!linesLonger[i].contains(replaceController) && i < 12 && !lines[lines.length-2].contains("controller = ")) {
                        linesLonger[linesLonger.length-2] = replaceController + countryTAG;
                    }

                    if (linesLonger[i].contains(replaceAdd_Core)) {
                        linesLonger[i] = replaceAdd_Core + countryTAG;
                    } else if (!linesLonger[i].contains(replaceAdd_Core) && i < 12 && !lines[lines.length-3].contains("add_core = ")) {
                        linesLonger[linesLonger.length-3] = replaceAdd_Core + countryTAG;
                    }
                } else {
                    System.out.println("Probably a sea tile!");
                }
            }
            writeToFile(linesLonger, filePath);

        } catch (IOException e) {
            System.out.println(e);
        }
        return linesLonger;
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
    
    public static void main(String[] args) {
        main = new Main();
    }
}