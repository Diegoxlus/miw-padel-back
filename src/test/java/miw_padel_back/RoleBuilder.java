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
        this.roleList.add(Role.ADMIN);
        return this;
    }

    public RoleBuilder addPlayerRole() {
        this.roleList.add(Role.PLAYER);
        return this;
    }

    public List<Role> build() {
        return this.roleList;
    }


}
