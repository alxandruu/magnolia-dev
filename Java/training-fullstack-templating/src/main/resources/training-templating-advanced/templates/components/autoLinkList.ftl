<br/>
The model test: ${model.test!"did not work!"}
<br/><br/>
<p>${content.text!}</p>
[#assign targetNode = model.targetNode!]
 
[#if targetNode?has_content]
<div>
<a href="${cmsfn.link(targetNode)!}">${targetNode.title!targetNode.@name}</a>
    <!-- TODO 3 -->
    
</div>
      
[#elseif cmsfn.editMode]
    <div>Target node could not be found.</div>
[/#if]