package co.jinear.core.model.enumtype.account;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static co.jinear.core.model.enumtype.account.PermissionType.PROCESS_REMINDER_JOB;

@Getter
@AllArgsConstructor
public enum RoleType {
    ADMIN(Stream.of(PermissionType.values()).collect(Collectors.toSet())),
    SERVICE(Set.of(PROCESS_REMINDER_JOB)),
    USER(new HashSet<>());

    private final Set<PermissionType> permissions;

    public Set<SimpleGrantedAuthority> getGrantedAuthorities() {
        Set<SimpleGrantedAuthority> perms = getPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toSet());
        perms.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return perms;
    }
}
