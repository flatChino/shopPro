package com.example.shoppro.service;


import com.example.shoppro.dto.ItemDTO;
import com.example.shoppro.dto.PageRequestDTO;
import com.example.shoppro.dto.PageResponseDTO;
import com.example.shoppro.entity.Item;
import com.example.shoppro.entity.ItemImg;
import com.example.shoppro.repository.ItemImgRepository;
import com.example.shoppro.repository.ItemRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.*;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
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
@Log4j2
public class ItemService {


    private  final ItemRepository itemRepository;
    private  final ModelMapper modelMapper;
    private  final  ItemImgService itemimgService;
    private  final  ItemImgRepository itemImgRepository;






    //이미지 등록할

    //상품등록
    public Long saveItem(ItemDTO itemDTO , List<MultipartFile> multipartFiles) throws IOException {
        //아이템 디티오로 받아내고, 멀티 파일을 리스트로 받아서 여러장의 사진을 저장한다. 그런 다음 예외 처리 한다.
        Item item = modelMapper.map(itemDTO,Item.class );
        //item 변수에 아이템 modelMapper.map으로 itemDto타입으로 받앙 Item.class형식으로 변환한다.

        item=
        itemRepository.save(item);
        //item안에 itemReopository형식으로 저장해준다 item을


        //이미지 등록 추가 예정
        itemimgService.saveImg(item.getId(), multipartFiles);
        //itemimgService에 이미지를 저장한다. 형식은(item.getId(상품 번호),multipartFiles(사진리스트형태이다.))



        return item.getId();//반환은 item.getid(아이템 아이디로 반환한다.)
    }


    public ItemDTO read(Long id){
        //Long id(id로 받아온다.)

        Item item=
        itemRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        //item에 itemRpository형에 아이디를 찾아서 유효성검사를 하고 넣어준다.

        ItemDTO itemDTO = modelMapper.map(item,ItemDTO.class)
                .setItemImgDTOList(item.getItemImgList());
        //itemDTO에 modelMapper.map(item로 받아 ItemDTO.class형으로 변환한다.)로 변환해서 넣어준다.




        return  itemDTO;//반환은 itemDTO형태로 반환한다.
    }

    public ItemDTO read(Long id,String email){
        //Long id(id로 받아온다.)

        Item item=
                itemRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        //item에 itemRpository형에 아이디를 찾아서 유효성검사를 하고 넣어준다.

        ItemDTO itemDTO = modelMapper.map(item,ItemDTO.class)
                .setItemImgDTOList(item.getItemImgList());
        //itemDTO에 modelMapper.map(item로 받아 ItemDTO.class형으로 변환한다.)로 변환해서 넣어준다.




        return  itemDTO;//반환은 itemDTO형태로 반환한다.
    }



    public PageResponseDTO<ItemDTO> list(PageRequestDTO pageRequestDTO, String email){
        //pageResponseDTO<ItemDTO> 형태로 list를 선언해준다. 파라미터(PageRequesDTO pageRequesDTO[페이지를 받기위한 파라미터]
        // String email[이메일을 받기위한 파라미터])

        //일단 기본으로 전부 가져오기
        /*List<Item>itemList=
        itemRepository.findAll();

        List<ItemDTO> itemDTOList=
                list().stream().map(itemDTO -> modelMapper.map(item, ItemDTO.class))
                  return itemDTOList;
        */


        //itemRepository.getAdminItemPage()

        Pageable pageable = pageRequestDTO.getPageable("id");
        //pageable 안에 pageRequestDTO.getPageable(id[Item_ID를 받아서 넣어준다.])형으로 넣어준다.
        
        Page<Item> items=
        itemRepository.getAdminItemPage(pageRequestDTO, pageable, email);
        //page<Item>형태에 item변수에itemRepository

        List<ItemDTO> itemDTOPage =
                items.getContent().stream().map(item -> modelMapper.map(item, ItemDTO.class))
                        .collect(Collectors.toList());
        //list<itemDTO>형태로 itemDTOPage를 변수 선언을 해주며 item을 stream map으로 ItemDTO class형으로 변경시켜준다.

        PageResponseDTO<ItemDTO> itemDTOPageResponseDTO
                = PageResponseDTO.<ItemDTO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(itemDTOPage)
                .total((int) items.getTotalElements())
                .build();
        

        return itemDTOPageResponseDTO;

    }

    public ItemDTO update(ItemDTO itemDTO,Long id, List<MultipartFile> multipartFiles, Integer[] delino,Long mainino ){
        //itemDTO 수정
        Item item=
        itemRepository.findById(itemDTO.getId()).orElseThrow(EntityNotFoundException::new);


        //set
        item.setItemName(item.getItemName());
        item.setPrice(itemDTO.getPrice());
        item.setItemDetail(item.getItemDetail());
        item.setItemSellStatus(item.getItemSellStatus());
        item.setStockNumber(item.getStockNumber());






        if (delino != null) {

            for (Integer ino : delino) {

                if (ino != null && ino.equals("")) {
                    log.info("삭제할 번호는 ino"+ino);
                    itemimgService.removeimg(ino.longValue());
                }
            }
        }
        try {

            itemimgService.update(id, multipartFiles, mainino);
        } catch (IOException e){

        }



        return  null;
    }





}
