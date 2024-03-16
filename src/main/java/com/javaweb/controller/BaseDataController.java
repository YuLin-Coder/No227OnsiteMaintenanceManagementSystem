package com.javaweb.controller;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.Null;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.tools.ant.taskdefs.condition.And;
import org.omg.CosNaming.NamingContextExtPackage.StringNameHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.javaweb.entity.Mainitem;
import com.javaweb.entity.Part;
import com.javaweb.entity.Partcategory;
import com.javaweb.entity.Partstorage;
import com.javaweb.entity.Permission;
import com.javaweb.entity.Projcategory;
import com.javaweb.entity.Supplier;
import com.javaweb.entity.Warehouse;
import com.javaweb.service.impl.ServiceFactory;
import com.javaweb.utils.BaseController;
import com.javaweb.utils.MyWebUtils;
import com.javaweb.utils.XLS;
import com.javaweb.views.CustomerView;
import com.javaweb.views.LoginBean;

/**
 * 基础数据管理的控制器
 * 
 * @author hades
 *
 */
@Controller
@Scope("prototype")
@RequestMapping("/baseData")
public class BaseDataController extends BaseController {

	private static final Logger logger = LoggerFactory.getLogger(BaseDataController.class);
	
	/**
	 * 零件信息
	 */
	@InitBinder("part")    
    public void initBinderPart(WebDataBinder binder) {    
            binder.setFieldDefaultPrefix("part.");    
    }    
	
	/**
	 * 
	 * 零件存储
	 * 
	 */
	@InitBinder("storage")    
    public void initBinderPartStorage(WebDataBinder binder) {    
            binder.setFieldDefaultPrefix("storage.");    
    }  
   
	@Autowired
	private ServiceFactory serviceFactory;

	/**
	 * 查看所有用户的视图
	 * 
	 * @return
	 */
	@RequestMapping("/queryPagedCustomerView")
	@ResponseBody
	public String queryPagedCustomerView(String key, String sort, String order, Integer page, Integer rows) {
		return responseSuccess(
				serviceFactory.getBaseDataManageService().queryPagedCustomerView(key, sort, order, page, rows));
	}

	/**
	 * 导出客户的excel表格
	 */
	@RequestMapping("/toCusttomerExcel")
	public void toCusttomerExcel(HttpServletRequest request, HttpServletResponse response) {
		String title = "上门维修系统-客户信息";
		logger.info(title);
		try {
			List<CustomerView> customerViews = serviceFactory.getBaseDataManageService().queryAllCustomer();
			XLS<CustomerView> myXls = new XLS<CustomerView>(CustomerView.class);
			String[] titles = new String[] { "用户编号", "用户名", "联系方式", "联系地址", "汽车编号", "汽车牌号", "汽车类型", "年检日期", "里程数",
					"发动机号" };
			HSSFWorkbook workbook = myXls.writeExcel(title, titles, customerViews, myXls.getCommonStyle(titles.length));
			myXls.printStream(response, title + ".xls", workbook);
		} catch (Exception e) {
			logger.info("下载失败!");
		}
	}

	/**
	 * 更新用户的信息
	 * 
	 * @param customerView
	 * @return
	 */
	@RequestMapping("/modifyCustomer")
	@ResponseBody
	public String modifyCustomer(CustomerView customerView) {
		boolean flag = serviceFactory.getBaseDataManageService().modifyCustomer(customerView);
		return flag ? responseSuccess(null) : responseFail("修改用户信息失败!");
	}

	/**
	 * 查询所有的权限
	 * 
	 * @return
	 */
	@RequestMapping("/queryallPermisson")
	@ResponseBody
	public String queryallPermisson() {
		return responseArraySuccess(serviceFactory.getBaseDataManageService().queryPermission());
	}

	/**
	 * 修改权限
	 * 
	 * @param permission
	 * @return
	 */
	@RequestMapping("/modifyPermission")
	@ResponseBody
	public String modifyPermission(Permission permission) {
		boolean flag = false;
		flag = serviceFactory.getBaseDataManageService().modifyPermission(permission);
		return flag ? responseSuccess(null) : responseFail("修改失败!");
	}

	/**
	 * 查询所有的维修项目大类
	 * 
	 * @return
	 */
	@RequestMapping("/queryAllProjCategory")
	@ResponseBody
	public String queryAllProjCategory() {
		return responseArraySuccess(serviceFactory.getBaseDataManageService().queryAllProjCategory());
	}

	/**
	 * 查询所有的维修项目
	 * 
	 * @return
	 */
	@RequestMapping("/queryAllMainItem")
	@ResponseBody
	public String queryAllMainItem() {
		return responseArraySuccess(serviceFactory.getBaseDataManageService().queryAllMainItem());
	}

	/**
	 * 修改维修项目的大类
	 * 
	 * @return
	 */
	@RequestMapping("/modifyPartCategory")
	@ResponseBody
	public String modifyPartCategory(String projcategorycode, String projcategory, String type, Integer id,
			String categoryflag) {
		boolean flag = false;
		if ("C".equals(type) || "U".equals(type) || "D".equals(type)) {
			Projcategory pc = new Projcategory();
			pc.setNumbering(projcategorycode);
			pc.setProjcatenum(projcategorycode);
			pc.setProjname(projcategory);
			if (id != null) {
				pc.setId(id);
			}
			if (categoryflag != null) {
				pc.setCatflag(categoryflag);
			}
			flag = serviceFactory.getBaseDataManageService().modifyPartCategory(pc, type);
		}
		return flag ? responseSuccess(null) : responseFail("修改维修项目dalei失败!");
	}

	/**
	 * 修改维修项目
	 * 
	 * @return
	 */
	@RequestMapping("/modifyMainitem")
	@ResponseBody
	public String modifyMainitem(Mainitem mainitem, String type) {
		boolean flag = false;
		if ("C".equals(type) || "U".equals(type) || "D".equals(type)) {
			flag = serviceFactory.getBaseDataManageService().modifyMainitem(mainitem, type);
		}
		return flag ? responseSuccess(null) : responseFail("修改维修项目失败!");
	}

	/**
	 * 分页查询供应商的信息
	 * 
	 * @param key
	 * @param sort
	 * @param order
	 * @param flag
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	@RequestMapping("/querySupplier")
	@ResponseBody
	public String querySupplier(String key, String sort, String order, String flag, Integer page, Integer rows) {
		return responseSuccess(
				serviceFactory.getBaseDataManageService().queryAllSupplier(key, sort, order, flag, page, rows));
	}

	/**
	 * 修改供应商的信息
	 * 
	 * @param supplier
	 * @param type
	 * @return
	 */
	@RequestMapping("/modifySupplier")
	@ResponseBody
	public String modifySupplier(Supplier supplier, String type) {
		if (supplier != null && org.apache.commons.lang.StringUtils.isBlank(supplier.getId() + "")) {
			supplier.setId(null);
		}
		boolean flag = serviceFactory.getBaseDataManageService().modifySupplier(supplier, type);
		return flag ? responseSuccess(null) : responseFail("修改失败!");
	}

	/**
	 * 导出供应商的excel
	 * 
	 * @param response
	 */
	@RequestMapping("/toSupplierExcel")
	public void toSupplierExcel(HttpServletResponse response) {
		try {
			List<Supplier> suppliers = serviceFactory.getBaseDataManageService().queryAllSupplier();
			XLS<Supplier> myXls = new XLS<Supplier>(Supplier.class);
			String fileName = "上门维修系统_供应商";
			String[] titles = new String[] { "编号", "供应商名称", "联系人", "手机", "联系方式", "传真", "邮箱", "地址", "邮政编码", "银行卡号",
					"标记" };
			HSSFWorkbook hssfWorkbook = myXls.writeExcel(fileName, titles, suppliers,
					myXls.getCommonStyle(titles.length));
			myXls.printStream(response, fileName + ".xls", hssfWorkbook);
		} catch (Exception e) {
			logger.error("下载失败!");
		}
	}

	/**
	 * 零件类别的管理{operation}:S:查询:M:修改
	 * 查询所有${pageContext.request.contextPath}/baseData/S/partCategoryManage.html
	 * 修改：
	 * 		添加：${pageContext.request.contextPath}/baseData/M/partCategoryManage.html?type=C  (一下post partcategroy对象)
	 * 		修改：${pageContext.request.contextPath}/baseData/M/partCategoryManage.html?type=U
	 * 		删除(还原):${pageContext.request.contextPath}/baseData/M/partCategoryManage.html?type=D
	 * 
	 * @param pageNo
	 * @param pageSize
	 * @param status
	 * @param partcategory
	 * @param type	C:添加，U：修改,D：删除（还原）
	 * @return
	 */
	@RequestMapping("/{operation}/partCategoryManage")
	@ResponseBody
	public String partCategoryManage(Integer page, Integer rows, String status, Partcategory partcategory,
			String type, @PathVariable("operation") String operation) {
		if ("S".equals(operation)) {
			return responseSuccess(serviceFactory.getBaseDataManageService().queryPagedPartCategory(page, rows, status));
		} else if ("M".equals(operation)) {
			boolean flag = serviceFactory.getBaseDataManageService().modifyPartCategory(partcategory, type);
			return flag?responseSuccess(null):responseFail("操作失败，请稍后重试!");
		}
		return responseFail("系统暂时无法提供该服务，请稍后重试!");
	}

	/**
	 * 仓库表的管理{operation}:S:查询:M:修改
	 * 查询所有${pageContext.request.contextPath}/baseData/S/warehouseManage.html
	 * 	修改：
	 * 		添加：${pageContext.request.contextPath}/baseData/M/partCategoryManage.html?type=C   
	 * 		修改：${pageContext.request.contextPath}/baseData/M/partCategoryManage.html?type=U
	 * 		删除(还原):${pageContext.request.contextPath}/baseData/M/partCategoryManage.html?type=D
	 * @param pageNo
	 * @param pageSize
	 * @param status
	 * @param warehouse
	 * @param type
	 * @param operation
	 * @return
	 */
	@RequestMapping("/{operation}/warehouseManage")
	@ResponseBody
	public String warehouseManage(Integer page, Integer rows, String status, Warehouse warehouse, String type,
			@PathVariable("operation") String operation) {
		if ("S".equals(operation)) {
			return responseSuccess(serviceFactory.getBaseDataManageService().queryPagedWarehouse(page, rows, status));
		} else if ("M".equals(operation)) {
			boolean flag= serviceFactory.getBaseDataManageService().modifyWarehouse(warehouse, type);
			return flag?responseSuccess(null):responseFail("操作失败，请稍后重试!");
		}
		return responseFail("系统暂时无法提供该服务，请稍后重试!");
	}
	
	/**
	 * 查询零件的类别
	 * @return
	 */
	@RequestMapping("/searchLJCategory")
	@ResponseBody
	public String searchLJCategory() {
		return responseArraySuccess(serviceFactory.getBaseDataManageService().queryAllPartCategory());
	}
	
	/**
	 * 查询供应商的信息
	 * @return
	 */
	@RequestMapping("/searchSuppliers")
	@ResponseBody
	public String searchSuppliers() {
		return responseArraySuccess(serviceFactory.getBaseDataManageService().queryAllSupplier());
	}
	
	/**
	 * 查询所有供应商的信息
	 * @return
	 */
	@RequestMapping("/searchWareHouse")
	@ResponseBody
	public String searchWareHouse() {
		return responseArraySuccess(serviceFactory.getBaseDataManageService().queryAllWareHouse());
	}
	
	/**
	 * 零件入库
	 * @return
	 */
	@RequestMapping("/storagePart")
	@ResponseBody
	public String storagePart(@ModelAttribute("part") Part part,@ModelAttribute("storage") Partstorage storage,HttpServletRequest request) {
		// 设置part的值为1
		part.setPartflag("1");
		// 设置零件存储表的信息
	    storage.setStoragetime(new Date());
	    LoginBean loginBean = MyWebUtils.getCurrentUser(request);
	    if(loginBean!=null){
	    	storage.setPurchaser(loginBean.getUsername());
		    storage.setJobnumber(loginBean.getJobnumber());		    
	    }
	    // 调用service添加
	    boolean flag = serviceFactory.getBaseDataManageService().addPartStorage(part, storage);	    
		return flag?(responseSuccess(null)):(responseFail("添加失败!"));
	}
	 
	/**
	 * 删除零件
	 * @return
	 */
	@RequestMapping("/delPart")
	@ResponseBody
	public String delPart(String partid) {
		boolean flag = false;
		if(StringUtils.isNotBlank(partid)){
			flag = serviceFactory.getBaseDataManageService().delPart(partid);
		}
		return flag?responseSuccess(null):responseFail("删除失败");
	}

}
