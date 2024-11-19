package com.example.shoppro.entity;


import com.example.shoppro.constant.ItemSellStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Table(name="item")
public class Item {


    @Id
    @Column(name = "item_id")//테이블에서 매핑될 컬럼
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //상품코드

    @Column(nullable = false, length = 50)
    private String itemName; //상품 이름 

    @Column(name="price", nullable = false)
    private  int price;//상품 가격

    @Column(nullable = false)
    private  int stockNumber;//재고 수량

    @Column(nullable = false)
    private  String itemDetail;//상품 상세설명
    //상품 판매 상태

    @Enumerated(EnumType.STRING)
    private ItemSellStatus itemSellStatus;

    private LocalDateTime regTime;//상품 등록시간
    private LocalDateTime updateTime;//상품 수정시간


}
