package zunik.ohimarket.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zunik.ohimarket.domain.PostCategory;
import zunik.ohimarket.repository.PostCategoryRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostCategoryService {
    private final PostCategoryRepository postCategoryRepository;

    @Transactional
    public List<PostCategory> findAll() {
        return postCategoryRepository.findByOrderBySortNumAsc();
    }
}
