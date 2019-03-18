package cn.com.starshopping.util.poi;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: thinknovo
 * Date: 2018/12/23
 * Description:  excel读写工具类，需要poi-4.0.1.jar、poi-ooxml-4.0.1.jar、poi-ooxml-schemas-4.0.1.jar、xmlbeans-3.0.2.jar四个核心jar包
 * Version: V1.0
 */
public class POIUtil {

    public static void main(String[] args) {
        String path = "F:" + File.separator + "one.xlsx";
        POIUtil poiUtil = new POIUtil();
        List<List<String>> lists = poiUtil.readExcelForPOI(path);
        for (List<String> list : lists) {
            System.out.println(list.get(0));
            System.out.println(list.get(1));
            System.out.println(list.get(2));
            System.out.println(list.get(3));
            System.out.println(list.get(4));
            System.out.println(list.get(5));
            System.out.println(list.get(6));
            System.out.println(list.get(7));
            System.out.println(list.get(8));
            System.out.println(list.get(9));
            System.out.println(list.get(10));

        }
//        POIUtil poiUtil = new POIUtil();
//        List<List<String>> lists = new ArrayList<>();
//        List<String> only_list = new ArrayList<>();
//        only_list.add("第一行第一列");
//        only_list.add("第一行第二列");
//        only_list.add("第一行第三列");
//        lists.add(only_list);
//
//        only_list = new ArrayList<>();
//        only_list.add("第二行第一列");
//        only_list.add("第二行第二列");
//        lists.add(only_list);
//
//        try {
//            Workbook workbook = poiUtil.creatExcelForPOI(lists, "我的页码01");
//            FileOutputStream fileOutputStream = new FileOutputStream("E:/one.xls");
//            workbook.write(fileOutputStream);
//            workbook.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }


    }

    /**
     * 根据fileType不同读取excel文件
     *
     * @param path 请求硬盘物理路径中的一个excel文件可以是xls，也可以是xlsx文件
     * @return 返回数据列表
     */
    public List<List<String>> readExcelForPOI(String path) {
        String fileType = path.substring(path.lastIndexOf(".") + 1);   // 获取文件的后缀名
        List<List<String>> lists = new ArrayList<>();          // 里面的list代表每一行数据，外面list代表所有行数据，实际项目中，需要把excel中的每一行数据做成POJO对象处理
        InputStream is = null;                                             // 生成输入流
        try {
            is = new FileInputStream(path);
            Workbook wb = null;
            if (fileType.equals("xls")) {                  // 判断是2003版本还是2007之后的版本，xls为2003版本，xlsx为2007版本
                wb = new HSSFWorkbook(is);                 // HSSFWorkbook类型对应2003版本
            } else if (fileType.equals("xlsx")) {
                wb = new XSSFWorkbook(is);                 // XSSFWorkbook类型对应2007之后版本
            } else {
                return null;
            }

            Sheet sheet = wb.getSheetAt(0);             //假设读取第一个工作页sheet，index默认从0开始，如果存在多个sheet，那么需要循环Sheet判断
            for (Row row : sheet) {                       //循环读取第一个工作页中的每一行记录
                ArrayList<String> list = new ArrayList<>();
                for (Cell cell : row) {                   // 循环读取一行中的每一列数据
                    cell.setCellType(CellType.STRING);    // 根据不同类型转化成字符串
                    list.add(cell.getStringCellValue());   // 获取当前列中的数据
                }
                lists.add(list);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return lists;
    }


    /**
     * 创建Excel.xls   返回一个Workbook对象，外部调用方式：首先创建一个OutputStream对象，然后通过workbook.write(out);完成输出
     *
     * @param lists 需要写入excel的数据
     * @param name  文件名
     * @return 返回一个excel poi Workbook对象，如果没有解析成功，或者没有传入数据列表，则会返回null
     * @throws IOException 如果IO失败会抛出异常
     */
    public Workbook creatExcelForPOI(List<List<String>> lists, String name) throws IOException {
        /*
        操作excel五个核心  1.Font字体   2.CellStyle样式单   3.Sheet页   4.Row行   5.Cell列
        过去企业使用的是DOM4J、SAX完成excel解析，现在直接使用POI完成解析
        DOM4J：(50M大小以下)解析机制，首先完整读取excel，把excel所有的内容放入了电脑内存中，操作性能极高，而且在使用读取规则更方便
        SAX：解析机制，基于excel每一个节点位置来循环读取加载
        POI：解析机制，类似于SAX，并且可以和DOM4J、SAX结合完成读写操作
         */
        Workbook wb = new HSSFWorkbook();                        //  创建2003 excel对象 HSSFWorkbook类型对应2003版本    XSSFWorkbook类型对应2007之后版本
        Sheet sheet = wb.createSheet(name);                      // 创建第一个sheet（页），并命名，注意这里只创建一页，如果业务需求，可以添加多页

        Font f = wb.createFont();                                // 创建字体
        f.setFontHeightInPoints((short) 10);                     // 创建字体样式：字体大小
        f.setColor(IndexedColors.BLACK.getIndex());              // 创建字体样式：字体类型(这里设置的是黑体)
        f.setBold(true);                                         // 创建字体样式：粗体

        CellStyle cs = wb.createCellStyle();                     // 创建单元格每列格式对象
        cs.setFont(f);                                           // 把字体样式保存到样式单中
        cs.setBorderLeft(BorderStyle.THIN);                      // 设置具有边框的效果：左边框
        cs.setBorderRight(BorderStyle.THIN);                     // 设置具有边框的效果：右边框
        cs.setBorderTop(BorderStyle.THIN);                       // 设置具有边框的效果：上边框
        cs.setBorderBottom(BorderStyle.THIN);                    // 设置具有边框的效果：下边框
        cs.setAlignment(HorizontalAlignment.CENTER);             // 设置文字居中的效果

        if (lists == null || lists.size() == 0) {                 // 如果没有传递数据列表，则直接返回null
            return null;
        }

        for (int i = 0; i < lists.size(); i++) {                 // 设置每行每列的值  Row 行,Cell 方格 , Row 和 Cell 都是从0开始计数的
            Row row01 = sheet.createRow(i);                      // 在当前sheet页上创建一个新行
            List<String> list_inner = lists.get(i);
            for (int j = 0; j < list_inner.size(); j++) {
                sheet.setColumnWidth(j, 256 * 35);            // 设置列宽。第一个参数表示要为第几列，第二个参数表示列的宽度，值为像素值。
                Cell cell01 = row01.createCell(j);               // 在row行上创建一列
                cell01.setCellValue(list_inner.get(j));         // 在此列上写入数据
                cell01.setCellStyle(cs);                         // 在此列上添加样式
            }
        }
        return wb;
    }
}
