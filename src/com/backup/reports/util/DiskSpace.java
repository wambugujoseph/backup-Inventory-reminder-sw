package com.backup.reports.util;

import java.io.File;

public class DiskSpace {

    private File file = new File(new ReadConfigFile().readBackUpPath());

    public double totalDiskSpace(){

        return this.file.getTotalSpace();
    }

    public double totalRemainSpace(){

        return this.file.getUsableSpace();
    }

    public double  usedDiskSpace(){

        return (totalDiskSpace() - totalRemainSpace());
    }

    public double diskUsage(){
        return usedDiskSpace()/totalDiskSpace();
    }
}
