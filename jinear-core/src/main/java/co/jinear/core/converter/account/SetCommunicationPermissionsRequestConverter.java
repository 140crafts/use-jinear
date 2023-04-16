package co.jinear.core.converter.account;


import co.jinear.core.model.request.account.SetCommunicationPermissionsRequest;
import co.jinear.core.model.vo.account.UpdateAccountCommunicationPermissionsVo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SetCommunicationPermissionsRequestConverter {

    UpdateAccountCommunicationPermissionsVo convert(String accountId, SetCommunicationPermissionsRequest communicationPermissionsRequest);
}
