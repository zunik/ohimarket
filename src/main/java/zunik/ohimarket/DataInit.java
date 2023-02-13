package zunik.ohimarket;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.transaction.annotation.Transactional;
import zunik.ohimarket.domain.PostCategory;
import zunik.ohimarket.repository.PostCategoryRepository;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@RequiredArgsConstructor
public class DataInit {
    private final PostCategoryRepository postCategoryRepository;

    /**
     * 글 카테고리 데이터 추가
     *
     * 동네질문, 동네소식, 일상, 취미생활, 해주세요
     */
    @Transactional
    @EventListener(ApplicationReadyEvent.class)
    public void initPostCategoryData() {
        Long count = postCategoryRepository.count();
        if (count != 0) {
            log.info("이미 카테고리 데이터가 존재함");
            return;
        }
        Map<String, String> categoryName = new LinkedHashMap<>() {{
            put("question", "동네질문");
            put("news", "동네소식");
            put("daily", "일상");
            put("hobby", "취미생활");
            put("help", "해주세요");
        }};

        List<PostCategory> postCategories = new ArrayList<>();
        AtomicInteger sortNum = new AtomicInteger();
        categoryName.forEach((name, displayName) -> {
            log.info(name, displayName);
            PostCategory postCategory = new PostCategory();
            postCategory.setName(name);
            postCategory.setDisplayName(displayName);
            postCategory.setSortNum(sortNum.getAndIncrement());
            postCategories.add(postCategory);
        });

        postCategoryRepository.saveAll(postCategories);
        log.info("카테고리 데이터를 생성함");
    }
}
