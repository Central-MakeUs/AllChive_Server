package allchive.server.api.content.model.mapper;


import allchive.server.api.common.util.UrlUtil;
import allchive.server.api.content.model.dto.request.CreateContentRequest;
import allchive.server.api.content.model.dto.response.ContentResponse;
import allchive.server.api.content.model.dto.response.ContentTagResponse;
import allchive.server.core.annotation.Mapper;
import allchive.server.domain.domains.content.domain.Content;
import allchive.server.domain.domains.content.domain.ContentTagGroup;
import allchive.server.tag.model.dto.response.TagResponse;
import java.util.List;

@Mapper
public class ContentMapper {
    public ContentResponse toContentResponse(
            Content content, List<ContentTagGroup> contentTagGroupList) {
        List<ContentTagGroup> tags =
                contentTagGroupList.stream()
                        .filter(contentTagGroup -> contentTagGroup.getContent().equals(content))
                        .toList();
        ContentTagGroup contentTagGroup = tags.stream().findFirst().orElse(null);
        String tag = contentTagGroup == null ? null : contentTagGroup.getTag().getName();
        return ContentResponse.of(content, tag, (long) tags.size());
    }

    public Content toEntity(CreateContentRequest request) {
        return Content.of(
                request.getCategoryId(),
                request.getContentType(),
                UrlUtil.convertUrlToKey(request.getImgUrl()),
                request.getLink(),
                request.getTitle(),
                request.getMemo());
    }

    public ContentTagResponse toContentTagResponse(
            Content content, List<ContentTagGroup> contentTagGroupList) {
        List<TagResponse> tagResponseList =
                contentTagGroupList.stream()
                        .map(contentTagGroup -> TagResponse.from(contentTagGroup.getTag()))
                        .toList();
        return ContentTagResponse.of(content, tagResponseList);
    }
}
