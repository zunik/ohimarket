package zunik.ohimarket.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zunik.ohimarket.domain.Post;
import zunik.ohimarket.repository.PostLikeRepository;
import zunik.ohimarket.repository.PostRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;

    @Transactional
    public void save(Post post) {
        postRepository.save(post);
    }

    @Transactional
    public void delete(Long postId, Long memberId) {
        Post post = postRepository.findById(postId).get();

        if (post.getMemberId() != memberId) {
            // 삭제하려는 글의 권한이 없음
            return;
        }

        // 게시물에 연결된 Like도 모두 제거
        postLikeRepository.deleteByPostId(postId);
        postRepository.delete(post);
    }

    @Transactional(readOnly = true)
    public Optional<Post> findById(Long id) {
        return postRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Post> findAll() {
        return postRepository.findByOrderByCreatedAtDesc();
    }

    @Transactional
    public void increaseViews(Long id) {
        postRepository.increaseViews(id);
    }

}
