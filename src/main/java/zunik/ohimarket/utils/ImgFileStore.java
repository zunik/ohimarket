package zunik.ohimarket.utils;

import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Slf4j
@Component
public class ImgFileStore {
    @Value("${file.dir}")
    private String fileDir;

    public String getFullPath(String filename) {
        return fileDir + filename;
    }

    public String getThumbnailFullPath(String filename) {
        return fileDir +  "S_" + filename;
    }

    public String storeFile(MultipartFile multipartFile) throws IOException {
        String originalFileName = multipartFile.getOriginalFilename();
        String storeFileName = createStoreFileName(originalFileName);

        File originalImg = new File(getFullPath(storeFileName));
        multipartFile.transferTo(originalImg);

        Thumbnails.of(originalImg)
                .size(65, 65)
                .crop(Positions.CENTER)
                .toFile(new File(getThumbnailFullPath(storeFileName)));

        return storeFileName;
    }

    /**
     * 특정 이미지의 원본과 섬네일까지 제거합니다.
     */
    public void deleteFile(String filename) {
        File img = new File(getFullPath(filename));
        File thumbnailImg = new File(getThumbnailFullPath(filename));
        img.delete();
        thumbnailImg.delete();
    }

    /**
     * 날짜와 uuid 를 조합해서 고유한 파일이름을 만듭니다.
     * 예시) 20230223-1311_5af12c0a-b6f5-46de-b292-f14ca8c4350b.jpg
     */
    private String createStoreFileName(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        String ext = originalFilename.substring(pos + 1);

        LocalDateTime current = LocalDateTime.now();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyyMMdd-HHmm");
        String formatedDateTime = current.format(format);

        return formatedDateTime + "_" + UUID.randomUUID() + "." + ext;
    }
}
