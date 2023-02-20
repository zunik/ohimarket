package zunik.ohimarket.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zunik.ohimarket.domain.Comment;
import zunik.ohimarket.domain.Member;
import zunik.ohimarket.domain.Post;
import zunik.ohimarket.domain.PostCategory;
import zunik.ohimarket.dto.PostCreateDto;
import zunik.ohimarket.dto.PostDetailResponseDto;
import zunik.ohimarket.repository.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;
    private final PostCategoryRepository postCategoryRepository;
    private final MemberRepository memberRepository;
    private final CommentRepository commentRepository;
    private final CommentQueryRepository commentQueryRepository;

    @Transactional
    public void save(Post post) {
        postRepository.save(post);
    }

    @Transactional
    public void delete(Long postId, Long memberId) {
        Post post = postRepository.findById(postId).get();

        if (post.getMemberId() != memberId) {
            // TODO 밖으로 Exc 넘겨서 권한 처리
            log.info("삭제하려는 글의 권한이 없음");
            return;
        }

        // 게시물에 연결된 Like, comment도 모두 제거
        postLikeRepository.deleteByPostId(postId);
        commentRepository.deleteByPostId(postId);
        postRepository.delete(post);
    }

    @Transactional(readOnly = true)
    public PostDetailResponseDto getDetail(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow();
        PostCategory postCategory = postCategoryRepository.findById(post.getPostCategoryName()).get();
        Member creator = memberRepository.findById(post.getMemberId()).get();
        List<Comment> comments = commentQueryRepository.findByPostId(postId);

        PostDetailResponseDto responseDto = new PostDetailResponseDto();
        responseDto.setPost(post);
        responseDto.setPostCategory(postCategory);
        responseDto.setCreator(creator);
        responseDto.setComments(comments);

        return responseDto;
    }


    @Transactional
    public void update(Long postId, Long memberId, PostCreateDto updateParam) {
        Post post = postRepository.findById(postId).get();

        if (post.getMemberId() != memberId) {
            // TODO 밖으로 Exc 넘겨서 권한 처리
            log.info("수정하려는 글에 권한이 없음");
            return;
        }

        post.setTitle(updateParam.getTitle());
        post.setContent(updateParam.getContent());
        post.setPostCategoryName(updateParam.getPostCategoryName());
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
