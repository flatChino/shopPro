package com.example.shoppro.repository;

import com.example.shoppro.constant.ItemSellStatus;
import com.example.shoppro.entity.Item;
import com.example.shoppro.entity.QItem;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.log4j.Log4j2;
import org.hibernate.boot.jaxb.mapping.marshall.InheritanceTypeMarshalling;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Log4j2
@SpringBootTest
class ItemRepositoryTest {

    @Autowired
    ItemRepository itemRepository;

    @PersistenceContext
    EntityManager entityManager;

    @Test
    @DisplayName("상품 저장 테스트")
    public void createItemTest(){

        for(int i=0; i<200;i++){

            Item item = Item.builder()
                    .itemName("테스트 상품명")
                    .price(10000)
                    .itemSellStatus(ItemSellStatus.SELL)
                    .stockNumber(100)
                    .itemDetail("aaa")
                    .regTime(LocalDateTime.now())
                    .updateTime(LocalDateTime.now())
                    .build();


            item.setItemName(item.getItemName()+i);
            item.setItemDetail(item.getItemDetail()+i);
            item.setPrice(item.getPrice()+i);
            Item item1=
            itemRepository.save(item);
            log.info(item1);


        }




    }


    @Test
    @DisplayName("제품명으로 검색 2가지에서 다시 2가지 검색")
    public void findByItemNmTest() {

        List<Item> itemListA =
                itemRepository.findByItemName("테스트상품1");
        itemListA.forEach(item -> log.info(item));
        System.out.println("---------------------------");

        itemListA =
                itemRepository.selectwhereItemName("테스트상품2");
        itemListA.forEach(item -> log.info(item));
        System.out.println("---------------------------");

        itemListA =
                itemRepository.findByItemNameContaining("테스트");
        itemListA.forEach(item -> log.info(item));
        System.out.println("---------------------------");

        itemListA =
                itemRepository.selectWItemNameLike("1");
        itemListA.forEach(item -> log.info(item));
        System.out.println("---------------------------");


    }

    @Test
    public void priceSearchtest(){

        //사용자가 검색창에 혹은 검색가능하도록 만들어놓은 곳에 값을
        // 입력하는 조건에 부합하는 아이템 리스트
        Integer price = 10020;
        //가격 검색 테스트
        List<Item> itemsA =
        itemRepository.findByPriceLessThan(price);
        for(Item item: itemsA){
            log.info(item);
            log.info("상품명 :"+item.getItemName());
            log.info("상품가격 :" +item.getPrice());
            log.info("상품 설명 :" +item.getItemDetail());
        }


        List<Item> itemsB =
                itemRepository.findByPriceGreaterThan(price);
        for(Item item: itemsB){
            log.info(item);
            log.info("상품명 :"+item.getItemName());
            log.info("상품가격 :" +item.getPrice());
            log.info("상품 설명 :" +item.getItemDetail());
        }


        List<Item> itemsC =
                itemRepository.findByPriceGreaterThan(price);
        for(Item item: itemsC){
            log.info(item);
            log.info("상품명 :"+item.getItemName());
            log.info("상품가격 :" +item.getPrice());
            log.info("상품 설명 :" +item.getItemDetail());
        }



    }
    @Test
    @DisplayName("페이징 추가까지")
    public void findbypricegreaterTehanEqualTest(){
        Pageable pageable = PageRequest.of(0,5, Sort.by("id").ascending());
        //sort.by의 정렬종건은 entity의 변수명이다.

        Integer price= 10020;

        List<Item> itemsList = itemRepository.findByPriceGreaterThanEqual(price,pageable);

        itemsList.forEach(item -> log.info(item));



    }
    @Test
    public void nativeQueryTest(){
        Pageable pageable = PageRequest.of(0,5, Sort.by("price").descending());
        String itemName= "테스트상품1";
        List<Item> itemList=
        itemRepository.nativeQuerySelectWhereNameLike(itemName,pageable);
        itemList.forEach(item -> log.info(item));
        //데이터 베이스에서는 자동으로 소문자로 저장되기 때문에 대문자로 입력하면 오류가난다.
    }



    @Test
    public void queryDslTest(){

        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);

        QItem qItem = QItem.item;
        //select * from item


        JPAQuery<Item> query = queryFactory.selectFrom(qItem).where(qItem.itemSellStatus.eq(ItemSellStatus.SELL))
                .where(qItem.itemDetail.like("%"+"1"+"%")).orderBy(qItem.price.desc());

        List<Item>itemList =query.fetch();

        for(Item item:itemList){
            System.out.println(item.getItemName());
        }

    }

    @Test
    public void queryDslTestB(){

        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);

        QItem qItem = QItem.item;
        //select * from item

        String keyword = null;
        ItemSellStatus itemSellStatus = ItemSellStatus.SELL;

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        if(keyword !=null){
            booleanBuilder.and(qItem.itemDetail.like("%"+keyword+"%"));
        }
        if(itemSellStatus!=null){
            if(itemSellStatus==ItemSellStatus.SELL){
                booleanBuilder.or(qItem.itemSellStatus.eq(ItemSellStatus.SELL));
            }
            else{
                booleanBuilder.or(qItem.itemSellStatus.eq(ItemSellStatus.SOLD_OUT));
            }
        }




        JPAQuery<Item> query = queryFactory.selectFrom(qItem).where(booleanBuilder).orderBy(qItem.price.desc());

        List<Item>itemList =query.fetch();

        for(Item item:itemList){
            System.out.println(item.getItemName());
        }

    }








}