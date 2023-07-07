package allchive.server.domain.domains.user.service;


import allchive.server.core.annotation.DomainService;
import allchive.server.domain.domains.user.adaptor.UserAdaptor;
import allchive.server.domain.domains.user.domain.User;
import allchive.server.domain.domains.user.domain.enums.OauthInfo;
import allchive.server.domain.domains.user.exception.exceptions.AlreadySignUpUserException;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@DomainService
@RequiredArgsConstructor
public class UserDomainService {
    private final UserAdaptor userAdaptor;

    public Boolean checkUserCanLogin(OauthInfo oauthInfo) {
        return userAdaptor.exist(oauthInfo);
    }

    @Transactional
    public User registerUser(String nickname, String profileImgUrl, OauthInfo oauthInfo) {
        validUserCanRegister(oauthInfo);
        final User newUser = User.of(nickname, profileImgUrl, oauthInfo);
        userAdaptor.save(newUser);
        return newUser;
    }

    @Transactional
    public User loginUser(OauthInfo oauthInfo) {
        User user = userAdaptor.queryUserByOauthInfo(oauthInfo);
        user.login();
        return user;
    }

    public Boolean checkUserCanRegister(OauthInfo oauthInfo) {
        return !userAdaptor.exist(oauthInfo);
    }

    public void validUserCanRegister(OauthInfo oauthInfo) {
        if (!checkUserCanRegister(oauthInfo)) throw AlreadySignUpUserException.EXCEPTION;
    }

    @Transactional
    public void deleteUserById(Long userId) {
        User user = userAdaptor.queryUserById(userId);
        user.withdrawUser();
    }

    @Transactional
    public void updateUserInfo(Long userId, String name, String email, String nickname, String imgUrl) {
        User user = userAdaptor.queryUserById(userId);
        user.updateInfo(name, email, nickname, imgUrl);
    }
}
