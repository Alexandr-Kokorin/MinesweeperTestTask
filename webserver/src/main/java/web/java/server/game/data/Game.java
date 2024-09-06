package web.java.server.game.data;

import java.time.OffsetDateTime;
import lombok.Getter;

@Getter
public class Game {

    private final int width;
    private final int height;
    private final int minesCount;
    private int notOpenCellsCount;
    private boolean completed;
    private final Cell[][] originalField;
    private final Cell[][] visibleField;
    private OffsetDateTime lastUpdate;

    public Game(int width, int height, int minesCount, Cell[][] originalField, Cell[][] visibleField) {
        this.width = width;
        this.height = height;
        this.minesCount = minesCount;
        notOpenCellsCount = width * height;
        completed = false;
        this.originalField = originalField;
        this.visibleField = visibleField;
        lastUpdate = OffsetDateTime.now();
    }

    public void decrementNotOpenCellsCount() {
        notOpenCellsCount--;
    }

    public void finishGame() {
        completed = true;
    }

    public Cell getOriginalCell(int row, int col) {
        return originalField[row][col];
    }

    public Cell getVisibleCell(int row, int col) {
        return visibleField[row][col];
    }

    public void setVisibleCell(int row, int col, Cell cell) {
        visibleField[row][col] = cell;
    }

    public void setLastUpdate(OffsetDateTime time) {
        lastUpdate = time;
    }
}
