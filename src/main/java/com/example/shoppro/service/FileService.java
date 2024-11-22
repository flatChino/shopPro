package com.example.shoppro.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

@Service
public class FileService {

    @Value("{itemImgLocation}")
    String itemImgLocation;//경로

    public String uploadFile(MultipartFile multipartFile) throws IOException {

        UUID uuid = UUID.randomUUID();//서로 다른 개체를 구별하기 위해
                                    //이름을 부여할 때 사용 실제 사용시 중복이
                                    //거의 없기 때문에
        String extension = multipartFile.getOriginalFilename()
                .substring(multipartFile.getOriginalFilename().lastIndexOf("."));
        //물지적인 파일이름
        String saveFileName =uuid.toString()+extension;
        
        
        //경로
        String fileUploadFullUrl= itemImgLocation+"/"+saveFileName;

        //물리적인 저장 //다른방법으로는
        FileOutputStream fos = new FileOutputStream(fileUploadFullUrl);
        fos.write(multipartFile.getBytes());
        fos.close();

        return saveFileName;
        

    }


}
