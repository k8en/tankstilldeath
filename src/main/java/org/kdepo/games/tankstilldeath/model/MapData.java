package org.kdepo.games.tankstilldeath.model;

public class MapData {

    private String pathToFolder;

    private String fileNameLayer0;

    private String fileNameLayer1;

    private String fileNameLayer2;

    public String getPathToFolder() {
        return pathToFolder;
    }

    public void setPathToFolder(String pathToFolder) {
        this.pathToFolder = pathToFolder;
    }

    public String getFileNameLayer0() {
        return fileNameLayer0;
    }

    public void setFileNameLayer0(String fileNameLayer0) {
        this.fileNameLayer0 = fileNameLayer0;
    }

    public String getFileNameLayer1() {
        return fileNameLayer1;
    }

    public void setFileNameLayer1(String fileNameLayer1) {
        this.fileNameLayer1 = fileNameLayer1;
    }

    public String getFileNameLayer2() {
        return fileNameLayer2;
    }

    public void setFileNameLayer2(String fileNameLayer2) {
        this.fileNameLayer2 = fileNameLayer2;
    }

    @Override
    public String toString() {
        return "MapData{" +
                "pathToFolder='" + pathToFolder + '\'' +
                ", fileNameLayer0='" + fileNameLayer0 + '\'' +
                ", fileNameLayer1='" + fileNameLayer1 + '\'' +
                ", fileNameLayer2='" + fileNameLayer2 + '\'' +
                '}';
    }
}
