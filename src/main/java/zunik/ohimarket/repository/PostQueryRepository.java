package zunik.ohimarket.repository;


import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import zunik.ohimarket.repository.dto.MemberSummeryDto;

import static zunik.ohimarket.domain.QPost.post;


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
}
