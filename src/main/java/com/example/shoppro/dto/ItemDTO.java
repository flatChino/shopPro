package com.example.shoppro.dto;


import com.example.shoppro.constant.ItemSellStatus;
import com.example.shoppro.entity.ItemImg;
import com.example.shoppro.entity.base.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.modelmapper.ModelMapper;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;

import java.util.List;
import java.util.stream.Collectors;


@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data

public class ItemDTO  {



    private Long id; //상품코드

    @NotBlank
    @Size(max = 50,min = 2,message = "상품명은 2~50자 입니다.")
    private String itemName; //상품 이름 

    @NotNull
    @PositiveOrZero
    private  int price;//상품 가격

    @NotNull
    @PositiveOrZero
    private  int stockNumber;//재고 수량

    @NotBlank
    private  String itemDetail;//상품 상세설명



    private ItemSellStatus itemSellStatus;//상품 판매 상태

    private List<ItemImgDTO> itemImgDTOList;

    public ItemDTO setItemImgDTOList(List<ItemImg>itemImgList){

        ModelMapper modelMapper = new ModelMapper();

        itemImgList.stream().map(itemImg -> modelMapper.map(itemImg,ItemImgDTO.class)).collect(Collectors.toList());

        this.itemImgDTOList =itemImgDTOList;

        return this;
    }







}
