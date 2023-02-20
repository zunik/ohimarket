package zunik.ohimarket.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zunik.ohimarket.domain.Comment;
import zunik.ohimarket.repository.CommentRepository;
import zunik.ohimarket.repository.PostRepository;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public void save(Comment comment) {
        // TODO 상위 Post가 삭제되지 않았는지 확인해야함
        commentRepository.save(comment);
        postRepository.increaseCommentCount(comment.getPostId());
    }
}
