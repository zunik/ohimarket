package zunik.ohimarket.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zunik.ohimarket.domain.PostCategory;
import zunik.ohimarket.repository.PostCategoryRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostCategoryService {
    private final PostCategoryRepository postCategoryRepository;

    @Transactional
    public List<PostCategory> findAll() {
        return postCategoryRepository.findByOrderBySortNumAsc();
    }

    /**
     *  key 로 DisplayName 을 바로 찾을 수 있게
     *  Dict 로 변환해서 넘겨줍니다.
     */
    @Transactional
    public Map<String, String> findAllTransMap() {
        Map<String, String> categoriesMap = new HashMap<>();
        List<PostCategory> postCategories = postCategoryRepository.findByOrderBySortNumAsc();
        postCategories.forEach(postCategory -> {
            categoriesMap.put(
                    postCategory.getName(),
                    postCategory.getDisplayName()
            );
        });

        return categoriesMap;
    }
}
