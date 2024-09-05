package web.java.server.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import web.java.server.controller.dto.ErrorResponse;
import web.java.server.controller.dto.GameInfoResponse;
import web.java.server.controller.dto.GameTurnRequest;
import web.java.server.controller.dto.NewGameRequest;

@AllArgsConstructor
@RestController
public class Controller {

    @Operation(summary = "Начало новой игры")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200",
                     description = "OK",
                     content = @Content(mediaType = "application/json",
                                        schema = @Schema(implementation = GameInfoResponse.class))),
        @ApiResponse(responseCode = "400",
                     description = "Ошибка запроса или некорректное действие",
                     content = @Content(mediaType = "application/json",
                                        schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping(path = "/new")
    public ResponseEntity<GameInfoResponse> createGame(@RequestBody NewGameRequest newGameRequest) {
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Ход пользователя")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200",
                     description = "OK",
                     content = @Content(mediaType = "application/json",
                                        schema = @Schema(implementation = GameInfoResponse.class))),
        @ApiResponse(responseCode = "400",
                     description = "Ошибка запроса или некорректное действие",
                     content = @Content(mediaType = "application/json",
                                        schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping(path = "/turn")
    public ResponseEntity<GameInfoResponse> makeMove(@RequestBody GameTurnRequest gameTurnRequest) {
        return ResponseEntity.ok().build();
    }
}
