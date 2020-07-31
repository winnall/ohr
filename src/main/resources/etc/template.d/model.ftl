<#ftl strip_whitespace = true>

<#assign charset="UTF-8">

<#setting number_format="computer">

<#import "macros.ftl" as macros>

| Binding | [[<@macros.binding_path prefix=link_prefix binding=binding!"xxx" />]] |
| Bridges | <#list bridges as bridge>[[<@macros.bridge_start_path prefix=link_prefix binding=binding bridge=bridge />]] \\  </#list> |
| Devices | <#list things as thing>[[<@macros.thing_start_path prefix=link_prefix binding=binding thing=thing/>]] \\  </#list> |

| Label         | ${label} |
| Channels      | <@macros.list_channels channels=channels /> |
| Configuration | <#assign keys = configuration?keys?sort><#list keys as key>${key} : ${configuration[key]}\\ </#list> |
| Properties    | <#assign keys = properties?keys?sort><#list keys as key>${key} : ${properties[key]}\\ </#list> |
| UID           | <#list uid.segments as segment>${segment}<#sep> - </#sep></#list> |
| Model         | <#list thingTypeUID.segments as segment>${segment}<#sep> - </#sep></#list> |
