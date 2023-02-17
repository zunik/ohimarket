package zunik.ohimarket.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import zunik.ohimarket.domain.Post;

import java.util.List;

public interface PostRepository  extends JpaRepository<Post, Long> {
    List<Post> findByOrderByCreatedAtDesc();

    @Modifying
    @Query("UPDATE Post p SET p.views = p.views + 1 WHERE p.id = :id")
    void increaseViews(Long id);

    @Modifying
    @Query("UPDATE Post p SET p.like_count = p.like_count + 1 WHERE p.id = :id")
    void increaseLikeCount(Long id);

    @Modifying
    @Query("UPDATE Post p SET p.like_count = p.like_count - 1 WHERE p.id = :id")
    void decreaseLikeCount(Long id);

    @Query("SELECT SUM(p.like_count) FROM Post p WHERE p.memberId = :memberId")
    int sumLikeCountFindByMemberId(Long memberId);

    @Query("SELECT SUM(p.views) FROM Post p WHERE p.memberId = :memberId")
    int sumViewsFindByMemberId(Long memberId);

}
