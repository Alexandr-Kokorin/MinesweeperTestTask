package web.java.server.game.data;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import lombok.Getter;
import org.springframework.stereotype.Component;

@Getter
@Component
public class GameLibrary {

    private final Map<UUID, Game> games;

    public GameLibrary() {
        games = new HashMap<>();
    }

    public void addGame(UUID id, Game game) {
        games.put(id, game);
    }

    public Game getGame(UUID id) {
        return games.get(id);
    }

    public void removeGame(UUID id) {
        games.remove(id);
    }
}
