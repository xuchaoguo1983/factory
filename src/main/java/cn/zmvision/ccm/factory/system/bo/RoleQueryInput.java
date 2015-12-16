package cn.zmvision.ccm.factory.system.bo;

import org.springframework.util.StringUtils;

import cn.zmvision.ccm.factory.base.bo.PageMeta;
import cn.zmvision.ccm.factory.system.dao.model.RoleExample;
import cn.zmvision.ccm.factory.system.dao.model.RoleExample.Criteria;

public class RoleQueryInput extends PageMeta {
	private String name;
	private Integer status;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public RoleExample getExample() {
		RoleExample example = new RoleExample();
		Criteria c = example.createCriteria();
		if (!StringUtils.isEmpty(name))
			c.andNameLike("%" + name + "%");

		if (status != null) {
			c.andStatusEqualTo(status);
		}

		return example;
	}
}