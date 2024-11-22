package com.example.shoppro.entity;


import com.example.shoppro.constant.ItemSellStatus;
import com.example.shoppro.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString(exclude = "itemImgList") //toString(exclude="변수명") 제외할 변수명
@Table(name="item")
public class Item extends BaseEntity {


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

   /* @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="item_id")
    private Member member;
    */


    @OneToMany(mappedBy = "item" ,cascade = CascadeType.REMOVE)//읽기만 가능한 상태
    private List<ItemImg> itemImgList;



}
