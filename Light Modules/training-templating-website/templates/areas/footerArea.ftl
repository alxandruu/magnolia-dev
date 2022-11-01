
[#-------------- ASSIGNMENTS --------------]
[#switch components?size]
    [#case 2]
        [#assign divColClass = "col-sm-6" /]
        [#break]
    [#case 3]
        [#assign divColClass = "col-sm-4" /]
        [#break]
    [#case 4]
        [#assign divColClass = "col-sm-3" /]
        [#break]
    [#default]
        [#assign divColClass = "col-sm-12" /]
[/#switch]
  
[#-------------- RENDERING --------------]
<div class="row" style="margin: 5rem 0">
    [#list components as component ]
    <div class="${divColClass}">
        [@cms.component content=component /]
    </div>
    [/#list]
</div>

