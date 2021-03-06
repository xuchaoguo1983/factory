package cn.zmvision.ccm.factory.system.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.zmvision.ccm.factory.base.BaseService;
import cn.zmvision.ccm.factory.system.dao.mapping.MenuMapper;
import cn.zmvision.ccm.factory.system.dao.mapping.RoleMenuMapper;
import cn.zmvision.ccm.factory.system.dao.mapping.UserRoleMapper;
import cn.zmvision.ccm.factory.system.dao.model.Menu;
import cn.zmvision.ccm.factory.system.dao.model.MenuExample;
import cn.zmvision.ccm.factory.system.dao.model.RoleMenuExample;
import cn.zmvision.ccm.factory.system.dao.model.RoleMenuKey;
import cn.zmvision.ccm.factory.system.dao.model.UserRoleExample;
import cn.zmvision.ccm.factory.system.dao.model.UserRoleKey;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;

/**
 * 菜单管理
 * 
 * @author xuchaoguo
 * 
 */
@Service
public class MenuService extends BaseService<Menu, MenuExample> {
	@Resource
	MenuMapper menuMapper;
	@Resource
	RoleMenuMapper roleMenuMapper;
	@Resource
	UserRoleMapper userRoleMapper;

	/**
	 * 查询某个角色关联的菜单项
	 * 
	 * @param roleId
	 * @return
	 */
	public List<Menu> queryAllByRoleId(Integer roleId) {
		RoleMenuExample example = new RoleMenuExample();
		example.createCriteria().andRoleIdEqualTo(roleId);

		List<RoleMenuKey> list = roleMenuMapper.selectByExample(example);
		if (list == null || list.size() == 0)
			return null;

		List<String> menuIds = new ArrayList<String>();
		for (RoleMenuKey key : list)
			menuIds.add(key.getMenuId());

		MenuExample example2 = new MenuExample();
		example2.createCriteria().andIdIn(menuIds);

		return menuMapper.selectByExample(example2);
	}

	/**
	 * 查询某个用户关联的菜单项
	 * 
	 * @param userId
	 * @return
	 */
	public List<Menu> queryAllByUserId(Integer userId) {
		UserRoleExample example = new UserRoleExample();
		example.createCriteria().andUserIdEqualTo(userId);

		List<UserRoleKey> urKeyList = userRoleMapper.selectByExample(example);
		if (urKeyList == null || urKeyList.size() == 0)
			return null;
		List<Integer> roleIds = new ArrayList<Integer>();
		for (UserRoleKey key : urKeyList)
			roleIds.add(key.getRoleId());

		RoleMenuExample example2 = new RoleMenuExample();
		example2.createCriteria().andRoleIdIn(roleIds);
		List<RoleMenuKey> rmKeyList = roleMenuMapper.selectByExample(example2);
		if (rmKeyList == null || rmKeyList.size() == 0)
			return null;

		List<String> menuIds = new ArrayList<String>();
		for (RoleMenuKey key : rmKeyList) {
			if (!menuIds.contains(key.getMenuId()))
				menuIds.add(key.getMenuId());
		}

		MenuExample example3 = new MenuExample();
		example3.createCriteria().andIdIn(menuIds);

		List<Menu> list = menuMapper.selectByExample(example3);

		// 子菜单有效，则父菜单自动有效
		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				Menu m = list.get(i);

				if (!m.getPid().equals("#") && !containsMenu(list, m.getPid())) {
					Menu parent = menuMapper.selectByPrimaryKey(m.getPid());
					if (parent != null)
						list.add(parent);
				}
			}
		}

		return list;
	}

	private boolean containsMenu(List<Menu> list, String id) {
		for (Menu m : list)
			if (m.getId().equals(id))
				return true;

		return false;
	}

	@Override
	public List<Menu> queryAllByExample(MenuExample example) {
		return menuMapper.selectByExample(example);
	}

	@Override
	public PageList<Menu> queryByPage(MenuExample example, PageBounds pageBounds) {
		return menuMapper.selectByExample(example, pageBounds);
	}

	public Menu queryById(String id) {
		return menuMapper.selectByPrimaryKey(id);
	}

	@Override
	public boolean save(Menu record) {
		if (this.queryById(record.getId()) != null)
			return menuMapper.updateByPrimaryKey(record) > 0;
		return menuMapper.insert(record) > 0;
	}

	public boolean deleteById(String id) {
		return menuMapper.deleteByPrimaryKey(id) > 0;
	}

	@Override
	public Menu queryById(Integer id) {
		throw new RuntimeException("not supported.");
	}

	@Override
	public boolean deleteById(Integer id) {
		throw new RuntimeException("not supported.");
	}
}
