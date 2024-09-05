package web.java.server.controller.dto;

import java.util.UUID;

public record GameTurnRequest(
    UUID game_id,
    int col,
    int row
) { }
