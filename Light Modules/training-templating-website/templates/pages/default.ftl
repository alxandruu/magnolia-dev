<!DOCTYPE html>
<html xml:lang="${cmsfn.language()}" lang="${cmsfn.language()}">
<head>
    [@cms.page /]

    [@cms.area name="htmlHeader"/]

</head>

[#-- Get the site definition --]
[#assign site = sitefn.site()!]
[#-- Get the theme configuration defined for this site --]
[#assign theme = sitefn.theme(site)!]

<body>
    
    <div class="container">
        <!-- Navigation -->
            [@cms.area name="navigation"/]
        <!-- End: Navigation -->

         [@cms.area name="pageHeader"/]
        
        <!-- Main Area -->
        [@cms.area name="main" /]
        <!-- End: Main Area-->
         
         <!-- Footer Area -->
        [@cms.area name="footer" /]
        <!-- End: Footer Area-->
        [#list theme.jsFiles as jsFile]
<script  type="text/javascript" src="${jsFile.link}"></script>

[/#list]
    </div>
    
</body>
</html>