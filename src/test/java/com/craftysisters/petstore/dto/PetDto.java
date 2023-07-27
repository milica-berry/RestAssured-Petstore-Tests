package com.craftysisters.petstore.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PetDto {
    private Long id;
    private Category category;
    @NonNull
    private String name;
    private List<String> photoUrls;
    private List<TagDto> tags = new ArrayList<>();
    private String status;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Category {
        private long id;
        private String name;
    }



}
