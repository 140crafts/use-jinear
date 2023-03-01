package co.jinear.core.model.vo.auth;

import co.jinear.core.model.enumtype.localestring.LocaleType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Getter
@Setter
@AllArgsConstructor
public class AuthResponseVo {
    private String accountId;
    private LocaleType locale;
    private Collection<GrantedAuthority> authorities;
}
