package zunik.ohimarket.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import zunik.ohimarket.domain.PostLike;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    PostLike findByPostIdAndMemberId(Long postId, Long memberId);

    Long deleteByPostId(Long postId);
}
