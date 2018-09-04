This module is aimed to help export or import excel;
you just need to complete configuration and pre-process Function and 
after-process Function, put in you Data,and then it works;

1.export use the core class MyWkBook and SheetConfig,you need to
 tell it the type of cell,how to response when get and empty value,empty
 line or exception . I place a demo in Test directory.
 
2.import use the core class WorkBookProcess,ExcelImportConfig,we need some
prepared work just like above;The properties "oid" and "bio" in the Config
class just help you to recognise unique operation and business. As how to 
store data is not sure,so I prepare a additional param for you to handle it.
There is a demo in Test directory too.
 
