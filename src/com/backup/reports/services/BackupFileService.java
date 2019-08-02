package com.backup.reports.services;

import com.backup.reports.database.FileRecordSchema;
import com.backup.reports.util.BackUpFile;
import com.backup.reports.util.ReadConfigFile;

import java.io.File;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class BackupFileService  implements Runnable {

    @Override
    public void run() {
        fileService();
    }

    /**
     * Getting files details and directories details
     */
    private void fileService() {
        boolean directoryRs, fileUpdate;
        String rootPath = new ReadConfigFile().readBackUpPath();
        File file = new File(rootPath);
        String directories[];
        int fileCount = 0;
        int directoryCount = 0;

        if (file.isDirectory()) {

            directories = file.list();
            ArrayList<String> directoryList = new ArrayList<>();
            ArrayList<String> fileList = new ArrayList<>();

            /**
             *  gets the Backup directories from the storage location
             */

            for (int i = 0; i < directories.length; i++) {
                File fileDirectory = new File(rootPath + directories[i]);
                if (fileDirectory.isDirectory()) {
                    if (!fileDirectory.isHidden()) {
                        directoryList.add(directoryCount, directories[i]);
                        directoryCount ++;
                        directoryRs = new FileRecordSchema().clientBackDiretoryUpdate(directories[i], (rootPath + directories[i]));
                    }
                }
            }

            /**
             * getting the zip files from each of the backup directory
             */
            for (int i = 0; i < directoryList.size(); i++) {
                //System.out.println(directoryList.get(i) + "------");
                File zipFile = new File(rootPath + directoryList.get(i));
                String directoryFile[] = zipFile.list();
                for (int j = 0; j < directoryFile.length; j++) {
                    File f1 = new File(rootPath + directoryList.get(i) + "/" + directoryFile[j]);
                    if (f1.isFile()) {
                        if (!f1.isHidden()) {
                            if (f1.getName().toLowerCase().endsWith(".zip")) {
                                fileList.add(fileCount, directoryFile[j]);
                                fileCount++;
                                //System.out.println(directoryFile[j]);
                                long size = ((f1.length())/1024)+1;
                                LocalDate date = Instant.ofEpochMilli(f1.lastModified()).atZone(ZoneId.systemDefault()).toLocalDate();
                                BackUpFile backUpFile = new BackUpFile(directoryFile[j],directoryList.get(i),date,size);
                                fileUpdate =new FileRecordSchema().fileRecordUpdate(backUpFile);
                            }
                        }
                    }
                }
            }
        }
    }
    /**
     *
     * @return
     * a List of all the server names
     */
    public List<String> getServerName(){
        return new FileRecordSchema().getServerNamesSchema();
    }


}
