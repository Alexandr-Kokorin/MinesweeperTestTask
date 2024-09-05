package web.java.server.controller.dto;

public record NewGameRequest(
    int width,
    int height,
    int mines_count
) { }
