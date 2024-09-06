package web.java.server.game;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.Random;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import web.java.server.game.data.Cell;
import web.java.server.game.data.Game;
import web.java.server.game.data.GameLibrary;

@AllArgsConstructor
@Component
@EnableScheduling
public class GameHandler {

    private GameLibrary gameLibrary;

    public UUID createGame(int width, int height, int minesCount) {
        Game game = new Game(
            width, height, minesCount,
            initialOriginalField(width, height, minesCount),
            initialVisibleField(width, height)
        );
        UUID id = UUID.randomUUID();
        gameLibrary.addGame(id, game);
        return id;
    }

    private Cell[][] initialOriginalField(int width, int height, int minesCount) {
        Cell[][] field = new Cell[height][width];
        setMines(width, height, minesCount, field);
        setValues(width, height, field);
        return field;
    }

    private void setMines(int width, int height, int minesCount, Cell[][] field) {
        Random random = new Random();
        while (minesCount > 0) {
            int row = random.nextInt(0, height);
            int col = random.nextInt(0, width);
            if (field[row][col] != Cell.M) {
                field[row][col] = Cell.M;
                minesCount--;
            }
        }
    }

    private void setValues(int width, int height, Cell[][] field) {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (field[i][j] != Cell.M) {
                    field[i][j] = countMines(width, height, i, j, field);
                }
            }
        }
    }

    private Cell countMines(int width, int height, int row, int col, Cell[][] field) {
        int count = 0;
        for (int i = Math.max(0, row - 1); i < Math.min(row + 2, height); i++) {
            for (int j = Math.max(0, col - 1); j < Math.min(col + 2, width); j++) {
                if (field[i][j] == Cell.M) {
                    count++;
                }
            }
        }
        return Cell.getCell(String.valueOf(count));
    }

    private Cell[][] initialVisibleField(int width, int height) {
        Cell[][] field = new Cell[height][width];
        for (int i = 0; i < height; i++) {
            Arrays.fill(field[i], Cell.SPACE);
        }
        return field;
    }

    public void updateGame(UUID id, int row, int col) {
        Game game = gameLibrary.getGame(id);
        if (game.getOriginalCell(row, col) != Cell.M) {
            openCellWithNumber(game, row, col);
            if (game.getMinesCount() == game.getNotOpenCellsCount()) {
                openField(game, Cell.M);
                game.finishGame();
            }
        } else {
            openField(game, Cell.X);
            game.finishGame();
        }
        game.setLastUpdate(OffsetDateTime.now());
    }

    private void openCellWithNumber(Game game, int row, int col) {
        if (game.getOriginalCell(row, col) != Cell.ZERO) {
            game.setVisibleCell(row, col, game.getOriginalCell(row, col));
        } else {
            game.setVisibleCell(row, col, Cell.ZERO);
            for (int i = Math.max(0, row - 1); i < Math.min(row + 2, game.getHeight()); i++) {
                for (int j = Math.max(0, col - 1); j < Math.min(col + 2, game.getWidth()); j++) {
                    if (game.getVisibleCell(i, j) == Cell.SPACE) {
                        openCellWithNumber(game, i, j);
                    }
                }
            }
        }
        game.decrementNotOpenCellsCount();
    }

    private void openField(Game game, Cell cell) {
        for (int i = 0; i < game.getHeight(); i++) {
            for (int j = 0; j < game.getWidth(); j++) {
                if (game.getOriginalCell(i, j) == Cell.M) {
                    game.setVisibleCell(i, j, cell);
                } else {
                    game.setVisibleCell(i, j, game.getOriginalCell(i, j));
                }
            }
        }
    }

    @Scheduled(fixedDelay = 60 * 1000)
    public void deleteOldGames() {
        for (UUID id : gameLibrary.getGames().keySet()) {
            if (gameLibrary.getGame(id).getLastUpdate().plusMinutes(5).isBefore(OffsetDateTime.now())) {
                gameLibrary.removeGame(id);
            }
        }
    }
}
