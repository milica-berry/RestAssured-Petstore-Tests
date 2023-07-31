package com.craftysisters.petstore.dto;

import com.craftysisters.petstore.enums.ErrorCodes;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorDto {


    private ErrorCodes code;
    private String type;
    private String message;

}
