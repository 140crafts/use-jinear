package co.jinear.core.converter.workspace;

import co.jinear.core.model.enumtype.token.TokenType;
import co.jinear.core.model.vo.token.GenerateTokenVo;
import co.jinear.core.system.NormalizeHelper;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Component
public class WorkspaceInvitationTokenConverter {

    private final Long INVITATION_TTL = TimeUnit.MILLISECONDS.convert(30, TimeUnit.DAYS);

    public GenerateTokenVo convert(String invitationId) {
        GenerateTokenVo generateTokenVo = new GenerateTokenVo();
        generateTokenVo.setRelatedObject(invitationId);
        generateTokenVo.setTtl(INVITATION_TTL);
        generateTokenVo.setCommonToken(NormalizeHelper.normalizeStrictly(UUID.randomUUID().toString()));
        generateTokenVo.setUniqueToken(UUID.randomUUID().toString());
        generateTokenVo.setTokenType(TokenType.WORKSPACE_INVITATION);
        return generateTokenVo;
    }
}
