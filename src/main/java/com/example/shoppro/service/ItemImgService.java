package com.example.shoppro.service;


import com.example.shoppro.entity.Item;
import com.example.shoppro.entity.ItemImg;
import com.example.shoppro.repository.ItemImgRepository;
import com.example.shoppro.repository.ItemRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.LongSummaryStatistics;

@Service
@Transactional
@RequiredArgsConstructor
@Log4j2
public class ItemImgService {
    private  final ItemImgRepository itemImgRepository;
    private  final  FileService fileService;
    private  final ItemRepository itemRepository;
    
    
    //이미지등록
    public void saveImg(Long id, List<MultipartFile>multipartFiles) throws IOException {
        //이미지 등록은 어디에 무엇을 저장할것인가
        //이미지는 아이템꺼
        //아이템 Pk 이미지 파일 이파일을 경로를 잘라서
        //경로와 함께 이름을 저장한다.


        log.info("아이템이미지 서비스로 들어온다 id"+id);
        if(multipartFiles != null){
            for(MultipartFile img : multipartFiles){
                if(!img.isEmpty()){
                    log.info("아이템 이미지 서비스로 들어온 이미지"+img.getOriginalFilename());
                    //물리적인 저장
                    String savedFileName= // uuid가 포함된 물리적인 파일이름
                    fileService.uploadFile(img);
                    
                    
                    
                    
                    //db저장
                    Item item=
                    itemRepository.findById(id).get();

                    String imgUrl ="/images/item/"+ savedFileName;

                    ItemImg itemImg = new ItemImg();
                    itemImg.setItem(item);
                    itemImg.setImgName(savedFileName);
                    itemImg.setImgUrl(imgUrl);
                    itemImg.setOriImgName(img.getOriginalFilename());
                    //대표이미지 여부 확인
                    if(multipartFiles.indexOf(img)==0){
                        itemImg.setRepimgYn("Y");
                    }
                    else{
                        itemImg.setRepimgYn("N");
                    }


                    itemImgRepository.save(itemImg);

                    
                    
                    
                }
            }
        }


    }
    public void removeimg(long id){


        ItemImg itemImg =
                itemImgRepository.findById(id).get();


         fileService.removefile(itemImg.getImgName());


        //db에서 지운값
        itemRepository.deleteById(id);

    }


    public void update(Long id, List<MultipartFile>multipartFiles, Long mainino) throws IOException {
        //이미지 등록은 어디에 무엇을 저장할것인가
        //이미지는 아이템꺼
        //아이템 Pk 이미지 파일 이파일을 경로를 잘라서
        //경로와 함께 이름을 저장한다.


        log.info("아이템이미지 서비스로 들어온다 id"+id);
        if(multipartFiles != null){
            for(MultipartFile img : multipartFiles){
                if(!img.isEmpty()){
                    log.info("아이템 이미지 서비스로 들어온 이미지"+img.getOriginalFilename());
                    //물리적인 저장
                    String savedFileName= // uuid가 포함된 물리적인 파일이름
                            fileService.uploadFile(img);




                    //db저장
                    Item item=
                            itemRepository.findById(id).get();

                    String imgUrl ="/images/item/"+ savedFileName;

                    ItemImg itemImg = new ItemImg();
                    itemImg.setItem(item);
                    itemImg.setImgName(savedFileName);
                    itemImg.setImgUrl(imgUrl);
                    itemImg.setOriImgName(img.getOriginalFilename());
                    //대표이미지 여부 확인


                    if(mainino == null){
                        if(multipartFiles.indexOf((img))==0) {

                            itemImg=
                            itemImgRepository.findByItemIdAndRepimgYn(id,"Y");
                            itemImg.setItem(item);
                            itemImg.setImgName(savedFileName);
                            itemImg.setImgUrl(imgUrl);
                            itemImg.setOriImgName(img.getOriginalFilename());


                            //itemImg.setRepimgYn("Y");
                        }
                        else {
                            itemImg.setRepimgYn("N");
                        }

                    }else {
                        itemImg.setRepimgYn("N");
                    }

                    itemImgRepository.save(itemImg);




                }
            }
        }


    }
}
