<!DOCTYPE html>
<html xml:lang="${cmsfn.language()}" lang="${cmsfn.language()}">
<head>
    [@cms.page /]

    [@cms.area name="htmlHeader"/]

</head>
  
<body>
    
    <div class="container">
        <!-- Navigation -->
            [@cms.area name="navigation"/]
        <!-- End: Navigation -->

        
        
        <!-- Main Area -->
        [@cms.area name="main" /]
        <!-- End: Main Area-->
         
         <!-- Footer Area -->
        [@cms.area name="footer" /]
        <!-- End: Footer Area-->
    </div>
    ${resfn.cachedJs(["/training-templating-website.*jquery.*js", "/training-templating-website.*bootstrap.*js"]) }
</body>
</html>