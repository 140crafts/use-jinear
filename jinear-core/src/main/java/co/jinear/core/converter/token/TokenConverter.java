package co.jinear.core.converter.token;

import co.jinear.core.model.dto.token.TokenDto;
import co.jinear.core.model.entity.token.Token;
import co.jinear.core.model.vo.token.GenerateTokenVo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TokenConverter {
    Token map(GenerateTokenVo generateTokenVo);

    TokenDto map(Token token);
}
