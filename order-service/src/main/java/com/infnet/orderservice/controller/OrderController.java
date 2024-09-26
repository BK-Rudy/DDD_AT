package com.infnet.orderservice.controller;

import com.infnet.orderservice.model.Order;
import com.infnet.orderservice.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(value = "/api/orders")
@Tag(name = "Pedidos", description = "API para gerenciamento de pedidos")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Operation(summary = "Busca um pedido pelo ID", description = "Busca um pedido com base no ID fornecido.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedido encontrado com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Order.class))),
            @ApiResponse(responseCode = "400", description = "Erro na busca do pedido", content = @Content)
    })
    @GetMapping("/{id}")
    public Optional<Order> findById(@PathVariable(value = "id") long id) throws Exception {
        return orderService.findById(id);
    }

    @Operation(summary = "Fecha o pedido pelo ID", description = "Fecha um pedido com base no ID fornecido.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedido fechado com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Order.class))),
            @ApiResponse(responseCode = "400", description = "Erro ao fechar o pedido", content = @Content)
    })
    @PatchMapping("/close/{id}")
    public Order close(@PathVariable(value = "id") long id){
        return orderService.close(id);
    }
}
