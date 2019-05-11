package wjy.weiai7lv.entity;


/**
 * 角色
 * @author wjy
 */
public class Role {

    private Integer id;
    private String roleName;
    private String roleAuthor;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleAuthor() {
        return roleAuthor;
    }

    public void setRoleAuthor(String roleAuthor) {
        this.roleAuthor = roleAuthor;
    }
}
