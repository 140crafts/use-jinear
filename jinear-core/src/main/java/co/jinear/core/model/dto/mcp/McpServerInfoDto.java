package co.jinear.core.model.dto.mcp;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * What this instance can tell a member about its own MCP server.
 * <p>
 * serverUrl exists so nobody has to work out their instance's address by hand. It is the
 * canonical resource URL, which is exactly the string a client such as Claude or ChatGPT
 * must be given, path included.
 */
@Getter
@Setter
@ToString
public class McpServerInfoDto {

    /** True only when the server is turned on in configuration and by the instance flag. */
    private Boolean enabled;
    private String serverUrl;
    private String documentationUrl;
}
