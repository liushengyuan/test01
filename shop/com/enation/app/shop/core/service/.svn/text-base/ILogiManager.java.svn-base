package com.enation.app.shop.core.service;

import java.util.List;
import java.util.Map;

import com.enation.app.shop.core.model.CustomerLogi;
import com.enation.app.shop.core.model.FreightStandard;
import com.enation.app.shop.core.model.GoodLogisDetail;
import com.enation.app.shop.core.model.Goods;
import com.enation.app.shop.core.model.Logi;
import com.enation.app.shop.core.model.LogiOrderDTO;
import com.enation.app.shop.core.model.LogisModel;
import com.enation.framework.database.Page;
/**
 * 物流公司管理类
 * @author fenlongli
 *
 */
public interface ILogiManager {
	/**
	 * 添加运费模板
	 * @param name
	 */
	public void freightAdd(FreightStandard freightStandard);
	/**
	 * 添加物流公司
	 * @param name
	 */
	public void saveAdd(Logi logi);
	
	/**
	 * 编辑物流公司
	 * @param id
	 * @param name
	 */
	public void saveEdit(Logi logi);
	
	/**
	 * 分页读取物流公司
	 * @param order
	 * @param page
	 * @param pageSize
	 * @return
	 */
	public Page pageLogi(String order ,Integer page,Integer pageSize);
	
	/**
	 * 分页读取运费模板列表
	 * @param order
	 * @param page
	 * @param pageSize
	 * @return
	 */
	public Page pagefreight(String order ,Integer page,Integer pageSize);
	
	/**
	 * 读取所有物流公司列表
	 * @return
	 */
	public List list();
	
	
	/**
	 * 删除物流公司
	 * @param id
	 */
	public void delete(Integer[] logi_id);
	
	/**
	 * 获取物流公司
	 * @param id
	 * @return
	 */
	public Logi getLogiById(Integer id);
	/**
	 * 获取运费模板
	 * @param id
	 * @return
	 */
	public FreightStandard getFreightById(Integer id);
	/**
	 * 删除运费模板
	 * @param id
	 * @return
	 */
	public void deleteFreight(Integer id);
	/**
	 * 编辑运费模板
	 * @param id
	 * @param name
	 */
	public void saveEditFreight(FreightStandard  freightStandard);
	/**
	 * 编辑平台模板
	 * @param id
	 * @param name
	 */
	public List<CustomerLogi> getLogiModel();
	/**
	 * 添加平台模板
	 * @param id
	 * @param name
	 */
	public void addLogiModel(LogisModel logiManager);
	/**
	 *  通过id获得平台模板
	 * @param id
	 * @param name
	 */
	public List<LogisModel> selectLogisModel(Integer id);
	/**
	 *  回显编辑平台模板
	 * @param id
	 * @param name
	 */
	public List<CustomerLogi> getLogiModel(String model_name,Integer storeid);
	/**
	 *  修改平台模板
	 * @param id
	 * @param name
	 */
	public void updateLogiModel(LogisModel logisModel);
	/**
	 *  删除平台模板
	 * @param id
	 * @param name
	 */
	public void deleteLogisModel(String id);
	/**
	 *  验证平台模板名称
	 * @param id
	 * @param name
	 */
	public int checkModelName(String modelname,Integer store_id);
	/**
	 *  获取平台模板名称
	 * @param id
	 * @param name
	 */
	public LogisModel getModelNameById(String id);
	/**
	 * 查询物流订单
	 * @param logiOrderMap
	 * @param page
	 * @param pageSize
	 * @param object
	 * @param sort
	 * @param order
	 * @return
	 */
	public Page searchLogiOrder(Map logiOrderMap, int page, int pageSize,
			Object object, String sort, String order);
	/**
	 * 导出物流订单
	 * @param logiOrderMap
	 * @return
	 */
	public List<LogiOrderDTO> exportLogiOrder(Map logiOrderMap);

/**
 *  通过id获得平台模板
 * @param id
 * @param name
 */
public List<LogisModel> findLogisModel(Integer id);
/**
 * 修改平台模板
 * @param id
 * @param name
 */
public void updateGoodLogisPrice(Goods goods);
/**
 *  添加商品详情页物流信息
 * @param id
 * @param name
 */
public void addGoodLogisDetail(GoodLogisDetail detail);
/**
 *  商品详情页计算价格查询
 * @param id
 * @param name
 */
  public List<GoodLogisDetail> findLogisDetail(Integer store_id,String session_id );
  /**
   * 选择不同的物流模版
   * @param id
   * @param name
   */
  public int selectDistinctFreightName(String product_name); 
  /**
   *  查询物流模版
   * @param id
   * @param name
   */
  public int selectModelByFreightId(Integer id);
  /**
   *  删除物流模版
   * @param id
   * @param name
   */
  public void deleteLogisModelByFreightId(Integer id);
  /**
   *  获取物流模版的is_name字段最大值
   * @param id
   * @param name
   */
  public int getMaxLogisModelNAME();
  /**
   *  获取物流模版的name的个数
   * @param id
   * @param name
   */
  public int selectDistinctLogisName(String name);
  /**
   *  获取物流模版的ename的个数
   * @param id
   * @param name
   */
  public int selectDistinctLogisEname(String name);
  public void addGoodLogisDetail1(GoodLogisDetail detail);
  public void updateLogisModel(Double price,String session_id,Integer goods_id);
  public List<GoodLogisDetail> findLogisDetail1(Integer good_id,String session_id );
  public LogisModel selectLogiModelByStore(Integer store_id,Integer freight_id);
  /**
   * 订单成功提交后，根据sessiongId删除es_good_logis_detail表中相关数据
   * @param sessionId
   */
  public void deleteLogisBySessiongId(String sessionId);
  public void deleteLogisBySessiong(String sessionId,Integer goods_id);
  public List<FreightStandard> getFreightByIdByDays();
  /**
   * 查询购物车运费总价 for APP
   * @param session_id
   */
  public int getFreightTotalPrice(String session_id);
  public FreightStandard getFreightStanddard(Integer product_name);
}
