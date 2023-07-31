package com.craftysisters.petstore.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class OrderDto {

    private Long id;
    private Long petId;
    private Long quantity;
    private String shipDate;
    private String status;
    private boolean complete;




}
