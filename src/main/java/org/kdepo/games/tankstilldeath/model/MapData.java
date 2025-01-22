package org.kdepo.games.tankstilldeath.model;

public class MapData {

    private String pathToFolder;

    /**
     * Map name to display on a briefing screen
     */
    private String mapName;

    /**
     * Next map to load after the current one. Is a resource name.
     * If next map is null then game will quit to the title screen
     */
    private String nextMap;

    private String fileNameLayer0;

    private String fileNameLayer1;

    private String fileNameLayer2;

    private String fileNameSpawnSpots;

    private String fileNameTanksToSpawn;

    private int activeTanksLimit;

    public String getPathToFolder() {
        return pathToFolder;
    }

    public void setPathToFolder(String pathToFolder) {
        this.pathToFolder = pathToFolder;
    }

    public String getMapName() {
        return mapName;
    }

    public void setMapName(String mapName) {
        this.mapName = mapName;
    }

    public String getNextMap() {
        return nextMap;
    }

    public void setNextMap(String nextMap) {
        this.nextMap = nextMap;
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

    public String getFileNameSpawnSpots() {
        return fileNameSpawnSpots;
    }

    public void setFileNameSpawnSpots(String fileNameSpawnSpots) {
        this.fileNameSpawnSpots = fileNameSpawnSpots;
    }

    public String getFileNameTanksToSpawn() {
        return fileNameTanksToSpawn;
    }

    public void setFileNameTanksToSpawn(String fileNameTanksToSpawn) {
        this.fileNameTanksToSpawn = fileNameTanksToSpawn;
    }

    public int getActiveTanksLimit() {
        return activeTanksLimit;
    }

    public void setActiveTanksLimit(int activeTanksLimit) {
        this.activeTanksLimit = activeTanksLimit;
    }

    @Override
    public String toString() {
        return "MapData{" +
                "pathToFolder='" + pathToFolder + '\'' +
                ", mapName='" + mapName + '\'' +
                ", nextMap='" + nextMap + '\'' +
                ", fileNameLayer0='" + fileNameLayer0 + '\'' +
                ", fileNameLayer1='" + fileNameLayer1 + '\'' +
                ", fileNameLayer2='" + fileNameLayer2 + '\'' +
                ", fileNameSpawnSpots='" + fileNameSpawnSpots + '\'' +
                ", fileNameTanksToSpawn='" + fileNameTanksToSpawn + '\'' +
                ", activeTanksLimit=" + activeTanksLimit +
                '}';
    }
}
