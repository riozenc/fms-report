package org.fms.report.common.util;

import com.riozenc.titanTool.mybatis.pagination.Page;

public class JsonGrid {
	  private Integer totalRow;
	  private Integer pageCurrent;
	  private Object list;

	  public JsonGrid(Page page, Object list) {
	    this.totalRow = page.getTotalRow();
	    this.pageCurrent = page.getPageCurrent();
	    this.list = list;
	  }
	  
	  public JsonGrid(int totalRow, int pageCurrent, Object list) {
	    this.totalRow = totalRow;
	    this.pageCurrent = pageCurrent;
	    this.list = list;
	  }

	  public Integer getTotalRow() {
	    return totalRow;
	  }

	  public void setTotalRow(Integer totalRow) {
	    this.totalRow = totalRow;
	  }

	  public Integer getPageCurrent() {
	    return pageCurrent;
	  }

	  public void setPageCurrent(Integer pageCurrent) {
	    this.pageCurrent = pageCurrent;
	  }

	  public Object getList() {
	    return list;
	  }

	  public void setList(Object list) {
	    this.list = list;
	  }

	}
