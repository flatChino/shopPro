package com.example.shoppro.dto;


import com.example.shoppro.entity.Item;
import jakarta.persistence.*;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@ToString

public class ItemImgDTO {


    private Long id;

    private String imgName;

    private String oriImgName;

    private String imgUrl;

    private String repimgYn;


    private Item item;

}
