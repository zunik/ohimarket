package zunik.ohimarket.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import zunik.ohimarket.domain.Comment;


public interface CommentRepository extends JpaRepository<Comment, Long> {
    Long deleteByPostId(Long postId);
}
