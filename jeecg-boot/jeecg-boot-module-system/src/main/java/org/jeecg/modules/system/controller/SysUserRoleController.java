package org.jeecg.modules.system.controller;


import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.system.entity.SysRole;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.system.entity.SysUserRole;
import org.jeecg.modules.system.service.ISysUserRoleService;
import org.jeecg.modules.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author cyb
 * @since 2019/11/17
 */
@Slf4j
@RestController
@RequestMapping("/sys/userRole")
@Api(tags = "用户角色")
public class SysUserRoleController {
	@Autowired
	private ISysUserService sysUserService;

	private final ISysUserRoleService sysUserRoleService;

	@Autowired
	public SysUserRoleController(ISysUserRoleService sysUserRoleService){
        this.sysUserRoleService = sysUserRoleService;
    }

    @ApiOperation("根据角色查询用户列表")
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public Result<IPage<SysUser>> queryPageList(SysUser sysUser, String roleId, @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
												@RequestParam(name="pageSize", defaultValue="10") Integer pageSize, HttpServletRequest req) {
        //查询该角色所在用户id列表
		SysRole sysRole = new SysRole();
		sysRole.setId(roleId);
		List<SysUser> usersByRole = sysUserRoleService.getUsersByRole(sysRole);
		List<String> userIdList = usersByRole
				.stream().map(e -> e.getId()).collect(Collectors.toList());
		//查询用户时限制角色条件并移除源id传参
		Result<IPage<SysUser>> result = new Result<>();
		sysUser.setId(null);
		QueryWrapper<SysUser> queryWrapper = QueryGenerator.initQueryWrapper(sysUser, req.getParameterMap());
		if(CollectionUtil.isNotEmpty(userIdList)){
			queryWrapper.lambda().in(SysUser::getId,userIdList);
		}
        Page<SysUser> page = new Page<>(pageNo, pageSize);
		IPage<SysUser> pageList = sysUserService.page(page, queryWrapper);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}

	@ApiOperation("移除权限")
	@RequestMapping(value = "/delete", method = RequestMethod.DELETE)
	public Result<SysRole> delete(SysUser sysUser,String roleId) {
		Result<SysRole> result = new Result<>();
		QueryWrapper<SysUserRole> sysUserRoleQueryWrapper = new QueryWrapper<>();
		sysUserRoleQueryWrapper.lambda()
				.eq(SysUserRole::getUserId,sysUser.getId())
				.eq(SysUserRole::getRoleId,roleId);

		List<SysUserRole> userRoles = sysUserRoleService.list(sysUserRoleQueryWrapper);
		if(CollectionUtil.isEmpty(userRoles)) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = sysUserRoleService.removeByIds(userRoles);
			if(ok) {
				result.success("删除成功!");
			}
		}
		return result;
	}

	@ApiOperation("添加权限")
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public Result<SysRole> add(SysUser sysUser,String roleId) {
		Result<SysRole> result = new Result<>();
		SysUserRole sysUserRole = new SysUserRole();
		sysUserRole.setUserId(sysUser.getId());
		sysUserRole.setRoleId(roleId);
		try {
			sysUserRoleService.save(sysUserRole);
			result.success("添加成功！");
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			result.error500("操作失败");
		}
		return result;
	}

}
