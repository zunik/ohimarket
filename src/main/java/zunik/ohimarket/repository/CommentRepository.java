package zunik.ohimarket.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import zunik.ohimarket.domain.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPostIdOrderByCreatedAtDesc(Long id);
}
