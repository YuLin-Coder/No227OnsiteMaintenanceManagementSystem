package com.javaweb.service.impl;

import java.util.ArrayList;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.javaweb.dao.DaoFactory;
import com.javaweb.entity.Partproc;
import com.javaweb.service.IFinancialManagementService;
import com.javaweb.utils.BeanUtil;
import com.javaweb.utils.PagedResult;
import com.javaweb.views.SettleOrderView;

/**
 * 财务管理的接口
 * @author hades
 *
 */
@Service
public class FinancialManagementService implements IFinancialManagementService{
	
	private static final Logger logger = LoggerFactory.getLogger(FinancialManagementService.class);
	
	@Resource
	private DaoFactory daoFactory;

	/**
	 * 分页查询所有的已经支付过的订单
	 */
	@Override
	public PagedResult<SettleOrderView> queryallSettled(String starttime, String endtime, Integer pageNo, Integer pageSize) {
		pageNo = pageNo == null ? 0 : pageNo;
		pageSize = pageSize == null ? 10 : pageSize;
		PageHelper.startPage(pageNo, pageSize);
		return BeanUtil.topagedResult(daoFactory.getOrdersMapper().selectSettledOrders(starttime, endtime));
	}

	/**
	 * 查询所有的采购的订单
	 */
	@Override
	public PagedResult<Partproc> queryAllPartProc(String starttime, String endtime, Integer pageNo, Integer pageSize) {
		pageNo = pageNo == null ? 0 : pageNo;
		pageSize = pageSize == null ? 10 : pageSize;
		PageHelper.startPage(pageNo, pageSize);
		return BeanUtil.topagedResult(daoFactory.getPartprocMapper().selectProced(starttime, endtime));
	}

	
	/**
	 * 接单统计
	 */
	@Override
	public Map<String, String> jiedantongji(String year) { 
		return daoFactory.getOrdersMapper().jiedantongji(year);
	}

	/**
	 * 销售统计
	 */
	@Override
	public Map<String, String> xiaoshouetongji(String year) {		 
		return daoFactory.getOrdersMapper().xiaoshouetongji(year);
	}

	/**
	 * 采购金额统计
	 */
	@Override
	public Map<String, String> caigoujinetongji(String year) {		 
		return daoFactory.getOrdersMapper().caigoujinetongji(year);
	}

	/**
	 * 占比统计
	 */
	@Override
	public String[] zhanbitongji(String year) { 
		return daoFactory.getOrdersMapper().zhanbitongji(year);
	} 
	
}
