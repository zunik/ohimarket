package zunik.ohimarket.repository;


import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import zunik.ohimarket.domain.Post;
import zunik.ohimarket.repository.dto.MemberSummeryDto;

import java.util.List;

import static zunik.ohimarket.domain.QPost.post;
import static zunik.ohimarket.domain.QComment.comment;


@Repository
@RequiredArgsConstructor
public class PostQueryRepository {
    private final JPAQueryFactory query;

    public MemberSummeryDto summaryByMemberId(Long memberId) {
        return query.select(Projections.bean(MemberSummeryDto.class,
                        post.views.sum().as("totalViews"),
                        post.likeCount.sum().as("totalLikeCount"),
                        post.commentCount.sum().as("totalCommentCount")))
                .from(post)
                .where(post.memberId.eq(memberId))
                .fetchOne();
    }

    /**
     * 특정 사용자의 '댓글단 글'의 리스트를 리턴합니다.
     */
    public List<Post> getCommentaryPosts(Long memberId) {
        return query.selectFrom(post)
                .join(comment).on(post.id.eq(comment.postId))
                .where(comment.member.id.eq(memberId))
                .groupBy(comment.postId)
                .orderBy(post.createdAt.desc())
                .fetch();
    }
}
