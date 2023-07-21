package com.craftysisters.petstore.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PetDto {
    private Long id;
    private Category category;
    private String name;
    private List<String> photoUrls;
    private List<TagDto> tags = new ArrayList<>();
    private String status;

    @Data
    @AllArgsConstructor
    public static class Category {
        private long id;
        private String name;
    }



}
