package zunik.ohimarket.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import zunik.ohimarket.domain.PostCategory;

import java.util.List;

public interface PostCategoryRepository extends JpaRepository<PostCategory, String> {
    List<PostCategory> findByOrderBySortNumAsc();
}
