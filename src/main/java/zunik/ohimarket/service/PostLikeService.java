package zunik.ohimarket.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zunik.ohimarket.domain.PostLike;
import zunik.ohimarket.repository.PostLikeRepository;
import zunik.ohimarket.repository.PostRepository;

@Service
@RequiredArgsConstructor
public class PostLikeService {
    private final PostLikeRepository postLikeRepository;
    private final PostRepository postRepository;

    @Transactional
    public void LikeToggle(Long postId, Long memberId) {
        PostLike postLike = postLikeRepository.findByPostIdAndMemberId(postId, memberId);

        if (postLike == null) {
            // 생성
            postLike = new PostLike();
            postLike.setPostId(postId);
            postLike.setMemberId(memberId);
            postLikeRepository.save(postLike);
            postRepository.increaseLikeCount(postId);
        } else {
            //삭제
            postLikeRepository.delete(postLike);
            postRepository.decreaseLikeCount(postId);
        }
    }

    @Transactional(readOnly = true)
    public boolean PostLikeCheck(Long postId, Long memberId) {
        PostLike postLike = postLikeRepository.findByPostIdAndMemberId(postId, memberId);

        return postLike == null? false : true;
    }
}
