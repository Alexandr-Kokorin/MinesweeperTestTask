package web.java.server.controller.dto;

import java.util.UUID;

public record GameInfoResponse(
    UUID game_id,
    int width,
    int height,
    int mines_count,
    String[][] field,
    boolean completed
) { }
