package com.dysen.opencard.common.bean;

/**
 * 地址选择Entity
 * 
 * @author JAY
 *
 */
public class AreaBean {
	private String id;
	private String name;



	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "AreaEntity [id=" + id + ", name=" + name + "]";
	}

	public AreaBean(String id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	public AreaBean() {
		super();
	}

}
