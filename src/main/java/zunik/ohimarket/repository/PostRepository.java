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
}
