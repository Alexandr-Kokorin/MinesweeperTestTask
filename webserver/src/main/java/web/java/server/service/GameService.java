package web.java.server.service;

import java.util.Objects;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import web.java.server.controller.dto.GameInfoResponse;
import web.java.server.controller.dto.GameTurnRequest;
import web.java.server.controller.dto.NewGameRequest;
import web.java.server.game.GameHandler;
import web.java.server.game.data.Cell;
import web.java.server.game.data.Game;
import web.java.server.game.data.GameLibrary;

@AllArgsConstructor
@Component
public class GameService {

    private GameHandler gameHandler;
    private GameLibrary gameLibrary;

    public GameInfoResponse createGame(NewGameRequest newGameRequest) {
        validateCreateGame(newGameRequest);
        UUID id = gameHandler.createGame(newGameRequest.width(), newGameRequest.height(), newGameRequest.mines_count());
        Game game = gameLibrary.getGame(id);
        return new GameInfoResponse(id, game.getWidth(), game.getHeight(), game.getMinesCount(),
            getStringField(game), game.isCompleted());
    }

    private void validateCreateGame(NewGameRequest newGameRequest) {
        if (newGameRequest.width() < 2 || newGameRequest.width() > 30) {
            throw HttpClientErrorException.BadRequest.create(HttpStatusCode.valueOf(400),
                "Ширина поля должна быть не менее 2 и не более 30", HttpHeaders.EMPTY, null, null);
        }
        if (newGameRequest.height() < 2 || newGameRequest.height() > 30) {
            throw HttpClientErrorException.BadRequest.create(HttpStatusCode.valueOf(400),
                "Высота поля должна быть не менее 2 и не более 30", HttpHeaders.EMPTY, null, null);
        }
        if (newGameRequest.mines_count() < 1 || newGameRequest.mines_count() > newGameRequest.width() * newGameRequest.height() - 1) {
            throw HttpClientErrorException.BadRequest.create(HttpStatusCode.valueOf(400),
                "Количество мин должно быть не менее 1 и не более " + (newGameRequest.width() * newGameRequest.height() - 1),
                HttpHeaders.EMPTY, null, null);
        }
    }

    public GameInfoResponse updateGame(GameTurnRequest gameTurnRequest) {
        validateUpdateGame(gameTurnRequest);
        gameHandler.updateGame(gameTurnRequest.game_id(), gameTurnRequest.row(), gameTurnRequest.col());
        Game game = gameLibrary.getGame(gameTurnRequest.game_id());
        return new GameInfoResponse(gameTurnRequest.game_id(), game.getWidth(), game.getHeight(),
            game.getMinesCount(), getStringField(game), game.isCompleted());
    }

    private void validateUpdateGame(GameTurnRequest gameTurnRequest) {
        Game game = gameLibrary.getGame(gameTurnRequest.game_id());
        if (Objects.isNull(game)) {
            throw HttpClientErrorException.BadRequest.create(HttpStatusCode.valueOf(400),
                "Игра с идентификатором " + gameTurnRequest.game_id() + " не была создана или устарела (неактуальна)",
                HttpHeaders.EMPTY, null, null);
        }
        if (game.isCompleted()) {
            throw HttpClientErrorException.BadRequest.create(HttpStatusCode.valueOf(400),
                "Игра завершена", HttpHeaders.EMPTY, null, null);
        }
        if (game.getVisibleCell(gameTurnRequest.row(), gameTurnRequest.col()) != Cell.SPACE) {
            throw HttpClientErrorException.BadRequest.create(HttpStatusCode.valueOf(400),
                "Ячейка уже открыта", HttpHeaders.EMPTY, null, null);
        }
    }

    private String[][] getStringField(Game game) {
        String[][] field = new String[game.getHeight()][game.getWidth()];
        for (int i = 0; i < game.getHeight(); i++) {
            for (int j = 0; j < game.getWidth(); j++) {
                field[i][j] = game.getVisibleCell(i, j).getValue();
            }
        }
        return field;
    }
}
