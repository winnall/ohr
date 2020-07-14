<#ftl strip_whitespace = true>

<#assign charset="UTF-8">

<#setting number_format="computer">

<#import "macros.ftl" as macros>

| Binding | [[<@macros.binding_path prefix=link_prefix binding=binding />]] |
| Model   | [[<@macros.model_start_path prefix=link_prefix binding=binding model=model />]] |
| Bridge  | [[<@macros.bridge_start_path prefix=link_prefix binding=binding bridge=bridge />]] |


| Label         | ${label} |
| Channels      | ${channels?size} |
| Configuration | <#assign keys = configuration?keys?sort><#list keys as key>${key} : ${configuration[key]}\\ </#list> |
| Properties    | <#assign keys = properties?keys?sort><#list keys as key>${key} : ${properties[key]}\\ </#list> |
| UID           | <#list uid.segments as segment>${segment}<#sep> - </#sep></#list> |
| Model         | <#list thingTypeUID.segments as segment>${segment}<#sep> - </#sep></#list> |
