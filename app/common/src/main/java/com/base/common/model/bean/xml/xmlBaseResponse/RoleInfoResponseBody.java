package com.base.common.model.bean.xml.xmlBaseResponse;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;


@Root(name = "Body")
public class RoleInfoResponseBody {

//    @Attribute(name = "xmlns")
//    public String NameAttribute;

    @Element(name = "Etrack_ProcInterfaceResponse")
    public RoleInfoResponse roleInfoResponse;

}