package allchive.server.domain.domains.content.repository;

import static allchive.server.domain.domains.content.domain.QContentTagGroup.contentTagGroup;
import static allchive.server.domain.domains.content.domain.QTag.tag;

import allchive.server.domain.domains.content.domain.Content;
import allchive.server.domain.domains.content.domain.ContentTagGroup;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ContentTagGroupCustomRepositoryImpl implements ContentTagGroupCustomRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<ContentTagGroup> queryContentTagGroupIn(List<Content> contentList) {
        return queryFactory
                .selectFrom(contentTagGroup)
                .join(contentTagGroup.tag, tag)
                .fetchJoin()
                .where(archivingIdEq(contentList))
                .orderBy(createdAtDesc())
                .fetch();
    }

    private BooleanExpression archivingIdEq(List<Content> contentList) {
        return contentTagGroup.content.in(contentList);
    }

    private OrderSpecifier<LocalDateTime> createdAtDesc() {
        return contentTagGroup.createdAt.desc();
    }
}
