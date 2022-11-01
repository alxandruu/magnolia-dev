<div class="row">
    <div class="${def.parameters.divColLeft!'col-sm-6'}">
        <div class="panel panel-default">
            <div class="panel-heading"> ${content.titleLeft!"RENDER YOUR LEFT TITLE HERE"} </div>
            <div class="panel-body">
                [@cms.area name="leftColumn" /]
            </div>
        </div>
    </div>
    <div class="${def.parameters.divColRight!'col-sm-6'}">
        <div class="panel panel-default">
            <div class="panel-heading"> ${content.titleRight!"RENDER YOUR RIGHT TITLE HERE"} </div>
            <div class="panel-body">
               [@cms.area name="rightColumn" /]
            </div>
        </div>
    </div>   
</div>