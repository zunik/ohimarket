package zunik.ohimarket.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import zunik.ohimarket.domain.Post;

import java.util.List;

public interface PostRepository  extends JpaRepository<Post, Long> {
    List<Post> findByOrderByCreatedAtDesc();
}
