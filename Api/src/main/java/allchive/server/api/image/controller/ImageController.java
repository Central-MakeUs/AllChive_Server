package allchive.server.api.image.controller;

import allchive.server.api.config.security.SecurityUtil;
import allchive.server.api.image.model.dto.response.ImageUrlResponse;
import allchive.server.infrastructure.s3.PresignedType;
import allchive.server.infrastructure.s3.S3PresignedUrlService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "6. [image]")
public class ImageController {
    private final S3PresignedUrlService s3PresignedUrlService;

    @Operation(summary = "카테고리 관련 이미지 업로드 url 요청할수 있는 api 입니다.")
    @GetMapping(value = "/category/image")
    public ImageUrlResponse getCategoryPresignedUrl() {
        Long userId = SecurityUtil.getCurrentUserId();
        return ImageUrlResponse.from(s3PresignedUrlService.getPreSignedUrl(userId, PresignedType.CATEGORY));
    }
}
