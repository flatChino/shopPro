package com.example.shoppro.controller;


import com.example.shoppro.dto.ItemDTO;
import com.example.shoppro.dto.PageRequestDTO;
import com.example.shoppro.dto.PageResponseDTO;
import com.example.shoppro.service.ItemService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Log4j2
public class ItemController {
    //boardController라고 생각하면됨

    private final ItemService itemService;
    @GetMapping("/admin/item/new")
     public String itemForm(Model model, Principal principal){
        if(principal ==null){

        }
        if(principal != null){
            log.info("현재 로그인 한 사람");
            log.info(principal.getName());

            // return "redirect:/";

        }


        model.addAttribute("itemDTO",new ItemDTO());

        return "/item/itemForm";
    }

    @PostMapping("/admin/item/new")
    public String itemFormPost(@Valid ItemDTO itemDTO, BindingResult bindingResult, List<MultipartFile> multipartFile,Model model){
        //들어오는값 확인
        log.info("아이템 포스트 :"+itemDTO);

        if(multipartFile!=null){
           for(MultipartFile img : multipartFile){
               if(!img.getOriginalFilename().equals("")){
                   log.info(img.getOriginalFilename());
                   log.info(img.getOriginalFilename());
                   log.info(img.getOriginalFilename());
                   log.info(img.getOriginalFilename());
                   log.info(img.getOriginalFilename());
               }
           }
        }

        if(bindingResult.hasErrors()){
            log.info("유효성검사 에러");
            log.info(bindingResult.getAllErrors());

            return "/item/itemForm";
        }

        try {

            long savedItemid=
                    itemService.saveItem(itemDTO,multipartFile);


        }catch (Exception e){
            log.info("파일등록간 문제가 발생했습니다.");
            model.addAttribute("msg","파일등록을 잘해주세요");

                return "/item/itemForm";
        }
        log.info("상품 등록됨");
        log.info("상품 등록됨");
        log.info("상품 등록됨");
        log.info("상품 등록됨");
        log.info("상품 등록됨");




        return null;
    }

    @GetMapping("/admin/read")
    public String adminread(Long id, Model model, RedirectAttributes redirectAttributes){

        try {

            ItemDTO itemDTO = itemService.read(id);

            model.addAttribute("itemDTO",itemDTO);

            return "item/read";

        }catch (EntityNotFoundException e){
            redirectAttributes.addFlashAttribute("msg","존재하지 않는 상품입니다.");

            return "reidrect:/admin/tiem/list";
            //item/list?msg=존재하지
        }


    }

    @GetMapping("admin/item/list")
    public String adminList(PageRequestDTO pageRequestDTO ,Model model,Principal principal){

        PageResponseDTO<ItemDTO> pageResponseDTO=
        itemService.list(pageRequestDTO, principal.getName());

        model.addAttribute("PageResponseDTO",pageResponseDTO);

        //model.addAttribute("list",itemService.list());

        return "item/list";
    }



}