package zunik.ohimarket.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import zunik.ohimarket.utils.ImgFileStore;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
@Controller
public class ImageController {
    private final ImgFileStore imgFileStore;

    /**
     * 이미지를 보여줍니다.
     *
     * @param isThumbnail 해당 값이 true 로 들어오면 썸네일 이미지를 보여줍니다.
     */
    @ResponseBody
    @GetMapping("/images/{imgName}")
    public ResponseEntity<Resource> downloadImage(
            @PathVariable String imgName,
            @RequestParam(value = "isThumbnail", required = false, defaultValue = "false") boolean isThumbnail) throws IOException {

        String fullPath = imgFileStore.getFullPath(imgName);

        if (isThumbnail) {
            // thumbnail 용 이미지일 경우, path 변경
            fullPath = imgFileStore.getThumbnailFullPath(imgName);
        }

        Resource resource = new FileSystemResource(fullPath);

        if (!resource.exists()) {
            return new ResponseEntity<Resource>(HttpStatus.NOT_FOUND);
        }

        Path filePath = Paths.get(fullPath);

        HttpHeaders headers =  new HttpHeaders();
        // 이미지명은 uuid로 고유하기 때문에, cache age를 길게 잡습니다.
        headers.setCacheControl(CacheControl.maxAge(365, TimeUnit.DAYS));
        headers.add(HttpHeaders.CONTENT_TYPE, Files.probeContentType(filePath));
        return new ResponseEntity<Resource>(resource, headers, HttpStatus.OK);
    }
}
