package cn.com.cnpc.cpoa.service;

import cn.com.cnpc.cpoa.core.AppService;
import cn.com.cnpc.cpoa.core.exception.AppException;
import cn.com.cnpc.cpoa.domain.SysUserDto;
import cn.com.cnpc.cpoa.mapper.SysUserDtoMapper;
import cn.com.cnpc.cpoa.utils.StrUtils;
import cn.com.cnpc.cpoa.utils.VCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户服务
 *
 * @author scchenyong@189.cn
 * @create 2019-01-14
 */
@Service
public class UserService extends AppService<SysUserDto> {

    @Autowired
    SysUserDtoMapper sysUserDtoMapper;

    @Autowired
    UserMenuService userMenuService;


    public SysUserDto getByAccount(String account) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("userAccount", account);
        List<SysUserDto> sysUserDtos = sysUserDtoMapper.selectList(params);
        if (null == sysUserDtos || sysUserDtos.isEmpty()) {
            return null;
        }
        SysUserDto sysUserDto1 = sysUserDtos.get(0);
        SysUserDto sysUserDto = sysUserDtoMapper.selectByPrimaryKey(sysUserDto1.getUserId());

        return sysUserDto;
    }

    public List<SysUserDto> selectPage1(Map<String, Object> params) {
//        List<SysUserDto> sysUserDtos = sysUserDtoMapper.selectList(params);
        List<SysUserDto> sysUserDtos1 = sysUserDtoMapper.selectAll();
        return sysUserDtos1;
    }
    public List<SysUserDto> selectPage(Map<String, Object> params) {
        List<SysUserDto> sysUserDtos = sysUserDtoMapper.selectList(params);

        return sysUserDtos;
    }

    /**
     * 删除用户链
     * @param id
     * @return
     */
    @Transactional
    public boolean deleteChain(String id) throws AppException{
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("userId", id);
        //1 删除用户
        int delete = delete(id);
        // 2 删除用户菜单
        int deleteM = userMenuService.deleteByAny(params);

        if (delete == 1) {
            return true;
        }else{
            throw new AppException("删除用户链接出错 用户id为"+id);
        }
    }

    public List<SysUserDto> selectList2(Map<String, Object> param2) {

        return sysUserDtoMapper.selectList2(param2);
    }

    public Integer selectCountBusiness(Map<String, Object> params) {
        return sysUserDtoMapper.selectCountBusiness(params);
    }

    public List<SysUserDto> selectList(Map<String, Object> param) {

        return sysUserDtoMapper.selectList(param);
    }

    public SysUserDto selectUserByUserName(String userName){
        return sysUserDtoMapper.selectUserByUserName(userName);
    }

    public List<SysUserDto> selectUserNotRole() {
        return sysUserDtoMapper.selectUserNotRole();
    }

    public void getVerificationCode(HttpServletResponse response, String account) throws Exception {
        SysUserDto user = getByAccount(account);
        if (user != null) {
            user.setUserVerificationCode(VCodeUtils.getCode(response.getOutputStream()));
            //有效时间为五分钟后
            Calendar nowTime = Calendar.getInstance();
            nowTime.add(Calendar.MINUTE, 5);
            user.setUserEffectiveTime(nowTime.getTime());
            updateNotNull(user);
        } else {
            VCodeUtils.getCode(response.getOutputStream());
        }
        return;
    }

}
