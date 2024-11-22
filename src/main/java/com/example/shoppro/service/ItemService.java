package com.example.shoppro.service;


import com.example.shoppro.dto.ItemDTO;
import com.example.shoppro.dto.PageRequestDTO;
import com.example.shoppro.dto.PageResponseDTO;
import com.example.shoppro.entity.Item;
import com.example.shoppro.repository.ItemImgRepository;
import com.example.shoppro.repository.ItemRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.*;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemService {


    private  final ItemRepository itemRepository;
    private  final ModelMapper modelMapper;
    private  final  ItemImgService itemimgService;

    //이미지 등록할

    //상품등록
    public Long saveItem(ItemDTO itemDTO , List<MultipartFile> multipartFiles) throws IOException {

        Item item = modelMapper.map(itemDTO,Item.class );


        item=
        itemRepository.save(item);

        //이미지 등록 추가 예정
        itemimgService.saveImg(item.getId(), multipartFiles);




        return item.getId();
    }


    public ItemDTO read(Long id){

        Item item=
        itemRepository.findById(id).orElseThrow(EntityNotFoundException::new);

        ItemDTO itemDTO = modelMapper.map(item,ItemDTO.class)
                .setItemImgDTOList(item.getItemImgList());





        return  itemDTO;
    }


    public PageResponseDTO<ItemDTO> list(PageRequestDTO pageRequestDTO, String email){

        //일단 기본으로 전부 가져오기
        /*List<Item>itemList=
        itemRepository.findAll();

        List<ItemDTO> itemDTOList=
                list().stream().map(itemDTO -> modelMapper.map(item, ItemDTO.class))
                  return itemDTOList;
        */


        //itemRepository.getAdminItemPage()

        Pageable pageable = pageRequestDTO.getPageable("id");

        Page<Item> items=
        itemRepository.getAdminItemPage(pageRequestDTO, pageable, email);
        List<ItemDTO> itemDTOPage =
                items.getContent().stream().map(item -> modelMapper.map(item, ItemDTO.class))
                        .collect(Collectors.toList());

        PageResponseDTO<ItemDTO> itemDTOPageResponseDTO
                = PageResponseDTO.<ItemDTO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(itemDTOPage)
                .total((int) items.getTotalElements())
                .build();
        return itemDTOPageResponseDTO;

    }



}
