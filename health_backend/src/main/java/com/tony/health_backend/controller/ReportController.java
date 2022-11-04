package com.tony.health_backend.controller;

import cn.hutool.core.date.DateUtil;
import com.tony.health_common.constant.MessageConstant;
import com.tony.health_common.entity.Result;
import com.tony.health_interface.service.MemberService;
import com.tony.health_interface.service.ReportService;
import com.tony.health_interface.service.SetMealService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 处理报表
 */
@RestController
@RequestMapping("/report")
public class ReportController {
    @DubboReference
    private MemberService memberService;
    @DubboReference
    private SetMealService setMealService;
    @DubboReference
    private ReportService reportService;

    /**
     * 获取会员折线图
     */
    @GetMapping("/getMemberReport")
    public Result getMemberReport() {
        Map<String, Object> map = new HashMap<>();
        List<String> months = new ArrayList<>();
        //插入过去一年的日期
        for (int i = 12; i >= 1; i--) {
            months.add(DateUtil.format(DateUtil.offsetMonth(DateUtil.date(), -i), new SimpleDateFormat("yyyy-MM")));

        }
        map.put("months", months);

        List<Integer> memberCount = memberService.findMemberCountByMonths(months);

        map.put("memberCount", memberCount);
        return new Result(true, MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS, map);

    }

    /**
     * 获取套餐饼形图
     */
    @GetMapping("/getSetmealReport")
    public Result getSetmealReport() {
        try {
            Map<String, Object> data = new HashMap<>();
            List<Map<String, Object>> setmealCount = setMealService.findSetmealCount();
            data.put("setmealCount", setmealCount);
            List<String> setmealNames = new ArrayList<>();
            for (Map<String, Object> map : setmealCount) {
                String name = map.get("name").toString();
                setmealNames.add(name);
            }
            data.put("setmealNames", setmealNames);

            return new Result(true, MessageConstant.GET_SETMEAL_COUNT_REPORT_SUCCESS, data);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_MEMBER_NUMBER_REPORT_FAIL);
        }
    }

    /**
     * 运营数据统计
     */
    @GetMapping("/getBusinessReportData")
    public Result getBusinessReportData() {
        try {
            Map<String, Object> data = reportService.getBusinessReportData();
            return new Result(true, MessageConstant.GET_BUSINESS_REPORT_SUCCESS, data);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_BUSINESS_REPORT_FAIL);
        }
    }

    /**
     * 导出运营数据
     */
    @RequestMapping("/exportBusinessReport")
    public Result exportBusinessReport(HttpServletRequest request, HttpServletResponse response) {
        try {
            Map<String, Object> result = reportService.getBusinessReportData();
            //取出数据，写入到excel
            String reportDate = result.get("reportDate").toString();
            Long todayNewMember = (Long) result.get("todayNewMember");
            Long totalMember = (Long) result.get("totalMember");
            Long thisWeekNewMember = (Long) result.get("thisWeekNewMember");
            Long thisMonthNewMember = (Long) result.get("thisMonthNewMember");
            Long todayOrderNumber = (Long) result.get("todayOrderNumber");
            Long todayVisitsNumber = (Long) result.get("todayVisitsNumber");
            Long thisWeekOrderNumber = (Long) result.get("thisWeekOrderNumber");
            Long thisWeekVisitsNumber = (Long) result.get("thisWeekVisitsNumber");
            Long thisMonthOrderNumber = (Long) result.get("thisMonthOrderNumber");
            Long thisMonthVisitsNumber = (Long) result.get("thisMonthVisitsNumber");
            List<Map<String, Object>> hotSetmeal = (List<Map<String, Object>>) result.get("hotSetmeal");

            //创建POI对象
            String filePath = System.getProperty("user.dir")+"/health_backend/src/main/resources/static/template/report_template.xlsx";
            XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(new File(filePath)));
            //读取第一个sheet
            XSSFSheet sheet = workbook.getSheetAt(0);
            //填充数据
            sheet.getRow(2).getCell(5).setCellValue(reportDate);
            sheet.getRow(4).getCell(5).setCellValue(todayNewMember);
            sheet.getRow(4).getCell(7).setCellValue(totalMember);
            sheet.getRow(5).getCell(5).setCellValue(thisWeekNewMember);
            sheet.getRow(5).getCell(7).setCellValue(thisMonthNewMember);
            sheet.getRow(7).getCell(5).setCellValue(todayOrderNumber);
            sheet.getRow(7).getCell(7).setCellValue(todayVisitsNumber);
            sheet.getRow(8).getCell(5).setCellValue(thisWeekOrderNumber);
            sheet.getRow(8).getCell(7).setCellValue(thisWeekVisitsNumber);
            sheet.getRow(9).getCell(5).setCellValue(thisMonthOrderNumber);
            sheet.getRow(9).getCell(7).setCellValue(thisMonthVisitsNumber);

            int rowNum = 12;
            for (Map<String, Object> map : hotSetmeal) {
                String name = map.get("name").toString();
                Long setmealCount = (Long) map.get("setmeal_count");
                String proportion = map.get("proportion").toString();

                XSSFRow row = sheet.getRow(rowNum++);
                row.getCell(4).setCellValue(name);
                row.getCell(5).setCellValue(setmealCount);
                row.getCell(6).setCellValue(proportion);
            }

            //下载excel文件，基于浏览器下载
            OutputStream out = response.getOutputStream();
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("content-Disposition", "attachment;filename=report.xlsx");
            workbook.write(out);

            out.flush();
            out.close();
            workbook.close();

            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_BUSINESS_REPORT_FAIL);
        }
    }

}
