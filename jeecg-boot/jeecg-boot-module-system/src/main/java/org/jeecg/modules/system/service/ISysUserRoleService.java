package org.jeecg.modules.system.service;

import java.util.List;
import java.util.Map;

import org.jeecg.modules.system.entity.SysRole;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.system.entity.SysUserRole;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 用户角色表 服务类
 * </p>
 *
 * @Author scott
 * @since 2018-12-21
 */
public interface ISysUserRoleService extends IService<SysUserRole> {
	
	/**
	 * 查询所有的用户角色信息
	 * @return
	 */
	Map<String,String> queryUserRole();

	/**
	 * 根据角色获取用户列表
	 * @author cyb
	 * @return 用户列表
	 */
	List<SysUser> getUsersByRole(SysRole sysRole);
}
