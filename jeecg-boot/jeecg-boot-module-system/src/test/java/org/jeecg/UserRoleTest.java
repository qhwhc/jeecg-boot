package org.jeecg;

import org.jeecg.modules.system.entity.SysRole;
import org.jeecg.modules.system.service.ISysUserRoleService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserRoleTest {
    @Autowired
    ISysUserRoleService sysUserRoleService;

    @Test
    public void getUserByrole(){
        System.out.println(sysUserRoleService.getUsersByRole(new SysRole()));
    }
}
