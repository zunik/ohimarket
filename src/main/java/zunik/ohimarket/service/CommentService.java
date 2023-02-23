package zunik.ohimarket.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zunik.ohimarket.controller.dto.CommentCreateDto;
import zunik.ohimarket.controller.dto.CommentUpdateDto;
import zunik.ohimarket.domain.Comment;
import zunik.ohimarket.domain.Member;
import zunik.ohimarket.domain.Post;
import zunik.ohimarket.exception.AccessDeniedException;
import zunik.ohimarket.exception.CommentNotFoundException;
import zunik.ohimarket.exception.PostNotFoundException;
import zunik.ohimarket.repository.CommentRepository;
import zunik.ohimarket.repository.PostRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public Comment save(CommentCreateDto createParam, Member loginMember) throws PostNotFoundException {
        String postToken = createParam.getPostToken();

        Post post = postRepository.findByToken(postToken).orElseThrow(
                () -> new PostNotFoundException()
        );

        Comment comment = new Comment();
        comment.setPostId(post.getId());
        comment.setMember(loginMember);
        comment.setContent(createParam.getContent());
        comment.setToken(UUID.randomUUID().toString());

        commentRepository.save(comment);
        postRepository.increaseCommentCount(comment.getPostId());

        return comment;
    }

    @Transactional(readOnly = true)
    public Comment findByToken(String commentToken) throws CommentNotFoundException {
        // 이미 댓글이 지워졌을 수 있음
        return commentRepository.findByToken(commentToken).orElseThrow(
                () -> new CommentNotFoundException()
        );
    }

    @Transactional
    public String update(String commentToken, CommentUpdateDto updateParam, Long memberId) throws PostNotFoundException, CommentNotFoundException {
        // 이미 댓글이 지워졌을 수 있습니다.
        Comment comment = commentRepository.findByToken(commentToken).orElseThrow(
                () -> new CommentNotFoundException()
        );
        // 이미 게시글이 지워졌을 수 있습니다.
        Post post = postRepository.findById(comment.getPostId()).orElseThrow(
                () -> new PostNotFoundException()
        );

        if (comment.getMember().getId() != memberId) {
            // 수정하려는 댓글에 권한이 없음
            throw new AccessDeniedException();
        }

        comment.setContent(updateParam.getContent());
        return post.getToken();
    }

    @Transactional
    public String delete(String token, Long memberId) throws PostNotFoundException, CommentNotFoundException, AccessDeniedException  {
        // 이미 댓글이 지워졌을 수 있습니다.
        Comment comment = commentRepository.findByToken(token).orElseThrow(
                () -> new CommentNotFoundException()
        );
        // 이미 게시글이 지워졌을 수 있습니다.
        Post post = postRepository.findById(comment.getPostId()).orElseThrow(
                () -> new PostNotFoundException()
        );

        if (comment.getMember().getId() != memberId) {
            // 인증자의 댓글이 아닐경우 삭제 실패
            throw new AccessDeniedException();
        }

        commentRepository.delete(comment);
        postRepository.decreaseCommentCount(comment.getPostId());
        return post.getToken();
    }
}
