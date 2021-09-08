package com.base.common.model.bean.xml.xmlBaseRequest;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;


public class RoleInfoBaseRequest {
//    @Element(name = "token", required = false)
//    public String TransId;

    @Attribute(name = "xmlns")
    public String NameAttribute;

    @Element(name = "content")
    public String content;

//    @Element(name = "retXml")
//    public String retXml;



}
