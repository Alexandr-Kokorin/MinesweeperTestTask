package web.java.server.game.data;

import java.util.HashMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Cell {

    SPACE(" "),
    ZERO("0"),
    ONE("1"),
    TWO("2"),
    THREE("3"),
    FOUR("4"),
    FIVE("5"),
    SIX("6"),
    SEVEN("7"),
    EIGHT("8"),
    M("M"),
    X("X");

    private final String value;
    private static final Map<String, Cell> MAP = new HashMap<>();

    static {
        for (Cell cell : Cell.values()) {
            MAP.put(cell.value, cell);
        }
    }

    public static Cell getCell(String value) {
        return MAP.get(value);
    }
}
