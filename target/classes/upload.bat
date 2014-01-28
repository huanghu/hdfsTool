@ECHO OFF
set mainPath=D:\java\program\ErpToOracle-barch\ebsdi\
set subPath=ebsdi-apps\src\main\resources\main\product\erp\mtlSystemItemsErp_U
set path=/user/huanghu/oozie/workflow/main/product/erp/mtlSystemItemsErp_U

%JAVA_HOME%\bin\java  -jar D:\java\tools\hdfsTool\hdfsTool-0.0.1-SNAPSHOT.jar %mainPath% %subPath% %path% ebsdi-apps,ebsdi-core,ebsdi-domain
rem %JAVA_HOME%\bin\java  -jar D:\java\tools\hdfsTool\hdfsTool-0.0.1-SNAPSHOT.jar %mainPath% %subPath% %path%  rbtAdapter-apps,rbtAdapter-core
pause