<html>
<body>

<h2>${task.names[0].text}</h2>
<hr>
<#if task.descriptions[0]??>
Description: ${task.descriptions[0].text}
</#if>
<#list content?keys as key>
    <#assign value = content[key]>
    
    ${key} : <input type="text" name="${key}" value="${value}"/><br/>
</#list>

<form action="complete" method="POST" enctype="multipart/form-data">
<input type="submit" value="Complete">
</form>
</body>
</html>