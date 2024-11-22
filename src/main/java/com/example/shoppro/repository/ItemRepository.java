package com.example.shoppro.repository;

import com.example.shoppro.entity.Item;
import com.example.shoppro.repository.search.ItemsearchRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item,Long>, ItemsearchRepository {

    public List<Item> findByItemName(String itemName);

    @Query("select i from Item i where  i.itemName= :itemName")
    public List<Item> selectwhereItemName(String itemName);


    public List<Item> findByItemNameContaining(String itemName);


    @Query("select i from Item i where i.itemName like concat('%',:itemName,'%')")
    public List<Item> selectWItemNameLike(String itemName);
    
    //상세설명으로 검색
    public List<Item> findByItemDetailContaining(String itemDetail);

    //가격으로 검색
    public List<Item> findByPriceLessThan(Integer price);

    public List<Item> findByPriceGreaterThan(Integer price);

    public List<Item> findByPriceGreaterThanOrderByPriceAsc(Integer price);

    public List<Item> findByPriceGreaterThanEqual(Integer price,Pageable pageable);

    //정렬까지 추가
    List<Item>findByPriceLessThanOrderByPriceDesc(Integer price);

    //nativeQuery
    @Query(value = "select Item from Item i where i.itemName = :itemName",nativeQuery = true)
    List<Item> nativeQuerySelectWhereNameLike(String itemName, Pageable pageable);





}
