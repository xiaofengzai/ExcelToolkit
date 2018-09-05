import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.data.core.excel.CellDataTypeConfig;
import com.data.core.excel.enums.DataTypeEnum;
import com.data.core.excel.SheetHeader;
import com.data.core.excel.in.ExcelImportConfig;
import com.data.core.excel.in.WorkBookProcess;
import org.junit.Test;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

public class ImportTest {
    @Test
    public  void importExcel() throws FileNotFoundException {
        ExcelImportConfig excelImportConfig=
                new ExcelImportConfig("abcd","ddd").addHeaders(Arrays.asList(
        new SheetHeader(new String[]{"编号","门店ID","门店名称","省份","城市","地区","具体地址","设备ID"},null)
        )).addCellDataTypeConfigs(
                Arrays.asList(
                        new CellDataTypeConfig("no",2, DataTypeEnum.NUMBER.getValue(),1,"0"),
                        new CellDataTypeConfig("storeName",2,DataTypeEnum.STRING.getValue(),1,"30"),
                        new CellDataTypeConfig("storeNo",2,DataTypeEnum.STRING.getValue(),1,"30"),
                        new CellDataTypeConfig("provinceId",1,DataTypeEnum.STRING.getValue(),1,"dd4"),
                        new CellDataTypeConfig("cityId",1,DataTypeEnum.STRING.getValue(),1,"dd5"),
                        new CellDataTypeConfig("regionId",1,DataTypeEnum.STRING.getValue(),1,"dd6"),
                        new CellDataTypeConfig("address",1,DataTypeEnum.STRING.getValue(),1,"dd7"),
                        new CellDataTypeConfig("deviceId",2,DataTypeEnum.STRING.getValue(),1,"dd8")
                )
                )
               // .disableBlankRow()
                ;
        ClassLoader classLoader = ImportTest.class.getClassLoader();
        URL url = classLoader.getResource("demo.xlsx");
        WorkBookProcess workBookProcess=new WorkBookProcess(
                new BufferedInputStream(new FileInputStream(url.getFile()))
                ,excelImportConfig
        );
        Integer rowCount=workBookProcess.execute((List<JSONObject> objects )-> objects.stream().forEach(item-> System.out.println(JSON.toJSONString(item))));
        System.out.println("一共"+rowCount);
    }

}
