package miw_padel_back;

import miw_padel_back.domain.models.Role;

import java.util.ArrayList;
import java.util.List;

public class RoleBuilder {
    List<Role> roleList;

    public RoleBuilder() {
        this.roleList = new ArrayList<>();
    }

    public RoleBuilder addAdminRole() {
        this.roleList.add(Role.ROLE_ADMIN);
        return this;
    }

    public RoleBuilder addPlayerRole() {
        this.roleList.add(Role.ROLE_PLAYER);
        return this;
    }

    public List<Role> build() {
        return this.roleList;
    }


}
