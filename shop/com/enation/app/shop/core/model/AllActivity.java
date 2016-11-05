package com.enation.app.shop.core.model;

import java.io.Serializable;

public class AllActivity implements Serializable{
    private Integer id;
    private String name;//活动名称
	private Integer index;//索引
    private Long start_time;//活动开始时间
    private Long end_time;//活动结束时间
    private Integer is_show;//是否显示
    private String line_color;//页面分割线颜色
    private Integer open;//活动是否开启 0 未开启  1开启 2已结束
    private Integer type;//活动类型 0 打折  1秒杀
    private Integer limitbuy;//是否限购  1 是 0 否
    private Integer limitnumber;//限购数量
   
	private Integer virtual;//虚拟库存 1是 0 否
    private Integer virtualcount;//虚拟的倍数
    private Integer bonus_select;//是否使用优惠券 1 使用 0不使用
    private Integer discountnumber;//折扣率
	public Integer getVirtual() {
		return virtual;
	}

	public void setVirtual(Integer virtual) {
		this.virtual = virtual;
	}

	public Integer getVirtualcount() {
		return virtualcount;
	}

	public void setVirtualcount(Integer virtualcount) {
		this.virtualcount = virtualcount;
	}

	public Integer getLimitbuy() {
		return limitbuy;
	}

	public void setLimitbuy(Integer limitbuy) {
		this.limitbuy = limitbuy;
	}

	public Integer getLimitnumber() {
		return limitnumber;
	}

	public void setLimitnumber(Integer limitnumber) {
		this.limitnumber = limitnumber;
	}

    public AllActivity() {
    	super();
    }
    public AllActivity(String name, Integer index, Long start_time,
			Long end_time, Integer is_show, String line_color, Integer type,
			Integer limitbuy, Integer limitnumber, Integer virtual,
			Integer virtualcount, Integer bonus_select,Integer discountnumber) {
		super();
		this.name = name;
		this.index = index;
		this.start_time = start_time;
		this.end_time = end_time;
		this.is_show = is_show;
		this.line_color = line_color;
		this.type = type;
		this.limitbuy = limitbuy;
		this.limitnumber = limitnumber;
		this.virtual = virtual;
		this.virtualcount = virtualcount;
		this.bonus_select = bonus_select;
		this.discountnumber=discountnumber;
	}
    public AllActivity(String name, Integer index, Long start_time,
			Long end_time, Integer is_show, String line_color, Integer type,
			Integer limitbuy, Integer virtual, Integer virtualcount,
			Integer limitnumber) {
		super();
		this.name = name;
		this.index = index;
		this.start_time = start_time;
		this.end_time = end_time;
		this.is_show = is_show;
		this.line_color = line_color;
		this.type = type;
		this.limitbuy = limitbuy;
		this.virtual = virtual;
		this.virtualcount = virtualcount;
		this.limitnumber = limitnumber;
	}

	public AllActivity(String name, Integer index, Long start_time, Long end_time, Integer is_show,
            String line_color) {
        super();
        this.name = name;
        this.index = index;
        this.start_time = start_time;
        this.end_time = end_time;
        this.is_show = is_show;
        this.line_color = line_color;
    }
    public AllActivity(String name, Integer index, Long start_time,
			Long end_time, Integer is_show, String line_color, Integer type,
			Integer limitbuy, Integer limitnumber) {
		super();
		this.name = name;
		this.index = index;
		this.start_time = start_time;
		this.end_time = end_time;
		this.is_show = is_show;
		this.line_color = line_color;
		this.type = type;
		this.limitbuy = limitbuy;
		this.limitnumber = limitnumber;
	}

	public AllActivity(String name, Integer index, Long start_time,
			Long end_time, Integer is_show, String line_color,
			Integer limitbuy, Integer limitnumber) {
		super();
		this.name = name;
		this.index = index;
		this.start_time = start_time;
		this.end_time = end_time;
		this.is_show = is_show;
		this.line_color = line_color;
		this.limitbuy = limitbuy;
		this.limitnumber = limitnumber;
	}

	public AllActivity(String name, Integer index, Long start_time, Long end_time, Integer is_show, String line_color,
            Integer type) {
        super();
        this.name = name;
        this.index = index;
        this.start_time = start_time;
        this.end_time = end_time;
        this.is_show = is_show;
        this.line_color = line_color;
        this.type = type;
    }
    public AllActivity(Integer id, String name, Integer index, Long start_time,
			Long end_time, Integer is_show, String line_color, Integer open,
			Integer type) {
		super();
		this.id = id;
		this.name = name;
		this.index = index;
		this.start_time = start_time;
		this.end_time = end_time;
		this.is_show = is_show;
		this.line_color = line_color;
		this.open = open;
		this.type = type;
	}

	public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Integer getIndex() {
        return index;
    }
    public void setIndex(Integer index) {
        this.index = index;
    }
    public Long getStart_time() {
        return start_time;
    }
    public void setStart_time(Long start_time) {
        this.start_time = start_time;
    }
    public Long getEnd_time() {
        return end_time;
    }
    public void setEnd_time(Long end_time) {
        this.end_time = end_time;
    }
    public Integer getIs_show() {
        return is_show;
    }
    public void setIs_show(Integer is_show) {
        this.is_show = is_show;
    }
    public String getLine_color() {
        return line_color;
    }
    public void setLine_color(String line_color) {
        this.line_color = line_color;
    }
    public Integer getOpen() {
        return open;
    }
    public void setOpen(Integer open) {
        this.open = open;
    }
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getBonus_select() {
		return bonus_select;
	}

	public void setBonus_select(Integer bonus_select) {
		this.bonus_select = bonus_select;
	}

	public Integer getDiscountnumber() {
		return discountnumber;
	}

	public void setDiscountnumber(Integer discountnumber) {
		this.discountnumber = discountnumber;
	}
    
}
