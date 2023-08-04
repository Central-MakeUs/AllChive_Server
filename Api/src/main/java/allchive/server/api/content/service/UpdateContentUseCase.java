package allchive.server.api.content.service;

import static allchive.server.core.consts.AllchiveConst.MINUS_ONE;
import static allchive.server.core.consts.AllchiveConst.PLUS_ONE;

import allchive.server.api.common.util.UrlUtil;
import allchive.server.api.config.security.SecurityUtil;
import allchive.server.api.content.model.dto.request.UpdateContentRequest;
import allchive.server.api.content.model.mapper.ContentMapper;
import allchive.server.core.annotation.UseCase;
import allchive.server.domain.domains.archiving.adaptor.ArchivingAdaptor;
import allchive.server.domain.domains.archiving.service.ArchivingDomainService;
import allchive.server.domain.domains.content.adaptor.ContentAdaptor;
import allchive.server.domain.domains.content.adaptor.TagAdaptor;
import allchive.server.domain.domains.content.domain.Content;
import allchive.server.domain.domains.content.domain.ContentTagGroup;
import allchive.server.domain.domains.content.domain.Tag;
import allchive.server.domain.domains.content.domain.enums.ContentType;
import allchive.server.domain.domains.content.service.ContentDomainService;
import allchive.server.domain.domains.content.service.ContentTagGroupDomainService;
import allchive.server.domain.domains.content.validator.ContentValidator;
import allchive.server.domain.domains.content.validator.TagValidator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@UseCase
@RequiredArgsConstructor
public class UpdateContentUseCase {
    private final ContentValidator contentValidator;
    private final TagValidator tagValidator;
    private final ContentAdaptor contentAdaptor;
    private final TagAdaptor tagAdaptor;
    private final ContentMapper contentMapper;
    private final ContentDomainService contentDomainService;
    private final ContentTagGroupDomainService contentTagGroupDomainService;
    private final ArchivingDomainService archivingDomainService;
    private final ArchivingAdaptor archivingAdaptor;

    @Transactional
    public void execute(Long contentId, UpdateContentRequest request) {
        validateExecution(contentId, request);
        regenerateContentTagGroup(contentId, request.getTagIds());
        updateArchiving(contentId, request.getArchivingId(), request.getContentType());
        contentDomainService.update(
                contentId,
                request.getContentType(),
                request.getArchivingId(),
                request.getLink(),
                request.getMemo(),
                UrlUtil.convertUrlToKey(request.getImgUrl()),
                request.getTitle());
    }

    private void updateArchiving(Long contentId, Long newArchivingId, ContentType contentType) {
        Content content = contentAdaptor.findById(contentId);
        if (!content.getArchivingId().equals(newArchivingId)) {
            archivingDomainService.updateContentCnt(
                    content.getArchivingId(), content.getContentType(), MINUS_ONE);
            archivingDomainService.updateContentCnt(newArchivingId, contentType, PLUS_ONE);
        }
    }

    private void validateExecution(Long contentId, UpdateContentRequest request) {
        Long userId = SecurityUtil.getCurrentUserId();
        contentValidator.verifyUser(contentId, userId);
        tagValidator.validateExistTagsAndUser(request.getTagIds(), userId);
    }

    private void regenerateContentTagGroup(Long contentId, List<Long> tagIds) {
        Content content = contentAdaptor.findById(contentId);
        contentTagGroupDomainService.deleteAllByContent(content);
        List<Tag> tags = tagAdaptor.queryTagByTagIdIn(tagIds);
        List<ContentTagGroup> contentTagGroupList =
                contentMapper.toContentTagGroupEntityList(content, tags);
        contentTagGroupDomainService.saveAll(contentTagGroupList);
    }
}
