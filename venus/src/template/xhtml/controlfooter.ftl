<#if parameters.type?default("input") == "input">
${parameters.after?if_exists}<#t/>
<#include "/${parameters.templateDir}/venus/controlfooter-core.ftl" />
    </td><#lt/>
</tr>
</#if>
