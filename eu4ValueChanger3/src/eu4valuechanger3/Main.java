/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package eu4valuechanger3;

import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

/**
 *
 * @author Tonnes
 */
public class Main {
    
    String provinceFileString = "";
    String provinceFileLineString;
    String fileNumberString = "";
    String fileNumberLineString;
    String tradeGoodString = "";
    String tradeGoodLineString;
    
    String trade_good = "";
    
    String fileName;
    String foundButLost;
    
    int counter = 0;
    int notFoundProv = 0;
    int randomNumb;
    
    boolean isWater = false;
    
    /**
     * --------------------------TEMPLATE---------------------
     * 
     * TAGS:
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
        
     * TEMPLATE:
     * # comment0
     * add_core = core0 (string)
     * owner = owner0 (string)
     * controller = control0 (string)
     * 
     * culture = culture0 (string)
     * religion = religion0 (string)
     * hre = hre0 (yes, no)
     * trade_goods = tradeGood0 (string)
     * capital = "name0" (string)
     * is_city = city0 (yes, no)
     * 
     * base_tax = tax0 (integer)
     * base_production = prod0 (integer)
     * base_manpower = manp0 (integer)
     * 
     * estate = estate0 (string)
     * 
     * discovered_by = discovered0 (string)...
     * 
     * --------------------TEMPLATE END----------------------
     */
    
    String[] templateInfo; //6 (tag, culture, relgion, tradeGood, capital, estate)
    String[] tradegoods; //Eu4 Tradegoods
    int[] development; //3 (tax, production, manpower)
    boolean[] discoveredBy; //14 (Different regions)       
    
    String[] files;
    String[] lines;
    String[] newArray;
    File[] foundFiles;
    boolean[] sectionsToFill;
    
    File directory;
    File filePath;
    File templateFile;
    
    BufferedReader buffReader;
    BufferedWriter buffWriter;
    FileWriter writer;
    Random random;
    
    static Main main;
    
    public Main() {  
        //getTradeGood();
        createStringArrayFromNUMBERSFile();
        theProcess();
    } 
    
    public boolean[] getSections() {
        //Edit after need ->
        
        sectionsToFill = new boolean[] {
            false,   //comment 0
            false,   //add_core 1
            false,   //owner 2
            false,   //controller 3
            false,   //culture 4
            false,   //religion 5
            false,   //hre 6
            false,  //yes-no (hre) -- 7
            false,   //trade_good 8
            false,   //capital 9
            false,   //is_city 10
            false,   //yes-no (is_city) -- 11
            false,   //base_development (tax, prod, manp) 12
            false,   //random development -- 13 
            false,  //estate 14
            true,   //discovered_by 15
            false,   //random trade good 16
            false,  //add permanent modifier to province 17 (NOT CREATED)
            false,  //add history to province 18 (NOT CREATED)
            false   //Random religion 19 (NOT CREATED)    
        };
        
        return sectionsToFill;
    }
    
    public boolean[] getDiscoveredBy() {
        discoveredBy = new boolean[] {
            true, //eastern - 0
            true, //western
            false, //muslim
            false, //ottoman
            false, //indian
            false, //nomad_group
            false, //chinese - 6
            false, //east_african
            false, //west_african
            false, //central_african
            false, //north_american
            false, //mesoamerican
            false, //south_american
            false  //andean - 13
        };
        return discoveredBy;
    }
    
    public String[] getArrayInformation() {
        templateInfo = new String[] {
            "NOR",           //Country tag
            "norwegian",     //Province culture
            "catholic",         //Province religion
            "grain",         //Province trade good
            "estate_nobles"  //Esate in province
        };
        
        return templateInfo;
    }
    
    public int[] getDevelopment() {
        development = new int[] {
            1, //Tax
            1, //Production
            1, //Manpower
            1, //Min random
            6 //Max random
        };
        
        return development;
    }
    
    public String getRandomTradeGood(String[] tradegoods) {
        random = new Random();
        int rand = random.nextInt(tradegoods.length);
        
        return tradegoods[rand];
    }
    
    public String getTradeGood() {
        tradeGoodString = "";
        tradeGoodLineString = "";
        try {
            buffReader = new BufferedReader(new FileReader("tradegoods.txt"));
            
            while((tradeGoodLineString = buffReader.readLine()) != null) {
                tradeGoodString += tradeGoodLineString + "\r\n";
            }
            buffReader.close();
            
            tradegoods = tradeGoodString.replace(" ", "").split("\\r?\\n");
            
        } catch (IOException e) {
            System.out.println(e);
        }
        
        trade_good = getRandomTradeGood(tradegoods);
        
        return trade_good;
    }
    
    /**
     * THE PROCESS
     */
    public void theProcess() {       
        while (counter < files.length) {  
            filePath = getOneFilePath(getNextNumber());
            
            replaceContent(filePath, counter);
            
            System.out.println("Counter: " + counter);
            if (isWater) {
                System.out.println("This is most likely a water province!");
            }
            System.out.println("\n");
        }
        //System.out.println("Could not find these " + this.notFoundProv + " files: " + this.foundButLost);
    }
    
    /**
     * Creating a array of numbers of files to change.
     * 
     * @param file
     * @return 
     */
    public String[] createStringArrayFromNUMBERSFile() {
        fileNumberString = "";
        fileNumberLineString = "";
        try {
            buffReader = new BufferedReader(new FileReader("filesNumbers.txt"));
            
            while((fileNumberLineString = buffReader.readLine()) != null) {
                fileNumberString += fileNumberLineString + "\r\n";
            }
            buffReader.close();
            
            files = fileNumberString.replace(" ", "").split("\\r?\\n");
            
        } catch (IOException e) {
            System.out.println(e);
        }
        
        return files;
    }
    
    /**
     * Counts the number of files, and find the next number.
     * 
     * @return 
     */
    public String getNextNumber() {
        String number = this.files[counter];
        this.counter++;
        
        return number;
    }
    
    public int getRandomNumb(int min, int max) {
        random = new Random();
        randomNumb = random.nextInt((max - min) + 1) + min;
        
        return randomNumb;
    }
    
    /**
     * Getting one path to a file dependant on the files array.
     * 
     * @param numb
     * @return 
     */
    public File getOneFilePath(String numb) {
        //foundButLost = "";
        
        directory = new File("C:\\Users\\Tonnes\\Documents\\Paradox Interactive\\Europa Universalis IV\\mod\\mapMod4\\history\\provinces");
        foundFiles = directory.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.startsWith(numb + " -");
            }
        });
        
            System.out.println("index: " + numb);
            System.out.println("path: " + Arrays.toString(foundFiles));
            filePath = foundFiles[0];

        return filePath;
    }
    
    public void replaceContent(File file, int number) {
        fileName = file.getName().toLowerCase();
        lines = createArrayFromFile(file); 
        sectionsToFill = getSections();
        templateInfo = getArrayInformation();
        development = getDevelopment();
        discoveredBy = getDiscoveredBy();
        
        newArray = new String[33];
        for (int i = 0; i < newArray.length; i++) {
            newArray[i] = "";
        }
        
        this.isWater = false;
        
        if (lines.length < 3) {
            isWater = true;
            newArray[1] = "# Water province";
        } else {
            isWater = false;
            //comment
            newArray[0] = "";
            if (sectionsToFill[0]) {
                newArray[0] = "# " + file.getName().replace(".txt", "");
            }
            
            newArray[1] = "";
            
            //add_core
            newArray[2] = "";
            if (sectionsToFill[1]) {
                newArray[2] = "add_core = " + templateInfo[0];
            }
            
            //owner
            newArray[3] = "";
            if (sectionsToFill[2]) {
                newArray[3] = "owner = " + templateInfo[0];
            }
            
            //controller
            newArray[4] = "";
            if (sectionsToFill[3]) {
                newArray[4] = "controller = " + templateInfo[0];
            }
            
            newArray[5] = "";
            
            //culture
            newArray[6] = "";
            if (sectionsToFill[4]) {
                newArray[6] = "culture = " + templateInfo[1];
            }  
            
            //religion
            newArray[7] = "";
            if (sectionsToFill[5]) {
                newArray[7] = "religion = " + templateInfo[2];
            }

            //hre
            newArray[8] = "";
            if (sectionsToFill[6]) {
                if (sectionsToFill[7]) {
                    newArray[8] = "hre = yes";
                } else {
                    newArray[8] = "hre = no";
                }
            }
            
            //trade good
            newArray[9] = "";
            if (sectionsToFill[8]) {
                if (sectionsToFill[16]) {
                    newArray[9] = "trade_goods = " + getTradeGood();
                } else {
                    newArray[9] = "trade_goods = " + templateInfo[3];
                }
            }

            //capital
            newArray[10] = "";
            if (sectionsToFill[9]) {
                newArray[10] = "capital = \"" + file.getName().replace(" - ", "").replaceAll("[0-9]", "").replace(".txt", "").replace(" -", "") + "\"";
            }
            
            //is_city
            newArray[11] = "";
            if (sectionsToFill[10]) {
                if (sectionsToFill[11]) {
                    newArray[11] = "is_city = yes";
                } else {
                    newArray[11] = "is_city = no";
                }
            }             

            newArray[12] = "";
            
            //base_tax, production, manpower
            newArray[13] = "";
            newArray[14] = "";
            newArray[15] = "";
            if (sectionsToFill[12]) {
                if (!sectionsToFill[13]) {
                    newArray[13] = "base_tax = " + development[0];
                    newArray[14] = "base_production = " + development[1];
                    newArray[15] = "base_manpower = " + development[2];
                } else {
                    newArray[13] = "base_tax = " + getRandomNumb(development[3], development[4]);
                    newArray[14] = "base_production = " + getRandomNumb(development[3], development[4]);
                    newArray[15] = "base_manpower = " + getRandomNumb(development[3], development[4]);
                }
            }

            newArray[16] = "";
            
            //estate
            newArray[17] = "";
            if (sectionsToFill[14]) {
                newArray[17] = "estate = " + templateInfo[4];
            }   
            
            newArray[18] = "";
            
            //discovered_by
            newArray[19] = "";
            newArray[20] = "";
            newArray[21] = "";
            newArray[22] = "";
            newArray[23] = "";
            newArray[24] = "";
            newArray[25] = "";
            newArray[26] = "";
            newArray[27] = "";
            newArray[28] = "";
            newArray[29] = "";
            newArray[30] = "";
            newArray[31] = "";
            newArray[32] = "";            
            if (sectionsToFill[15]) {     
                if (discoveredBy[0]) {
                    newArray[19] = "discovered_by = eastern";
                }
                if (discoveredBy[1]) {
                    newArray[20] = "discovered_by = western"; 
                }
                if (discoveredBy[2]) {
                    newArray[21] = "discovered_by = muslim"; 
                }
                if (discoveredBy[3]) {
                    newArray[22] = "discovered_by = ottoman"; 
                }
                if (discoveredBy[4]) {
                    newArray[23] = "discovered_by = indian"; 
                }
                if (discoveredBy[5]) {
                    newArray[24] = "discovered_by = nomad_group"; 
                }
                if (discoveredBy[6]) {
                    newArray[25] = "discovered_by = chinese"; 
                }
                if (discoveredBy[7]) {
                    newArray[26] = "discovered_by = east_african"; 
                }
                if (discoveredBy[8]) {
                    newArray[27] = "discovered_by = west_african"; 
                }                
                if (discoveredBy[9]) {
                    newArray[28] = "discovered_by = central_african"; 
                }  
                if (discoveredBy[10]) {
                    newArray[29] = "discovered_by = north_american"; 
                }
                if (discoveredBy[11]) {
                    newArray[30] = "discovered_by = mesoamerican"; 
                }
                if (discoveredBy[12]) {
                    newArray[31] = "discovered_by = south_american"; 
                }
                if (discoveredBy[13]) {
                    newArray[32] = "discovered_by = andean"; 
                }                
            }
        }
        
        writeToFile(newArray, file);
    }
    
    /**
     * Getting a path and converting the file to a string array.
     * 
     * @param file
     * @return 
     */
    public String[] createArrayFromFile(File file) {
        provinceFileString = "";
        provinceFileLineString = "";
        try {
            buffReader = new BufferedReader(new FileReader(file));
            
            while((provinceFileLineString = buffReader.readLine()) != null) {
                provinceFileString += provinceFileLineString + "\r\n";
            }
            buffReader.close();
            
            lines = provinceFileString.split("\\r?\\n");

        } catch (IOException e) {
            System.out.println(e);
        }
        return lines;
    }    
    
    /**
     * Last part of the process (writes the array to the file)
     * 
     * @param newFileArray
     * @param lines
     * @param filepath 
     */
    public void writeToFile(String[] newFileArray, File filepath) {
        if (isWater == false) {
            try {
                buffWriter = new BufferedWriter(writer = new FileWriter(filepath));
                for (int i = 0; i < newFileArray.length; i++) {               
                    buffWriter.write(newFileArray[i]);   
                    buffWriter.newLine();
                }
                buffWriter.flush();
                buffWriter.close();  
            } catch (IOException e) {
                System.out.println(e);
            }
        }
    }
    
    public static void main(String[] args) {
        main = new Main();
    }
}