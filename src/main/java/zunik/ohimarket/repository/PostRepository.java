package zunik.ohimarket.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import zunik.ohimarket.domain.Post;

import java.util.List;
import java.util.Optional;

public interface PostRepository  extends JpaRepository<Post, Long> {
    List<Post> findByOrderByCreatedAtDesc();

    List<Post> findByMemberIdOrderByCreatedAtDesc(Long id);

    Optional<Post> findByToken(String Token);

    @Modifying
    @Query("UPDATE Post p SET p.views = p.views + 1 WHERE p.id = :id")
    void increaseViews(Long id);

    @Modifying
    @Query("UPDATE Post p SET p.likeCount = p.likeCount + 1 WHERE p.id = :id")
    void increaseLikeCount(Long id);

    @Modifying
    @Query("UPDATE Post p SET p.likeCount = p.likeCount - 1 WHERE p.id = :id")
    void decreaseLikeCount(Long id);

    @Modifying
    @Query("UPDATE Post p SET p.commentCount = p.commentCount + 1 WHERE p.id = :id")
    void increaseCommentCount(Long id);

    @Modifying
    @Query("UPDATE Post p SET p.commentCount = p.commentCount - 1 WHERE p.id = :id")
    void decreaseCommentCount(Long id);
}
