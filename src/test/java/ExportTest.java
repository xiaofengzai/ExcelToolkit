import com.data.core.excel.CellDataTypeConfig;
import com.data.core.excel.export.MyWkBook;
import com.data.core.excel.export.SheetConfig;
import com.data.core.excel.SheetHeader;
import com.data.model.People;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class ExportTest {
    @Test
    public  void exportExcel() {
        SheetConfig config=new SheetConfig();
        config.setHeaders(Arrays.asList(
                new SheetHeader(
                        new String[]{"no","name","age","phone","address","birthday"},1,null
                )
        ));
        config.setLineSize(1000);
        config.setSheetIndex(0);
        config.setCellDataTypeConfigs(Arrays.asList(
                new CellDataTypeConfig("name",1,1,1,null),
                new CellDataTypeConfig("age",2,2,1,"30"),
                new CellDataTypeConfig("phone",1,1,1,null),
                new CellDataTypeConfig("address",1,1,1,null),
                new CellDataTypeConfig("birthday",1,3,1,null)
        ));
        MyWkBook myWkBook=new MyWkBook(new XSSFWorkbook(),config);
//        Consumer<People> consumer=new Consumer<People>() {
//            @Override
//            public void accept(People people) {
//                people.setAge(33);
//            }
//        };
        myWkBook.addData(getPeople(),(List<People> list)->list.forEach(people -> {people.setAge(20);}));
        try {
            myWkBook.getWorkbook().write(new FileOutputStream(new File("D://testConsumers.xlsx")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static  List<People> getPeople(){
        return Arrays.asList(
                new People("wen",23,"227272","dangd",new Date()),
                new People("wenf",34,"227272","dangd",new Date()),
                new People("wen",12,"43","dangd",new Date()),
                new People("ddd",23,"433","dangd",new Date()),
                new People("dd",17,"227272","fdfd",new Date()),
                new People("ddd",43,"227272","fddf",new Date()),
                new People("rr",23,"227272","dangd",new Date()),
                new People("ee",34,"227272","dangd",new Date())
        );
    }
}
