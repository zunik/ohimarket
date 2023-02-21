package zunik.ohimarket.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import zunik.ohimarket.utils.ImgFileStore;

import java.net.MalformedURLException;

@RequiredArgsConstructor
@Controller
public class ImageController {
    private final ImgFileStore imgFileStore;

    @ResponseBody
    @GetMapping("/images/{imgName}")
    public Resource downloadImage(@PathVariable String imgName) throws MalformedURLException {
        return new UrlResource("file:" + imgFileStore.getFullPath(imgName));
    }

    @ResponseBody
    @GetMapping("/images/{imgName}/thumbnail")
    public Resource downloadThumbnailImage(@PathVariable String imgName) throws MalformedURLException {
        return new UrlResource("file:" + imgFileStore.getThumbnailFullPath(imgName));
    }
}
